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
 * Input-only synchronized fluid storage dedicated to accepting a specific {@link Fluid} type and forbidding extraction.
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
public class InputFluidStorage extends SyncedFluidStorage
{
    /**
     * The specific fluid accepted by this input storage.
     */
    private final Fluid fluid;

    /**
     * Constructs an InputFluidStorage for the specified fluid and capacity.
     *
     * @param blockEntity the owning block entity
     * @param capacity    the maximum capacity in droplets
     * @param fluid       the accepted fluid type
     */
    public InputFluidStorage(BlockEntity blockEntity, long capacity, Fluid fluid)
    {
        super(blockEntity, capacity);
        this.fluid = fluid;
    }

    /**
     * Checks whether the given fluid variant matches the configured accepted fluid.
     *
     * @param variant the fluid variant attempting insertion
     * @return {@code true} if matching the expected fluid, {@code false} otherwise
     */
    @Override
    public boolean canInsert(@NonNull FluidVariant variant)
    {
        return variant.isOf(fluid);
    }

    /**
     * Indicates whether the fluid variant can be extracted from this storage.
     *
     * @param variant the fluid variant attempting extraction
     * @return {@code false}
     */
    @Override
    public boolean canExtract(@NonNull FluidVariant variant)
    {
        return false;
    }
}