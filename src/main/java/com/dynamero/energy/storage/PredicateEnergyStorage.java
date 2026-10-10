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

package com.dynamero.energy.storage;

import java.util.function.Predicate;
import java.util.function.Supplier;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import team.reborn.energy.api.EnergyStorage;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import com.dynamero.shared.annotations.*;

/**
 * Conditional synchronized energy storage that delegates insertion and extraction permissions
 * to configurable predicates and boolean flags, and provides helper predicates for item stacks.
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
public class PredicateEnergyStorage extends SyncedEnergyStorage
{
    /**
     * Predicate determining whether energy insertion is permitted based on the insertion flag.
     */
    private final Predicate<Boolean> canInsert;

    /**
     * Predicate determining whether energy extraction is permitted based on the extraction flag.
     */
    private final Predicate<Boolean> canExtract;

    /**
     * Current state flag passed into the insertion predicate test.
     */
    private boolean insertionFlag;

    /**
     * Current state flag passed into the extraction predicate test.
     */
    private boolean extractionFlag;

    /**
     * Constructs a PredicateEnergyStorage using flag suppliers for initial values.
     *
     * @param blockEntity    the owning block entity
     * @param capacity       the maximum energy capacity
     * @param maxInsert      the maximum energy insertion rate per transaction
     * @param maxExtract     the maximum energy extraction rate per transaction
     * @param insertionFlag  supplier for the initial insertion flag
     * @param extractionFlag supplier for the initial extraction flag
     * @param canInsert      predicate to test insertion permission
     * @param canExtract     predicate to test extraction permission
     */
    public PredicateEnergyStorage(BlockEntity blockEntity, long capacity, long maxInsert, long maxExtract,
                                Supplier<Boolean> insertionFlag, Supplier<Boolean> extractionFlag,
                                Predicate<Boolean> canInsert, Predicate<Boolean> canExtract)
    {
        this(blockEntity, capacity, maxInsert, maxExtract,
            insertionFlag.get(), extractionFlag.get(),
            canInsert, canExtract);
    }

    /**
     * Constructs a PredicateEnergyStorage with initial flags defaulted to true.
     *
     * @param blockEntity the owning block entity
     * @param capacity    the maximum energy capacity
     * @param maxInsert   the maximum energy insertion rate per transaction
     * @param maxExtract  the maximum energy extraction rate per transaction
     * @param canInsert   predicate to test insertion permission
     * @param canExtract  predicate to test extraction permission
     */
    public PredicateEnergyStorage(BlockEntity blockEntity, long capacity, long maxInsert, long maxExtract,
                                Predicate<Boolean> canInsert, Predicate<Boolean> canExtract)
    {
        this(blockEntity, capacity, maxInsert, maxExtract, true, true, canInsert, canExtract);
    }

    /**
     * Constructs a PredicateEnergyStorage with explicit initial boolean flag values.
     *
     * @param blockEntity    the owning block entity
     * @param capacity       the maximum energy capacity
     * @param maxInsert      the maximum energy insertion rate per transaction
     * @param maxExtract     the maximum energy extraction rate per transaction
     * @param insertionFlag  initial insertion flag value
     * @param extractionFlag initial extraction flag value
     * @param canInsert      predicate to test insertion permission
     * @param canExtract     predicate to test extraction permission
     */
    public PredicateEnergyStorage(BlockEntity blockEntity, long capacity, long maxInsert, long maxExtract,
                                boolean insertionFlag, boolean extractionFlag,
                                Predicate<Boolean> canInsert, Predicate<Boolean> canExtract)
    {
        super(blockEntity, capacity, maxInsert, maxExtract);
        this.canInsert = canInsert;
        this.canExtract = canExtract;
        this.insertionFlag = insertionFlag;
        this.extractionFlag = extractionFlag;
    }

    /**
     * Sets the insertion flag value tested by {@link #supportsInsertion()}.
     *
     * @param flag the new insertion flag
     */
    public void setInsertionFlag(boolean flag)
    {
        this.insertionFlag = flag;
    }

    /**
     * Sets the extraction flag value tested by {@link #supportsExtraction()}.
     *
     * @param flag the new extraction flag
     */
    public void setExtractionFlag(boolean flag)
    {
        this.extractionFlag = flag;
    }

    /**
     * Evaluates whether energy insertion is currently permitted.
     *
     * @return {@code true} if insertion is allowed, {@code false} otherwise
     */
    @Override
    public boolean supportsInsertion()
    {
        return this.canInsert.test(this.insertionFlag);
    }

    /**
     * Evaluates whether energy extraction is currently permitted.
     *
     * @return {@code true} if extraction is allowed, {@code false} otherwise
     */
    @Override
    public boolean supportsExtraction()
    {
        return this.canExtract.test(this.extractionFlag);
    }

    /**
     * Creates an item stack predicate testing whether energy can be inserted using a supplier for the amount.
     *
     * @param amount supplier of the energy amount to test insertion with
     * @return a predicate matching insertable item stacks
     */
    public static Predicate<ItemStack> createInsertable(Supplier<Long> amount)
    {
        return stack ->
        {
            EnergyStorage storage = ContainerItemContext.withConstant(stack).find(EnergyStorage.ITEM);
            if(storage == null || !storage.supportsInsertion())
                return false;

            try(Transaction transaction = Transaction.openOuter())
            {
                return storage.insert(amount.get(),  transaction) > 0;
            }
        };
    }

    /**
     * Creates an item stack predicate testing whether a fixed amount of energy can be inserted.
     *
     * @param amount the energy amount to test insertion with
     * @return a predicate matching insertable item stacks
     */
    public static Predicate<ItemStack> createInsertable(long amount)
    {
        return createInsertable(() -> amount);
    }

    /**
     * Creates an item stack predicate testing whether at least 1 unit of energy can be inserted.
     *
     * @return a predicate matching insertable item stacks
     */
    public static Predicate<ItemStack> createInsertable()
    {
        return createInsertable(1);
    }

    /**
     * Creates an item stack predicate testing whether energy can be extracted using a supplier for the amount.
     *
     * @param amount supplier of the energy amount to test extraction with
     * @return a predicate matching extractable item stacks
     */
    public static Predicate<ItemStack> createExtractable(Supplier<Long> amount)
    {
        return stack ->
        {
            EnergyStorage storage = ContainerItemContext.withConstant(stack).find(EnergyStorage.ITEM);
            if(storage == null || !storage.supportsExtraction())
                return false;

            try(Transaction transaction = Transaction.openOuter())
            {
                return storage.extract(amount.get(),  transaction) > 0;
            }
        };
    }

    /**
     * Creates an item stack predicate testing whether a fixed amount of energy can be extracted.
     *
     * @param amount the energy amount to test extraction with
     * @return a predicate matching extractable item stacks
     */
    public static Predicate<ItemStack> createExtractable(long amount)
    {
        return createExtractable(() -> amount);
    }

    /**
     * Creates an item stack predicate testing whether at least 1 unit of energy can be extracted.
     *
     * @return a predicate matching extractable item stacks
     */
    public static Predicate<ItemStack> createExtractable()
    {
        return createExtractable(1);
    }
}