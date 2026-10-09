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

package com.dynamero.pressure.storage;

import net.minecraft.world.level.block.entity.BlockEntity;

import com.dynamero.shared.annotations.*;

/**
 * Input-only synchronized pressure storage that permits insertion and forbids extraction.
 */
@Developer("TheMentor")
@CreatedAt("2026-01-12")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
public class InputPressureStorage extends SyncedPressureStorage
{
    /**
     * Constructs an InputPressureStorage instance with the specified capacity and transfer limits.
     *
     * @param blockEntity the owning block entity
     * @param capacity    the maximum pressure capacity
     * @param maxInsert   the maximum pressure insertion rate
     * @param maxExtract  the maximum pressure extraction rate
     */
    public InputPressureStorage(BlockEntity blockEntity, double capacity, double maxInsert, double maxExtract)
    {
        super(blockEntity, capacity, maxInsert, maxExtract);
    }

    /**
     * Indicates whether pressure insertion is supported.
     *
     * @return {@code true}
     */
    @Override
    public boolean supportsInsertion()
    {
        return true;
    }

    /**
     * Indicates whether pressure extraction is supported.
     *
     * @return {@code false}
     */
    @Override
    public boolean supportsExtraction()
    {
        return false;
    }
}