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

package com.dynamero.base.block;

import java.util.OptionalInt;

import com.dynamero.shared.annotations.*;
import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Custom base leaves block supporting waterlogging, distance decay from logs,
 * persistent player placement, and tinted dripping leaf particles.
 */
@Developer("TurtyWurty")
@CreatedAt("2026-08-10")
@ModifiedAt("2026-08-10")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public class BaseLeavesBlock extends Block implements SimpleWaterloggedBlock
{
	/**
	 * Serialization codec for the leaves block.
	 */
	public static MapCodec<? extends BaseLeavesBlock> CODEC;

	/**
	 * The property name used for tracking leaf distance from logs.
	 */
	public static final String DISTANCE_PROPERTY_NAME = "distance";

	/**
	 * Maximum distance from a log before the leaves decay.
	 */
	public static final int DECAY_DISTANCE = 13;

	/**
	 * Integer block state property representing distance from supporting logs.
	 */
	public static final IntegerProperty DISTANCE = IntegerProperty.create(DISTANCE_PROPERTY_NAME, 1, DECAY_DISTANCE);

	/**
	 * Boolean block state property indicating if leaves are placed by player and won't decay.
	 */
	public static final BooleanProperty PERSISTENT = BlockStateProperties.PERSISTENT;

	/**
	 * Boolean block state property indicating if leaves are submerged in water.
	 */
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	/**
	 * Tick delay in ticks for leaf decay updates.
	 */
	private static final int TICK_DELAY = 1;

	/**
	 * Constructs a BaseLeavesBlock with default foliage properties and state defaults.
	 *
	 * @param settings the base block properties to configure
	 */
	public BaseLeavesBlock(Properties settings)
	{
		super(settings
					.mapColor(MapColor.PLANT)
					.strength(0.2F)
					.randomTicks()
					.sound(SoundType.GRASS)
					.noOcclusion()
					.isValidSpawn(Blocks::ocelotOrParrot)
					.isSuffocating(Blocks::never)
					.isViewBlocking(Blocks::never)
					.ignitedByLava()
					.pushReaction(PushReaction.DESTROY)
					.isRedstoneConductor(Blocks::never));

		registerDefaultState(this.stateDefinition.any()
								.setValue(DISTANCE, DECAY_DISTANCE)
								.setValue(PERSISTENT, Boolean.FALSE)
								.setValue(WATERLOGGED, Boolean.FALSE));
	}

	@Override
	protected @NotNull MapCodec<? extends Block> codec()
	{
		return CODEC;
	}

	@Override
	protected @NotNull VoxelShape getBlockSupportShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos)
	{
		return Shapes.empty();
	}

	@Override
	protected boolean isRandomlyTicking(BlockState state)
	{
		return state.getValue(DISTANCE) == DECAY_DISTANCE && !state.getValue(PERSISTENT);
	}

	@Override
	protected void randomTick(@NotNull BlockState state, @NotNull ServerLevel world, @NotNull BlockPos pos, @NotNull RandomSource random)
	{
		if(this.shouldDecay(state))
		{
			dropResources(state, world, pos);
			world.removeBlock(pos, false);
		}
	}

	@Override
	protected int getLightDampening(@NotNull BlockState state)
	{
		return 1;
	}

	@Override
	protected void tick(@NotNull BlockState state, ServerLevel world, @NotNull BlockPos pos, @NotNull RandomSource random)
	{
		world.setBlock(pos, updateDistanceFromLogs(state, world, pos), Block.UPDATE_ALL);
	}

	@Override
	protected @NotNull BlockState updateShape(BlockState state, @NotNull LevelReader world, @NotNull ScheduledTickAccess tickView, @NotNull BlockPos pos, @NotNull Direction direction, @NotNull BlockPos neighborPos, @NotNull BlockState neighborState, @NotNull RandomSource random)
	{
		if (state.getValue(WATERLOGGED))
			tickView.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));

		int distance = getDistanceFromLog(neighborState) + 1;
		if (distance != 1 || state.getValue(DISTANCE) != distance)
			tickView.scheduleTick(pos, this, TICK_DELAY);

		return state;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, @NotNull BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(DISTANCE, PERSISTENT, WATERLOGGED);
	}

	@Override
	protected @NotNull FluidState getFluidState(BlockState state)
	{
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx)
	{
		FluidState fluidState = ctx.getLevel().getFluidState(ctx.getClickedPos());
		BlockState blockState = defaultBlockState()
				.setValue(PERSISTENT, Boolean.TRUE)
				.setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
		return updateDistanceFromLogs(blockState, ctx.getLevel(), ctx.getClickedPos());
	}

	@Override
	public void animateTick(@NotNull BlockState state, Level world, BlockPos pos, @NotNull RandomSource random)
	{
		if (world.isRainingAt(pos.above()))
		{
			if (random.nextInt(15) == 1)
			{
				BlockPos blockPos = pos.below();
				BlockState blockState = world.getBlockState(blockPos);
				if (!blockState.canOcclude() || !blockState.isFaceSturdy(world, blockPos, Direction.UP))
					ParticleUtils.spawnParticleBelow(world, pos, random, ParticleTypes.DRIPPING_WATER);
			}
		}
	}

	/**
	 * Determines whether the leaves should decay naturally.
	 *
	 * @param state the block state
	 * @return true if the leaves are not persistent and reach maximum decay distance
	 */
	protected boolean shouldDecay(BlockState state)
	{
		return !state.getValue(PERSISTENT) && state.getValue(DISTANCE) == DECAY_DISTANCE;
	}

	/**
	 * Updates the distance property of the leaves block state based on surrounding logs and leaves.
	 *
	 * @param state the block state
	 * @param world the world accessor
	 * @param pos   the block position
	 * @return the updated block state
	 */
	private static BlockState updateDistanceFromLogs(BlockState state, LevelAccessor world, BlockPos pos)
	{
		int distance = DECAY_DISTANCE;
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

		for (Direction direction : Direction.values())
		{
			mutable.setWithOffset(pos, direction);
			distance = Math.min(distance, getDistanceFromLog(world.getBlockState(mutable)) + 1);
			if (distance == 1)
				break;
		}

		return state.setValue(DISTANCE, distance);
	}

	/**
	 * Computes the distance from a log for a neighbor block state.
	 *
	 * @param state the neighbor block state
	 * @return the distance value, or {@link #DECAY_DISTANCE} if not determined
	 */
	private static int getDistanceFromLog(BlockState state)
	{
		return getOptionalDistanceFromLog(state).orElse(DECAY_DISTANCE);
	}

	/**
	 * Retrieves the optional distance from a log based on whether the state is a log or has a distance property.
	 *
	 * @param state the block state
	 * @return an OptionalInt containing the distance if present, or empty
	 */
	public static OptionalInt getOptionalDistanceFromLog(BlockState state)
	{
		if (state.is(BlockTags.LOGS))
			return OptionalInt.of(0);
		else if (state.hasProperty(DISTANCE))
			return OptionalInt.of(state.getValue(DISTANCE));
		else if (state.hasProperty(LeavesBlock.DISTANCE))
			return OptionalInt.of(state.getValue(LeavesBlock.DISTANCE));
		else
			return OptionalInt.empty();
	}

	/**
	 * Spawns a tinted leaf particle below the leaf block if unobstructed.
	 *
	 * @param world    the world
	 * @param pos      the leaf block position
	 * @param random   the random source
	 * @param state    the leaf block state
	 * @param posBelow the block position directly below
	 */
	private void spawnLeafParticle(Level world, BlockPos pos, RandomSource random, BlockState state, BlockPos posBelow)
	{
		if(!(random.nextFloat() >= 0.01F))
			if(!isFaceFull(state.getCollisionShape(world, posBelow), Direction.UP))
				this.spawnLeafParticle(world, pos, random);
	}

	/**
	 * Spawns a tinted leaf particle at the given position.
	 *
	 * @param world  the world
	 * @param pos    the block position
	 * @param random the random source
	 */
	protected void spawnLeafParticle(Level world, BlockPos pos, RandomSource random)
	{
		var tintedParticleEffect = ColorParticleOption.create(ParticleTypes.TINTED_LEAVES, world.getClientLeafTintColor(pos));
		ParticleUtils.spawnParticleBelow(world, pos, random, tintedParticleEffect);
	}
}