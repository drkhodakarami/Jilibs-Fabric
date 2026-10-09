/*
 * Copyright (c) 2025 Alireza Khodakarami
 *
 * Licensed under the MIT, (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://opensource.org/license/mit
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dynamero.dispersion.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.impl.transfer.DebugMessages;
import org.jetbrains.annotations.NotNull;

import net.minecraft.CrashReport;
import net.minecraft.ReportedException;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import com.dynamero.base.blockentity.AbstractBaseBE;
import com.dynamero.dispersion.base.DispersionVariantAttributes;
import com.dynamero.dispersion.base.interfaces.DispersionVariant;
import com.dynamero.dispersion.base.records.DispersionStackPayload;
import com.dynamero.dispersion.base.storage.DispersionStorage;
import com.dynamero.dispersion.base.storage.SingleDispersionStorage;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.enumerations.MappedDirection;

/**
 * Utility helper providing methods for dispersion storage player interactions, dispersion transfers,
 * transaction simulations, capacity checks, and automated dispersion distribution among adjacent block entities.
 */
@SuppressWarnings({"unused", "UnstableApiUsage"})
@Developer("TheMentor")
@CreatedAt("2026-01-12")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
public class DispersionHelper
{
    /**
     * Handles player hand interaction with a dispersion storage, attempting bidirectional transfer with appropriate sound effects.
     *
     * @param storage the world dispersion storage
     * @param player  the interacting player
     * @param hand    the player hand holding the item
     * @return {@code true} if a transfer occurred, {@code false} otherwise
     * @throws ReportedException if an unexpected error occurs during interaction
     */
    public static boolean interactWithDispersionStorage(Storage<DispersionVariant> storage, Player player, InteractionHand hand)
    {
        Storage<DispersionVariant> handStorage = ContainerItemContext.forPlayerInteraction(player, hand).find(DispersionStorage.ITEM);
        if (handStorage == null)
            return false;

        Item handItem = player.getItemInHand(hand).getItem();

        try
        {
            return moveWithSound(storage, handStorage, player, true, handItem) || moveWithSound(handStorage, storage, player, false, handItem);
        }
        catch (Exception e)
        {
            CrashReport report = CrashReport.forThrowable(e, "Interacting with dispersion storage");
            report.addCategory("Interaction details")
                .setDetail("Player", () -> DebugMessages.forPlayer(player))
                .setDetail("Hand", hand)
                .setDetail("Hand item", handItem::toString)
                .setDetail("Dispersion storage", () -> Objects.toString(storage, null));
            throw new ReportedException(report);
        }
    }

