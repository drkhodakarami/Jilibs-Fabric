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

import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;

import com.dynamero.shared.annotations.*;

/**
 * Inventory slot that accepts items matching a primary item tag while excluding items matching a restriction tag.
 */
@SuppressWarnings("unused")
@Developer("The Mentor")
@CreatedAt("2025-04-18")
@Repository("https://github.com/drkhodakarami/___PROJECTS___")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
public class LimitedTagSlot extends PredicateSlot
{
    /**
     * Constructs a LimitedTagSlot with default stack limits.
     *
     * @param inventory   the container inventory
     * @param index       the slot index
     * @param x           the x-coordinate on screen
     * @param y           the y-coordinate on screen
     * @param tagKey      the required item tag
     * @param limitTagKey the excluded item tag
     */
    public LimitedTagSlot(Container inventory, int index, int x, int y, TagKey<Item> tagKey, TagKey<Item> limitTagKey)
    {
        super(inventory, index, x, y, itemStack -> itemStack.is(tagKey) && !itemStack.is(limitTagKey));
    }

    /**
     * Constructs a LimitedTagSlot with a custom maximum item stack count.
     *
     * @param inventory   the container inventory
     * @param index       the slot index
     * @param x           the x-coordinate on screen
     * @param y           the y-coordinate on screen
     * @param maxCount    the maximum stack count allowed in this slot
     * @param tagKey      the required item tag
     * @param limitTagKey the excluded item tag
     */
    public LimitedTagSlot(Container inventory, int index, int x, int y, int maxCount, TagKey<Item> tagKey, TagKey<Item> limitTagKey)
    {
        super(inventory, index, x, y, maxCount, itemStack -> itemStack.is(tagKey) && !itemStack.is(limitTagKey));
    }
}