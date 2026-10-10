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

package com.dynamero.gas;

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
import com.dynamero.gas.base.GasComponent;
import com.dynamero.gas.base.GasVariantAttributes;
import com.dynamero.gas.base.interfaces.GasVariant;
import com.dynamero.gas.base.storage.GasStorage;
import com.dynamero.gas.base.storage.SingleGasStorage;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.enumerations.MappedDirection;

/**
 * Utility helper providing methods for gas storage player interactions, gas transfers,
 * transaction simulations, capacity checks, and automated gas distribution among adjacent block entities.
 */
@SuppressWarnings({"unused", "UnstableApiUsage"})
@Developer("TheMentor")
@CreatedAt("2026-10-08")
@ModifiedAt("2026-10-08")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public class GasHelper
{
    /**
     * Handles player hand interaction with a gas storage, attempting bidirectional gas transfer with appropriate sound effects.
     *
     * @param storage the world gas storage
     * @param player  the interacting player
     * @param hand    the player hand holding the item
     * @return {@code true} if a gas transfer occurred, {@code false} otherwise
     * @throws ReportedException if an unexpected error occurs during interaction
     */
    public static boolean interactWithGasStorage(Storage<GasVariant> storage, Player player, InteractionHand hand)
    {
        Storage<GasVariant> handStorage = ContainerItemContext.forPlayerInteraction(player, hand).find(GasStorage.ITEM);
        if (handStorage == null)
            return false;

        Item handItem = player.getItemInHand(hand).getItem();

        try
        {
            return moveWithSound(storage, handStorage, player, true, handItem) || moveWithSound(handStorage, storage, player, false, handItem);
        }
        catch (Exception e)
        {
            CrashReport report = CrashReport.forThrowable(e, "Interacting with gas storage");
            report.addCategory("Interaction details")
                .setDetail("Player", () -> DebugMessages.forPlayer(player))
                .setDetail("Hand", hand)
                .setDetail("Hand item", handItem::toString)
                .setDetail("Gas storage", () -> Objects.toString(storage, null));
            throw new ReportedException(report);
        }
    }

    /**
     * Transfers gas from one storage to another.
     *
     * @param from the source storage
     * @param to   the destination storage
     * @return {@code true} if gas was transferred, {@code false} otherwise
     */
    private static boolean move(Storage<GasVariant> from, Storage<GasVariant> to)
    {
        for (StorageView<GasVariant> view : from.nonEmptyViews()) {
            GasVariant resource = view.getResource();

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
     * Transfers gas from one storage to another playing sound effects on success.
     *
     * @param from     the source storage
     * @param to       the destination storage
     * @param player   the player triggering the transfer
     * @param fill     whether this transfer represents a fill action
     * @param handItem the item held in hand
     * @return {@code true} if gas was transferred, {@code false} otherwise
     */
    private static boolean moveWithSound(Storage<GasVariant> from, Storage<GasVariant> to, Player player, boolean fill, Item handItem)
    {
        for(StorageView<GasVariant> view : from)
        {
            if (!view.isResourceBlank()) {
                GasVariant resource = view.getResource();

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
                        SoundEvent sound = fill ? GasVariantAttributes.getFillSound(resource, handItem) : GasVariantAttributes.getEmptySound(resource, handItem);
                        player.level().playSound(player, player.getX(), player.getEyeY(), player.getZ(), sound, SoundSource.PLAYERS, 1.0F, 1.0F);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Checks whether the given single gas storage is empty.
     *
     * @param storage the storage to check
     * @return {@code true} if empty, {@code false} otherwise
     */
    public static boolean isEmpty(SingleGasStorage storage)
    {
        return storage.getAmount() == 0;
    }

    /**
     * Checks whether the given single variant gas storage is empty.
     *
     * @param storage the storage to check
     * @return {@code true} if empty, {@code false} otherwise
     */
    public static boolean isEmpty(SingleVariantStorage<@NotNull GasVariant> storage)
    {
        return storage.getAmount() == 0;
    }

    /**
     * Checks whether the given gas storage is empty across all views.
     *
     * @param storage the storage to check
     * @return {@code true} if empty, {@code false} otherwise
     */
    public static boolean isEmpty(Storage<GasVariant> storage)
    {
        for (StorageView<GasVariant> view : storage)
            if (view.getAmount() > 0)
                return false;

        return true;
    }

    /**
     * Transfers gas between an item in the container inventory slot and the single gas storage.
     *
     * @param world          the level
     * @param pos            the block position
     * @param storage        the target single gas storage
     * @param inputInventory the container inventory containing the item
     * @param inputSlot      the slot index of the item
     * @param fullTransfer   whether only complete transfers are accepted
     * @return {@code true} if a transfer succeeded, {@code false} otherwise
     */
    public static boolean handleStorageTransfer(Level world, BlockPos pos, SingleGasStorage storage,
                                                Container inputInventory,
                                                int inputSlot,
                                                boolean fullTransfer)
    {
        if(transferToStorage(world, pos, storage, inputInventory, inputSlot, fullTransfer))
            return true;
        return transferFromStorage(world, pos, storage, inputInventory, inputSlot, fullTransfer);
    }

    /**
     * Transfers gas between an item in the container inventory slot and the single variant gas storage.
     *
     * @param world          the level
     * @param pos            the block position
     * @param storage        the target single variant storage
     * @param inputInventory the container inventory containing the item
     * @param inputSlot      the slot index of the item
     * @param fullTransfer   whether only complete transfers are accepted
     * @return {@code true} if a transfer succeeded, {@code false} otherwise
     */
    public static boolean handleStorageTransfer(Level world, BlockPos pos, SingleVariantStorage<@NotNull GasVariant> storage,
                                                Container inputInventory,
                                                int inputSlot,
                                                boolean fullTransfer)
    {
        if(transferToStorage(world, pos, storage, inputInventory, inputSlot, fullTransfer))
            return true;
        return transferFromStorage(world, pos, storage, inputInventory, inputSlot, fullTransfer);
    }

    /**
     * Transfers gas between an item in the container inventory slot and the gas storage.
     *
     * @param world          the level
     * @param pos            the block position
     * @param storage        the target gas storage
     * @param inputInventory the container inventory containing the item
     * @param inputSlot      the slot index of the item
     * @param fullTransfer   whether only complete transfers are accepted
     * @return {@code true} if a transfer succeeded, {@code false} otherwise
     */
    public static boolean handleStorageTransfer(Level world, BlockPos pos, Storage<GasVariant> storage,
                                                Container inputInventory,
                                                int inputSlot,
                                                boolean fullTransfer)
    {
        if(transferToStorage(world, pos, storage, inputInventory, inputSlot, fullTransfer))
            return true;
        return transferFromStorage(world, pos, storage, inputInventory, inputSlot, fullTransfer);
    }

    /**
     * Transfers gas from the block storage into an item container in the specified inventory slot.
     *
     * @param world          the level
     * @param pos            the block position
     * @param storage        the source gas storage
     * @param inputInventory the inventory containing the container item
     * @param inputSlot      the slot index
     * @param fullTransfer   whether only complete transfers are accepted
     * @return {@code true} if gas was transferred, {@code false} otherwise
     */
    private static boolean transferFromStorage(Level world, BlockPos pos, Storage<GasVariant> storage, Container inputInventory, int inputSlot, boolean fullTransfer)
    {
        Storage<GasVariant> slotStorage = ContainerItemContext.withConstant(inputInventory.getItem(inputSlot)).find(GasStorage.ITEM);

        if (slotStorage == null)
            return false;

        for(StorageView<GasVariant> storageView : storage)
        {
            GasVariant variant = storageView.getResource();

            if (storageView.isResourceBlank() || storageView.getAmount() == 0)
                continue;

            for (StorageView<GasVariant> view : slotStorage)
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
                        SoundEvent sound = GasVariantAttributes.getFillSound(variant, inputInventory.getItem(inputSlot).getItem());
                        world.playSound(null, pos, sound, SoundSource.BLOCKS, 1.0F, 1.0F);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Transfers gas from an item container in the specified inventory slot into the block storage.
     *
     * @param world          the level
     * @param pos            the block position
     * @param storage        the target gas storage
     * @param inputInventory the inventory containing the container item
     * @param inputSlot      the slot index
     * @param fullTransfer   whether only complete transfers are accepted
     * @return {@code true} if gas was transferred, {@code false} otherwise
     */
    private static boolean transferToStorage(Level world, BlockPos pos, Storage<GasVariant> storage, Container inputInventory, int inputSlot, boolean fullTransfer)
    {
        Storage<GasVariant> slotStorage = ContainerItemContext.withConstant(inputInventory.getItem(inputSlot)).find(GasStorage.ITEM);

        if(slotStorage == null)
            return false;

        for (StorageView<GasVariant> view : slotStorage)
        {
            if(view.isResourceBlank() || view.getAmount() == 0)
                continue;

            GasVariant variant = view.getResource();

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
                    SoundEvent sound = GasVariantAttributes.getEmptySound(variant, inputInventory.getItem(inputSlot).getItem());
                    world.playSound(null, pos, sound, SoundSource.BLOCKS, 1.0F, 1.0F);
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Interacts with gas storages at the given block position using the player's held item.
     *
     * @param world  the level
     * @param pos    the block position
     * @param player the interacting player
     * @param hand   the hand used
     * @return {@code true} if an interaction took place, {@code false} otherwise
     */
    public static boolean interactWithBlock(Level world, BlockPos pos, Player player, InteractionHand hand)
    {
        Storage<GasVariant> storage;
        for (MappedDirection direction : MappedDirection.values())
        {
            storage = GasStorage.SIDED.find(world, pos, MappedDirection.toDirection(direction));

            if(storage != null)
                if(interactWithGasStorage(storage, player, hand))
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

        Storage<GasVariant> itemStorage = ContainerItemContext.withConstant(itemStack).find(GasStorage.ITEM);
        Storage<GasVariant> slotStorage = ContainerItemContext.withConstant(slotStack).find(GasStorage.ITEM);

        boolean variantFlag = false;
        boolean itemFlag = false;

        if(itemStorage == null || slotStorage == null)
            return false;

        for(StorageView<GasVariant> slotView : slotStorage)
        {
            if(slotView.isResourceBlank())
                continue;
            for(StorageView<GasVariant> itemView : itemStorage)
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
     * Simulates gas insertion into the storage within an existing transaction.
     *
     * @param storage the target gas storage
     * @param variant the gas variant to insert
     * @param outer   the enclosing transaction
     * @return the simulated inserted amount in droplets
     */
    public static long simulateInsertion(Storage<GasVariant> storage, GasVariant variant, Transaction outer)
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
     * Simulates gas insertion into the storage opening a temporary transaction.
     *
     * @param storage the target gas storage
     * @param variant the gas variant to insert
     * @return the simulated inserted amount in droplets
     */
    public static long simulateInsertion(Storage<GasVariant> storage, GasVariant variant)
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
     * Simulates gas extraction from the storage within an existing transaction.
     *
     * @param storage the target gas storage
     * @param variant the gas variant to extract
     * @param outer   the enclosing transaction
     * @return the simulated extracted amount in droplets
     */
    public static long simulateExtraction(Storage<GasVariant> storage, GasVariant variant, Transaction outer)
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
     * Simulates gas extraction from the storage opening a temporary transaction.
     *
     * @param storage the target gas storage
     * @param variant the gas variant to extract
     * @return the simulated extracted amount in droplets
     */
    public static long simulateExtraction(Storage<GasVariant> storage, GasVariant variant)
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
     * Checks whether the item in the specified inventory slot holds the same gas as the given storage.
     *
     * @param inventory the container inventory
     * @param slot      the slot index
     * @param storage   the gas storage to compare against
     * @return {@code true} if both hold the same gas variant, {@code false} otherwise
     */
    public static boolean sameGasInStorage(Container inventory, int slot, Storage<GasVariant> storage)
    {
        ItemStack slotStack = inventory.getItem(slot);

        if(slotStack.isEmpty())
            return false;

        Storage<GasVariant> slotStorage = ContainerItemContext.withConstant(slotStack).find(GasStorage.ITEM);

        return sameGasInStorage(slotStorage, storage);
    }

    /**
     * Compares two gas storages to determine if they contain matching gas variants.
     *
     * @param storage1 the first storage
     * @param storage2 the second storage
     * @return {@code true} if matching, {@code false} otherwise
     */
    public static boolean sameGasInStorage(Storage<GasVariant> storage1, Storage<GasVariant> storage2)
    {
        if(storage1 == null || storage2 == null)
            return false;

        for(StorageView<GasVariant> slotView : storage1)
        {
            if (slotView.isResourceBlank())
                continue;
            boolean found = false;
            for (StorageView<GasVariant> storageView : storage2)
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
     * Checks whether the given gas storage is full.
     *
     * @param storage the gas storage to check
     * @return {@code true} if full or null, {@code false} if any capacity remains
     */
    public static boolean isStorageFull(Storage<GasVariant> storage)
    {
        if(storage == null)
            return true;

        for(StorageView<GasVariant> slotView : storage)
        {
            if (slotView.isResourceBlank())
                return false;

            if(slotView.getAmount() < slotView.getCapacity())
                return false;
        }

        return true;
    }

    /**
     * Checks whether the storage can accept the gas amount described by the gas component.
     *
     * @param stack   the gas component to insert
     * @param storage the target storage
     * @return {@code true} if the storage can accommodate the gas, {@code false} otherwise
     */
    public static boolean canAccept(GasComponent stack, Storage<GasVariant> storage)
    {
        if(storage == null)
            return false;

        try(Transaction transaction = Transaction.openOuter())
        {
            long amount = storage.insert(stack.getGas(), Long.MAX_VALUE, transaction);
            return stack.getAmount() <= amount && amount > 0;
        }
    }

    /**
     * Looks up an adjacent gas storage in the specified direction, excluding blacklisted positions.
     *
     * @param world     the level
     * @param pos       the source block position
     * @param direction the direction to probe
     * @param blacklist optional set of blacklisted block positions
     * @return the adjacent gas storage, or null if blacklisted or absent
     */
    public static Storage<GasVariant> getStorage(Level world, BlockPos pos, Direction direction, Set<BlockPos> blacklist)
    {
        BlockPos adjacentPos = pos.relative(direction);
        if(blacklist != null && blacklist.contains(adjacentPos))
            return null;

        return GasStorage.SIDED.find(world, adjacentPos, direction.getOpposite());
    }

    /**
     * Finds all adjacent gas storages across all directions, excluding blacklisted positions.
     *
     * @param world     the level
     * @param pos       the source block position
     * @param blacklist optional set of blacklisted block positions
     * @return a list of adjacent gas storages
     */
    public static List<Storage<GasVariant>> getAllStorages(Level world, BlockPos pos, Set<BlockPos> blacklist)
    {
        List<Storage<GasVariant>> storages = new ArrayList<>();
        for (Direction direction : Direction.values())
        {
            BlockPos adjacentPos = pos.relative(direction);
            if(blacklist != null && blacklist.contains(adjacentPos))
                continue;

            Storage<GasVariant> storage = GasStorage.SIDED.find(world, adjacentPos, direction.getOpposite());

            if(storage != null)
                storages.add(storage);
        }

        return storages;
    }

    /**
     * Spreads gas from the storage to adjacent block entities with equal amount and blacklist controls.
     *
     * @param blockEntity the source block entity
     * @param storage     the source gas storage
     * @param equalAmount whether to divide gas equally among all recipients
     * @param blacklist   optional set of block positions to exclude from transfer
     */
    public static void spread(BlockEntity blockEntity, Storage<GasVariant> storage, boolean equalAmount, Set<BlockPos> blacklist)
    {
        BlockPos adjacentPos;

        List<Storage<GasVariant>> adjacentStorages = getAllStorages(blockEntity.getLevel(), blockEntity.getBlockPos(), blacklist);

        if(adjacentStorages.isEmpty())
            return;

        for (StorageView<GasVariant> storageView : storage)
        {
            GasVariant variant = storageView.getResource();
            long currentAmount = storageView.getAmount();
            long totalInserted = 0;
            long totalExtractable = simulateExtraction(storage, variant);

            long finalAmount = equalAmount ? totalExtractable / adjacentStorages.size() : totalExtractable;

            for(Storage<GasVariant> adjacentStorage : adjacentStorages)
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
     * Spreads gas from the storage equally among adjacent block entities excluding blacklisted positions.
     *
     * @param blockEntity the source block entity
     * @param storage     the source gas storage
     * @param blacklist   optional set of block positions to exclude from transfer
     */
    public static void spread(BlockEntity blockEntity, Storage<GasVariant> storage,Set<BlockPos> blacklist)
    {
        spread(blockEntity, storage, true, blacklist);
    }

    /**
     * Spreads gas from the storage to adjacent block entities with equal amount control and no blacklist.
     *
     * @param blockEntity the source block entity
     * @param storage     the source gas storage
     * @param equalAmount whether to divide gas equally among all recipients
     */
    public static void spread(BlockEntity blockEntity, Storage<GasVariant> storage, boolean equalAmount)
    {
        spread(blockEntity, storage, equalAmount, null);
    }

    /**
     * Spreads gas from the storage equally among all adjacent block entities.
     *
     * @param blockEntity the source block entity
     * @param storage     the source gas storage
     */
    public static void spread(BlockEntity blockEntity, Storage<GasVariant> storage)
    {
        spread(blockEntity, storage, true, null);
    }
}