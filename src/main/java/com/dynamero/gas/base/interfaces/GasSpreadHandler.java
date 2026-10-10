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

package com.dynamero.gas.base.interfaces;

import java.util.Set;

import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;

import com.dynamero.gas.GasHelper;
import com.dynamero.shared.annotations.*;

/**
 * Handler interface providing default methods for simulating gas insertion and extraction,
 * and spreading stored gases across adjacent compatible block entities.
 *
 * @param <T> the gas storage type
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
public interface GasSpreadHandler<T extends Storage<GasVariant>>
{
    /**
     * Simulates gas insertion into the storage within an existing transaction.
     *
     * @param storage the gas storage to insert into
     * @param variant the gas variant to insert
     * @param outer   the enclosing transaction
     * @return the simulated inserted amount in droplets
     */
    default long simulateGasInsertion(T storage, GasVariant variant, Transaction outer)
    {
        return GasHelper.simulateInsertion(storage, variant, outer);
    }

    /**
     * Simulates gas insertion into the storage opening a temporary transaction.
     *
     * @param storage the gas storage to insert into
     * @param variant the gas variant to insert
     * @return the simulated inserted amount in droplets
     */
    default long simulateGasInsertion(T storage, GasVariant variant)
    {
        return GasHelper.simulateInsertion(storage, variant);
    }

    /**
     * Simulates gas extraction from the storage within an existing transaction.
     *
     * @param storage the gas storage to extract from
     * @param variant the gas variant to extract
     * @param outer   the enclosing transaction
     * @return the simulated extracted amount in droplets
     */
    default long simulateGasExtraction(T storage, GasVariant variant, Transaction outer)
    {
        return GasHelper.simulateExtraction(storage, variant, outer);
    }

    /**
     * Simulates gas extraction from the storage opening a temporary transaction.
     *
     * @param storage the gas storage to extract from
     * @param variant the gas variant to extract
     * @return the simulated extracted amount in droplets
     */
    default long simulateGasExtraction(T storage, GasVariant variant)
    {
        return GasHelper.simulateExtraction(storage, variant);
    }

    /**
     * Spreads gas from the storage to adjacent block entities with equal distribution and blacklist controls.
     *
     * @param blockEntity the source block entity
     * @param storage     the source gas storage
     * @param equalAmount whether to divide gas equally among all recipients
     * @param blacklist   set of block positions to exclude from transfer
     */
    default void spreadGas(BlockEntity blockEntity, T storage, boolean equalAmount, Set<BlockPos> blacklist)
    {
        GasHelper.spread(blockEntity, storage, equalAmount, blacklist);
    }

    /**
     * Spreads gas from the storage to adjacent block entities excluding specified positions.
     *
     * @param blockEntity the source block entity
     * @param storage     the source gas storage
     * @param blacklist   set of block positions to exclude from transfer
     */
    default void spreadGas(BlockEntity blockEntity, T storage, Set<BlockPos> blacklist)
    {
        GasHelper.spread(blockEntity, storage, blacklist);
    }

    /**
     * Spreads gas from the storage to adjacent block entities with equal amount control.
     *
     * @param blockEntity the source block entity
     * @param storage     the source gas storage
     * @param equalAmount whether to divide gas equally among all recipients
     */
    default void spreadGas(BlockEntity blockEntity, T storage, boolean equalAmount)
    {
        GasHelper.spread(blockEntity, storage, equalAmount);
    }

    /**
     * Spreads gas from the storage equally among all adjacent compatible block entities.
     *
     * @param blockEntity the source block entity
     * @param storage     the source gas storage
     */
    default void spreadGas(BlockEntity blockEntity, Storage<GasVariant> storage)
    {
        GasHelper.spread(blockEntity, storage);
    }
}