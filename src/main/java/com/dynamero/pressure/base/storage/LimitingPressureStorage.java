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

package com.dynamero.pressure.base.storage;

import java.util.Objects;

import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;

import com.dynamero.pressure.base.interfaces.PressureStorage;
import com.dynamero.pressure.base.interfaces.PressureUnit;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.utils.MathHelper;

/**
 * A pressure storage that will apply additional per-insert and per-extract limits to another storage.
 */
@Developer("TheMentor")
@CreatedAt("2026-08-10")
@ModifiedAt("2026-08-10")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public class LimitingPressureStorage implements PressureStorage
{
    /**
     * The underlying pressure storage to delegate bounded operations to.
     */
    protected final PressureStorage backingStorage;

    /**
     * Maximum pressure insertion rate allowed per operation.
     */
    protected final double maxInsert;

    /**
     * Maximum pressure extraction rate allowed per operation.
     */
    protected final double maxExtract;

    /**
     * Create a new limiting storage.
     *
     * @param backingStorage Storage to delegate to.
     * @param maxInsert      The maximum amount of pressure that can be inserted in one operation.
     * @param maxExtract     The maximum amount of pressure that can be extracted in one operation.
     */
    public LimitingPressureStorage(PressureStorage backingStorage, double maxInsert, double maxExtract)
    {
        Objects.requireNonNull(backingStorage);
        MathHelper.notNegative(maxInsert);
        MathHelper.notNegative(maxExtract);

        this.backingStorage = backingStorage;
        this.maxInsert = maxInsert;
        this.maxExtract = maxExtract;
    }

    /**
     * Checks if insertion is supported by the delegate and the limit is greater than zero.
     *
     * @return {@code true} if insertion is permitted
     */
    @Override
    public boolean supportsInsertion()
    {
        return maxInsert > 0 && backingStorage.supportsInsertion();
    }

    /**
     * Checks if extraction is supported by the delegate and the limit is greater than zero.
     *
     * @return {@code true} if extraction is permitted
     */
    @Override
    public boolean supportsExtraction()
    {
        return maxExtract > 0 && backingStorage.supportsExtraction();
    }

    /**
     * Inserts pressure into the delegate clamped by {@link #maxInsert}.
     *
     * @param maxAmount   the maximum pressure amount requested
     * @param unit        the pressure unit used
     * @param transaction the transaction context
     * @return the inserted pressure amount
     */
    @Override
    public double insert(double maxAmount, PressureUnit unit, TransactionContext transaction)
    {
        return backingStorage.insert(Math.min(maxAmount, maxInsert), unit, transaction);
    }

    /**
     * Extracts pressure from the delegate clamped by {@link #maxExtract}.
     *
     * @param maxAmount   the maximum pressure amount requested
     * @param unit        the pressure unit used
     * @param transaction the transaction context
     * @return the extracted pressure amount
     */
    @Override
    public double extract(double maxAmount, PressureUnit unit, TransactionContext transaction)
    {
        return backingStorage.extract(Math.min(maxAmount, maxExtract), unit, transaction);
    }

    /**
     * Retrieves the current pressure amount from the backing storage.
     *
     * @return the pressure amount
     */
    @Override
    public double getAmount()
    {
        return backingStorage.getAmount();
    }

    /**
     * Retrieves the maximum pressure capacity from the backing storage.
     *
     * @return the pressure capacity
     */
    @Override
    public double getCapacity()
    {
        return backingStorage.getCapacity();
    }
}