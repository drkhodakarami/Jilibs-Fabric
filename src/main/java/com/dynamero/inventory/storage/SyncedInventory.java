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

package com.dynamero.inventory.storage;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import com.dynamero.base.blockentity.AbstractBaseBE;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.interfaces.Syncable;

/**
 * Recipe-compatible inventory synchronized with a block entity, automatically tracking changes and updating state.
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
public class SyncedInventory extends RecipeInventory implements Syncable
{
    /**
     * Owning block entity associated with this inventory.
     */
    private final BlockEntity blockEntity;

    /**
     * Dirty flag indicating whether inventory contents have been modified since the last sync.
     */
    private boolean isDirty = false;

    /**
     * Constructs a SyncedInventory associated with a block entity and specific slot count.
     *
     * @param blockEntity the owning block entity
     * @param size        the slot count
     */
    public SyncedInventory(BlockEntity blockEntity, int size)
    {
        super(size);
        this.blockEntity = blockEntity;
    }

    /**
     * Constructs a SyncedInventory associated with a block entity and initialized with item stacks.
     *
     * @param blockEntity the owning block entity
     * @param items       the initial item stacks
     */
    public SyncedInventory(BlockEntity blockEntity, ItemStack... items)
    {
        super(items);
        this.blockEntity = blockEntity;
    }

    /**
     * Synchronizes inventory state with the server block entity if modified.
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
     * Marks the inventory as modified, triggering the dirty flag for subsequent synchronization.
     */
    @Override
    public void setChanged()
    {
        super.setChanged();
        this.isDirty = true;
    }

    /**
     * Retrieves the associated block entity.
     *
     * @return the owning block entity
     */
    public BlockEntity getBlockEntity()
    {
        return this.blockEntity;
    }

    /**
     * Retrieves the associated block entity cast to {@link AbstractBaseBE} if applicable.
     *
     * @return the block entity cast to {@link AbstractBaseBE}, or {@code null}
     */
    @Nullable
    public AbstractBaseBE<?> getJiBlockEntity()
    {
        if(this.blockEntity instanceof AbstractBaseBE<?> be)
            return be;
        return  null;
    }
}