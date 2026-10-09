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

package com.dynamero.heat.base.storage;

import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import org.jspecify.annotations.Nullable;

import com.dynamero.heat.base.interfaces.HeatStorage;
import com.dynamero.heat.base.interfaces.HeatUnit;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.utils.MathHelper;

/**
 * A heat storage that delegates to another heat storage,
 * with an optional boolean supplier to check that the storage is still valid.
 * This can be used for easier item heat storage implementation, or overridden for custom delegation logic.
 */
@SuppressWarnings("unused")
@Developer("TheMentor")
@CreatedAt("2026-01-12")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
public class DelegatingHeatStorage implements HeatStorage
{
    /**
     * Supplier resolving the backing heat storage to delegate operations to.
     */
    protected final Supplier<HeatStorage> backingStorage;

    /**
     * Predicate checking whether the backing storage is currently valid for operations.
     */
    protected final BooleanSupplier validPredicate;

    /**
     * Create a new instance.
     * @param backingStorage Storage to delegate to.
     * @param validPredicate A function that can return false to prevent any operation, or true to call the delegate as usual.
     *                       {@code null} can be passed if no filtering is necessary.
     */
    public DelegatingHeatStorage(HeatStorage backingStorage, @Nullable BooleanSupplier validPredicate)
    {
        this(() -> backingStorage, validPredicate);
        Objects.requireNonNull(backingStorage);
    }

    /**
     * More general constructor that allows the backing storage to change over time.
     *
     * @param backingStorage supplier of the storage to delegate to
     * @param validPredicate validation predicate or null
     */
    public DelegatingHeatStorage(Supplier<HeatStorage> backingStorage, @Nullable BooleanSupplier validPredicate)
    {
        this.backingStorage = Objects.requireNonNull(backingStorage);
        this.validPredicate = validPredicate == null ? () -> true : validPredicate;
    }

    /**
     * Checks if insertion is supported by the delegate while valid.
     *
     * @return {@code true} if valid and delegate supports insertion
     */
    @Override
    public boolean supportsInsertion()
    {
        return validPredicate.getAsBoolean() && backingStorage.get().supportsInsertion();
    }

    /**
     * Checks if extraction is supported by the delegate while valid.
     *
     * @return {@code true} if valid and delegate supports extraction
     */
    @Override
    public boolean supportsExtraction()
    {
        return validPredicate.getAsBoolean() && backingStorage.get().supportsExtraction();
    }

    /**
     * Inserts heat into the delegate storage if valid.
     *
     * @param maxAmount   the maximum heat amount to insert
     * @param unit        the heat unit used
     * @param transaction the transaction context
     * @return the inserted heat amount
     */
    @Override
    public double insert(double maxAmount, HeatUnit unit, TransactionContext transaction)
    {
        MathHelper.notNegative(maxAmount);

        if (validPredicate.getAsBoolean())
            return backingStorage.get().insert(maxAmount, unit, transaction);
        else
            return 0;
    }

    /**
     * Extracts heat from the delegate storage if valid.
     *
     * @param maxAmount   the maximum heat amount to extract
     * @param unit        the heat unit used
     * @param transaction the transaction context
     * @return the extracted heat amount
     */
    @Override
    public double extract(double maxAmount, HeatUnit unit, TransactionContext transaction)
    {
        MathHelper.notNegative(maxAmount);

        if (validPredicate.getAsBoolean())
            return backingStorage.get().extract(maxAmount, unit, transaction);
        else
            return 0;
    }

    /**
     * Retrieves the current heat amount stored in the delegate if valid.
     *
     * @return the stored heat amount, or 0 if invalid
     */
    @Override
    public double getAmount()
    {
        if (validPredicate.getAsBoolean())
            return backingStorage.get().getAmount();
        else
            return 0;
    }

    /**
     * Retrieves the maximum heat capacity of the delegate if valid.
     *
     * @return the heat capacity, or 0 if invalid
     */
    @Override
    public double getCapacity()
    {
        if (validPredicate.getAsBoolean())
            return backingStorage.get().getCapacity();
        else
            return 0;
    }
}