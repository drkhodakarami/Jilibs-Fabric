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

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import com.dynamero.pressure.base.interfaces.PressureItem;
import com.dynamero.pressure.base.interfaces.PressureStorage;
import com.dynamero.pressure.base.interfaces.PressureUnit;
import com.dynamero.pressure.base.storage.DelegatingPressureStorage;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.utils.MathHelper;

/**
 * Implementation of pressure storage for item containers backed by ContainerItemContext.
 */
@Developer("TheMentor")
@CreatedAt("2026-01-12")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
public class SimpleItemPressureStorageImpl implements PressureStorage
{
    /**
     * Creates a delegating pressure storage wrapper for the given item context and parameters.
     *
     * @param ctx        the container item context
     * @param capacity   the maximum pressure capacity
     * @param maxInsert  the maximum rate of pressure insertion
     * @param maxExtract the maximum rate of pressure extraction
     * @return a configured delegating pressure storage instance
     */
    public static PressureStorage createSimpleStorage(ContainerItemContext ctx, double capacity, double maxInsert, double maxExtract)
    {
        MathHelper.notNegative(capacity);
        MathHelper.notNegative(maxInsert);
        MathHelper.notNegative(maxExtract);

        Item startingItem = ctx.getItemVariant().getItem();

        return new DelegatingPressureStorage(
                new SimpleItemPressureStorageImpl(ctx, capacity, maxInsert, maxExtract),
                () -> ctx.getItemVariant().isOf(startingItem) && ctx.getAmount() > 0
        );
    }

    /**
     * Container item context holding the item stack.
     */
    private final ContainerItemContext context;

    /**
     * Maximum pressure capacity per item.
     */
    private final double capacity;

    /**
     * Maximum pressure amount that can be inserted per operation.
     */
    private final double maxInsert;

    /**
     * Maximum pressure amount that can be extracted per operation.
     */
    private final double maxExtract;

    /**
     * Constructs a SimpleItemPressureStorageImpl instance.
     *
     * @param context    the container item context
     * @param capacity   the pressure capacity
     * @param maxInsert  the maximum insertion rate
     * @param maxExtract the maximum extraction rate
     */
    private SimpleItemPressureStorageImpl(ContainerItemContext context, double capacity, double maxInsert, double maxExtract)
    {
        this.context = context;
        this.capacity = capacity;
        this.maxInsert = maxInsert;
        this.maxExtract = maxExtract;
    }

    /**
     * Attempts to set the stored pressure of the stack within the given transaction.
     *
     * @param pressureAmountPerCount the pressure amount per item
     * @param count                  the number of items
     * @param unit                   the pressure unit
     * @param transaction            the active transaction context
     * @return {@code true} if pressure was successfully updated, {@code false} otherwise
     */
    private boolean trySetPressure(double pressureAmountPerCount, long count, PressureUnit unit, TransactionContext transaction)
    {
        ItemStack newStack = context.getItemVariant().toStack();
        PressureItem.setStoredPressureUnchecked(newStack, pressureAmountPerCount, unit);
        ItemVariant newVariant = ItemVariant.of(newStack);

        // Try to convert exactly `count` items.
        try (Transaction nested = transaction.openNested())
        {
            if (context.extract(context.getItemVariant(), count, nested) == count &&
                context.insert(newVariant, count, nested) == count)
            {
                nested.commit();
                return true;
            }
        }

        return false;
    }

    /**
     * Checks whether insertion is supported based on maxInsert.
     *
     * @return {@code true} if maxInsert is greater than 0, {@code false} otherwise
     */
    @Override
    public boolean supportsInsertion()
    {
        return maxInsert > 0;
    }

    /**
     * Checks whether extraction is supported based on maxExtract.
     *
     * @return {@code true} if maxExtract is greater than 0, {@code false} otherwise
     */
    @Override
    public boolean supportsExtraction()
    {
        return maxExtract > 0;
    }

    /**
     * Inserts pressure into the item storage up to the specified maximum amount.
     *
     * @param maxAmount   the maximum pressure amount to insert
     * @param unit        the pressure unit of the inserted amount
     * @param transaction the active transaction context
     * @return the actual amount of pressure inserted
     */
    @Override
    public double insert(double maxAmount, PressureUnit unit, TransactionContext transaction)
    {
        long count = context.getAmount();

        double maxAmountPerCount = maxAmount / count;
        double currentAmountPerCount = getAmount() / count;
        double insertedPerCount = Math.min(maxInsert, Math.min(maxAmountPerCount, capacity - currentAmountPerCount));

        if (insertedPerCount > 0 &&
            trySetPressure(currentAmountPerCount + insertedPerCount, count, unit, transaction))
            return insertedPerCount * count;

        return 0;
    }

    /**
     * Extracts pressure from the item storage up to the specified maximum amount.
     *
     * @param maxAmount   the maximum pressure amount to extract
     * @param unit        the pressure unit of the extracted amount
     * @param transaction the active transaction context
     * @return the actual amount of pressure extracted
     */
    @Override
    public double extract(double maxAmount, PressureUnit unit, TransactionContext transaction)
    {
        long count = context.getAmount();

        double maxAmountPerCount = maxAmount / count;
        double currentAmountPerCount = getAmount() / count;
        double extractedPerCount = Math.min(maxExtract, Math.min(maxAmountPerCount, currentAmountPerCount));

        if (extractedPerCount > 0 &&
            trySetPressure(currentAmountPerCount - extractedPerCount, count, unit, transaction))
            return extractedPerCount * count;

        return 0;
    }

    /**
     * Returns the total pressure currently stored across all items in context.
     *
     * @return total stored pressure
     */
    @Override
    public double getAmount()
    {
        return context.getAmount() * PressureItem.getStoredPressureUnchecked(context.getItemVariant());
    }

    /**
     * Returns the total pressure capacity across all items in context.
     *
     * @return total pressure capacity
     */
    @Override
    public double getCapacity()
    {
        return context.getAmount() * capacity;
    }
}