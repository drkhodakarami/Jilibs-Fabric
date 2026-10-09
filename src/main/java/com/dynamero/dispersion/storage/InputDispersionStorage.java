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

package com.dynamero.dispersion.storage;

import org.jspecify.annotations.NonNull;

import net.minecraft.world.level.block.entity.BlockEntity;

import com.dynamero.dispersion.base.interfaces.DispersionVariant;
import com.dynamero.dispersion.base.records.Dispersion;
import com.dynamero.shared.annotations.*;

/**
 * Input-only synchronized dispersion storage dedicated to accepting a specific {@link Dispersion} type and forbidding extraction.
 */
@SuppressWarnings("unused")
@Developer("TheMentor")
@CreatedAt("2026-01-12")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
public class InputDispersionStorage extends SyncedDispersionStorage
{
    /**
     * The specific dispersion accepted by this input storage.
     */
    private final Dispersion dispersion;

    /**
     * Constructs an InputDispersionStorage for the specified dispersion and capacity.
     *
     * @param blockEntity the owning block entity
     * @param capacity    the maximum capacity in droplets
     * @param dispersion  the accepted dispersion type
     */
    public InputDispersionStorage(BlockEntity blockEntity, long capacity, Dispersion dispersion)
    {
        super(blockEntity, capacity);
        this.dispersion = dispersion;
    }

    /**
     * Checks whether the given dispersion variant matches the configured accepted dispersion.
     *
     * @param variant the dispersion variant attempting insertion
     * @return {@code true} if matching the expected dispersion, {@code false} otherwise
     */
    @Override
    public boolean canInsert(@NonNull DispersionVariant variant)
    {
        return variant.isOf(dispersion);
    }

    /**
     * Indicates whether the dispersion variant can be extracted from this storage.
     *
     * @param variant the dispersion variant attempting extraction
     * @return {@code false}
     */
    @Override
    public boolean canExtract(@NonNull DispersionVariant variant)
    {
        return false;
    }
}