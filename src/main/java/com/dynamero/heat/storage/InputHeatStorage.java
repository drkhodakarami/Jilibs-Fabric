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

package com.dynamero.heat.storage;

import net.minecraft.world.level.block.entity.BlockEntity;

import com.dynamero.shared.annotations.*;

/**
 * Input-only synchronized heat storage that permits insertion and forbids extraction.
 */
@SuppressWarnings("unused")
@Developer("TurtyWurty")
@CreatedAt("2026-10-08")
@ModifiedAt("2026-10-08")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public class InputHeatStorage extends SyncedHeatStorage
{
    /**
     * Constructs an InputHeatStorage instance with the specified capacity and maximum insertion rate.
     *
     * @param blockEntity the owning block entity
     * @param capacity    the maximum heat capacity
     * @param maxInsert   the maximum heat insertion rate
     */
    public InputHeatStorage(BlockEntity blockEntity, double capacity, double maxInsert)
    {
        super(blockEntity, capacity, maxInsert, 0);
    }

    /**
     * Indicates whether heat insertion is supported.
     *
     * @return {@code true}
     */
    @Override
    public boolean supportsInsertion()
    {
        return true;
    }

    /**
     * Indicates whether heat extraction is supported.
     *
     * @return {@code false}
     */
    @Override
    public boolean supportsExtraction()
    {
        return false;
    }
}