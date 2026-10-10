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

import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import com.dynamero.shared.annotations.*;

/**
 * Inventory slot that validates item placement against a custom item stack predicate and optional stack size limit.
 */
@SuppressWarnings("unused")
@Developer("TurtyWurty")
@CreatedAt("2026-10-08")
@ModifiedAt("2026-10-08")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public class PredicateSlot extends Slot
{
    /**
     * Predicate determining whether an item stack can be placed into this slot.
     */
    private final Predicate<ItemStack> predicate;

    /**
     * Maximum allowable item count for this slot, or zero if unbounded by slot logic.
     */
    private int maxCount;

    /**
     * Constructs a PredicateSlot with a placement predicate and default stack count limit.
     *
     * @param inventory the container inventory
     * @param index     the slot index
     * @param x         the x-coordinate on screen
     * @param y         the y-coordinate on screen
     * @param predicate the placement predicate
     */
    public PredicateSlot(Container inventory, int index, int x, int y, Predicate<ItemStack> predicate)
    {
        super(inventory, index, x, y);
        this.predicate = predicate;
    }

    /**
     * Constructs a PredicateSlot with a placement predicate and custom maximum item count.
     *
     * @param inventory the container inventory
     * @param index     the slot index
     * @param x         the x-coordinate on screen
     * @param y         the y-coordinate on screen
     * @param maxCount  the maximum allowable item count
     * @param predicate the placement predicate
     */
    public PredicateSlot(Container inventory, int index, int x, int y, int maxCount, Predicate<ItemStack> predicate)
    {
        this(inventory, index, x, y, predicate);
        this.maxCount = maxCount;
    }

    /**
     * Constructs a PredicateSlot delegating placement checks to {@link SimpleContainer#canPlaceItem(int, ItemStack)}.
     *
     * @param inventory the simple container inventory
     * @param index     the slot index
     * @param x         the x-coordinate on screen
     * @param y         the y-coordinate on screen
     */
    public PredicateSlot(SimpleContainer inventory, int index, int x, int y)
    {
        this(inventory, index, x, y, stack -> inventory.canPlaceItem(index, stack));
    }

    /**
     * Constructs a PredicateSlot delegating to {@link SimpleContainer#canPlaceItem(int, ItemStack)} with a custom maximum item count.
     *
     * @param inventory the simple container inventory
     * @param index     the slot index
     * @param x         the x-coordinate on screen
     * @param y         the y-coordinate on screen
     * @param maxCount  the maximum allowable item count
     */
    public PredicateSlot(SimpleContainer inventory, int index, int x, int y, int maxCount)
    {
        this(inventory, index, x, y);
        this.maxCount = maxCount;
    }

    /**
     * Evaluates whether the given item stack may be placed into this slot.
     *
     * @param stack the item stack being placed
     * @return {@code true} if placement is permitted, {@code false} otherwise
     */
    @Override
    public boolean mayPlace(@NotNull ItemStack stack)
    {
        if(maxCount == 0)
            return this.predicate.test(stack);
        return this.predicate.test(stack) && getItem().getCount() < maxCount;
    }
}