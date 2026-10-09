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

package com.dynamero.shared.interfaces;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import com.dynamero.shared.annotations.*;

/**
 * Represents a factory for creating block entity tickers.
 *
 * @param <T> the type of the block entity
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

@FunctionalInterface
public interface BlockEntityTickerFactory<T extends BlockEntity>
{
    /**
     * Creates a new block entity ticker for the specified world, block state, and block entity type.
     *
     * @param world the world where the block entity is located
     * @param state the block state of the block
     * @param type  the type of the block entity
     * @return a new block entity ticker
     */
    BlockEntityTicker<T> create(Level world, BlockState state, BlockEntityType<?> type);
}