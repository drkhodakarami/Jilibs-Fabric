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

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import com.dynamero.shared.annotations.*;

/**
 * Output-only inventory implementation that rejects manual placement of items into any slot.
 */
@SuppressWarnings("unused")
@Developer("TurtyWurty")
@ModifiedBy("The Mentor")
@CreatedAt("2025-04-18")
@Repository("https://github.com/DaRealTurtyWurty/Industria")
@Discord("https://discord.turtywurty.dev/")
@Youtube("https://www.youtube.com/@TurtyWurty")
public class OutputInventory extends PredicateInventory
{
    /**
     * Constructs an OutputInventory with a fixed slot count.
     *
     * @param blockEntity the owning block entity
     * @param size        the slot count
     */
    public OutputInventory(BlockEntity blockEntity, int size)
    {
        super(blockEntity, size, (slotIndex, stack) -> false);
    }

    /**
     * Constructs an OutputInventory initialized with given item stacks.
     *
     * @param blockEntity the owning block entity
     * @param items       the initial item stacks
     */
    public OutputInventory(BlockEntity blockEntity, ItemStack... items)
    {
        super(blockEntity, (slotIndex, stack) -> false, items);
    }
}