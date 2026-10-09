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

import net.minecraft.world.Container;

import com.dynamero.shared.annotations.*;

/**
 * Output-only inventory slot that forbids manual insertion of any items.
 */
@SuppressWarnings("unused")
@Developer("TurtyWurty")
@ModifiedBy("The Mentor")
@CreatedAt("2025-04-18")
@Repository("https://github.com/DaRealTurtyWurty/Industria")
@Discord("https://discord.turtywurty.dev/")
@Youtube("https://www.youtube.com/@TurtyWurty")
public class OutputSlot extends PredicateSlot
{
    /**
     * Constructs an OutputSlot that disallows item insertion.
     *
     * @param inventory the container inventory
     * @param index     the slot index
     * @param x         the x-coordinate on screen
     * @param y         the y-coordinate on screen
     */
    public OutputSlot(Container inventory, int index, int x, int y)
    {
        super(inventory, index, x, y, itemStack -> false);
    }
}