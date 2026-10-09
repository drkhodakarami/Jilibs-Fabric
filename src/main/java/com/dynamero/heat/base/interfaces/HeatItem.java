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

package com.dynamero.heat.base.interfaces;

import java.util.Optional;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;

import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.item.ItemStack;

import com.dynamero.Jilibs;
import com.dynamero.heat.HeatConstants;
import com.dynamero.heat.SimpleItemHeatStorageImpl;
import com.dynamero.heat.base.HeatComponent;
import com.dynamero.shared.annotations.*;

/**
 * Simple battery-like heat containing item. If this is implemented on an item:
 * <ul>
 *     <li>The heat will directly be stored in the components.</li>
 *     <li>Helper functions in this class to work with the stored heat can be used.</li>
 *     <li>An IHeatStorage will automatically be provided for queries through {@link HeatStorage#ITEM}.</li>
 * </ul>
 */
@SuppressWarnings("unused")
@Developer("TheMentor")
@CreatedAt("2026-01-12")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
public interface HeatItem
{
    /**
     * Return a base heat storage implementation for items, with fixed capacity, and per-operation insertion and extraction limits.
     * This is used internally for items that implement IHeatItem, but it may also be used outside of that.
     * The heat is stored in the Jilibs.Components.HEAT of the stacks.
     *
     * <p>Stackable energy containers are supported just fine, and they will distribute energy evenly.
     * For example, insertion of 3 units of energy into a stack of 2 items using this class will either insert 0 or 2 depending on the remaining capacity.
     *
     * @param context   the container item context
     * @param capacity  the maximum heat capacity
     * @param maxInsert the maximum heat insertion rate per transaction
     * @param maxExtract the maximum heat extraction rate per transaction
     * @return a new heat storage implementation
     */
    static HeatStorage createStorage(ContainerItemContext context, double capacity, double maxInsert, double maxExtract)
    {
        return SimpleItemHeatStorageImpl.createSimpleStorage(context, capacity, maxInsert, maxExtract);
    }

    /**
     * Retrieves the currently stored heat, ignoring the count and without checking the current item.
     *
     * @param stack the item stack
     * @return the currently stored heat amount
     */
    static double getStoredHeatUnchecked(ItemStack stack) {
        return ((HeatComponent) stack.getOrDefault(Jilibs.Components.HEAT, HeatConstants.Kelvin.ROOM_TEMPERATURE)).getHeat();
    }

    /**
     * Retrieves the stored heat from an item variant.
     *
     * @param variant the item variant
     * @return the stored heat amount
     */
    static double getStoredHeatUnchecked(ItemVariant variant)
    {
        return getStoredHeatUnchecked(variant, variant.getComponentsPatch());
    }

    /**
     * Retrieves the stored heat from an item variant and data component patch.
     *
     * @param variant    the item variant
     * @param components the components patch
     * @return the stored heat amount
     */
    static double getStoredHeatUnchecked(ItemVariant variant, DataComponentPatch components)
    {
        Optional<HeatComponent> value = Optional.ofNullable(components.get(variant, Jilibs.Components.HEAT));
        return value.map(HeatComponent::getHeat).orElse(0D);
    }

    /**
     * Sets the heat, ignoring the count and without checking the current item.
     *
     * @param stack  the item stack
     * @param amount the heat amount
     * @param unit   the heat unit
     */
    static void setStoredHeatUnchecked(ItemStack stack, double amount, HeatUnit unit)
    {
        stack.set(Jilibs.Components.HEAT, new HeatComponent(amount, unit));
    }

    /**
     * Removes the heat component, ignoring the count and without checking the current item.
     *
     * @param stack the item stack
     */
    static void removeStoredHeatUnchecked(ItemStack stack)
    {
        stack.remove(Jilibs.Components.HEAT);
    }

    /**
     * Retrieves the maximum heat capacity of the item stack.
     *
     * @param stack current stack
     * @return the max heat that can be stored in this item stack (ignoring current stack size)
     */
    double getHeatCapacity(ItemStack stack);

    /**
     * Retrieves the maximum heat insertion limit of the item stack per operation.
     *
     * @param stack current stack
     * @return the max amount of heat that can be inserted in this item stack (ignoring current stack size) in a single operation
     */
    double getHeatMaxInput(ItemStack stack);

    /**
     * Retrieves the maximum heat extraction limit of the item stack per operation.
     *
     * @param stack current stack
     * @return the max amount of heat that can be extracted from this item stack (ignoring current stack size) in a single operation
     */
    double getHeatMaxOutput(ItemStack stack);

    /**
     * Retrieves the heat stored in the stack. Count is ignored.
     *
     * @param stack the item stack
     * @return the heat stored in the stack
     */
    default double getStoredHeat(ItemStack stack)
    {
        return getStoredHeatUnchecked(stack);
    }

    /**
     * Directly sets the heat stored in the stack. Count is ignored.
     *
     * @param stack  the item stack
     * @param amount the heat amount
     * @param unit   the heat unit
     */
    default void setStoredHeat(ItemStack stack, double amount, HeatUnit unit)
    {
        setStoredHeatUnchecked(stack, amount, unit);
    }

    /**
     * Directly removes the heat stored in the stack. Count is ignored.
     *
     * @param stack the item stack
     */
    default void removeStoredHeat(ItemStack stack)
    {
        removeStoredHeatUnchecked(stack);
    }

    /**
     * Tries to use exactly {@code amount} heat if available.
     *
     * @param stack  the item stack
     * @param amount the heat amount to use
     * @param unit   the heat unit
     * @return {@code true} if successful, {@code false} if insufficient heat
     * @throws IllegalArgumentException if the count of the stack is not exactly 1
     */
    default boolean tryUseHeat(ItemStack stack, double amount, HeatUnit unit)
    {
        if (stack.getCount() != 1)
            throw new IllegalArgumentException("Invalid count: " + stack.getCount());

        double newAmount = getStoredHeat(stack) - amount;

        if (newAmount < 0)
            return false;

        setStoredHeat(stack, newAmount, unit);
        return true;
    }
}