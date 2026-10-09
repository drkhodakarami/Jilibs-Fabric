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

package com.dynamero.pressure.storage;

import java.util.function.Predicate;
import java.util.function.Supplier;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import com.dynamero.pressure.base.interfaces.PressureStorage;
import com.dynamero.pressure.base.interfaces.PressureUnit;
import com.dynamero.shared.annotations.*;

/**
 * Conditional synchronized pressure storage delegating insertion and extraction permission checks to predicates,
 * and offering static predicate helper builders for evaluating item stack insertability and extractability.
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
public class PredicatePressureStorage extends SyncedPressureStorage
{
    /**
     * Predicate evaluating whether insertion is allowed based on the insertion flag.
     */
    private final Predicate<Boolean> canInsert;

    /**
     * Predicate evaluating whether extraction is allowed based on the extraction flag.
     */
    private final Predicate<Boolean> canExtract;

    /**
     * State flag checked by the insertion predicate.
     */
    private boolean insertionFlag;

    /**
     * State flag checked by the extraction predicate.
     */
    private boolean extractionFlag;

    /**
     * Constructs a PredicatePressureStorage instance with suppliers for initial flag states.
     *
     * @param blockEntity    the owning block entity
     * @param capacity       the maximum pressure capacity
     * @param maxInsert      the maximum insertion rate
     * @param maxExtract     the maximum extraction rate
     * @param insertionFlag  supplier providing the initial insertion flag state
     * @param extractionFlag supplier providing the initial extraction flag state
     * @param canInsert      predicate determining insertion permission
     * @param canExtract     predicate determining extraction permission
     */
    public PredicatePressureStorage(BlockEntity blockEntity, long capacity, long maxInsert, long maxExtract,
                                Supplier<Boolean> insertionFlag, Supplier<Boolean> extractionFlag,
                                Predicate<Boolean> canInsert, Predicate<Boolean> canExtract)
    {
        this(blockEntity, capacity, maxInsert, maxExtract,
            insertionFlag.get(), extractionFlag.get(),
            canInsert, canExtract);
    }

    /**
     * Constructs a PredicatePressureStorage instance with default true flag states.
     *
     * @param blockEntity the owning block entity
     * @param capacity    the maximum pressure capacity
     * @param maxInsert   the maximum insertion rate
     * @param maxExtract  the maximum extraction rate
     * @param canInsert   predicate determining insertion permission
     * @param canExtract  predicate determining extraction permission
     */
    public PredicatePressureStorage(BlockEntity blockEntity, long capacity, long maxInsert, long maxExtract,
                                Predicate<Boolean> canInsert, Predicate<Boolean> canExtract)
    {
        this(blockEntity, capacity, maxInsert, maxExtract, true, true, canInsert, canExtract);
    }

    /**
     * Constructs a PredicatePressureStorage instance with explicit initial flag states.
     *
     * @param blockEntity    the owning block entity
     * @param capacity       the maximum pressure capacity
     * @param maxInsert      the maximum insertion rate
     * @param maxExtract     the maximum extraction rate
     * @param insertionFlag  initial insertion flag state
     * @param extractionFlag initial extraction flag state
     * @param canInsert      predicate determining insertion permission
     * @param canExtract     predicate determining extraction permission
     */
    public PredicatePressureStorage(BlockEntity blockEntity, long capacity, long maxInsert, long maxExtract,
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
     * Sets the insertion state flag.
     *
     * @param flag the new insertion flag state
     */
    public void setInsertionFlag(boolean flag)
    {
        this.insertionFlag = flag;
    }

    /**
     * Sets the extraction state flag.
     *
     * @param flag the new extraction flag state
     */
    public void setExtractionFlag(boolean flag)
    {
        this.extractionFlag = flag;
    }

    /**
     * Evaluates whether insertion is supported by testing the insertion predicate against the current insertion flag.
     *
     * @return {@code true} if insertion is allowed, {@code false} otherwise
     */
    @Override
    public boolean supportsInsertion()
    {
        return this.canInsert.test(this.insertionFlag);
    }

    /**
     * Evaluates whether extraction is supported by testing the extraction predicate against the current extraction flag.
     *
     * @return {@code true} if extraction is allowed, {@code false} otherwise
     */
    @Override
    public boolean supportsExtraction()
    {
        return this.canExtract.test(this.extractionFlag);
    }

    /**
     * Creates an item stack predicate testing whether pressure can be inserted into the stack, using amount and unit suppliers.
     *
     * @param amount supplier of the pressure amount
     * @param unit   supplier of the pressure unit
     * @return predicate accepting an item stack
     */
    public static Predicate<ItemStack> createInsertable(Supplier<Double> amount, Supplier<PressureUnit> unit)
    {
        return stack ->
        {
            PressureStorage storage = ContainerItemContext.withConstant(stack).find(PressureStorage.ITEM);
            if(storage == null || !storage.supportsInsertion())
                return false;

            try(Transaction transaction = Transaction.openOuter())
            {
                return storage.insert(amount.get(), unit.get(),  transaction) > 0;
            }
        };
    }

    /**
     * Creates an item stack predicate testing whether pressure can be inserted into the stack, using a fixed amount and unit.
     *
     * @param amount the pressure amount
     * @param unit   the pressure unit
     * @return predicate accepting an item stack
     */
    public static Predicate<ItemStack> createInsertable(double amount, PressureUnit unit)
    {
        return createInsertable(() -> amount, () -> unit);
    }

    /**
     * Creates an item stack predicate testing whether 1000 Pascal of pressure can be inserted into the stack.
     *
     * @return predicate accepting an item stack
     */
    public static Predicate<ItemStack> createInsertable()
    {
        return createInsertable(1000, PressureUnit.PASCAL);
    }

    /**
     * Creates an item stack predicate testing whether pressure can be extracted from the stack, using amount and unit suppliers.
     *
     * @param amount supplier of the pressure amount
     * @param unit   supplier of the pressure unit
     * @return predicate accepting an item stack
     */
    public static Predicate<ItemStack> createExtractable(Supplier<Double> amount, Supplier<PressureUnit> unit)
    {
        return stack ->
        {
            PressureStorage storage = ContainerItemContext.withConstant(stack).find(PressureStorage.ITEM);
            if(storage == null || !storage.supportsExtraction())
                return false;

            try(Transaction transaction = Transaction.openOuter())
            {
                return storage.extract(amount.get(), unit.get(),  transaction) > 0;
            }
        };
    }

    /**
     * Creates an item stack predicate testing whether pressure can be extracted from the stack, using a fixed amount and unit.
     *
     * @param amount the pressure amount
     * @param unit   the pressure unit
     * @return predicate accepting an item stack
     */
    public static Predicate<ItemStack> createExtractable(double amount, PressureUnit unit)
    {
        return createExtractable(() -> amount, () -> unit);
    }

    /**
     * Creates an item stack predicate testing whether 1000 Pascal of pressure can be extracted from the stack.
     *
     * @return predicate accepting an item stack
     */
    public static Predicate<ItemStack> createExtractable()
    {
        return createExtractable(1000, PressureUnit.PASCAL);
    }
}