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

package com.dynamero.energy.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import team.reborn.energy.api.EnergyStorage;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import com.dynamero.base.blockentity.AbstractBaseBE;
import com.dynamero.shared.annotations.*;

/**
 * Utility methods for energy transaction simulation, status checks, adjacent storage discovery,
 * and automated energy distribution among adjacent block entities.
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
public class EnergyHelper
{
    /**
     * Simulates energy insertion into the storage within an existing transaction.
     *
     * @param storage the target energy storage
     * @param outer   the enclosing transaction
     * @return the simulated inserted amount
     */
    public static long simulateInsertion(EnergyStorage storage, Transaction outer)
    {
        long amount;
        try(Transaction transaction = outer.openNested())
        {
            amount = storage.insert(Long.MAX_VALUE, transaction);
            transaction.abort();
        }
        return amount;
    }

    /**
     * Simulates energy insertion into the storage opening a temporary transaction.
     *
     * @param storage the target energy storage
     * @return the simulated inserted amount
     */
    public static long simulateInsertion(EnergyStorage storage)
    {
        long amount;
        try(Transaction transaction = Transaction.openOuter())
        {
            amount = storage.insert(Long.MAX_VALUE, transaction);
            transaction.abort();
        }
        return amount;
    }

    /**
     * Simulates energy extraction from the storage within an existing transaction.
     *
     * @param storage the target energy storage
     * @param outer   the enclosing transaction
     * @return the simulated extracted amount
     */
    public static long simulateExtraction(EnergyStorage storage, Transaction outer)
    {
        long amount;
        try(Transaction transaction = outer.openNested())
        {
            amount = storage.extract(Long.MAX_VALUE, transaction);
            transaction.abort();
        }
        return amount;
    }

    /**
     * Simulates energy extraction from the storage opening a temporary transaction.
     *
     * @param storage the target energy storage
     * @return the simulated extracted amount
     */
    public static long simulateExtraction(EnergyStorage storage)
    {
        long amount;
        try(Transaction transaction = Transaction.openOuter())
        {
            amount = storage.extract(Long.MAX_VALUE, transaction);
            transaction.abort();
        }
        return amount;
    }

    /**
     * Checks whether the energy storage is empty.
     *
     * @param storage the energy storage to check
     * @return {@code true} if the stored energy is 0, {@code false} otherwise
     */
    public static boolean isEmpty(EnergyStorage storage)
    {
        return storage.getAmount() == 0;
    }

    /**
     * Checks whether the energy storage is completely full.
     *
     * @param storage the energy storage to check
     * @return {@code true} if stored amount is equal to or exceeds capacity, {@code false} otherwise
     */
    public static boolean isStorageFull(EnergyStorage storage)
    {
        return storage.getAmount() >= storage.getCapacity();
    }

    /**
     * Checks whether the energy storage has enough capacity remaining to accept the specified amount.
     *
     * @param storage the energy storage to check
     * @param amount  the energy amount to test
     * @return {@code true} if the storage can accommodate the amount, {@code false} otherwise
     */
    public static boolean canAccept(EnergyStorage storage, int amount)
    {
        return storage.getAmount() + amount <= storage.getCapacity();
    }

    /**
     * Looks up an adjacent energy storage in the specified direction, excluding blacklisted positions.
     *
     * @param world     the level
     * @param pos       the source block position
     * @param direction the direction to probe
     * @param blacklist optional set of blacklisted block positions
     * @return the adjacent energy storage, or null if none found or position is blacklisted
     */
    public static EnergyStorage getStorage(Level world, BlockPos pos, Direction direction, Set<BlockPos> blacklist)
    {
        BlockPos adjacentPos = pos.relative(direction);
        if(blacklist != null && blacklist.contains(adjacentPos))
            return null;
        return EnergyStorage.SIDED.find(world, pos, direction.getOpposite());
    }

    /**
     * Finds all adjacent energy storages across all directions, excluding blacklisted positions.
     *
     * @param world     the level
     * @param pos       the source block position
     * @param blacklist optional set of blacklisted block positions
     * @return a list of adjacent energy storages
     */
    public static List<EnergyStorage> getAllStorages(Level world, BlockPos pos, Set<BlockPos> blacklist)
    {
        List<EnergyStorage> storages = new ArrayList<>();
        for (Direction direction : Direction.values())
        {
            BlockPos adjacentPos = pos.relative(direction);
            if(blacklist != null && blacklist.contains(adjacentPos))
                continue;
            EnergyStorage storage = EnergyStorage.SIDED.find(world, adjacentPos, direction.getOpposite());

            if(storage != null)
                storages.add(storage);
        }
        return storages;
    }

    /**
     * Spreads energy from the storage to adjacent block entities with equal amount and blacklist controls.
     *
     * @param blockEntity the source block entity
     * @param storage     the source energy storage
     * @param equalAmount whether to divide energy equally among all recipients
     * @param blacklist   optional set of block positions to exclude from transfer
     */
    public static void spread(BlockEntity blockEntity, EnergyStorage storage, boolean equalAmount, Set<BlockPos> blacklist)
    {
        List<EnergyStorage> storages = getAllStorages(blockEntity.getLevel(), blockEntity.getBlockPos(), blacklist);

        if(storages.isEmpty())
            return;

        long extractable = simulateExtraction(storage);
        long finalAmount = equalAmount ? extractable / storages.size() : extractable;
        long current = storage.getAmount();
        long totalInserted = 0;

        try(Transaction transaction = Transaction.openOuter())
        {
            for (EnergyStorage adjacentStorage : storages)
            {
                var insertable = simulateInsertion(adjacentStorage, transaction);

                if(insertable < finalAmount)
                    continue;

                long inserted = adjacentStorage.insert(finalAmount, transaction);
                totalInserted += inserted;
            }

            if(totalInserted > 0)
            {
                try(Transaction inner = transaction.openNested())
                {
                    var extracted = storage.extract(totalInserted, inner);
                    if(extracted > 0)
                        inner.commit();
                }
            }

            transaction.commit();

            if(current != storage.getAmount())
            {
                if (blockEntity instanceof AbstractBaseBE<?> be)
                    be.update();
                else
                    blockEntity.setChanged();
            }
        }
    }

    /**
     * Spreads energy from the storage equally among adjacent block entities excluding blacklisted positions.
     *
     * @param blockEntity the source block entity
     * @param storage     the source energy storage
     * @param blacklist   optional set of block positions to exclude from transfer
     */
    public static void spread(BlockEntity blockEntity, EnergyStorage storage, Set<BlockPos> blacklist)
    {
        spread(blockEntity, storage, true, blacklist);
    }

    /**
     * Spreads energy from the storage to adjacent block entities with equal amount control and no blacklist.
     *
     * @param blockEntity the source block entity
     * @param storage     the source energy storage
     * @param equalAmount whether to divide energy equally among all recipients
     */
    public static void spread(BlockEntity blockEntity, EnergyStorage storage, boolean equalAmount)
    {
        spread(blockEntity, storage, equalAmount, null);
    }

    /**
     * Spreads energy from the storage equally among all adjacent block entities.
     *
     * @param blockEntity the source block entity
     * @param storage     the source energy storage
     */
    public static void spread(BlockEntity blockEntity, EnergyStorage storage)
    {
        spread(blockEntity, storage, true, null);
    }
}