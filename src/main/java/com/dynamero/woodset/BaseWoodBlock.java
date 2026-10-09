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

package com.dynamero.woodset;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

import com.dynamero.shared.annotations.*;
import com.dynamero.shared.interfaces.TickedBlock;

/**
 * Represents a custom wood block based on {@link RotatedPillarBlock}, supporting random ticking functionality.
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
public class BaseWoodBlock extends RotatedPillarBlock implements TickedBlock
{
    /**
     * Indicates whether this block represents a stripped variant of a wood log or wood block.
     */
    public final boolean isStripped;

    /**
     * Constructs a new BaseWoodBlock.
     *
     * @param settings   the block behavior settings
     * @param isStripped {@code true} if this is a stripped wood block, {@code false} otherwise
     */
    public BaseWoodBlock(Properties settings, boolean isStripped)
    {
        super(settings);
        this.isStripped = isStripped;

        registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.Y).setValue(TICK_LEVEL, 0));
    }

    /**
     * Gets the block state for placement, configuring initial random tick level properties.
     *
     * @param ctx the block placement context
     * @return the configured placement block state
     */
    @Override
    public @NotNull BlockState getStateForPlacement(@NotNull BlockPlaceContext ctx)
    {
        return withRandomTick(ctx, super.getStateForPlacement(ctx), this, 0, 20);
    }

    /**
     * Adds block state properties including {@link TickedBlock#TICK_LEVEL} to the state definition builder.
     *
     * @param builder the block state definition builder
     */
    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, @NotNull BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(TICK_LEVEL);
    }

    /**
     * Handles random server ticks for custom ticking logic.
     *
     * @param state  the current block state
     * @param world  the server level
     * @param pos    the block position
     * @param random the random source
     */
    @Override
    protected void randomTick(@NotNull BlockState state, @NotNull ServerLevel world, @NotNull BlockPos pos, @NotNull RandomSource random)
    {
        super.randomTick(state, world, pos, random);
        tickBlock(state, world, pos, random);
    }

    /**
     * Determines whether this block state is currently eligible for random ticking.
     *
     * @param state the block state to check
     * @return {@code true} if stripped and tick level is positive, {@code false} otherwise
     */
    @Override
    protected boolean isRandomlyTicking(@NotNull BlockState state)
    {
        return this.isStripped && state.getValue(TICK_LEVEL) > 0;
    }
}