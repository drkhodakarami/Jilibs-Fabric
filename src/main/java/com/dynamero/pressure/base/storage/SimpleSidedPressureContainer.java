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
import org.jspecify.annotations.Nullable;

import net.minecraft.core.Direction;

import com.dynamero.pressure.base.interfaces.PressureStorage;
import com.dynamero.pressure.base.interfaces.PressureUnit;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.utils.MathHelper;

/**
 * A base pressure storage implementation with a dynamic capacity, and per-side per-operation insertion and extraction limits.
 * {@link #getSideStorage} can be used to get an {@code IPressureStorage} implementation for a given side.
 * Make sure to override {@link #onFinalCommit} to call {@code markDirty} and similar functions.
 */
@Developer("TheMentor")
@CreatedAt("2026-01-12")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
public abstract class SimpleSidedPressureContainer extends SnapshotParticipant<Double>
{
    /**
     * Current stored pressure amount.
     */
    public double amount = 0;

    /**
     * Array of sided pressure storages indexed by 3D data value (with index 6 representing null direction).
     */
    private final SideStorage[] sideStorages = new SideStorage[7];

    /**
     * Constructs a SimpleSidedPressureContainer initializing sided storages for all 6 directions plus null.
     */
    public SimpleSidedPressureContainer()
    {
        for (int i = 0; i < 7; ++i)
            sideStorages[i] = new SideStorage(i == 6 ? null : Direction.from3DDataValue(i));
    }

    /**
     * Returns the current capacity of this storage.
     *
     * @return The current capacity of this storage.
     */
    public abstract double getCapacity();

    /**
     * Returns the maximum amount of pressure that can be inserted from the given side.
     *
     * @param side the direction or null
     * @return The maximum amount of pressure that can be inserted in a single operation from the passed side.
     */
    public abstract long getMaxInsert(@Nullable Direction side);

    /**
     * Returns the maximum amount of pressure that can be extracted from the given side.
     *
     * @param side the direction or null
     * @return The maximum amount of pressure that can be extracted in a single operation from the passed side.
     */
    public abstract long getMaxExtract(@Nullable Direction side);

    /**
     * Returns the sided storage wrapper for the specified side.
     *
     * @param side the direction or null
     * @return A {@link PressureStorage} implementation for the passed side.
     */
    public PressureStorage getSideStorage(@Nullable Direction side)
    {
        return sideStorages[side == null ? 6 : side.get3DDataValue()];
    }

    /**
     * Creates a transaction rollback snapshot of the pressure amount.
     *
     * @return amount snapshot
     */
    @Override
    protected @NotNull Double createSnapshot()
    {
        return amount;
    }

    /**
     * Restores the pressure amount from a transaction snapshot.
     *
     * @param snapshot the previous amount snapshot
     */
    @Override
    protected void readSnapshot(@NotNull Double snapshot)
    {
        amount = snapshot;
    }

    /**
     * Sided storage wrapper delegating to the container with per-side limits.
     */
    private class SideStorage implements PressureStorage
    {
        /**
         * The mapped direction, or null for non-directional access.
         */
        private final Direction side;

        /**
         * Constructs a SideStorage for the specified direction.
         *
         * @param side the direction
         */
        private SideStorage(Direction side)
        {
            this.side = side;
        }

        /**
         * Checks whether pressure insertion is supported on this side.
         *
         * @return {@code true} if maxInsert is greater than zero
         */
        @Override
        public boolean supportsInsertion()
        {
            return getMaxInsert(side) > 0;
        }

        /**
         * Checks whether pressure extraction is supported on this side.
         *
         * @return {@code true} if maxExtract is greater than zero
         */
        @Override
        public boolean supportsExtraction()
        {
            return getMaxExtract(side) > 0;
        }

        /**
         * Inserts pressure through this side bounded by side limits and remaining capacity.
         *
         * @param maxAmount   the maximum pressure amount
         * @param unit        the pressure unit used
         * @param transaction the transaction context
         * @return the inserted pressure amount
         */
        @Override
        public double insert(double maxAmount, PressureUnit unit, TransactionContext transaction)
        {
            MathHelper.notNegative(maxAmount);

            double inserted = Math.min(getMaxInsert(side), Math.min(maxAmount, getCapacity() - amount));

            if (inserted > 0)
            {
                updateSnapshots(transaction);
                amount += inserted;
                return inserted;
            }

            return 0;
        }

        /**
         * Extracts pressure through this side bounded by side limits and stored amount.
         *
         * @param maxAmount   the maximum pressure amount
         * @param unit        the pressure unit used
         * @param transaction the transaction context
         * @return the extracted pressure amount
         */
        @Override
        public double extract(double maxAmount, PressureUnit unit, TransactionContext transaction)
        {
            MathHelper.notNegative(maxAmount);

            double extracted = Math.min(getMaxExtract(side), Math.min(maxAmount, amount));

            if (extracted > 0)
            {
                updateSnapshots(transaction);
                amount -= extracted;
                return extracted;
            }

            return 0;
        }

        /**
         * Retrieves the stored pressure amount.
         *
         * @return the pressure amount
         */
        @Override
        public double getAmount()
        {
            return amount;
        }

        /**
         * Retrieves the pressure capacity of the container.
         *
         * @return the pressure capacity
         */
        @Override
        public double getCapacity()
        {
            return SimpleSidedPressureContainer.this.getCapacity();
        }
    }
}