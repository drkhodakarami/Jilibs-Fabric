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

package com.dynamero.pressure.base.interfaces;

import java.util.Set;

import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;

import com.dynamero.pressure.PressureHelper;
import com.dynamero.shared.annotations.*;

/**
 * Handler interface providing default methods for simulating pressure insertion and extraction,
 * and spreading stored pressure across adjacent block entities.
 *
 * @param <T> the pressure storage type
 */
@Developer("TheMentor")
@CreatedAt("2026-10-08")
@ModifiedAt("2026-10-08")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public interface PressureSpreadHandler<T extends PressureStorage>
{
    /**
     * Simulates pressure insertion into the storage within an existing transaction.
     *
     * @param storage the pressure storage to insert into
     * @param outer   the enclosing transaction
     * @return the simulated inserted amount
     */
    default double simulateInsertion(T storage, Transaction outer)
    {
        return PressureHelper.simulateInsertion(storage, outer);
    }

    /**
     * Simulates pressure insertion into the storage opening a temporary transaction.
     *
     * @param storage the pressure storage to insert into
     * @return the simulated inserted amount
     */
    default double simulateInsertion(T storage)
    {
        return PressureHelper.simulateInsertion(storage);
    }

    /**
     * Simulates pressure extraction from the storage within an existing transaction.
     *
     * @param storage the pressure storage to extract from
     * @param outer   the enclosing transaction
     * @return the simulated extracted amount
     */
    default double simulateExtraction(T storage, Transaction outer)
    {
        return PressureHelper.simulateExtraction(storage, outer);
    }

    /**
     * Simulates pressure extraction from the storage opening a temporary transaction.
     *
     * @param storage the pressure storage to extract from
     * @return the simulated extracted amount
     */
    default double simulateExtraction(T storage)
    {
        return PressureHelper.simulateExtraction(storage);
    }

    /**
     * Spreads pressure to adjacent block entities balancing pressure to an average, with blacklist support.
     *
     * @param blockEntity the source block entity
     * @param storage     the source pressure storage
     * @param equalAmount whether to distribute equally
     * @param blacklist   set of block positions to exclude
     */
    default void spread(BlockEntity blockEntity, T storage, boolean equalAmount, Set<BlockPos> blacklist)
    {
        PressureHelper.spread(blockEntity, storage, equalAmount, blacklist);
    }

    /**
     * Spreads pressure to adjacent block entities excluding blacklisted positions.
     *
     * @param blockEntity the source block entity
     * @param storage     the source pressure storage
     * @param blacklist   set of block positions to exclude
     */
    default void spread(BlockEntity blockEntity, T storage, Set<BlockPos> blacklist)
    {
        PressureHelper.spread(blockEntity, storage, blacklist);
    }

    /**
     * Spreads pressure to adjacent block entities with equal distribution control and no blacklist.
     *
     * @param blockEntity the source block entity
     * @param storage     the source pressure storage
     * @param equalAmount whether to distribute equally
     */
    default void spread(BlockEntity blockEntity, T storage, boolean equalAmount)
    {
        PressureHelper.spread(blockEntity, storage, equalAmount);
    }

    /**
     * Spreads pressure to all adjacent compatible block entities.
     *
     * @param blockEntity the source block entity
     * @param storage     the source pressure storage
     */
    default void spread(BlockEntity blockEntity, T storage)
    {
        PressureHelper.spread(blockEntity, storage);
    }
}