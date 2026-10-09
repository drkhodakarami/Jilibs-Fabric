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

import com.dynamero.pressure.base.interfaces.PressureStorage;
import com.dynamero.pressure.base.interfaces.PressureUnit;
import com.dynamero.shared.annotations.*;

/**
 * A pressure storage that can't accept pressure, but will allow extracting any amount of pressure.
 * Creative pressure sources are a possible use case.
 * {@link #INSTANCE} can be used instead of creating a new object every time.
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
public class InfinitPressureStorage implements PressureStorage
{
    /**
     * Singleton infinite pressure storage instance.
     */
    public static final InfinitPressureStorage INSTANCE = new InfinitPressureStorage();

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
     * Rejects all pressure insertion attempts.
     *
     * @param maxAmount   the maximum pressure amount
     * @param unit        the pressure unit
     * @param transaction the transaction context
     * @return 0
     */
    @Override
    public double insert(double maxAmount, PressureUnit unit, TransactionContext transaction)
    {
        return 0;
    }

    /**
     * Extracts any requested amount of pressure infinitely.
     *
     * @param maxAmount   the requested extraction amount
     * @param unit        the pressure unit
     * @param transaction the transaction context
     * @return the full requested amount
     */
    @Override
    public double extract(double maxAmount, PressureUnit unit, TransactionContext transaction)
    {
        return maxAmount;
    }

    /**
     * Returns maximum possible double value representing infinite stored pressure.
     *
     * @return {@link Double#MAX_VALUE}
     */
    @Override
    public double getAmount()
    {
        return Double.MAX_VALUE;
    }

    /**
     * Returns maximum possible double value representing infinite pressure capacity.
     *
     * @return {@link Double#MAX_VALUE}
     */
    @Override
    public double getCapacity()
    {
        return Double.MAX_VALUE;
    }
}