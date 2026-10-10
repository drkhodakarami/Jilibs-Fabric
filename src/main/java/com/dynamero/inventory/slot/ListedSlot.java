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

package com.dynamero.inventory.slot;

import java.util.List;

import net.minecraft.world.Container;
import net.minecraft.world.item.Item;

import com.dynamero.shared.annotations.*;

/**
 * Inventory slot that restricts insertable items to an explicit whitelist of allowed items.
 */
@SuppressWarnings("unused")
@Developer("The Mentor")
@CreatedAt("2026-10-08")
@ModifiedAt("2026-10-08")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public class ListedSlot extends PredicateSlot
{
    /**
     * Constructs a ListedSlot with default stack limits.
     *
     * @param inventory the container inventory
     * @param index     the slot index
     * @param x         the x-coordinate on screen
     * @param y         the y-coordinate on screen
     * @param list      the list of allowed items
     */
    public ListedSlot(Container inventory, int index, int x, int y, List<Item> list)
    {
        super(inventory, index, x, y, itemStack -> list.contains(itemStack.getItem()));
    }

    /**
     * Constructs a ListedSlot with a custom maximum item stack count.
     *
     * @param inventory the container inventory
     * @param index     the slot index
     * @param x         the x-coordinate on screen
     * @param y         the y-coordinate on screen
     * @param maxCount  the maximum stack count allowed in this slot
     * @param list      the list of allowed items
     */
    public ListedSlot(Container inventory, int index, int x, int y, int maxCount, List<Item> list)
    {
        super(inventory, index, x, y, maxCount, itemStack -> list.contains(itemStack.getItem()));
    }
}