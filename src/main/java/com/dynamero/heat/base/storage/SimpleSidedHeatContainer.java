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
import org.jspecify.annotations.Nullable;

import net.minecraft.core.Direction;

import com.dynamero.heat.base.interfaces.HeatStorage;
import com.dynamero.heat.base.interfaces.HeatUnit;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.utils.MathHelper;

/**
 * A base heat storage implementation with a dynamic capacity, and per-side per-operation insertion and extraction limits.
 * {@link #getSideStorage} can be used to get an {@code IHeatStorage} implementation for a given side.
 * Make sure to override {@link #onFinalCommit} to call {@code markDirty} and similar functions.
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
public abstract class SimpleSidedHeatContainer extends SnapshotParticipant<Double>
{
    /**
     * Current stored heat amount.
     */
    public double amount = 0;

    /**
     * Array of sided heat storages indexed by 3D data value (with index 6 representing null direction).
     */
    private final SideStorage[] sideStorages = new SideStorage[7];

    /**
     * Constructs a SimpleSidedHeatContainer initializing sided storages for all 6 directions plus null.
     */
    public SimpleSidedHeatContainer()
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
     * Returns the maximum amount of heat that can be inserted from the given side.
     *
     * @param side the direction or null
     * @return The maximum amount of heat that can be inserted in a single operation from the passed side.
     */
    public abstract long getMaxInsert(@Nullable Direction side);

    /**
     * Returns the maximum amount of heat that can be extracted from the given side.
     *
     * @param side the direction or null
     * @return The maximum amount of heat that can be extracted in a single operation from the passed side.
     */
    public abstract long getMaxExtract(@Nullable Direction side);

    /**
     * Returns the sided storage wrapper for the specified side.
     *
     * @param side the direction or null
     * @return An {@link HeatStorage} implementation for the passed side.
     */
    public HeatStorage getSideStorage(@Nullable Direction side)
    {
        return sideStorages[side == null ? 6 : side.get3DDataValue()];
    }

    /**
     * Creates a transaction rollback snapshot of the heat amount.
     *
     * @return amount snapshot
     */
    @Override
    protected @NotNull Double createSnapshot()
    {
        return amount;
    }

    /**
     * Restores the heat amount from a transaction snapshot.
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
    private class SideStorage implements HeatStorage
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
         * Checks whether heat insertion is supported on this side.
         *
         * @return {@code true} if maxInsert is greater than zero
         */
        @Override
        public boolean supportsInsertion()
        {
            return getMaxInsert(side) > 0;
        }

        /**
         * Checks whether heat extraction is supported on this side.
         *
         * @return {@code true} if maxExtract is greater than zero
         */
        @Override
        public boolean supportsExtraction()
        {
            return getMaxExtract(side) > 0;
        }

        /**
         * Inserts heat through this side bounded by side limits and remaining capacity.
         *
         * @param maxAmount   the maximum heat amount
         * @param unit        the heat unit used
         * @param transaction the transaction context
         * @return the inserted heat amount
         */
        @Override
        public double insert(double maxAmount, HeatUnit unit, TransactionContext transaction)
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
         * Extracts heat through this side bounded by side limits and stored amount.
         *
         * @param maxAmount   the maximum heat amount
         * @param unit        the heat unit used
         * @param transaction the transaction context
         * @return the extracted heat amount
         */
        @Override
        public double extract(double maxAmount, HeatUnit unit, TransactionContext transaction)
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
         * Retrieves the stored heat amount.
         *
         * @return the heat amount
         */
        @Override
        public double getAmount()
        {
            return amount;
        }

        /**
         * Retrieves the heat capacity of the container.
         *
         * @return the heat capacity
         */
        @Override
        public double getCapacity()
        {
            return SimpleSidedHeatContainer.this.getCapacity();
        }
    }
}