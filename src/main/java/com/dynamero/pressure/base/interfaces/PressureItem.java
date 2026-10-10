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

package com.dynamero.pressure.base.interfaces;

import java.util.Optional;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;

import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.item.ItemStack;

import com.dynamero.Jilibs;
import com.dynamero.pressure.PressureConstants;
import com.dynamero.pressure.SimpleItemPressureStorageImpl;
import com.dynamero.pressure.base.PressureComponent;
import com.dynamero.shared.annotations.*;

/**
 * Simple container-like pressure containing item. If this is implemented on an item:
 * <ul>
 *     <li>The pressure will directly be stored in the components.</li>
 *     <li>Helper functions in this class to work with the stored pressure can be used.</li>
 *     <li>An IPressureStorage will automatically be provided for queries through {@link PressureStorage#ITEM}.</li>
 * </ul>
 */
@SuppressWarnings("unused")
@Developer("TheMentor")
@CreatedAt("2026-10-08")
@ModifiedAt("2026-10-08")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public interface PressureItem
{
    /**
     * Return a base pressure storage implementation for items, with fixed capacity, and per-operation insertion and extraction limits.
     * This is used internally for items that implement IPressureItem, but it may also be used outside of that.
     * The pressure is stored in the {@link Jilibs.Components#PRESSURE} of the stacks.
     *
     * <p>Stackable containers are supported just fine, and they will distribute pressure evenly.
     * For example, insertion of 3 units into a stack of 2 items using this class will either insert 0 or 2 depending on the remaining capacity.
     *
     * @param context   the container item context
     * @param capacity  the maximum pressure capacity
     * @param maxInsert the maximum pressure insertion rate per transaction
     * @param maxExtract the maximum pressure extraction rate per transaction
     * @return a new pressure storage implementation
     */
    static PressureStorage createStorage(ContainerItemContext context, double capacity, double maxInsert, double maxExtract)
    {
        return SimpleItemPressureStorageImpl.createSimpleStorage(context, capacity, maxInsert, maxExtract);
    }

    /**
     * Retrieves the currently stored pressure, ignoring the count and without checking the current item.
     *
     * @param stack the item stack
     * @return the currently stored pressure amount
     */
    static double getStoredPressureUnchecked(ItemStack stack) {
        return ((PressureComponent)stack.getOrDefault(Jilibs.Components.PRESSURE, PressureConstants.Pascal.ATMOSPHERE)).getPressure();
    }

    /**
     * Retrieves the stored pressure from an item variant.
     *
     * @param variant the item variant
     * @return the stored pressure amount
     */
    static double getStoredPressureUnchecked(ItemVariant variant)
    {
        return getStoredPressureUnchecked(variant, variant.getComponentsPatch());
    }

    /**
     * Retrieves the stored pressure from an item variant and data component patch.
     *
     * @param variant    the item variant
     * @param components the components patch
     * @return the stored pressure amount
     */
    static double getStoredPressureUnchecked(ItemVariant variant, DataComponentPatch components)
    {
        Optional<PressureComponent> value = Optional.ofNullable(components.get(variant, Jilibs.Components.PRESSURE));
        return value.map(PressureComponent::getPressure).orElse(0D);
    }

    /**
     * Sets the pressure, ignoring the count and without checking the current item.
     *
     * @param stack  the item stack
     * @param amount the pressure amount
     * @param unit   the pressure unit
     */
    static void setStoredPressureUnchecked(ItemStack stack, double amount, PressureUnit unit)
    {
        stack.set(Jilibs.Components.PRESSURE, new PressureComponent(amount, unit));
    }

    /**
     * Removes the pressure component, ignoring the count and without checking the current item.
     *
     * @param stack the item stack
     */
    static void removeStoredPressureUnchecked(ItemStack stack)
    {
        stack.remove(Jilibs.Components.PRESSURE);
    }

    /**
     * Retrieves the maximum pressure capacity of the item stack.
     *
     * @param stack current stack
     * @return the max pressure that can be stored in this item stack (ignoring current stack size)
     */
    double getPressureCapacity(ItemStack stack);

    /**
     * Retrieves the maximum pressure insertion limit of the item stack per operation.
     *
     * @param stack current stack
     * @return the max amount of pressure that can be inserted in this item stack (ignoring current stack size) in a single operation
     */
    double getPressureMaxInput(ItemStack stack);

    /**
     * Retrieves the maximum pressure extraction limit of the item stack per operation.
     *
     * @param stack current stack
     * @return the max amount of pressure that can be extracted from this item stack (ignoring current stack size) in a single operation
     */
    double getPressureMaxOutput(ItemStack stack);

    /**
     * Retrieves the pressure stored in the stack. Count is ignored.
     *
     * @param stack the item stack
     * @return the pressure stored in the stack
     */
    default double getStoredPressure(ItemStack stack)
    {
        return getStoredPressureUnchecked(stack);
    }

    /**
     * Directly sets the pressure stored in the stack. Count is ignored.
     *
     * @param stack  the item stack
     * @param amount the pressure amount
     * @param unit   the pressure unit
     */
    default void setStoredPressure(ItemStack stack, double amount, PressureUnit unit)
    {
        setStoredPressureUnchecked(stack, amount, unit);
    }

    /**
     * Directly removes the pressure stored in the stack. Count is ignored.
     *
     * @param stack the item stack
     */
    default void removeStoredPressure(ItemStack stack)
    {
        removeStoredPressureUnchecked(stack);
    }

    /**
     * Tries to use exactly {@code amount} pressure if available.
     *
     * @param stack  the item stack
     * @param amount the pressure amount to use
     * @param unit   the pressure unit
     * @return {@code true} if successful, {@code false} if insufficient pressure
     * @throws IllegalArgumentException if the count of the stack is not exactly 1
     */
    default boolean tryUsePressure(ItemStack stack, double amount, PressureUnit unit)
    {
        if (stack.getCount() != 1)
            throw new IllegalArgumentException("Invalid count: " + stack.getCount());

        double newAmount = getStoredPressure(stack) - amount;

        if (newAmount < 0)
            return false;

        setStoredPressure(stack, newAmount, unit);
        return true;
    }
}