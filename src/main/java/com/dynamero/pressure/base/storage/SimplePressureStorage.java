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

import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import org.jetbrains.annotations.NotNull;

import com.dynamero.pressure.base.interfaces.PressureStorage;
import com.dynamero.pressure.base.interfaces.PressureUnit;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.utils.MathHelper;

/**
 * A base pressure storage implementation with fixed capacity, and per-operation insertion and extraction limits.
 * Make sure to override {@link #onFinalCommit} to call {@code markDirty} and similar functions.
 */
@Developer("TheMentor")
@CreatedAt("2026-01-12")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
public class SimplePressureStorage extends SnapshotParticipant<Double> implements PressureStorage
{
    /**
     * Current stored pressure amount.
     */
    public double amount = 0;

    /**
     * Maximum pressure capacity.
     */
    public final double capacity;

    /**
     * Maximum pressure insertion rate allowed per operation.
     */
    public final double maxInsert;

    /**
     * Maximum pressure extraction rate allowed per operation.
     */
    public final double maxExtract;

    /**
     * Constructs a SimplePressureStorage with fixed capacity and transfer limits.
     *
     * @param capacity   the maximum pressure capacity
     * @param maxInsert  the maximum pressure insertion per operation
     * @param maxExtract the maximum pressure extraction per operation
     */
    public SimplePressureStorage(double capacity, double maxInsert, double maxExtract)
    {
        MathHelper.notNegative(capacity);
        MathHelper.notNegative(maxInsert);
        MathHelper.notNegative(maxExtract);

        this.capacity = capacity;
        this.maxInsert = maxInsert;
        this.maxExtract = maxExtract;
    }

    /**
     * Indicates whether pressure insertion is supported.
     *
     * @return {@code true} if maxInsert is greater than zero
     */
    @Override
    public boolean supportsInsertion()
    {
        return maxInsert > 0;
    }

    /**
     * Indicates whether pressure extraction is supported.
     *
     * @return {@code true} if maxExtract is greater than zero
     */
    @Override
    public boolean supportsExtraction()
    {
        return maxExtract > 0;
    }

    /**
     * Inserts pressure into this storage bounded by available capacity and maxInsert.
     *
     * @param maxAmount   the maximum pressure amount to insert
     * @param unit        the pressure unit used
     * @param transaction the transaction context
     * @return the inserted pressure amount
     */
    @Override
    public double insert(double maxAmount, PressureUnit unit, TransactionContext transaction)
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
     * Extracts pressure from this storage bounded by stored amount and maxExtract.
     *
     * @param maxAmount   the maximum pressure amount to extract
     * @param unit        the pressure unit used
     * @param transaction the transaction context
     * @return the extracted pressure amount
     */
    @Override
    public double extract(double maxAmount, PressureUnit unit, TransactionContext transaction)
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
     * Retrieves the current stored pressure amount.
     *
     * @return the pressure amount
     */
    @Override
    public double getAmount()
    {
        return amount;
    }

    /**
     * Retrieves the maximum pressure capacity.
     *
     * @return the pressure capacity
     */
    @Override
    public double getCapacity()
    {
        return capacity;
    }

    /**
     * Creates a transaction rollback snapshot of the current pressure amount.
     *
     * @return snapshot of amount
     */
    @Override
    protected @NotNull Double createSnapshot()
    {
        return amount;
    }

    /**
     * Restores the pressure amount from a transaction rollback snapshot.
     *
     * @param snapshot the previous amount snapshot
     */
    @Override
    protected void readSnapshot(@NotNull Double snapshot)
    {
        amount = snapshot;
    }
}