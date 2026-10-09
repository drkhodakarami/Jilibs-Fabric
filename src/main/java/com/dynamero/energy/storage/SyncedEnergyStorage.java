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

import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import net.minecraft.world.level.block.entity.BlockEntity;

import com.dynamero.base.blockentity.AbstractBaseBE;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.interfaces.Syncable;
import com.dynamero.shared.interfaces.Updatable;

/**
 * Synchronized energy storage linked to a {@link BlockEntity} that automatically marks dirty
 * and triggers network/world updates when transactions are committed.
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
public class SyncedEnergyStorage extends SimpleEnergyStorage implements Syncable
{
    /**
     * The owning block entity for synchronization and state notifications.
     */
    private final BlockEntity blockEntity;

    /**
     * Flag indicating whether changes need to be synchronized to the client.
     */
    private boolean isDirty = false;

    /**
     * Constructs a SyncedEnergyStorage linked to the specified block entity.
     *
     * @param blockEntity the owning block entity
     * @param capacity    the maximum energy capacity
     * @param maxInsert   the maximum energy insertion rate per transaction
     * @param maxExtract  the maximum energy extraction rate per transaction
     */
    public SyncedEnergyStorage(BlockEntity blockEntity, long capacity, long maxInsert, long maxExtract)
    {
        super(capacity, maxInsert, maxExtract);
        this.blockEntity = blockEntity;
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
     * Synchronizes pending changes to clients if running on the server side and currently dirty.
     */
    @Override
    public void sync()
    {
        //noinspection DataFlowIssue
        if(this.isDirty && blockEntity != null && this.blockEntity.hasLevel() && !this.blockEntity.getLevel().isClientSide())
        {
            this.isDirty = false;
            if(blockEntity instanceof AbstractBaseBE<?> be)
                be.update();
            else
                blockEntity.setChanged();
        }
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