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

package com.dynamero.gas.storage;

import net.minecraft.world.level.block.entity.BlockEntity;

import com.dynamero.gas.base.interfaces.GasVariant;
import com.dynamero.gas.base.records.Gas;
import com.dynamero.shared.annotations.*;
import org.jspecify.annotations.NonNull;

/**
 * Input-only synchronized gas storage dedicated to accepting a specific {@link Gas} type and forbidding extraction.
 */
@SuppressWarnings("unused")
@Developer("TheMentor")
@CreatedAt("2026-01-12")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
public class InputGasStorage extends SyncedGasStorage
{
    /**
     * The specific gas accepted by this input storage.
     */
    private final Gas gas;

    /**
     * Constructs an InputGasStorage for the specified gas and capacity.
     *
     * @param blockEntity the owning block entity
     * @param capacity    the maximum capacity in droplets
     * @param gas         the accepted gas type
     */
    public InputGasStorage(BlockEntity blockEntity, long capacity, Gas gas)
    {
        super(blockEntity, capacity);
        this.gas = gas;
    }

    /**
     * Checks whether the given gas variant matches the configured accepted gas.
     *
     * @param variant the gas variant attempting insertion
     * @return {@code true} if matching the expected gas, {@code false} otherwise
     */
    @Override
    public boolean canInsert(@NonNull GasVariant variant)
    {
        return variant.isOf(gas);
    }

    /**
     * Indicates whether the gas variant can be extracted from this storage.
     *
     * @param variant the gas variant attempting extraction
     * @return {@code false}
     */
    @Override
    public boolean canExtract(@NonNull GasVariant variant)
    {
        return false;
    }
}