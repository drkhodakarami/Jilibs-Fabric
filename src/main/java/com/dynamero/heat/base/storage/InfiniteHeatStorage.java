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

import com.dynamero.heat.base.interfaces.HeatStorage;
import com.dynamero.heat.base.interfaces.HeatUnit;
import com.dynamero.shared.annotations.*;

/**
 * A heat storage that can't accept heat, but will allow extracting any amount of heat.
 * Creative heat sources are a possible use case.
 * {@link #INSTANCE} can be used instead of creating a new object every time.
 */
@SuppressWarnings("unused")
@Developer("TurtyWurty")
@CreatedAt("2026-08-10")
@ModifiedAt("2026-08-10")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public class InfiniteHeatStorage implements HeatStorage
{
    /**
     * Singleton infinite heat storage instance.
     */
    public static final InfiniteHeatStorage INSTANCE = new InfiniteHeatStorage();

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
     * Rejects all heat insertion attempts.
     *
     * @param maxAmount   the maximum heat amount
     * @param unit        the heat unit
     * @param transaction the transaction context
     * @return 0
     */
    @Override
    public double insert(double maxAmount, HeatUnit unit, TransactionContext transaction)
    {
        return 0;
    }

    /**
     * Extracts any requested amount of heat infinitely.
     *
     * @param maxAmount   the requested extraction amount
     * @param unit        the heat unit
     * @param transaction the transaction context
     * @return the full requested amount
     */
    @Override
    public double extract(double maxAmount, HeatUnit unit, TransactionContext transaction)
    {
        return maxAmount;
    }

    /**
     * Returns maximum possible double value representing infinite stored heat.
     *
     * @return {@link Double#MAX_VALUE}
     */
    @Override
    public double getAmount()
    {
        return Double.MAX_VALUE;
    }

    /**
     * Returns maximum possible double value representing infinite heat capacity.
     *
     * @return {@link Double#MAX_VALUE}
     */
    @Override
    public double getCapacity()
    {
        return Double.MAX_VALUE;
    }
}