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

package com.dynamero.dispersion.block;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import com.dynamero.base.block.AbstractBaseBlock;
import com.dynamero.dispersion.utils.DispersionHelper;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.properties.BlockProperties;

/**
 * Abstract base block for dispersion container blocks, enabling automatic player item interaction
 * for filling and emptying dispersion containers via {@link DispersionHelper#interactWithBlock}.
 */
@SuppressWarnings("unused")
@Developer("TheMentor")
@CreatedAt("2026-08-10")
@ModifiedAt("2026-08-10")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public abstract class AbstractDispersionContainerBlock extends AbstractBaseBlock
{
    /**
     * Constructs an AbstractDispersionContainerBlock enabling ticking on the block properties.
     *
     * @param properties      the vanilla block properties
     * @param blockProperties the custom block properties builder
     */
    public AbstractDispersionContainerBlock(Properties properties, BlockProperties<?> blockProperties)
    {
        super(properties, blockProperties.tick());
    }

    /**
     * Handles player item interactions on this block, attempting dispersion transfer before falling back to default behavior.
     *
     * @param stack  the item stack held by the player
     * @param state  the current block state
     * @param level  the world level
     * @param pos    the block position
     * @param player the interacting player
     * @param hand   the interaction hand used
     * @param hit    the block hit raycast result
     * @return {@link InteractionResult#SUCCESS} if a dispersion interaction occurred, or super result otherwise
     */
    @Override
    protected @NotNull InteractionResult useItemOn(ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit)
    {
        if(DispersionHelper.interactWithBlock(level, pos, player, hand))
            return InteractionResult.SUCCESS;
        return super.useItemOn(stack, state, level, pos, player, hand, hit);
    }
}