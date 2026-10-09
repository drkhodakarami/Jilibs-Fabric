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

import java.util.Objects;
import java.util.function.Supplier;

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import org.jspecify.annotations.Nullable;

import net.minecraft.core.Direction;

import com.dynamero.pressure.EmptyPressureStorage;
import com.dynamero.pressure.PressureConstants;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.utils.BaseHelper;

/**
 * A pressure storage that can be queried for pressure transfer.
 *
 * <p>Pressure storages are used to store pressure and transfer it between machines.
 * They can be queried for the amount of pressure they contain, the maximum amount of pressure they can store,
 * and the amount of pressure they can accept or provide in a single operation.</p>
 *
 *     <ul>
 *         <li>{@link #supportsInsertion()} and {@link #supportsExtraction()} can be used to determine if the storage can accept or provide pressure.</li>
 *         <li>{@link #insert} and {@link #extract} can be used to transfer pressure to or from the storage.</li>
 *         <li>{@link #getAmount()} and {@link #getCapacity()} can be used to query the current amount of pressure and the maximum amount of pressure that can be stored.</li>
 *         <li>{@link #getRemainingCapacity()} can be used to query the remaining capacity of the storage.</li>
 *         <li>{@link #getMinCapacity()} can be used to query the minimum amount of pressure that can be stored in the storage.</li>
 *         <li>{@link #SIDED} can be used to query pressure storages from blocks.</li>
 *         <li>{@link #ITEM} can be used to query pressure storages from items.</li>
 *         <li>{@link #EMPTY} can be used as a default value for optional pressure storages.</li>
 *     </ul>
 */
@SuppressWarnings("unused")
@Developer("TheMentor")
@CreatedAt("2026-08-10")
@ModifiedAt("2026-08-10")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public interface PressureStorage
{
    /**
     * Sided block access to pressure storages.
     * The {@code Direction} parameter may be null, meaning that the full storage (ignoring side restrictions) should be queried.
     */
    BlockApiLookup<PressureStorage, @Nullable Direction> SIDED = BlockApiLookup.get(BaseHelper.id("jilibs", "sided_pressure"), PressureStorage.class, Direction.class);

    /**
     * Item access to pressure storages.
     * Querying should always happen through {@link ContainerItemContext#find}.
     */
    ItemApiLookup<PressureStorage, ContainerItemContext> ITEM = ItemApiLookup.get(BaseHelper.id("jilibs", "pressure"), PressureStorage.class, ContainerItemContext.class);

    /**
     * Always empty pressure storage.
     */
    Supplier<PressureStorage> EMPTY = () -> Objects.requireNonNull(EmptyPressureStorage.INSTANCE);

    /**
     * Return false if calling {@link #insert} will absolutely always return 0, or true otherwise or in doubt.
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
     * @return {@code true} if extraction is supported, {@code false} otherwise
     */
    default boolean supportsExtraction()
    {
        return true;
    }

    /**
     * Return the unit of pressure that this storage uses. Default is Pascal.
     *
     * @return the pressure unit
     */
    default PressureUnit getUnit()
    {
        return PressureUnit.PASCAL;
    }

    /**
     * Return the ambient/room atmospheric pressure in the unit of pressure that this storage uses.
     *
     * @return the ambient atmospheric pressure in this storage's unit
     */
    default double getRootPressure()
    {
        return getUnit().convertFromBaseUnit(PressureConstants.Pascal.ATMOSPHERE);
    }

    /**
     * Return the remaining capacity of this storage.
     *
     * @return the remaining pressure capacity
     */
    default double getRemainingCapacity()
    {
        return getCapacity() - getAmount();
    }

    /**
     * Return the minimum amount of pressure that can be stored in this storage.
     *
     * @return the minimum pressure capacity
     */
    default double getMinCapacity()
    {
        return getUnit().getMinValue();
    }

    /**
     * Return true if the stored pressure in this storage is above ambient atmospheric pressure.
     *
     * @return {@code true} if stored pressure exceeds ambient atmospheric pressure, {@code false} otherwise
     */
    default boolean isAboveRoomPressure()
    {
        return getAmount() > getRootPressure();
    }

    /**
     * Try to insert up to some amount of pressure into this storage.
     *
     * @param maxAmount   The maximum amount of pressure to insert.
     * @param unit        The pressure unit to be used
     * @param transaction The transaction this operation is part of.
     * @return A nonnegative value not greater than maxAmount: the amount that was inserted.
     */
    double insert(double maxAmount, PressureUnit unit, TransactionContext transaction);

    /**
     * Try to extract up to some amount of pressure from this storage.
     *
     * @param maxAmount   The maximum amount of pressure to extract.
     * @param unit        The pressure unit to be used
     * @param transaction The transaction this operation is part of.
     * @return A nonnegative value not greater than maxAmount: the amount that was extracted.
     */
    double extract(double maxAmount, PressureUnit unit, TransactionContext transaction);

    /**
     * Return the current amount of pressure that is stored.
     *
     * @return the current stored pressure amount
     */
    double getAmount();

    /**
     * Return the maximum amount of pressure that could be stored.
     *
     * @return the maximum pressure capacity
     */
    double getCapacity();

    /**
     * Try to adjust pressure in this storage. If the amount is negative, extract; if positive, insert.
     *
     * @param maxAmount   The maximum amount of pressure to adjust. May be negative.
     * @param unit        The pressure unit to be used
     * @param transaction The transaction this operation is part of.
     * @return A nonnegative value: the amount that was extracted or inserted.
     */
    default double adjust(double maxAmount, PressureUnit unit, TransactionContext transaction)
    {
        if(maxAmount > 0)
            return insert(maxAmount, unit, transaction);
        if(maxAmount < 0)
            return extract(Math.abs(maxAmount), unit, transaction);
        return 0;
    }
}