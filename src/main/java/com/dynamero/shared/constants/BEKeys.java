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

package com.dynamero.shared.constants;

import com.dynamero.shared.annotations.*;
import com.dynamero.shared.exceptions.Exceptions;

/**
 * A class containing constants representing various key names used in the block.
 *
 * @author TheMentor
 * @since 2025-04-18
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
public class BEKeys
{
    /**
     * Key for the pressure storage in a block entity.
     */
    public static final String PRESSURE_STORAGE = "pressure.storage";

    /**
     * Key for the pressure amount in a block entity.
     */
    public static final String PRESSURE_AMOUNT = "pressure.amount";

    /**
     * Key for the pressure capacity in a block entity.
     */
    public static final String PRESSURE_CAPACITY = "pressure.capacity";

    /**
     * Key for the dispersion storage in a block entity.
     */
    public static final String DISPERSION_STORAGE = "dispersion.storage";

    /**
     * Key for the dispersion amount in a block entity.
     */
    public static final String DISPERSION_AMOUNT = "dispersion.amount";

    /**
     * Key for the dispersion capacity in a block entity.
     */
    public static final String DISPERSION_CAPACITY = "dispersion.capacity";

    /**
     * Key for the dispersion variant in a block entity.
     */
    public static final String DISPERSION_VARIANT = "dispersion.variant";

    /**
     * Key indicating whether a block entity has dispersion.
     */
    public static final String HAS_DISPERSION = "has.dispersion";

    /**
     * Key for the energy storage in a block entity.
     */
    public static final String ENERGY_STORAGE = "energy.storage";

    /**
     * Key for the energy amount in a block entity.
     */
    public static final String ENERGY_AMOUNT = "energy.amount";

    /**
     * Key for the energy capacity in a block entity.
     */
    public static final String ENERGY_CAPACITY = "energy.capacity";

    /**
     * Key indicating whether a block entity has energy.
     */
    public static final String HAS_ENERGY = "has.energy";

    /**
     * Key for the fluid storage in a block entity.
     */
    public static final String FLUID_STORAGE = "fluid.storage";

    /**
     * Key for the fluid amount in a block entity.
     */
    public static final String FLUID_AMOUNT = "fluid.amount";

    /**
     * Key for the fluid capacity in a block entity.
     */
    public static final String FLUID_CAPACITY = "fluid.capacity";

    /**
     * Key for the fluid variant in a block entity.
     */
    public static final String FLUID_VARIANT = "fluid.variant";

    /**
     * Key indicating whether a block entity has fluid.
     */
    public static final String HAS_FLUID = "has.fluid";

    /**
     * Key for the heat storage in a block entity.
     */
    public static final String HEAT_STORAGE = "heat.storage";

    /**
     * Key for the heat amount in a block entity.
     */
    public static final String HEAT_AMOUNT = "heat.amount";

    /**
     * Key for the heat capacity in a block entity.
     */
    public static final String HEAT_CAPACITY = "heat.capacity";

    /**
     * Key indicating whether a block entity has heat.
     */
    public static final String HAS_HEAT = "has.heat";

    /**
     * Key for the gas storage in a block entity.
     */
    public static final String GAS_STORAGE = "gas.storage";

    /**
     * Key for the gas amount in a block entity.
     */
    public static final String GAS_AMOUNT = "gas.amount";

    /**
     * Key for the gas capacity in a block entity.
     */
    public static final String GAS_CAPACITY = "gas.capacity";

    /**
     * Key for the gas variant in a block entity.
     */
    public static final String GAS_VARIANT = "gas.variant";

    /**
     * Key indicating whether a block entity has gas.
     */
    public static final String HAS_GAS = "has.gas";

    /**
     * Key for the inventory storage in a block entity.
     */
    public static final String INVETORY_STORAGE = "inventory.storage";

    /**
     * Key indicating whether a block entity has an inventory.
     */
    public static final String HAS_INVENTORY = "has.inventory";

    /**
     * Key for the progress amount in a block entity.
     */
    public static final String PROGRESS_AMOUNT = "progress.amount";

    /**
     * Key for the maximum progress value in a block entity.
     */
    public static final String PROGRESS_MAX = "progress.max";

    /**
     * Key for the cooldown amount in a block entity.
     */
    public static final String COOLDOWN_AMOUNT = "cooldown.amount";

    /**
     * Key for the maximum cooldown value in a block entity.
     */
    public static final String COOLDOWN_MAX = "cooldown.max";

    /**
     * Key for the burn amount in a block entity.
     */
    public static final String BURN_AMOUNT = "burn.amount";

    /**
     * Key for the maximum burn value in a block entity.
     */
    public static final String BURN_MAX = "burn.max";

    /**
     * Key indicating whether a block entity is dirty.
     */
    public static final String IS_DIRTY = "is.dirty";

    /**
     * Key indicating whether the client-side version of a block entity is dirty.
     */
    public static final String IS_DIRTY_CLIENT = "is.dirty.client";

    /**
     * Key for the world information in a block entity.
     */
    public static final String WORLD = "world";

    /**
     * Key for the position information in a block entity.
     */
    public static final String POS = "pos";

    /**
     * Key for the cached state of a block entity.
     */
    public static final String CACHED_STATE = "cached.state";

    /**
     * Key for the tick count in a block entity.
     */
    public static final String TICKS = "ticks";

    /**
     * Key for the client-side tick count in a block entity.
     */
    public static final String TICKS_CLIENT = "ticks.client";

    public static final String FLUID_STACK_LIST = "fluid.stack.list";

    public static final String ITEM_STACK_LIST = "item.stack.list";

    public static final String GAS_STACK_LIST = "gas.stack.list";

    public static final String ENERGY_STACK_LIST = "energy.stack.list";

    public static final String PRESSURE_STACK_LIST = "pressure.stack.list";

    public static final String HEAT_STACK_LIST = "heat.stack.list";

    public static final String BLOCK_POS_LIST = "block.pos.list";

    public static final String CONFIGURED_INGREDIENT_LIST = "configured.ingredient.list";

    public static final String COORDINATE_DATA_LIST = "coordinate.data.list";

    public static final String DOUBLE_LIST = "double.list";

    public static final String FLOAT_LIST = "float.list";

    public static final String HAND_LIST = "hand.list";

    public static final String INTEGER_LIST = "integer.list";

    public static final String LONG_LIST = "long.list";

    public static final String OUTPUT_ITEM_STACK_LIST = "output.item.stack.list";

    public static final String STACK_DATA_LIST = "stack.data.list";

    public static final String STRING_LIST = "string.list";

    public static final String HEAT_COMPONENT = "heat.component";

    public static final String PRESSURE_COMPONENT = "pressure.component";

    // Private constructor to prevent instantiation
    BEKeys() {
        Exceptions.throwCtorAssertion();
    }
}