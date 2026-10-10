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

import java.util.Objects;
import java.util.function.Supplier;

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import org.jspecify.annotations.Nullable;

import net.minecraft.core.Direction;

import com.dynamero.heat.EmptyHeatStorage;
import com.dynamero.heat.SimpleItemHeatStorageImpl;
import com.dynamero.heat.base.HeatConnector;
import com.dynamero.heat.base.storage.DelegatingHeatStorage;
import com.dynamero.heat.base.storage.SimpleHeatStorage;
import com.dynamero.heat.base.storage.SimpleSidedHeatContainer;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.utils.BaseHelper;

/**
 * A heat storage that can be queried for heat transfer.
 *
 * <p>Heat storages are used to store heat and transfer it between machines.
 * They can be queried for the amount of heat they contain, the maximum amount of heat they can store,
 * and the amount of heat they can accept or provide in a single operation.</p>
 *
 *      <ul>
 *         <li>{@link #supportsInsertion()} and {@link #supportsExtraction()} can be used to determine if the storage can accept or provide heat.
 *         <li>{@link #insert} and {@link #extract} can be used to transfer heat to or from the storage.
 *         <li>{@link #getAmount()} and {@link #getCapacity()} can be used to query the current amount of heat and the maximum amount of heat that can be stored.
 *         <li>{@link #getRemainingCapacity()} can be used to query the remaining capacity of the storage.
 *         <li>{@link #getMinCapacity()} can be used to query the minimum amount of heat that can be stored in the storage.
 *         <li></li>
 *         <li>{@link #SIDED} can be used to query heat storages from blocks.
 *         <li>{@link #ITEM} can be used to query heat storages from items.
 *         <li>{@link #EMPTY} can be used as a default value for optional heat storages.
 *         <li></li>
 *         <li>{@link SimpleHeatStorage} and {@link HeatConnector} are provided as base implementations.
 *     </ul>
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
public interface HeatStorage
{
    /**
     * Sided block access to heat storages.
     * The {@code Direction} parameter may be null, meaning that the full storage (ignoring side restrictions) should be queried.
     * Refer to {@link BlockApiLookup} for documentation on how to use this field.
     *
     * <p>The system is push based. That means that heat sources are responsible for pushing heat to nearby machines.
     * Machines and wires should NOT pull heat from other sources.
     *
     * <p>{@link SimpleHeatStorage} and {@link SimpleSidedHeatContainer} are provided as base implementations.
     *
     * <p>When the operations supported by a heat storage change,
     * that is if the return value of {@link HeatStorage#supportsInsertion} or {@link HeatStorage#supportsExtraction} changes,
     * the storage should notify its neighbors with a block update so that they can refresh their connections if necessary.
     *
     * <p>This may be queried safely both on the logical server and on the logical client threads.
     * On the server thread (i.e. with a server world), all transfer functionality is always supported.
     * On the client thread (i.e. with a client world), contents of queried HeatStorages are unreliable and should not be modified.
     */
    BlockApiLookup<HeatStorage, @Nullable Direction> SIDED = BlockApiLookup.get(BaseHelper.id("jilibs", "sided_heat"), HeatStorage.class, Direction.class);

    /**
     * Item access to heat storages.
     * Querying should always happen through {@link ContainerItemContext#find}.
     *
     * <p>{@link SimpleItemHeatStorageImpl} is provided as an implementation example.
     * Instances of it can be optained through {@link HeatItem#createStorage}.
     * Custom implementations should treat the context as a wrapper around a single slot,
     * and always check the current item variant and amount before any operation, like {@code SimpleItemEnergyStorageImpl} does it.
     * The check can be handled by {@link DelegatingHeatStorage}.
     *
     * <p>This may be queried both client-side and server-side.
     * Returned APIs should behave the same regardless of the logical side.
     */
    ItemApiLookup<HeatStorage, ContainerItemContext> ITEM = ItemApiLookup.get(BaseHelper.id("jilibs", "heat"), HeatStorage.class, ContainerItemContext.class);

    /**
     * Always empty heat storage.
     */
    Supplier<HeatStorage> EMPTY = () -> Objects.requireNonNull(EmptyHeatStorage.INSTANCE);

    /**
     * Return false if calling {@link #insert} will absolutely always return 0, or true otherwise or in doubt.
     *
     * <p>Note: This function is meant to be used by cables or other devices that can transfer heat to know if
     * they should interact with this storage at all.
     *
     * @return {@code true} if insertion is supported, {@code false} otherwise
     */
    default boolean supportsInsertion()
    {
        return true;
    }

    /**
     * Return false if calling {@link #extract} will absolutely always return 0, or true otherwise or in doubt.
     *
     * <p>Note: This function is meant to be used by cables or other devices that can transfer heat to know if
     * they should interact with this storage at all.
     *
     * @return {@code true} if extraction is supported, {@code false} otherwise
     */
    default boolean supportsExtraction()
    {
        return true;
    }

    /**
     * Return the unit of heat that this storage uses. Default is Kelvin.
     *
     * @return the heat unit
     */
    default HeatUnit getUnit()
    {
        return HeatUnit.KELVIN;
    }

    /**
     * Return the room temperature in the unit of heat that this storage uses.
     * Default unit system is Kelvin.
     *
     * @return the room temperature in this storage's unit
     */
    default double getRootTemperature()
    {
        return HeatUnit.getRoomTemperature(getUnit());
    }

    /**
     * Return the remaining capacity of this storage.
     *
     * <p>This is equivalent to {@link #getCapacity()} - {@link #getAmount()}.
     *
     * @return the remaining heat capacity
     */
    default double getRemainingCapacity()
    {
        return getCapacity() - getAmount();
    }

    /**
     * Return the minimum amount of heat that can be stored in this storage.
     *
     * <p>This defaults to 0, but can be overridden to provide a minimum amount of heat that must be stored in this storage.
     *
     * @return the minimum heat capacity
     */
    default double getMinCapacity()
    {
        return getUnit().getMinValue();
    }

    /**
     * Return true if the temperature of heat <i>(physicists please ignore)</i> stored in this storage is above room temperature.
     *
     * @return {@code true} if stored heat exceeds room temperature, {@code false} otherwise
     */
    default boolean isAboveRoomTemperature()
    {
        return getAmount() > getRootTemperature();
    }

    /**
     * Try to insert up to some amount of heat into this storage.
     *
     * @param maxAmount The maximum amount of heat to insert. May be negative.
     * @param unit The heat unit to be used
     * @param transaction The transaction this operation is part of.
     * @return A nonnegative integer not greater than maxAmount: the amount that was inserted.
     */
    double insert(double maxAmount, HeatUnit unit, TransactionContext transaction);

    /**
     * Try to extract up to some amount of heat from this storage.
     *
     * @param maxAmount The maximum amount of heat to extract. May not be negative.
     * @param unit The heat unit to be used
     * @param transaction The transaction this operation is part of.
     * @return A nonnegative integer not greater than maxAmount: the amount that was extracted.
     */
    double extract(double maxAmount, HeatUnit unit, TransactionContext transaction);

    /**
     * Return the current amount of heat that is stored.
     *
     * @return the current stored heat amount
     */
    double getAmount();

    /**
     * Return the maximum amount of heat that could be stored.
     *
     * @return the maximum heat capacity
     */
    double getCapacity();

    /**
     * Try to adjusting with some amount of heat at this storage. If the smount is negative, we extract the positive value, if it's positive, we insert the value.
     *
     * @param maxAmount The maximum amount of heat to adjust. May be negative.
     * @param unit The heat unit to be used
     * @param transaction The transaction this operation is part of.
     * @return A nonnegative integer not greater than maxAmount: the amount that was extracted or inserted.
     */
    default double adjust(double maxAmount, HeatUnit unit, TransactionContext transaction)
    {
        if(maxAmount > 0)
            return insert(maxAmount, unit, transaction);
        if(maxAmount < 0)
            return extract(Math.abs(maxAmount), unit, transaction);
        return 0;
    }
}