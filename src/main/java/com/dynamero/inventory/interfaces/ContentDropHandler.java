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

package com.dynamero.inventory.interfaces;

import net.minecraft.core.BlockPos;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.level.Level;

import com.dynamero.inventory.base.InventoryConnector;
import com.dynamero.shared.annotations.*;

/**
 * Interface providing default logic for dropping inventory contents into the world upon block removal.
 *
 * @param <T> the container inventory type
 */
@ThanksTo(discordUsers = "TheWhyEvenHow")
@Developer("TurtyWurty")
@CreatedAt("2026-10-08")
@ModifiedAt("2026-10-08")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public interface ContentDropHandler<T extends SimpleContainer> extends InventoryConnectorProvider<InventoryConnector<T>>
{
    /**
     * Drops all contents of the managed inventory connector at the specified world position.
     *
     * @param world the level
     * @param pos   the block position
     */
    default void dropContent(Level world, BlockPos pos)
    {
        InventoryConnector<?> inventory = getInventoryConnector();

        if(inventory != null)
            inventory.dropContent(world, pos);
    }
}