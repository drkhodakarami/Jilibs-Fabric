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

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import com.dynamero.shared.annotations.*;

@SuppressWarnings("unused")
@Developer("TheMentor")
@CreatedAt("2026-10-08")
@ModifiedAt("2026-10-08")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public interface TickProvider
{
    <T extends BlockEntity> @Nullable BlockEntityTicker<T> fallbackTicker(Level level, BlockState state, BlockEntityType<T> type);

    default <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level)
    {
        return ((level1, blockPos, blockState, blockEntity) -> {
            if(blockEntity instanceof SyncedTicking syncedTicker)
            {
                if(!level.isClientSide())
                    syncedTicker.tick();
                else
                    syncedTicker.tickClient();
            }
            else if(blockEntity instanceof Ticking ticker)
            {
                if(!level.isClientSide())
                    ticker.tick();
                else
                    ticker.tickClient();
            }
        });
    }
}