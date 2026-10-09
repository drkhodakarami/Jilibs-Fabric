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
import org.jetbrains.annotations.Nullable;

import net.minecraft.world.level.block.entity.BlockEntity;

import com.dynamero.base.blockentity.AbstractBaseBE;
import com.dynamero.fluid.base.SingleFluidStorage;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.interfaces.Syncable;
import com.dynamero.shared.interfaces.Updatable;
import com.dynamero.shared.network.FluidComponent;
import org.jspecify.annotations.NonNull;

/**
 * Synchronized single fluid storage linked to a {@link BlockEntity} that tracks dirty state,
 * triggers block updates on transaction commit, and synchronizes with clients.
 */
@SuppressWarnings("unused")
@Developer("TheMentor")
@CreatedAt("2026-01-12")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
public class SyncedFluidStorage extends SingleFluidStorage implements Syncable
{
    /**
     * The owning block entity for synchronization and state notifications.
     */
    private final BlockEntity blockEntity;

    /**
     * Maximum fluid capacity in droplet units.
     */
    private final long capacity;

    /**
     * Flag indicating whether changes need to be synchronized to the client.
     */
    private boolean isDirty = false;

    /**
     * Constructs a SyncedFluidStorage linked to the specified block entity with a fixed capacity.
     *
     * @param blockEntity the owning block entity
     * @param capacity    the maximum capacity in droplets
     */
    public SyncedFluidStorage(BlockEntity blockEntity, long capacity)
    {
        this.blockEntity = blockEntity;
        this.capacity = capacity;
    }

    /**
     * Synchronizes pending changes to clients if running on the server side and currently dirty.
     */
    @Override
    public void sync()
    {
        //noinspection DataFlowIssue
        if(this.isDirty && this.blockEntity != null && this.blockEntity.hasLevel() && !this.blockEntity.getLevel().isClientSide())
        {
            this.isDirty = false;
            if(this.blockEntity instanceof AbstractBaseBE<?> be)
                be.update();
            else
                this.blockEntity.setChanged();
        }
    }

    /**
     * Retrieves the maximum capacity for the given fluid variant.
     *
     * @param variant the fluid variant
     * @return the capacity in droplets
     */
    @Override
    protected long getCapacity(@NonNull FluidVariant variant)
    {
        return this.capacity;
    }

    /**
     * Invoked upon successful transaction commit, marking the storage dirty and updating the block entity.
     */
    @Override
    protected void onFinalCommit()
    {
        super.onFinalCommit();
        this.isDirty = true;

        if(blockEntity instanceof Updatable updatable)
            updatable.update();
        else
            blockEntity.setChanged();
    }

    /**
     * Evaluates whether the given fluid variant can be inserted into this storage.
     *
     * @param variant the fluid variant attempting insertion
     * @return {@code true} if insertion is permitted, {@code false} otherwise
     */
    @Override
    public boolean canInsert(@NonNull FluidVariant variant)
    {
        return super.canInsert(variant);
    }

    /**
     * Evaluates whether the given fluid variant can be extracted from this storage.
     *
     * @param variant the fluid variant attempting extraction
     * @return {@code true} if extraction is permitted, {@code false} otherwise
     */
    @Override
    public boolean canExtract(@NonNull FluidVariant variant)
    {
        return super.canExtract(variant);
    }

    /**
     * Checks whether the specified fluid component stack can be fully inserted into this storage.
     *
     * @param fluidStack the fluid component to test
     * @return {@code true} if matching variant or blank and sufficient capacity remains, {@code false} otherwise
     */
    public boolean canInsert(FluidComponent fluidStack)
    {
        return (this.variant == fluidStack.getFluid() || this.variant.isBlank()) && fluidStack.getAmount() <= this.capacity - this.amount;
    }

    /**
     * Checks whether the specified fluid component stack can be fully extracted from this storage.
     *
     * @param fluidStack the fluid component to test
     * @return {@code true} if matching variant and sufficient fluid is stored, {@code false} otherwise
     */
    public boolean canExtract(FluidComponent fluidStack)
    {
        return this.variant == fluidStack.getFluid() && fluidStack.getAmount() <= this.amount;
    }

    /**
     * Explicitly marks this storage as dirty requiring client synchronization.
     */
    public void markDirty()
    {
        this.isDirty = true;
    }

    /**
     * Retrieves the associated block entity.
     *
     * @return the block entity
     */
    public BlockEntity getBlockEntity()
    {
        return blockEntity;
    }

    /**
     * Retrieves the block entity cast to {@link AbstractBaseBE} if applicable.
     *
     * @return the cast block entity, or null if it does not extend AbstractBaseBE
     */
    @Nullable
    public AbstractBaseBE<?> getBE()
    {
        if(this.blockEntity instanceof AbstractBaseBE<?> be)
            return be;
        return  null;
    }
}