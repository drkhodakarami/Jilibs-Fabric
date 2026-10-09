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

package com.dynamero.fluid;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.*;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import com.dynamero.base.blockentity.AbstractBaseBE;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.enumerations.MappedDirection;
import com.dynamero.shared.network.FluidComponent;

/**
 * Utility helper class providing methods for fluid transfers, inventory slot validation,
 * transaction simulations, capacity checks, and automated fluid distribution among block entities.
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
@Developer("TheMentor")
@CreatedAt("2026-01-12")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
public class FluidHelper
{
    /**
     * Checks whether the specified single fluid storage contains zero fluid.
     *
     * @param storage the storage to check
     * @return {@code true} if empty, {@code false} otherwise
     */
    public static boolean isEmpty(SingleFluidStorage storage)
    {
        return storage.getAmount() == 0;
    }

    /**
     * Checks whether the specified single variant storage contains zero fluid.
     *
     * @param storage the storage to check
     * @return {@code true} if empty, {@code false} otherwise
     */
    public static boolean isEmpty(SingleVariantStorage<@NotNull FluidVariant> storage)
    {
        return storage.getAmount() == 0;
    }

    /**
     * Checks whether the specified fluid storage contains zero fluid across all views.
     *
     * @param storage the storage to check
     * @return {@code true} if empty, {@code false} otherwise
     */
    public static boolean isEmpty(Storage<FluidVariant> storage)
    {
        for (StorageView<FluidVariant> view : storage)
            if (view.getAmount() > 0)
                return false;

        return true;
    }

    /**
     * Transfers fluid between an item in the container inventory slot and the single fluid storage.
     *
     * @param world          the level
     * @param pos            the block position
     * @param storage        the target single fluid storage
     * @param inputInventory the container inventory containing the item
     * @param inputSlot      the slot index of the item
     * @param fullTransfer   whether only complete transfers are accepted
     * @return {@code true} if a transfer succeeded, {@code false} otherwise
     */
    public static boolean handleStorageTransfer(Level world, BlockPos pos, SingleFluidStorage storage,
                                                Container inputInventory,
                                                int inputSlot,
                                                boolean fullTransfer)
    {
        if(transferToStorage(world, pos, storage, inputInventory, inputSlot, fullTransfer))
            return true;
        return transferFromStorage(world, pos, storage, inputInventory, inputSlot, fullTransfer);
    }

    /**
     * Transfers fluid between an item in the container inventory slot and the single variant storage.
     *
     * @param world          the level
     * @param pos            the block position
     * @param storage        the target single variant storage
     * @param inputInventory the container inventory containing the item
     * @param inputSlot      the slot index of the item
     * @param fullTransfer   whether only complete transfers are accepted
     * @return {@code true} if a transfer succeeded, {@code false} otherwise
     */
    public static boolean handleStorageTransfer(Level world, BlockPos pos, SingleVariantStorage<@NotNull FluidVariant> storage,
                                                Container inputInventory,
                                                int inputSlot,
                                                boolean fullTransfer)
    {
        if(transferToStorage(world, pos, storage, inputInventory, inputSlot, fullTransfer))
            return true;
        return transferFromStorage(world, pos, storage, inputInventory, inputSlot, fullTransfer);
    }

    /**
     * Transfers fluid between an item in the container inventory slot and the fluid storage.
     *
     * @param world          the level
     * @param pos            the block position
     * @param storage        the target fluid storage
     * @param inputInventory the container inventory containing the item
     * @param inputSlot      the slot index of the item
     * @param fullTransfer   whether only complete transfers are accepted
     * @return {@code true} if a transfer succeeded, {@code false} otherwise
     */
    public static boolean handleStorageTransfer(Level world, BlockPos pos, Storage<FluidVariant> storage,
                                                Container inputInventory,
                                                int inputSlot,
                                                boolean fullTransfer)
    {
        if(transferToStorage(world, pos, storage, inputInventory, inputSlot, fullTransfer))
            return true;
        return transferFromStorage(world, pos, storage, inputInventory, inputSlot, fullTransfer);
    }

    /**
     * Transfers fluid from the block storage into an item container in the specified inventory slot.
     *
     * @param world          the level
     * @param pos            the block position
     * @param storage        the source fluid storage
     * @param inputInventory the inventory containing the container item
     * @param inputSlot      the slot index
     * @param fullTransfer   whether only complete transfers are accepted
     * @return {@code true} if fluid was transferred, {@code false} otherwise
     */
    private static boolean transferFromStorage(Level world, BlockPos pos, Storage<FluidVariant> storage, Container inputInventory, int inputSlot, boolean fullTransfer)
    {
        Storage<FluidVariant> slotStorage = ContainerItemContext.withConstant(inputInventory.getItem(inputSlot)).find(FluidStorage.ITEM);

        if (slotStorage == null)
            return false;

        for(StorageView<FluidVariant> storageView : storage)
        {
            FluidVariant variant = storageView.getResource();

            if (storageView.isResourceBlank() || storageView.getAmount() == 0)
                continue;

            for (StorageView<FluidVariant> view : slotStorage)
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
                        SoundEvent sound = FluidVariantAttributes.getFillSound(variant);
                        world.playSound(null, pos, sound, SoundSource.BLOCKS, 1.0F, 1.0F);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Transfers fluid from an item container in the specified inventory slot into the block storage.
     *
     * @param world          the level
     * @param pos            the block position
     * @param storage        the target fluid storage
     * @param inputInventory the inventory containing the container item
     * @param inputSlot      the slot index
     * @param fullTransfer   whether only complete transfers are accepted
     * @return {@code true} if fluid was transferred, {@code false} otherwise
     */
    private static boolean transferToStorage(Level world, BlockPos pos, Storage<FluidVariant> storage, Container inputInventory, int inputSlot, boolean fullTransfer)
    {
        Storage<FluidVariant> slotStorage = ContainerItemContext.withConstant(inputInventory.getItem(inputSlot)).find(FluidStorage.ITEM);

        if(slotStorage == null)
            return false;

        for (StorageView<FluidVariant> view : slotStorage)
        {
            if(view.isResourceBlank() || view.getAmount() == 0)
                continue;

            FluidVariant variant = view.getResource();

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
                    SoundEvent sound = FluidVariantAttributes.getEmptySound(variant);
                    world.playSound(null, pos, sound, SoundSource.BLOCKS, 1.0F, 1.0F);
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Interacts with fluid storages at the given block position using the player's held item.
     *
     * @param world  the level
     * @param pos    the block position
     * @param player the interacting player
     * @param hand   the hand used
     * @return {@code true} if an interaction took place, {@code false} otherwise
     */
    public static boolean interactWithBlock(Level world, BlockPos pos, Player player, InteractionHand hand)
    {
        Storage<FluidVariant> storage;
        for (MappedDirection direction : MappedDirection.values())
        {
            storage = FluidStorage.SIDED.find(world, pos, MappedDirection.toDirection(direction));

            if(storage != null)
                if(FluidStorageUtil.interactWithFluidStorage(storage, player, hand))
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
        // EMPTY BUCKET
        ItemStack slotStack = inventory.getItem(slot);

        if(itemStack.isEmpty())
            return false;

        if(slotStack.isEmpty())
            return true;

        if(!slotStack.is(itemStack.getItem()))
            return false;

        if(slotStack.getCount() + itemStack.getCount() > slotStack.getMaxStackSize())
            return false;

        Storage<FluidVariant> itemStorage = ContainerItemContext.withConstant(itemStack).find(FluidStorage.ITEM);
        Storage<FluidVariant> slotStorage = ContainerItemContext.withConstant(slotStack).find(FluidStorage.ITEM);

        boolean variantFlag = false;
        boolean itemFlag = false;

        if(itemStorage == null || slotStorage == null)
            return false;

        for(StorageView<FluidVariant> slotView : slotStorage)
        {
            if(slotView.isResourceBlank())
                continue;
            for(StorageView<FluidVariant> itemView : itemStorage)
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
     * Checks whether the specified inventory slot contains an empty bucket.
     *
     * @param inventory the container inventory
     * @param slot      the slot index
     * @return {@code true} if holding an empty bucket, {@code false} otherwise
     */
    public static boolean isEmptyBucket(Container inventory, int slot)
    {
        return inventory.getItem(slot).is(Items.BUCKET);
    }

    /**
     * Simulates fluid insertion into the storage within an existing transaction.
     *
     * @param storage the target fluid storage
     * @param variant the fluid variant to insert
     * @param outer   the enclosing transaction
     * @return the simulated inserted amount in droplets
     */
    public static long simulateInsertion(Storage<FluidVariant> storage, FluidVariant variant, Transaction outer)
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
     * Simulates fluid insertion into the storage opening a temporary transaction.
     *
     * @param storage the target fluid storage
     * @param variant the fluid variant to insert
     * @return the simulated inserted amount in droplets
     */
    public static long simulateInsertion(Storage<FluidVariant> storage, FluidVariant variant)
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
     * Simulates fluid extraction from the storage within an existing transaction.
     *
     * @param storage the target fluid storage
     * @param variant the fluid variant to extract
     * @param outer   the enclosing transaction
     * @return the simulated extracted amount in droplets
     */
    public static long simulateExtraction(Storage<FluidVariant> storage, FluidVariant variant, Transaction outer)
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
     * Simulates fluid extraction from the storage opening a temporary transaction.
     *
     * @param storage the target fluid storage
     * @param variant the fluid variant to extract
     * @return the simulated extracted amount in droplets
     */
    public static long simulateExtraction(Storage<FluidVariant> storage, FluidVariant variant)
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
     * Checks whether the item in the specified inventory slot holds the same fluid as the given storage.
     *
     * @param inventory the container inventory
     * @param slot      the slot index
     * @param storage   the fluid storage to compare against
     * @return {@code true} if both hold the same fluid variant, {@code false} otherwise
     */
    public static boolean sameFluidInStorage(Container inventory, int slot, Storage<FluidVariant> storage)
    {
        ItemStack slotStack = inventory.getItem(slot);

        if(slotStack.isEmpty())
            return false;

        Storage<FluidVariant> slotStorage = ContainerItemContext.withConstant(slotStack).find(FluidStorage.ITEM);

        return sameFluidInStorage(slotStorage, storage);
    }

    /**
     * Compares two fluid storages to determine if they contain matching fluid variants.
     *
     * @param storage1 the first storage
     * @param storage2 the second storage
     * @return {@code true} if matching, {@code false} otherwise
     */
    public static boolean sameFluidInStorage(Storage<FluidVariant> storage1, Storage<FluidVariant> storage2)
    {
        if(storage1 == null || storage2 == null)
            return false;

        for(StorageView<FluidVariant> slotView : storage1)
        {
            if (slotView.isResourceBlank())
                continue;
            boolean found = false;
            for (StorageView<FluidVariant> storageView : storage2)
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
     * Checks whether the given fluid storage is full.
     *
     * @param storage the fluid storage to check
     * @return {@code true} if full or null, {@code false} if any capacity remains
     */
    public static boolean isStorageFull(Storage<FluidVariant> storage)
    {
        if(storage == null)
            return true;

        for(StorageView<FluidVariant> slotView : storage)
        {
            if (slotView.isResourceBlank())
                return false;

            if(slotView.getAmount() < slotView.getCapacity())
                return false;
        }

        return true;
    }

    /**
     * Checks whether the storage can accept the fluid amount described by the fluid component.
     *
     * @param stack   the fluid component to insert
     * @param storage the target storage
     * @return {@code true} if the storage can accommodate the fluid, {@code false} otherwise
     */
    public static boolean canAccept(FluidComponent stack, Storage<FluidVariant> storage)
    {
        if(storage == null)
            return false;

        try(Transaction transaction = Transaction.openOuter())
        {
            long amount = storage.insert(stack.getFluid(), Long.MAX_VALUE, transaction);
            return stack.getAmount() <= amount && amount > 0;
        }
    }

    /**
     * Looks up an adjacent fluid storage in the specified direction, excluding blacklisted positions.
     *
     * @param world     the level
     * @param pos       the source block position
     * @param direction the direction to probe
     * @param blacklist optional set of blacklisted block positions
     * @return the adjacent fluid storage, or null if blacklisted or absent
     */
    public static Storage<FluidVariant> getStorage(Level world, BlockPos pos, Direction direction, Set<BlockPos> blacklist)
    {
        BlockPos adjacentPos = pos.relative(direction);
        if(blacklist != null && blacklist.contains(adjacentPos))
            return null;

        return FluidStorage.SIDED.find(world, adjacentPos, direction.getOpposite());
    }

    /**
     * Finds all adjacent fluid storages across all directions, excluding blacklisted positions.
     *
     * @param world     the level
     * @param pos       the source block position
     * @param blacklist optional set of blacklisted block positions
     * @return a list of adjacent fluid storages
     */
    public static List<Storage<FluidVariant>> getAllStorages(Level world, BlockPos pos, Set<BlockPos> blacklist)
    {
        List<Storage<FluidVariant>> storages = new ArrayList<>();
        for (Direction direction : Direction.values())
        {
            BlockPos adjacentPos = pos.relative(direction);
            if(blacklist != null && blacklist.contains(adjacentPos))
                continue;

            Storage<FluidVariant> storage = FluidStorage.SIDED.find(world, adjacentPos, direction.getOpposite());

            if(storage != null)
                storages.add(storage);
        }

        return storages;
    }

    /**
     * Spreads fluid from the storage to adjacent block entities with equal amount and blacklist controls.
     *
     * @param blockEntity the source block entity
     * @param storage     the source fluid storage
     * @param equalAmount whether to divide fluid equally among all recipients
     * @param blacklist   optional set of block positions to exclude from transfer
     */
    public static void spread(BlockEntity blockEntity, Storage<FluidVariant> storage, boolean equalAmount,Set<BlockPos> blacklist)
    {
        BlockPos adjacentPos;

        List<Storage<FluidVariant>> adjacentStorages = getAllStorages(blockEntity.getLevel(), blockEntity.getBlockPos(), blacklist);

        if(adjacentStorages.isEmpty())
            return;

        for (StorageView<FluidVariant> storageView : storage)
        {
            FluidVariant variant = storageView.getResource();
            long currentAmount = storageView.getAmount();
            long totalInserted = 0;
            long totalExtractable = simulateExtraction(storage, variant);

            long finalAmount = equalAmount ? totalExtractable / adjacentStorages.size() : totalExtractable;

            for(Storage<FluidVariant> adjacentStorage : adjacentStorages)
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
     * Spreads fluid from the storage equally among adjacent block entities excluding blacklisted positions.
     *
     * @param blockEntity the source block entity
     * @param storage     the source fluid storage
     * @param blacklist   optional set of block positions to exclude from transfer
     */
    public static void spread(BlockEntity blockEntity, Storage<FluidVariant> storage,Set<BlockPos> blacklist)
    {
        spread(blockEntity, storage, true, blacklist);
    }

    /**
     * Spreads fluid from the storage to adjacent block entities with equal amount control and no blacklist.
     *
     * @param blockEntity the source block entity
     * @param storage     the source fluid storage
     * @param equalAmount whether to divide fluid equally among all recipients
     */
    public static void spread(BlockEntity blockEntity, Storage<FluidVariant> storage, boolean equalAmount)
    {
        spread(blockEntity, storage, equalAmount, null);
    }

    /**
     * Spreads fluid from the storage equally among all adjacent block entities.
     *
     * @param blockEntity the source block entity
     * @param storage     the source fluid storage
     */
    public static void spread(BlockEntity blockEntity, Storage<FluidVariant> storage)
    {
        spread(blockEntity, storage, true, null);
    }
}