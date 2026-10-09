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

import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import org.jetbrains.annotations.NotNull;

import com.dynamero.heat.base.interfaces.HeatStorage;
import com.dynamero.heat.base.interfaces.HeatUnit;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.utils.MathHelper;

/**
 * A base heat storage implementation with fixed capacity, and per-operation insertion and extraction limits.
 * Make sure to override {@link #onFinalCommit} to call {@code markDirty} and similar functions.
 * A base heat storage implementation with fixed capacity, and per-operation insertion and extraction limits.
 * Make sure to override {@link #onFinalCommit} to call {@code markDirty} and similar functions.
 */

@SuppressWarnings("unused")
@Developer("TheMentor")
@CreatedAt("2026-01-12")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
public class SimpleHeatStorage extends SnapshotParticipant<Double> implements HeatStorage
{
    /**
     * Current stored heat amount.
     */
    public double amount = 0;

    /**
     * Maximum heat capacity.
     */
    public final double capacity;

    /**
     * Maximum heat insertion rate allowed per operation.
     */
    public final double maxInsert;

    /**
     * Maximum heat extraction rate allowed per operation.
     */
    public final double maxExtract;

    /**
     * Constructs a SimpleHeatStorage with fixed capacity and transfer limits.
     *
     * @param capacity   the maximum heat capacity
     * @param maxInsert  the maximum heat insertion per operation
     * @param maxExtract the maximum heat extraction per operation
     */
    public SimpleHeatStorage(double capacity, double maxInsert, double maxExtract)
    {
        MathHelper.notNegative(capacity);
        MathHelper.notNegative(maxInsert);
        MathHelper.notNegative(maxExtract);

        this.capacity = capacity;
        this.maxInsert = maxInsert;
        this.maxExtract = maxExtract;
    }

    /**
     * Indicates whether heat insertion is supported.
     *
     * @return {@code true} if maxInsert is greater than zero
     */
    @Override
    public boolean supportsInsertion()
    {
        return maxInsert > 0;
    }

    /**
     * Indicates whether heat extraction is supported.
     *
     * @return {@code true} if maxExtract is greater than zero
     */
    @Override
    public boolean supportsExtraction()
    {
        return maxExtract > 0;
    }

    /**
     * Inserts heat into this storage bounded by available capacity and maxInsert.
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

        double inserted = Math.min(maxInsert, Math.min(maxAmount, capacity - amount));

        if (inserted > 0)
        {
            updateSnapshots(transaction);
            amount += inserted;
            return inserted;
        }

        return 0;
    }

    /**
     * Extracts heat from this storage bounded by stored amount and maxExtract.
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

        double extracted = Math.min(maxExtract, Math.min(maxAmount, amount));

        if (extracted > 0)
        {
            updateSnapshots(transaction);
            amount -= extracted;
            return extracted;
        }

        return 0;
    }

    /**
     * Retrieves the current stored heat amount.
     *
     * @return the heat amount
     */
    @Override
    public double getAmount()
    {
        return amount;
    }

    /**
     * Retrieves the maximum heat capacity.
     *
     * @return the heat capacity
     */
    @Override
    public double getCapacity()
    {
        return capacity;
    }

    /**
     * Creates a transaction rollback snapshot of the current heat amount.
     *
     * @return snapshot of amount
     */
    @Override
    protected @NotNull Double createSnapshot()
    {
        return amount;
    }

    /**
     * Restores the heat amount from a transaction rollback snapshot.
     *
     * @param snapshot the previous amount snapshot
     */
    @Override
    protected void readSnapshot(@NotNull Double snapshot)
    {
        amount = snapshot;
    }
}