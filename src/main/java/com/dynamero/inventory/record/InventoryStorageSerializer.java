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

package com.dynamero.inventory.record;

import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import com.dynamero.shared.annotations.*;
import com.dynamero.shared.interfaces.DataSerializer;

/**
 * Data serializer record handling persistence for simple container inventories.
 *
 * @param inventory the container inventory to serialize or deserialize
 * @param <T>       the container inventory type
 */
@Developer("TheMentor")
@CreatedAt("2026-01-12")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
public record InventoryStorageSerializer<T extends SimpleContainer>(T inventory) implements DataSerializer
{
    /**
     * Saves all items in the inventory into the value output.
     *
     * @param view the value output to write to
     */
    @Override
    public void saveAdditional(ValueOutput view)
    {
        ContainerHelper.saveAllItems(view, this.inventory.getItems());
    }

    /**
     * Loads all items into the inventory from the value input.
     *
     * @param view the value input to read from
     */
    @Override
    public void loadAdditional(ValueInput view)
    {
        ContainerHelper.loadAllItems(view, this.inventory.getItems());
    }
}