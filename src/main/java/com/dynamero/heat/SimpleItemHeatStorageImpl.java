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

package com.dynamero.heat;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import com.dynamero.heat.base.interfaces.HeatItem;
import com.dynamero.heat.base.interfaces.HeatStorage;
import com.dynamero.heat.base.interfaces.HeatUnit;
import com.dynamero.heat.base.storage.DelegatingHeatStorage;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.utils.MathHelper;

/**
 * Implementation of heat storage for item containers backed by ContainerItemContext.
 */
@Developer("TurtyWurty")
@CreatedAt("2026-10-08")
@ModifiedAt("2026-10-08")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public class SimpleItemHeatStorageImpl implements HeatStorage
{
    /**
     * Creates a delegating heat storage wrapper for the given item context and parameters.
     *
     * @param ctx       the container item context
     * @param capacity  the maximum heat capacity
     * @param maxInsert the maximum rate of heat insertion
     * @param maxExtract the maximum rate of heat extraction
     * @return a configured delegating heat storage instance
     */
    public static HeatStorage createSimpleStorage(ContainerItemContext ctx, double capacity, double maxInsert, double maxExtract)
    {
        MathHelper.notNegative(capacity);
        MathHelper.notNegative(maxInsert);
        MathHelper.notNegative(maxExtract);

        Item startingItem = ctx.getItemVariant().getItem();

        return new DelegatingHeatStorage(
                new SimpleItemHeatStorageImpl(ctx, capacity, maxInsert, maxExtract),
                () -> ctx.getItemVariant().isOf(startingItem) && ctx.getAmount() > 0
        );
    }

    /**
     * Container item context holding the item stack.
     */
    private final ContainerItemContext context;

    /**
     * Maximum heat capacity per item.
     */
    private final double capacity;

    /**
     * Maximum heat amount that can be inserted per operation.
     */
    private final double maxInsert;

    /**
     * Maximum heat amount that can be extracted per operation.
     */
    private final double maxExtract;

    /**
     * Constructs a SimpleItemHeatStorageImpl instance.
     *
     * @param context   the container item context
     * @param capacity  the heat capacity
     * @param maxInsert the maximum insertion rate
     * @param maxExtract the maximum extraction rate
     */
    private SimpleItemHeatStorageImpl(ContainerItemContext context, double capacity, double maxInsert, double maxExtract)
    {
        this.context = context;
        this.capacity = capacity;
        this.maxInsert = maxInsert;
        this.maxExtract = maxExtract;
    }

    /**
     * Attempts to set the stored heat of the stack within the given transaction.
     *
     * @param heatAmountPerCount the heat amount per item
     * @param count              the number of items
     * @param unit               the heat unit
     * @param transaction        the active transaction context
     * @return {@code true} if heat was successfully updated, {@code false} otherwise
     */
    private boolean trySetHeat(double heatAmountPerCount, long count, HeatUnit unit, TransactionContext transaction)
    {
        ItemStack newStack = context.getItemVariant().toStack();
        HeatItem.setStoredHeatUnchecked(newStack, heatAmountPerCount, unit);
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
     * Inserts heat into the item storage up to the specified maximum amount.
     *
     * @param maxAmount   the maximum heat amount to insert
     * @param unit        the heat unit of the inserted amount
     * @param transaction the active transaction context
     * @return the actual amount of heat inserted
     */
    @Override
    public double insert(double maxAmount, HeatUnit unit, TransactionContext transaction)
    {
        long count = context.getAmount();

        double maxAmountPerCount = maxAmount / count;
        double currentAmountPerCount = getAmount() / count;
        double insertedPerCount = Math.min(maxInsert, Math.min(maxAmountPerCount, capacity - currentAmountPerCount));

        if (insertedPerCount > 0 &&
            trySetHeat(currentAmountPerCount + insertedPerCount, count, unit, transaction))
                return insertedPerCount * count;

        return 0;
    }

    /**
     * Extracts heat from the item storage up to the specified maximum amount.
     *
     * @param maxAmount   the maximum heat amount to extract
     * @param unit        the heat unit of the extracted amount
     * @param transaction the active transaction context
     * @return the actual amount of heat extracted
     */
    @Override
    public double extract(double maxAmount, HeatUnit unit, TransactionContext transaction)
    {
        long count = context.getAmount();

        double maxAmountPerCount = maxAmount / count;
        double currentAmountPerCount = getAmount() / count;
        double extractedPerCount = Math.min(maxExtract, Math.min(maxAmountPerCount, currentAmountPerCount));

        if (extractedPerCount > 0 &&
            trySetHeat(currentAmountPerCount - extractedPerCount, count, unit, transaction))
                return extractedPerCount * count;

        return 0;
    }

    /**
     * Returns the total heat currently stored across all items in context.
     *
     * @return total stored heat
     */
    @Override
    public double getAmount()
    {
        return context.getAmount() * HeatItem.getStoredHeatUnchecked(context.getItemVariant());
    }

    /**
     * Returns the total heat capacity across all items in context.
     *
     * @return total heat capacity
     */
    @Override
    public double getCapacity()
    {
        return context.getAmount() * capacity;
    }
}