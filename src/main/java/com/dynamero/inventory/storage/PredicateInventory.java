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

import java.util.function.BiPredicate;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import com.dynamero.shared.annotations.*;

/**
 * Block-entity-synchronized inventory constraining item placement via a slot-and-stack predicate.
 */
@SuppressWarnings("unused")
@Developer("TurtyWurty")
@CreatedAt("2026-08-10")
@ModifiedAt("2026-08-10")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public class PredicateInventory extends SyncedInventory
{
    /**
     * Predicate validating whether an item stack can be placed into a specific slot index.
     */
    private final BiPredicate<ItemStack, Integer> predicate;

    /**
     * Constructs a PredicateInventory with a specified size and placement predicate.
     *
     * @param blockEntity the owning block entity
     * @param size        the slot count
     * @param predicate   the predicate testing item placement for a given slot index
     */
    public PredicateInventory(BlockEntity blockEntity, int size, BiPredicate<ItemStack, Integer> predicate)
    {
        super(blockEntity, size);
        this.predicate = predicate;
    }

    /**
     * Constructs a PredicateInventory initialized with item stacks and a placement predicate.
     *
     * @param blockEntity the owning block entity
     * @param predicate   the predicate testing item placement for a given slot index
     * @param items       the initial item stacks
     */
    public PredicateInventory(BlockEntity blockEntity, BiPredicate<ItemStack, Integer> predicate, ItemStack... items)
    {
        super(blockEntity, items);
        this.predicate = predicate;
    }

    /**
     * Evaluates whether an item stack can be placed into the specified slot index using the predicate.
     *
     * @param slotIndex the slot index
     * @param stack     the item stack being placed
     * @return {@code true} if placement is permitted, {@code false} otherwise
     */
    @Override
    public boolean canPlaceItem(int slotIndex, @NotNull ItemStack stack)
    {
        return this.predicate.test(stack, slotIndex);
    }

    /**
     * Retrieves the placement validation predicate.
     *
     * @return the predicate testing item placement
     */
    public BiPredicate<ItemStack, Integer> getPredicate()
    {
        return this.predicate;
    }
}