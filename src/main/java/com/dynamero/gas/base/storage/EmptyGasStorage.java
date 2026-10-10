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

package com.dynamero.gas.base.storage;

import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import org.jetbrains.annotations.NotNull;

import com.dynamero.gas.base.interfaces.GasVariant;
import com.dynamero.shared.annotations.*;
import org.jspecify.annotations.NonNull;

/**
 * Immutable no-op gas storage with zero capacity that rejects all insertions and extractions.
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
public class EmptyGasStorage extends SingleGasStorage
{
    /**
     * Singleton instance of the empty gas storage.
     */
    public static final EmptyGasStorage INSTANCE = new EmptyGasStorage();

    /**
     * Returns zero capacity for any gas variant.
     *
     * @param variant the gas variant
     * @return 0
     */
    @Override
    protected long getCapacity(@NonNull GasVariant variant)
    {
        return 0;
    }

    /**
     * Rejects insertion into the empty storage.
     *
     * @param insertedVariant the gas variant
     * @param maxAmount       the maximum amount to insert
     * @param transaction     the transaction context
     * @return 0
     */
    @Override
    public long insert(@NonNull GasVariant insertedVariant, long maxAmount, @NotNull TransactionContext transaction)
    {
        return 0;
    }

    /**
     * Rejects extraction from the empty storage.
     *
     * @param extractedVariant the gas variant
     * @param maxAmount        the maximum amount to extract
     * @param transaction      the transaction context
     * @return 0
     */
    @Override
    public long extract(@NonNull GasVariant extractedVariant, long maxAmount, @NotNull TransactionContext transaction)
    {
        return 0;
    }

    /**
     * Returns zero stored gas.
     *
     * @return 0
     */
    @Override
    public long getAmount()
    {
        return 0;
    }

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
}