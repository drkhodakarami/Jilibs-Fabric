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

package com.dynamero.mixin;

import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import com.dynamero.shared.annotations.*;

/**
 * Mixin accessor for {@link BlockEntityType} exposing the valid blocks set for runtime mutation.
 */
@Developer("TurtyWurty")
@ModifiedBy("TheMentor")
@CreatedAt("2025-04-18")
@Repository("https://github.com/DaRealTurtyWurty/Industria")
@Discord("https://discord.turtywurty.dev/")
@Youtube("https://www.youtube.com/@TurtyWurty")
@Mixin(BlockEntityType.class)
public interface BlockEntityTypeAccessor
{
    /**
     * Sets the collection of blocks valid for this block entity type.
     *
     * @param blocks the new set of valid blocks
     */
    @Accessor("validBlocks")
    void setBlocks(Set<Block> blocks);

    /**
     * Retrieves the current set of blocks valid for this block entity type.
     *
     * @return set of valid blocks
     */
    @Accessor("validBlocks")
    Set<Block> getBlocks();
}