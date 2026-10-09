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

package com.dynamero.inventory.base;

import org.jetbrains.annotations.NotNull;
import oshi.util.tuples.Pair;

import net.minecraft.world.SimpleContainer;

import com.dynamero.shared.annotations.*;
import com.dynamero.shared.enumerations.MappedDirection;

/**
 * Container-handler inventory connector variant providing factory methods to duplicate connector layouts.
 *
 * @param <T> the container inventory type
 */
@SuppressWarnings("unused")
@ThanksTo(discordUsers = "TheWhyEvenHow")
@Developer("TurtyWurty")
@CreatedAt("2026-08-10")
@ModifiedAt("2026-08-10")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public class InventoryConnectorCH<T extends SimpleContainer> extends InventoryConnector<T>
{
    /**
     * Creates a new InventoryConnectorCH with a structural copy of the given connector's sided inventories.
     *
     * @param inventory the source inventory connector
     * @return a cloned InventoryConnectorCH instance
     */
    public static InventoryConnectorCH<@NotNull SimpleContainer> copyOf(InventoryConnector<?> inventory)
    {
        var storage = new InventoryConnectorCH<>();
        for(Pair<MappedDirection, ? extends SimpleContainer> entry : inventory.getSidedInventories())
            storage.addStorage(new SimpleContainer(entry.getB().getContainerSize()), entry.getA());
        return storage;
    }
}