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
 * Output-only synchronized heat storage that permits extraction and forbids insertion.
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
public class OutputHearStorage extends SyncedHeatStorage
{
    /**
     * Constructs an OutputHearStorage instance with the specified capacity and maximum extraction rate.
     *
     * @param blockEntity the owning block entity
     * @param capacity    the maximum heat capacity
     * @param maxExtract  the maximum heat extraction rate
     */
    public OutputHearStorage(BlockEntity blockEntity, double capacity, double maxExtract)
    {
        super(blockEntity, capacity, 0, maxExtract);
    }

    /**
     * Indicates whether heat insertion is supported.
     *
     * @return {@code false}
     */
    @Override
    public boolean supportsInsertion()
    {
        return false;
    }

    /**
     * Indicates whether heat extraction is supported.
     *
     * @return {@code true}
     */
    @Override
    public boolean supportsExtraction()
    {
        return true;
    }
}