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

package com.dynamero.pressure;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import org.jspecify.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import com.dynamero.base.blockentity.AbstractBaseBE;
import com.dynamero.pressure.base.interfaces.PressureStorage;
import com.dynamero.pressure.base.interfaces.PressureUnit;
import com.dynamero.shared.annotations.*;

/**
 * Utility helper methods for pressure transfers, transaction simulations, capacity checks,
 * adjacent pressure storage discovery, and pressure spreading between block entities.
 */
@SuppressWarnings("unused")
@Developer("TheMentor")
@CreatedAt("2026-10-08")
@ModifiedAt("2026-10-08")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public class PressureHelper
{
    /**
     * Moves pressure between two pressure storages, and returns the amount that was successfully moved.
     *
     * @param from        the source storage (may be null)
     * @param to          the target storage (may be null)
     * @param maxAmount   the maximum amount that may be moved
     * @param unit        the pressure unit used for transfer calculations
     * @param transaction the transaction this transfer is part of, or {@code null} if a new transaction should be opened
     * @return the amount of pressure that was successfully moved
     */
    public static double move(@Nullable PressureStorage from, @Nullable PressureStorage to, double maxAmount, PressureUnit unit, @Nullable TransactionContext transaction)
    {
        if (from == null || to == null)
            return 0;

        double maxExtracted;
        try (Transaction extractionTestTransaction = Transaction.openNested(transaction))
        {
            maxExtracted = from.extract(maxAmount, unit, extractionTestTransaction);
        }

        try (Transaction moveTransaction = Transaction.openNested(transaction))
        {
            double accepted = to.insert(maxExtracted, unit, moveTransaction);

            if (from.extract(accepted, unit, moveTransaction) == accepted)
            {
                moveTransaction.commit();
                return accepted;
            }
        }

        return 0;
    }

    /**
     * Checks if the passed stack offers a pressure storage through {@link PressureStorage#ITEM}.
     *
     * @param stack the item stack to check
     * @return {@code true} if offering a pressure storage, {@code false} otherwise
     */
    public static boolean isPressureStorage(ItemStack stack)
    {
        return ContainerItemContext.withConstant(stack).find(PressureStorage.ITEM) != null;
    }

    /**
     * Simulates pressure insertion into the storage within an existing transaction.
     *
     * @param storage the target pressure storage
     * @param outer   the enclosing transaction
     * @return the simulated inserted amount
     */
    public static double simulateInsertion(PressureStorage storage, Transaction outer)
    {
        double amount;
        try(Transaction transaction = outer.openNested())
        {
            amount = storage.insert(Double.MAX_VALUE, storage.getUnit(), transaction);
            transaction.abort();
        }
        return amount;
    }

    /**
     * Simulates pressure insertion into the storage opening a temporary transaction.
     *
     * @param storage the target pressure storage
     * @return the simulated inserted amount
     */
    public static double simulateInsertion(PressureStorage storage)
    {
        double amount;
        try(Transaction transaction = Transaction.openOuter())
        {
            amount = storage.insert(Long.MAX_VALUE, storage.getUnit(), transaction);
            transaction.abort();
        }
        return amount;
    }

    /**
     * Simulates pressure extraction from the storage within an existing transaction.
     *
     * @param storage the target pressure storage
     * @param outer   the enclosing transaction
     * @return the simulated extracted amount
     */
    public static double simulateExtraction(PressureStorage storage, Transaction outer)
    {
        double amount;
        try(Transaction transaction = outer.openNested())
        {
            amount = storage.extract(Long.MAX_VALUE, storage.getUnit(), transaction);
            transaction.abort();
        }
        return amount;
    }

    /**
     * Simulates pressure extraction from the storage opening a temporary transaction.
     *
     * @param storage the target pressure storage
     * @return the simulated extracted amount
     */
    public static double simulateExtraction(PressureStorage storage)
    {
        double amount;
        try(Transaction transaction = Transaction.openOuter())
        {
            amount = storage.extract(Long.MAX_VALUE, storage.getUnit(), transaction);
            transaction.abort();
        }
        return amount;
    }

    /**
     * Checks whether the pressure storage contains zero pressure.
     *
     * @param storage the pressure storage to check
     * @return {@code true} if amount is zero, {@code false} otherwise
     */
    public static boolean isEmpty(PressureStorage storage)
    {
        return storage.getAmount() == 0;
    }

    /**
     * Checks whether the pressure storage is at or above capacity.
     *
     * @param storage the pressure storage to check
     * @return {@code true} if amount is equal to or exceeds capacity, {@code false} otherwise
     */
    public static boolean isStorageFull(PressureStorage storage)
    {
        return storage.getAmount() >= storage.getCapacity();
    }

    /**
     * Checks whether the pressure storage can accept the specified pressure amount without exceeding capacity.
     *
     * @param storage the pressure storage to check
     * @param amount  the pressure amount to test
     * @return {@code true} if capacity is sufficient, {@code false} otherwise
     */
    public static boolean canAccept(PressureStorage storage, int amount)
    {
        return storage.getAmount() + amount <= storage.getCapacity();
    }

    /**
     * Looks up an adjacent pressure storage in the specified direction, excluding blacklisted positions.
     *
     * @param world     the level
     * @param pos       the source block position
     * @param direction the direction to probe
     * @param blacklist optional set of blacklisted block positions
     * @return the adjacent pressure storage, or null if blacklisted or absent
     */
    public static PressureStorage getStorage(Level world, BlockPos pos, Direction direction, Set<BlockPos> blacklist)
    {
        BlockPos adjacentPos = pos.relative(direction);
        if(blacklist != null && blacklist.contains(adjacentPos))
            return null;
        return PressureStorage.SIDED.find(world, pos, direction.getOpposite());
    }

    /**
     * Finds all adjacent pressure storages across all directions, excluding blacklisted positions.
     *
     * @param world     the level
     * @param pos       the source block position
     * @param blacklist optional set of blacklisted block positions
     * @return a list of adjacent pressure storages
     */
    public static List<PressureStorage> getAllStorages(Level world, BlockPos pos, Set<BlockPos> blacklist)
    {
        List<PressureStorage> storages = new ArrayList<>();
        for (Direction direction : Direction.values())
        {
            BlockPos adjacentPos = pos.relative(direction);
            if(blacklist != null && blacklist.contains(adjacentPos))
                continue;
            PressureStorage storage = PressureStorage.SIDED.find(world, adjacentPos, direction.getOpposite());

            if(storage != null)
                storages.add(storage);
        }
        return storages;
    }

    /**
     * Spreads pressure from the source storage to adjacent block entities balancing pressure to an average.
     *
     * @param blockEntity the source block entity
     * @param storage     the source pressure storage
     * @param equalAmount whether to divide equally
     * @param blacklist   optional set of blacklisted block positions
     */
    public static void spread(BlockEntity blockEntity, PressureStorage storage, boolean equalAmount, Set<BlockPos> blacklist)
    {
        List<PressureStorage> storages = getAllStorages(blockEntity.getLevel(), blockEntity.getBlockPos(), blacklist);

        if(storages.isEmpty())
            return;

        for (PressureStorage heatStorage : storages)
        {
            try(Transaction transaction = Transaction.openOuter())
            {
                double current = storage.getAmount();
                double sideHeat = heatStorage.getAmount();
                double average = (current + sideHeat) * 0.5;

                if(current > average)
                    storage.extract(current - average, storage.getUnit(), transaction);
                else if(current < average)
                    storage.insert(average - current, storage.getUnit(), transaction);

                if(sideHeat > average)
                    heatStorage.extract(sideHeat - average, storage.getUnit(), transaction);
                else if(sideHeat < average)
                    heatStorage.insert(average - sideHeat, storage.getUnit(), transaction);

                transaction.commit();
            }
        }

        if(blockEntity instanceof AbstractBaseBE<?> be)
            be.update();
        else
            blockEntity.setChanged();
    }

    /**
     * Spreads pressure to adjacent block entities excluding blacklisted positions.
     *
     * @param blockEntity the source block entity
     * @param storage     the source pressure storage
     * @param blacklist   set of blacklisted block positions
     */
    public static void spread(BlockEntity blockEntity, PressureStorage storage, Set<BlockPos> blacklist)
    {
        spread(blockEntity, storage, true, blacklist);
    }

    /**
     * Spreads pressure to adjacent block entities without a blacklist.
     *
     * @param blockEntity the source block entity
     * @param storage     the source pressure storage
     * @param equalAmount whether to divide equally
     */
    public static void spread(BlockEntity blockEntity, PressureStorage storage, boolean equalAmount)
    {
        spread(blockEntity, storage, equalAmount, null);
    }

    /**
     * Spreads pressure equally to all adjacent compatible block entities.
     *
     * @param blockEntity the source block entity
     * @param storage     the source pressure storage
     */
    public static void spread(BlockEntity blockEntity, PressureStorage storage)
    {
        spread(blockEntity, storage, true, null);
    }
}