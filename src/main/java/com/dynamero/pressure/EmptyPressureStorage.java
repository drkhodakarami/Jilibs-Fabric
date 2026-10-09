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

package com.dynamero.pressure;

import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;

import com.dynamero.pressure.base.interfaces.PressureStorage;
import com.dynamero.pressure.base.interfaces.PressureUnit;
import com.dynamero.shared.annotations.*;

/**
 * Immutable no-op pressure storage with zero capacity that rejects all insertions and extractions.
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
public final class EmptyPressureStorage implements PressureStorage
{
    /**
     * Singleton instance of the empty pressure storage.
     */
    public static final PressureStorage INSTANCE = new EmptyPressureStorage();

    /**
     * Constructs an EmptyPressureStorage instance.
     */
    public EmptyPressureStorage()
    {}

    /**
     * Indicates that insertion is not supported.
     *
     * @return {@code false}
     */
    @Override
    public boolean supportsInsertion()
    {
        return false;
    }

    /**
     * Indicates that extraction is not supported.
     *
     * @return {@code false}
     */
    @Override
    public boolean supportsExtraction()
    {
        return false;
    }

    /**
     * Rejects insertion into the empty pressure storage.
     *
     * @param maxAmount   the maximum pressure amount to insert
     * @param unit        the pressure unit of the amount
     * @param transaction the transaction context
     * @return 0
     */
    @Override
    public double insert(double maxAmount, PressureUnit unit, TransactionContext transaction)
    {
        return 0;
    }

    /**
     * Rejects extraction from the empty pressure storage.
     *
     * @param maxAmount   the maximum pressure amount to extract
     * @param unit        the pressure unit of the amount
     * @param transaction the transaction context
     * @return 0
     */
    @Override
    public double extract(double maxAmount, PressureUnit unit, TransactionContext transaction)
    {
        return 0;
    }

    /**
     * Returns zero stored pressure.
     *
     * @return 0
     */
    @Override
    public double getAmount()
    {
        return 0;
    }

    /**
     * Returns zero pressure capacity.
     *
     * @return 0
     */
    @Override
    public double getCapacity()
    {
        return 0;
    }
}