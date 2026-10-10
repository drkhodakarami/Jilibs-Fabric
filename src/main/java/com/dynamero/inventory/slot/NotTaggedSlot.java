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
 * Inventory slot that rejects any items belonging to a specified item tag.
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
public class NotTaggedSlot extends PredicateSlot
{
    /**
     * Constructs a NotTaggedSlot with default stack limits.
     *
     * @param inventory the container inventory
     * @param index     the slot index
     * @param x         the x-coordinate on screen
     * @param y         the y-coordinate on screen
     * @param tagKey    the item tag to reject
     */
    public NotTaggedSlot(Container inventory, int index, int x, int y, TagKey<Item> tagKey)
    {
        super(inventory, index, x, y, itemStack -> !itemStack.is(tagKey));
    }

    /**
     * Constructs a NotTaggedSlot with a custom maximum item stack count.
     *
     * @param inventory the container inventory
     * @param index     the slot index
     * @param x         the x-coordinate on screen
     * @param y         the y-coordinate on screen
     * @param maxCount  the maximum stack count allowed in this slot
     * @param tagKey    the item tag to reject
     */
    public NotTaggedSlot(Container inventory, int index, int x, int y, int maxCount, TagKey<Item> tagKey)
    {
        super(inventory, index, x, y, maxCount, itemStack -> !itemStack.is(tagKey));
    }
}