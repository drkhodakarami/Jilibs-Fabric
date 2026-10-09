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

package com.dynamero.inventory.storage;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

import com.dynamero.shared.annotations.*;

/**
 * Simple container implementing Minecraft's {@link RecipeInput} to serve as input data for recipe matching and crafting checks.
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
public class RecipeInventory extends SimpleContainer implements RecipeInput
{
    /**
     * Constructs a RecipeInventory with a specific slot capacity.
     *
     * @param size the slot count
     */
    public RecipeInventory(int size)
    {
        super(size);
    }

    /**
     * Constructs a RecipeInventory populated with initial item stacks.
     *
     * @param items the initial item stacks
     */
    public RecipeInventory(ItemStack... items)
    {
        super(items);
    }

    /**
     * Retrieves the item stack at the specified slot index.
     *
     * @param slot the slot index
     * @return the item stack in the slot
     */
    @Override
    public @NotNull ItemStack getItem(int slot)
    {
        return super.getItem(slot);
    }

    /**
     * Retrieves the total size of this recipe inventory.
     *
     * @return container size
     */
    @Override
    public int size()
    {
        return getContainerSize();
    }
}