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

package com.dynamero.fluid.base.interfaces;

import java.util.Set;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;

import com.dynamero.fluid.FluidHelper;
import com.dynamero.shared.annotations.*;

/**
 * Handler interface providing default methods for simulating fluid insertion and extraction,
 * and spreading stored fluids across adjacent compatible block entities.
 *
 * @param <T> the fluid storage type
 */
@SuppressWarnings("unused")
@Developer("TheMentor")
@CreatedAt("2026-01-12")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
public interface FluidSpreadHandler<T extends Storage<FluidVariant>>
{
    /**
     * Simulates fluid insertion into the storage within an existing transaction.
     *
     * @param storage the fluid storage to insert into
     * @param variant the fluid variant to insert
     * @param outer   the enclosing transaction
     * @return the simulated inserted amount in droplets
     */
    default long simulateFluidInsertion(T storage, FluidVariant variant, Transaction outer)
    {
        return FluidHelper.simulateInsertion(storage, variant, outer);
    }

    /**
     * Simulates fluid insertion into the storage opening a temporary transaction.
     *
     * @param storage the fluid storage to insert into
     * @param variant the fluid variant to insert
     * @return the simulated inserted amount in droplets
     */
    default long simulateFluidInsertion(T storage, FluidVariant variant)
    {
        return FluidHelper.simulateInsertion(storage, variant);
    }

    /**
     * Simulates fluid extraction from the storage within an existing transaction.
     *
     * @param storage the fluid storage to extract from
     * @param variant the fluid variant to extract
     * @param outer   the enclosing transaction
     * @return the simulated extracted amount in droplets
     */
    default long simulateFluidExtraction(T storage, FluidVariant variant, Transaction outer)
    {
        return FluidHelper.simulateExtraction(storage, variant, outer);
    }

    /**
     * Simulates fluid extraction from the storage opening a temporary transaction.
     *
     * @param storage the fluid storage to extract from
     * @param variant the fluid variant to extract
     * @return the simulated extracted amount in droplets
     */
    default long simulateFluidExtraction(T storage, FluidVariant variant)
    {
        return FluidHelper.simulateExtraction(storage, variant);
    }

    /**
     * Spreads fluid from the storage to adjacent block entities with equal distribution and blacklist controls.
     *
     * @param blockEntity the source block entity
     * @param storage     the source fluid storage
     * @param equalAmount whether to divide fluid equally among all recipients
     * @param blacklist   set of block positions to exclude from transfer
     */
    default void spreadFluid(BlockEntity blockEntity, T storage, boolean equalAmount, Set<BlockPos> blacklist)
    {
        FluidHelper.spread(blockEntity, storage, equalAmount, blacklist);
    }

    /**
     * Spreads fluid from the storage to adjacent block entities excluding specified positions.
     *
     * @param blockEntity the source block entity
     * @param storage     the source fluid storage
     * @param blacklist   set of block positions to exclude from transfer
     */
    default void spreadFluid(BlockEntity blockEntity, T storage, Set<BlockPos> blacklist)
    {
        FluidHelper.spread(blockEntity, storage, blacklist);
    }

    /**
     * Spreads fluid from the storage to adjacent block entities with equal amount control.
     *
     * @param blockEntity the source block entity
     * @param storage     the source fluid storage
     * @param equalAmount whether to divide fluid equally among all recipients
     */
    default void spreadFluid(BlockEntity blockEntity, T storage, boolean equalAmount)
    {
        FluidHelper.spread(blockEntity, storage, equalAmount);
    }

    /**
     * Spreads fluid from the storage equally among all adjacent compatible block entities.
     *
     * @param blockEntity the source block entity
     * @param storage     the source fluid storage
     */
    default void spreadFluid(BlockEntity blockEntity, Storage<FluidVariant> storage)
    {
        FluidHelper.spread(blockEntity, storage);
    }
}