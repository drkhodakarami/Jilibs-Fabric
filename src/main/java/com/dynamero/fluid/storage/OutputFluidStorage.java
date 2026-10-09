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

package com.dynamero.fluid.storage;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;

import com.dynamero.shared.annotations.*;
import org.jspecify.annotations.NonNull;

/**
 * Output-only synchronized fluid storage dedicated to dispensing a specific {@link Fluid} type and forbidding insertion.
 */
@SuppressWarnings("unused")
@Developer("TheMentor")
@CreatedAt("2026-01-12")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
public class OutputFluidStorage extends SyncedFluidStorage
{
    /**
     * The specific fluid dispensed by this output storage.
     */
    private final Fluid fluid;

    /**
     * Constructs an OutputFluidStorage for the specified fluid and capacity.
     *
     * @param blockEntity the owning block entity
     * @param capacity    the maximum capacity in droplets
     * @param fluid       the extractable fluid type
     */
    public OutputFluidStorage(BlockEntity blockEntity, long capacity, Fluid fluid)
    {
        super(blockEntity, capacity);
        this.fluid = fluid;
    }

    /**
     * Indicates whether the fluid variant can be inserted into this storage.
     *
     * @param variant the fluid variant attempting insertion
     * @return {@code false}
     */
    @Override
    public boolean canInsert(@NonNull FluidVariant variant)
    {
        return false;
    }

    /**
     * Checks whether the given fluid variant matches the configured extractable fluid.
     *
     * @param variant the fluid variant attempting extraction
     * @return {@code true} if matching the expected fluid, {@code false} otherwise
     */
    @Override
    public boolean canExtract(@NonNull FluidVariant variant)
    {
        return variant.isOf(fluid);
    }
}