    /**
     * Transfers dispersion from one storage to another.
     *
     * @param from the source storage
     * @param to   the destination storage
     * @return {@code true} if dispersion was transferred, {@code false} otherwise
     */
    private static boolean move(Storage<DispersionVariant> from, Storage<DispersionVariant> to)
    {
        for (StorageView<DispersionVariant> view : from.nonEmptyViews()) {
            DispersionVariant resource = view.getResource();

            long maxExtracted;
            try(Transaction transaction = Transaction.openOuter()) {
                maxExtracted = view.extract(resource, Long.MAX_VALUE, transaction);
            }

            try(Transaction transaction = Transaction.openOuter()) {
                long accepted = to.insert(resource, maxExtracted, transaction);
                if(accepted > 0 && view.extract(resource, accepted, transaction) == accepted) {
                    transaction.commit();
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Transfers dispersion from one storage to another playing sound effects on success.
     *
     * @param from     the source storage
     * @param to       the destination storage
     * @param player   the player triggering the transfer
     * @param fill     whether this transfer represents a fill action
     * @param handItem the item held in hand
     * @return {@code true} if dispersion was transferred, {@code false} otherwise
     */
    private static boolean moveWithSound(Storage<DispersionVariant> from, Storage<DispersionVariant> to, Player player, boolean fill, Item handItem)
    {
        for(StorageView<DispersionVariant> view : from)
        {
            if (!view.isResourceBlank()) {
                DispersionVariant resource = view.getResource();

                long maxExtracted;
                try (Transaction extractionTestTransaction = Transaction.openOuter())
                {
                    maxExtracted = view.extract(resource, Long.MAX_VALUE, extractionTestTransaction);
                    extractionTestTransaction.abort();
                }

                try (Transaction transferTransaction = Transaction.openOuter())
                {
                    long accepted = to.insert(resource, maxExtracted, transferTransaction);
                    if (accepted > 0L && view.extract(resource, accepted, transferTransaction) == accepted)
                    {
                        transferTransaction.commit();
                        SoundEvent sound = fill ? DispersionVariantAttributes.getFillSound(resource, handItem) : DispersionVariantAttributes.getEmptySound(resource, handItem);
                        player.level().playSound(player, player.getX(), player.getEyeY(), player.getZ(), sound, SoundSource.PLAYERS, 1.0F, 1.0F);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Checks whether the given single dispersion storage is empty.
     *
     * @param storage the storage to check
     * @return {@code true} if empty, {@code false} otherwise
     */
    public static boolean isEmpty(SingleDispersionStorage storage)
    {
        return storage.getAmount() == 0;
    }

    /**
     * Checks whether the given single variant dispersion storage is empty.
     *
     * @param storage the storage to check
     * @return {@code true} if empty, {@code false} otherwise
     */
    public static boolean isEmpty(SingleVariantStorage<@NotNull DispersionVariant> storage)
    {
        return storage.getAmount() == 0;
    }

    /**
     * Checks whether the given dispersion storage is empty across all views.
     *
     * @param storage the storage to check
     * @return {@code true} if empty, {@code false} otherwise
     */
    public static boolean isEmpty(Storage<DispersionVariant> storage)
    {
        for (StorageView<DispersionVariant> view : storage)
            if (view.getAmount() > 0)
                return false;

        return true;
    }

    /**
     * Transfers dispersion between an item in the container inventory slot and the single dispersion storage.
     *
     * @param world          the level
     * @param pos            the block position
     * @param storage        the target single dispersion storage
     * @param inputInventory the container inventory containing the item
     * @param inputSlot      the slot index of the item
     * @param fullTransfer   whether only complete transfers are accepted
     * @return {@code true} if a transfer succeeded, {@code false} otherwise
     */
    public static boolean handleStorageTransfer(Level world, BlockPos pos, SingleDispersionStorage storage,
                                                Container inputInventory,
                                                int inputSlot,
                                                boolean fullTransfer)
    {
        if(transferToStorage(world, pos, storage, inputInventory, inputSlot, fullTransfer))
            return true;
        return transferFromStorage(world, pos, storage, inputInventory, inputSlot, fullTransfer);
    }

    /**
     * Transfers dispersion between an item in the container inventory slot and the single variant dispersion storage.
     *
     * @param world          the level
     * @param pos            the block position
     * @param storage        the target single variant storage
     * @param inputInventory the container inventory containing the item
     * @param inputSlot      the slot index of the item
     * @param fullTransfer   whether only complete transfers are accepted
     * @return {@code true} if a transfer succeeded, {@code false} otherwise
     */
    public static boolean handleStorageTransfer(Level world, BlockPos pos, SingleVariantStorage<@NotNull DispersionVariant> storage,
                                                Container inputInventory,
                                                int inputSlot,
                                                boolean fullTransfer)
    {
        if(transferToStorage(world, pos, storage, inputInventory, inputSlot, fullTransfer))
            return true;
        return transferFromStorage(world, pos, storage, inputInventory, inputSlot, fullTransfer);
    }

    /**
     * Transfers dispersion between an item in the container inventory slot and the dispersion storage.
     *
     * @param world          the level
     * @param pos            the block position
     * @param storage        the target dispersion storage
     * @param inputInventory the container inventory containing the item
     * @param inputSlot      the slot index of the item
     * @param fullTransfer   whether only complete transfers are accepted
     * @return {@code true} if a transfer succeeded, {@code false} otherwise
     */
    public static boolean handleStorageTransfer(Level world, BlockPos pos, Storage<DispersionVariant> storage,
                                                Container inputInventory,
                                                int inputSlot,
                                                boolean fullTransfer)
    {
        if(transferToStorage(world, pos, storage, inputInventory, inputSlot, fullTransfer))
            return true;
        return transferFromStorage(world, pos, storage, inputInventory, inputSlot, fullTransfer);
    }

    /**
     * Transfers dispersion from the block storage into an item container in the specified inventory slot.
     *
     * @param world          the level
     * @param pos            the block position
     * @param storage        the source dispersion storage
     * @param inputInventory the inventory containing the container item
     * @param inputSlot      the slot index
     * @param fullTransfer   whether only complete transfers are accepted
     * @return {@code true} if dispersion was transferred, {@code false} otherwise
     */
    private static boolean transferFromStorage(Level world, BlockPos pos, Storage<DispersionVariant> storage, Container inputInventory, int inputSlot, boolean fullTransfer)
    {
        Storage<DispersionVariant> slotStorage = ContainerItemContext.withConstant(inputInventory.getItem(inputSlot)).find(DispersionStorage.ITEM);

        if (slotStorage == null)
            return false;

        for(StorageView<DispersionVariant> storageView : storage)
        {
            DispersionVariant variant = storageView.getResource();

            if (storageView.isResourceBlank() || storageView.getAmount() == 0)
                continue;

            for (StorageView<DispersionVariant> view : slotStorage)
            {
                try (Transaction transaction = Transaction.openOuter())
                {
                    long storageTransfer = slotStorage.insert(variant, Long.MAX_VALUE, transaction);
                    long extracted = storage.extract(variant, storageTransfer, transaction);

                    if (storageTransfer == 0)
                        continue;

                    if (storageTransfer > 0)
                    {
                        if (fullTransfer && storageTransfer != extracted)
                            continue;

                        transaction.commit();
                        SoundEvent sound = DispersionVariantAttributes.getFillSound(variant, inputInventory.getItem(inputSlot).getItem());
                        world.playSound(null, pos, sound, SoundSource.BLOCKS, 1.0F, 1.0F);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Transfers dispersion from an item container in the specified inventory slot into the block storage.
     *
     * @param world          the level
     * @param pos            the block position
     * @param storage        the target dispersion storage
     * @param inputInventory the inventory containing the container item
     * @param inputSlot      the slot index
     * @param fullTransfer   whether only complete transfers are accepted
     * @return {@code true} if dispersion was transferred, {@code false} otherwise
     */
    private static boolean transferToStorage(Level world, BlockPos pos, Storage<DispersionVariant> storage, Container inputInventory, int inputSlot, boolean fullTransfer)
    {
        Storage<DispersionVariant> slotStorage = ContainerItemContext.withConstant(inputInventory.getItem(inputSlot)).find(DispersionStorage.ITEM);

        if(slotStorage == null)
            return false;

        for (StorageView<DispersionVariant> view : slotStorage)
        {
            if(view.isResourceBlank() || view.getAmount() == 0)
                continue;

            DispersionVariant variant = view.getResource();

            try(Transaction transaction = Transaction.openOuter())
            {
                long extracted = slotStorage.extract(variant, Long.MAX_VALUE, transaction);
                long storageTransfer = storage.insert(variant, extracted, transaction);

                if(storageTransfer == 0)
                    continue;

                if(storageTransfer > 0)
                {
                    if(fullTransfer && storageTransfer != extracted)
                        continue;

                    if(storageTransfer < extracted)
                        slotStorage.insert(variant, extracted - storageTransfer, transaction);

                    transaction.commit();
                    SoundEvent sound = DispersionVariantAttributes.getEmptySound(variant, inputInventory.getItem(inputSlot).getItem());
                    world.playSound(null, pos, sound, SoundSource.BLOCKS, 1.0F, 1.0F);
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Interacts with dispersion storages at the given block position using the player's held item.
     *
     * @param world  the level
     * @param pos    the block position
     * @param player the interacting player
     * @param hand   the hand used
     * @return {@code true} if an interaction took place, {@code false} otherwise
     */
    public static boolean interactWithBlock(Level world, BlockPos pos, Player player, InteractionHand hand)
    {
        Storage<DispersionVariant> storage;
        for (MappedDirection direction : MappedDirection.values())
        {
            storage = DispersionStorage.SIDED.find(world, pos, MappedDirection.toDirection(direction));

            if(storage != null)
                if(interactWithDispersionStorage(storage, player, hand))
                    return true;
        }

        return false;
    }

    /**
     * Validates whether the given item stack is compatible with the item currently in the specified inventory slot.
     *
     * @param inventory the container inventory
     * @param slot      the slot index
     * @param itemStack the item stack to test
     * @return {@code true} if valid and compatible, {@code false} otherwise
     */
    public static boolean isValidSlot(Container inventory, int slot, ItemStack itemStack)
    {
        ItemStack slotStack = inventory.getItem(slot);

        if(itemStack.isEmpty())
            return false;

        if(slotStack.isEmpty())
            return true;

        if(!slotStack.is(itemStack.getItem()))
            return false;

        if(slotStack.getCount() + itemStack.getCount() > slotStack.getMaxStackSize())
            return false;

        Storage<DispersionVariant> itemStorage = ContainerItemContext.withConstant(itemStack).find(DispersionStorage.ITEM);
        Storage<DispersionVariant> slotStorage = ContainerItemContext.withConstant(slotStack).find(DispersionStorage.ITEM);

        boolean variantFlag = false;
        boolean itemFlag = false;

        if(itemStorage == null || slotStorage == null)
            return false;

        for(StorageView<DispersionVariant> slotView : slotStorage)
        {
            if(slotView.isResourceBlank())
                continue;
            for(StorageView<DispersionVariant> itemView : itemStorage)
            {
                if(itemView.isResourceBlank())
                    continue;

                if(slotView.getResource() == itemView.getResource())
                    variantFlag = true;

                if(slotView.getAmount() == itemView.getAmount())
                    itemFlag = true;
            }
        }
        return variantFlag && itemFlag;
    }

    /**
     * Simulates dispersion insertion into the storage within an existing transaction.
     *
     * @param storage the target dispersion storage
     * @param variant the dispersion variant to insert
     * @param outer   the enclosing transaction
     * @return the simulated inserted amount in droplets
     */
    public static long simulateInsertion(Storage<DispersionVariant> storage, DispersionVariant variant, Transaction outer)
    {
        long amount;
        try(Transaction transaction = outer.openNested())
        {
            amount = storage.insert(variant, Long.MAX_VALUE, transaction);
            transaction.abort();
        }
        return amount;
    }

    /**
     * Simulates dispersion insertion into the storage opening a temporary transaction.
     *
     * @param storage the target dispersion storage
     * @param variant the dispersion variant to insert
     * @return the simulated inserted amount in droplets
     */
    public static long simulateInsertion(Storage<DispersionVariant> storage, DispersionVariant variant)
    {
        long amount;
        try(Transaction transaction = Transaction.openOuter())
        {
            amount = storage.insert(variant, Long.MAX_VALUE, transaction);
            transaction.abort();
        }
        return amount;
    }

    /**
     * Simulates dispersion extraction from the storage within an existing transaction.
     *
     * @param storage the target dispersion storage
     * @param variant the dispersion variant to extract
     * @param outer   the enclosing transaction
     * @return the simulated extracted amount in droplets
     */
    public static long simulateExtraction(Storage<DispersionVariant> storage, DispersionVariant variant, Transaction outer)
    {
        long amount;
        try(Transaction transaction = outer.openNested())
        {
            amount = storage.extract(variant, Long.MAX_VALUE, transaction);
            transaction.abort();
        }
        return amount;
    }

    /**
     * Simulates dispersion extraction from the storage opening a temporary transaction.
     *
     * @param storage the target dispersion storage
     * @param variant the dispersion variant to extract
     * @return the simulated extracted amount in droplets
     */
    public static long simulateExtraction(Storage<DispersionVariant> storage, DispersionVariant variant)
    {
        long amount;
        try(Transaction transaction = Transaction.openOuter())
        {
            amount = storage.extract(variant, Long.MAX_VALUE, transaction);
            transaction.abort();
        }
        return amount;
    }

    /**
     * Checks whether the item in the specified inventory slot holds the same dispersion as the given storage.
     *
     * @param inventory the container inventory
     * @param slot      the slot index
     * @param storage   the dispersion storage to compare against
     * @return {@code true} if both hold the same dispersion variant, {@code false} otherwise
     */
    public static boolean sameDispersionInStorage(Container inventory, int slot, Storage<DispersionVariant> storage)
    {
        ItemStack slotStack = inventory.getItem(slot);

        if(slotStack.isEmpty())
            return false;

        Storage<DispersionVariant> slotStorage = ContainerItemContext.withConstant(slotStack).find(DispersionStorage.ITEM);

        return sameDispersionInStorage(slotStorage, storage);
    }

    /**
     * Compares two dispersion storages to determine if they contain matching dispersion variants.
     *
     * @param storage1 the first storage
     * @param storage2 the second storage
     * @return {@code true} if matching, {@code false} otherwise
     */
    public static boolean sameDispersionInStorage(Storage<DispersionVariant> storage1, Storage<DispersionVariant> storage2)
    {
        if(storage1 == null || storage2 == null)
            return false;

        for(StorageView<DispersionVariant> slotView : storage1)
        {
            if (slotView.isResourceBlank())
                continue;
            boolean found = false;
            for (StorageView<DispersionVariant> storageView : storage2)
            {
                if (storageView.isResourceBlank())
                    continue;

                if(slotView.getResource() == storageView.getResource())
                    found = true;
            }
            if(!found)
                return false;
        }

        return true;
    }

    /**
     * Checks whether the given dispersion storage is full.
     *
     * @param storage the dispersion storage to check
     * @return {@code true} if full or null, {@code false} if any capacity remains
     */
    public static boolean isStorageFull(Storage<DispersionVariant> storage)
    {
        if(storage == null)
            return true;

        for(StorageView<DispersionVariant> slotView : storage)
        {
            if (slotView.isResourceBlank())
                return false;

            if(slotView.getAmount() < slotView.getCapacity())
                return false;
        }

        return true;
    }

    /**
     * Checks whether the storage can accept the dispersion amount described by the stack payload.
     *
     * @param stack   the dispersion stack payload to insert
     * @param storage the target storage
     * @return {@code true} if the storage can accommodate the dispersion, {@code false} otherwise
     */
    public static boolean canAccept(DispersionStackPayload stack, Storage<DispersionVariant> storage)
    {
        if(storage == null)
            return false;

        try(Transaction transaction = Transaction.openOuter())
        {
            long amount = storage.insert(stack.variant(), Long.MAX_VALUE, transaction);
            return stack.amount() <= amount && amount > 0;
        }
    }

    /**
     * Looks up an adjacent dispersion storage in the specified direction, excluding blacklisted positions.
     *
     * @param world     the level
     * @param pos       the source block position
     * @param direction the direction to probe
     * @param blacklist optional set of blacklisted block positions
     * @return the adjacent dispersion storage, or null if blacklisted or absent
     */
    public static Storage<DispersionVariant> getStorage(Level world, BlockPos pos, Direction direction, Set<BlockPos> blacklist)
    {
        BlockPos adjacentPos = pos.relative(direction);
        if(blacklist != null && blacklist.contains(adjacentPos))
            return null;

        return DispersionStorage.SIDED.find(world, adjacentPos, direction.getOpposite());
    }

    /**
     * Finds all adjacent dispersion storages across all directions, excluding blacklisted positions.
     *
     * @param world     the level
     * @param pos       the source block position
     * @param blacklist optional set of blacklisted block positions
     * @return a list of adjacent dispersion storages
     */
    public static List<Storage<DispersionVariant>> getAllStorages(Level world, BlockPos pos, Set<BlockPos> blacklist)
    {
        List<Storage<DispersionVariant>> storages = new ArrayList<>();
        for (Direction direction : Direction.values())
        {
            BlockPos adjacentPos = pos.relative(direction);
            if(blacklist != null && blacklist.contains(adjacentPos))
                continue;

            Storage<DispersionVariant> storage = DispersionStorage.SIDED.find(world, adjacentPos, direction.getOpposite());

            if(storage != null)
                storages.add(storage);
        }

        return storages;
    }

    /**
     * Spreads dispersion from the storage to adjacent block entities with equal amount and blacklist controls.
     *
     * @param blockEntity the source block entity
     * @param storage     the source dispersion storage
     * @param equalAmount whether to divide dispersion equally among all recipients
     * @param blacklist   optional set of block positions to exclude from transfer
     */
    public static void spread(BlockEntity blockEntity, Storage<DispersionVariant> storage, boolean equalAmount, Set<BlockPos> blacklist)
    {
        List<Storage<DispersionVariant>> adjacentStorages = getAllStorages(blockEntity.getLevel(), blockEntity.getBlockPos(), blacklist);

        if(adjacentStorages.isEmpty())
            return;

        for (StorageView<DispersionVariant> storageView : storage)
        {
            DispersionVariant variant = storageView.getResource();

            long currentAmount = storageView.getAmount();
            long totalInserted = 0;
            long totalExtractable = simulateExtraction(storage, variant);

            long finalAmount = equalAmount ? totalExtractable / adjacentStorages.size() : totalExtractable;

            for(Storage<DispersionVariant> adjacentStorage : adjacentStorages)
            {
                var insertable = simulateInsertion(adjacentStorage, variant);
                if(insertable < finalAmount)
                    continue;

                try(Transaction transaction = Transaction.openOuter())
                {
                    long inserted = adjacentStorage.insert(variant, finalAmount, transaction);

                    if(inserted == finalAmount)
                        totalInserted += inserted;

                    transaction.commit();
                }
            }

            if(totalInserted > 0)
            {
                try(Transaction transaction = Transaction.openOuter())
                {
                    storage.extract(variant, totalInserted, transaction);
                    transaction.commit();
                }
            }

            if(currentAmount != storageView.getAmount())
            {
                if (blockEntity instanceof AbstractBaseBE<?> be)
                    be.update();
                else
                    blockEntity.setChanged();
            }
        }
    }

    /**
     * Spreads dispersion from the storage equally among adjacent block entities excluding blacklisted positions.
     *
     * @param blockEntity the source block entity
     * @param storage     the source dispersion storage
     * @param blacklist   optional set of block positions to exclude from transfer
     */
    public static void spread(BlockEntity blockEntity, Storage<DispersionVariant> storage,Set<BlockPos> blacklist)
    {
        spread(blockEntity, storage, true, blacklist);
    }

    /**
     * Spreads dispersion from the storage to adjacent block entities with equal amount control and no blacklist.
     *
     * @param blockEntity the source block entity
     * @param storage     the source dispersion storage
     * @param equalAmount whether to divide dispersion equally among all recipients
     */
    public static void spread(BlockEntity blockEntity, Storage<DispersionVariant> storage, boolean equalAmount)
    {
        spread(blockEntity, storage, equalAmount, null);
    }

    /**
     * Spreads dispersion from the storage equally among all adjacent block entities.
     *
     * @param blockEntity the source block entity
     * @param storage     the source dispersion storage
     */
    public static void spread(BlockEntity blockEntity, Storage<DispersionVariant> storage)
    {
        spread(blockEntity, storage, true, null);
    }
}