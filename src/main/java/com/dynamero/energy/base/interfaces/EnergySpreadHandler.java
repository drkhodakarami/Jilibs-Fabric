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

package com.dynamero.energy.base.interfaces;

import java.util.Set;

import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import team.reborn.energy.api.EnergyStorage;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;

import com.dynamero.energy.utils.EnergyHelper;
import com.dynamero.shared.annotations.*;

/**
 * Handler interface providing default methods for simulating energy insertion and extraction,
 * and spreading stored energy to adjacent block entities.
 *
 * @param <T> the energy storage type
 */
@SuppressWarnings("unused")
@Developer("TheMentor")
@CreatedAt("2026-08-10")
@ModifiedAt("2026-08-10")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public interface EnergySpreadHandler<T extends EnergyStorage>
{
    /**
     * Simulates energy insertion into the storage within an existing transaction.
     *
     * @param storage the energy storage to insert into
     * @param outer   the enclosing transaction
     * @return the simulated inserted amount
     */
    default long simulateInsertion(T storage, Transaction outer)
    {
        return EnergyHelper.simulateInsertion(storage, outer);
    }

    /**
     * Simulates energy insertion into the storage.
     *
     * @param storage the energy storage to insert into
     * @return the simulated inserted amount
     */
    default long simulateInsertion(T storage)
    {
        return EnergyHelper.simulateInsertion(storage);
    }

    /**
     * Simulates energy extraction from the storage within an existing transaction.
     *
     * @param storage the energy storage to extract from
     * @param outer   the enclosing transaction
     * @return the simulated extracted amount
     */
    default long simulateExtraction(T storage, Transaction outer)
    {
        return EnergyHelper.simulateExtraction(storage, outer);
    }

    /**
     * Simulates energy extraction from the storage.
     *
     * @param storage the energy storage to extract from
     * @return the simulated extracted amount
     */
    default long simulateExtraction(T storage)
    {
        return EnergyHelper.simulateExtraction(storage);
    }

    /**
     * Spreads energy from the storage to adjacent block entities with options for equal distribution and position exclusion.
     *
     * @param blockEntity the source block entity
     * @param storage     the source energy storage
     * @param equalAmount whether to distribute energy evenly among all recipients
     * @param blacklist   set of block positions to exclude from transfer
     */
    default void spread(BlockEntity blockEntity, T storage, boolean equalAmount, Set<BlockPos> blacklist)
    {
        EnergyHelper.spread(blockEntity, storage, equalAmount, blacklist);
    }

    /**
     * Spreads energy from the storage to adjacent block entities excluding specified positions.
     *
     * @param blockEntity the source block entity
     * @param storage     the source energy storage
     * @param blacklist   set of block positions to exclude from transfer
     */
    default void spread(BlockEntity blockEntity, T storage, Set<BlockPos> blacklist)
    {
        EnergyHelper.spread(blockEntity, storage, blacklist);
    }

    /**
     * Spreads energy from the storage to adjacent block entities with equal distribution control.
     *
     * @param blockEntity the source block entity
     * @param storage     the source energy storage
     * @param equalAmount whether to distribute energy evenly among all recipients
     */
    default void spread(BlockEntity blockEntity, T storage, boolean equalAmount)
    {
        EnergyHelper.spread(blockEntity, storage, equalAmount);
    }

    /**
     * Spreads energy from the storage to all adjacent compatible block entities.
     *
     * @param blockEntity the source block entity
     * @param storage     the source energy storage
     */
    default void spread(BlockEntity blockEntity, T storage)
    {
        EnergyHelper.spread(blockEntity, storage);
    }
}