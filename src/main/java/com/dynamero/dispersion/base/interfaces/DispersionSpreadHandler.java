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

package com.dynamero.dispersion.base.interfaces;

import java.util.Set;

import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;

import com.dynamero.dispersion.utils.DispersionHelper;
import com.dynamero.shared.annotations.*;

/**
 * Handler interface providing default methods for simulating dispersion insertion and extraction,
 * and spreading stored dispersions across adjacent compatible block entities.
 *
 * @param <T> the dispersion storage type
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
public interface DispersionSpreadHandler<T extends Storage<DispersionVariant>>
{
    /**
     * Simulates dispersion insertion into the storage within an existing transaction.
     *
     * @param storage the dispersion storage to insert into
     * @param variant the dispersion variant to insert
     * @param outer   the enclosing transaction
     * @return the simulated inserted amount in droplets
     */
    default long simulateDispersionInsertion(T storage, DispersionVariant variant, Transaction outer)
    {
        return DispersionHelper.simulateInsertion(storage, variant, outer);
    }

    /**
     * Simulates dispersion insertion into the storage opening a temporary transaction.
     *
     * @param storage the dispersion storage to insert into
     * @param variant the dispersion variant to insert
     * @return the simulated inserted amount in droplets
     */
    default long simulateDispersionInsertion(T storage, DispersionVariant variant)
    {
        return DispersionHelper.simulateInsertion(storage, variant);
    }

    /**
     * Simulates dispersion extraction from the storage within an existing transaction.
     *
     * @param storage the dispersion storage to extract from
     * @param variant the dispersion variant to extract
     * @param outer   the enclosing transaction
     * @return the simulated extracted amount in droplets
     */
    default long simulateDispersionExtraction(T storage, DispersionVariant variant, Transaction outer)
    {
        return DispersionHelper.simulateExtraction(storage, variant, outer);
    }

    /**
     * Simulates dispersion extraction from the storage opening a temporary transaction.
     *
     * @param storage the dispersion storage to extract from
     * @param variant the dispersion variant to extract
     * @return the simulated extracted amount in droplets
     */
    default long simulateDispersionExtraction(T storage, DispersionVariant variant)
    {
        return DispersionHelper.simulateExtraction(storage, variant);
    }

    /**
     * Spreads dispersion from the storage to adjacent block entities with equal distribution and blacklist controls.
     *
     * @param blockEntity the source block entity
     * @param storage     the source dispersion storage
     * @param equalAmount whether to divide dispersion equally among all recipients
     * @param blacklist   set of block positions to exclude from transfer
     */
    default void spreadDispersion(BlockEntity blockEntity, T storage, boolean equalAmount, Set<BlockPos> blacklist)
    {
        DispersionHelper.spread(blockEntity, storage, equalAmount, blacklist);
    }

    /**
     * Spreads dispersion from the storage to adjacent block entities excluding specified positions.
     *
     * @param blockEntity the source block entity
     * @param storage     the source dispersion storage
     * @param blacklist   set of block positions to exclude from transfer
     */
    default void spreadDispersion(BlockEntity blockEntity, T storage, Set<BlockPos> blacklist)
    {
        DispersionHelper.spread(blockEntity, storage, blacklist);
    }

    /**
     * Spreads dispersion from the storage to adjacent block entities with equal amount control.
     *
     * @param blockEntity the source block entity
     * @param storage     the source dispersion storage
     * @param equalAmount whether to divide dispersion equally among all recipients
     */
    default void spreadDispersion(BlockEntity blockEntity, T storage, boolean equalAmount)
    {
        DispersionHelper.spread(blockEntity, storage, equalAmount);
    }

    /**
     * Spreads dispersion from the storage equally among all adjacent compatible block entities.
     *
     * @param blockEntity the source block entity
     * @param storage     the source dispersion storage
     */
    default void spreadDispersion(BlockEntity blockEntity, Storage<DispersionVariant> storage)
    {
        DispersionHelper.spread(blockEntity, storage);
    }
}