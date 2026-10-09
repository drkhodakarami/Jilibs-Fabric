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
 * Input-only synchronized energy storage that accepts incoming energy and forbids extraction.
 */
@SuppressWarnings("unused")
@Developer("TurtyWurty")
@CreatedAt("2026-08-10")
@ModifiedAt("2026-08-10")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public class InputEnergyStorage extends SyncedEnergyStorage
{
    /**
     * Constructs an InputEnergyStorage with the specified capacity and maximum insertion rate.
     *
     * @param blockEntity the owning block entity
     * @param capacity    the maximum energy capacity
     * @param maxInsert   the maximum energy insertion rate per transaction
     */
    public InputEnergyStorage(BlockEntity blockEntity, long capacity, long maxInsert)
    {
        super(blockEntity, capacity, maxInsert, 0);
    }

    /**
     * Indicates whether energy insertion is supported.
     *
     * @return {@code true}
     */
    @Override
    public boolean supportsInsertion()
    {
        return true;
    }

    /**
     * Indicates whether energy extraction is supported.
     *
     * @return {@code false}
     */
    @Override
    public boolean supportsExtraction()
    {
        return false;
    }
}