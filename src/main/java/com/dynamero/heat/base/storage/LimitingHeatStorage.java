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

import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;

import com.dynamero.heat.base.interfaces.HeatStorage;
import com.dynamero.heat.base.interfaces.HeatUnit;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.utils.MathHelper;

/**
 * A heat storage that will apply additional per-insert and per-extract limits to another storage.
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
public class LimitingHeatStorage implements HeatStorage
{
    /**
     * The underlying heat storage to delegate bounded operations to.
     */
    protected final HeatStorage backingStorage;

    /**
     * Maximum heat insertion rate allowed per operation.
     */
    protected final double maxInsert;

    /**
     * Maximum heat extraction rate allowed per operation.
     */
    protected final double maxExtract;

    /**
     * Create a new limiting storage.
     * @param backingStorage Storage to delegate to.
     * @param maxInsert The maximum amount of heat that can be inserted in one operation.
     * @param maxExtract The maximum amount of heat that can be extracted in one operation.
     */
    public LimitingHeatStorage(HeatStorage backingStorage, double maxInsert, double maxExtract)
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
     * Inserts heat into the delegate clamped by {@link #maxInsert}.
     *
     * @param maxAmount   the maximum heat amount requested
     * @param unit        the heat unit used
     * @param transaction the transaction context
     * @return the inserted heat amount
     */
    @Override
    public double insert(double maxAmount, HeatUnit unit, TransactionContext transaction)
    {
        return backingStorage.insert(Math.min(maxAmount, maxInsert), unit, transaction);
    }

    /**
     * Extracts heat from the delegate clamped by {@link #maxExtract}.
     *
     * @param maxAmount   the maximum heat amount requested
     * @param unit        the heat unit used
     * @param transaction the transaction context
     * @return the extracted heat amount
     */
    @Override
    public double extract(double maxAmount, HeatUnit unit, TransactionContext transaction)
    {
        return backingStorage.extract(Math.min(maxAmount, maxExtract), unit, transaction);
    }

    /**
     * Retrieves the current heat amount from the backing storage.
     *
     * @return the heat amount
     */
    @Override
    public double getAmount()
    {
        return backingStorage.getAmount();
    }

    /**
     * Retrieves the maximum heat capacity from the backing storage.
     *
     * @return the heat capacity
     */
    @Override
    public double getCapacity()
    {
        return backingStorage.getCapacity();
    }
}