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

package com.dynamero.base.blockentity;

import com.dynamero.shared.annotations.*;
import com.dynamero.shared.interfaces.BlockEntityScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Abstract base block entity with screen/container menu integration.
 *
 * @param <T> the concrete block entity type extending this class
 * @param <B> the screen handler / container menu type
 */
@SuppressWarnings("unused")
@Developer("TurtyWurty")
@ModifiedBy("The Mentor")
@ThanksTo(discordUsers = "TheWhyEvenHow")
@CreatedAt("2025-04-18")
@Repository("https://github.com/DaRealTurtyWurty/Industria")
@Discord("https://discord.turtywurty.dev/")
@Youtube("https://www.youtube.com/@TurtyWurty")
public abstract class AbstractScreenBE<T extends AbstractScreenBE<T, B>, B> extends AbstractBaseBE<T> implements BlockEntityScreen<B>
{
	/**
	 * Constructs a new AbstractScreenBE with the specified block entity type, position, and state.
	 *
	 * @param type  the block entity type
	 * @param pos   the block position in the world
	 * @param state the initial block state
	 */
	public AbstractScreenBE(BlockEntityType<T> type, BlockPos pos, BlockState state)
	{
		super(type, pos, state);
	}
}