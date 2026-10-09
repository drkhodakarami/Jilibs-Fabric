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

package com.dynamero.energy.storage;

import net.minecraft.world.level.block.entity.BlockEntity;

import com.dynamero.shared.annotations.*;

/**
 * Output-only synchronized energy storage that provides energy for extraction and forbids insertion.
 */
@SuppressWarnings("unused")
@Developer("TheMentor")
@CreatedAt("2026-01-12")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
public class OutputEnergyStorage extends SyncedEnergyStorage
{
    /**
     * Constructs an OutputEnergyStorage with the specified capacity and maximum extraction rate.
     *
     * @param blockEntity the owning block entity
     * @param capacity    the maximum energy capacity
     * @param maxExtract  the maximum energy extraction rate per transaction
     */
    public OutputEnergyStorage(BlockEntity blockEntity, long capacity, long maxExtract)
    {
        super(blockEntity, capacity, 0, maxExtract);
    }

    /**
     * Indicates whether energy insertion is supported.
     *
     * @return {@code false}
     */
    @Override
    public boolean supportsInsertion()
    {
        return false;
    }

    /**
     * Indicates whether energy extraction is supported.
     *
     * @return {@code true}
     */
    @Override
    public boolean supportsExtraction()
    {
        return true;
    }
}