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

import java.util.function.Function;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.base.ExtractionOnlyStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import org.jetbrains.annotations.NotNull;

import net.minecraft.world.item.Item;

import com.dynamero.gas.base.interfaces.GasVariant;
import com.dynamero.shared.annotations.*;

/**
 * Extraction-only single slot gas storage for filled container items (such as gas bottles),
 * which exchanges the full container item for an empty variant upon full gas extraction.
 */
@SuppressWarnings("unused")
@Developer("TheMentor")
@CreatedAt("2026-01-12")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
public class FullItemGasStorage implements ExtractionOnlyStorage<GasVariant>, SingleSlotStorage<GasVariant>
{
    /**
     * Context representing the container item in a player hand or slot.
     */
    private final ContainerItemContext context;

    /**
     * The expected filled item type.
     */
    private final Item fullItem;

    /**
     * Function mapping a filled item variant to its empty counterpart.
     */
    private final Function<ItemVariant, ItemVariant> fullToEmptyMapping;

    /**
     * The gas variant contained within the item.
     */
    private final GasVariant containedGas;

    /**
     * The droplet volume amount contained within the item.
     */
    private final long containedAmount;

    /**
     * Constructs a FullItemGasStorage using a fixed empty item definition.
     *
     * @param context         the container item context
     * @param fullItem        the item representing the empty container
     * @param containedGas    the gas variant contained in the item
     * @param containedAmount the amount contained in droplets
     */
    public FullItemGasStorage(ContainerItemContext context, Item fullItem, GasVariant containedGas, long containedAmount)
    {
        this(context, fullVariant -> ItemVariant.of(fullItem, fullVariant.getComponentsPatch()), containedGas, containedAmount);
    }

    /**
     * Constructs a FullItemGasStorage using a mapping function from full to empty item variant.
     *
     * @param context            the container item context
     * @param fullToEmptyMapping mapping function from full to empty variant
     * @param containedGas       the gas variant contained in the item
     * @param containedAmount    the amount contained in droplets
     */
    public FullItemGasStorage(ContainerItemContext context, Function<ItemVariant, ItemVariant> fullToEmptyMapping, GasVariant containedGas, long containedAmount)
    {
        StoragePreconditions.notBlankNotNegative(containedGas, containedAmount);

        this.context = context;
        this.fullItem = context.getItemVariant().getItem();
        this.fullToEmptyMapping = fullToEmptyMapping;
        this.containedGas = containedGas;
        this.containedAmount = containedAmount;
    }

    /**
     * Extracts gas from the full container item, exchanging it for an empty item upon complete extraction.
     *
     * @param resource    the gas variant to extract
     * @param maxAmount   the maximum droplet amount to extract
     * @param transaction the transaction context
     * @return the extracted amount if exchange succeeds, 0 otherwise
     */
    @Override
    public long extract(@NotNull GasVariant resource, long maxAmount, @NotNull TransactionContext transaction)
    {
        StoragePreconditions.notBlankNotNegative(resource, maxAmount);

        if(!context.getItemVariant().isOf(fullItem)) return 0;

        if(resource.equals(containedGas) && maxAmount >= containedAmount)
        {
            ItemVariant emptyItem = fullToEmptyMapping.apply(context.getItemVariant());
            if(context.exchange(emptyItem, 1, transaction) == 1)
            {
                return containedAmount;
            }
        }

        return 0;
    }

    /**
     * Checks whether the contained gas resource is blank.
     *
     * @return {@code true} if blank, {@code false} otherwise
     */
    @Override
    public boolean isResourceBlank()
    {
        return getResource().isBlank();
    }

    /**
     * Retrieves the contained gas variant.
     *
     * @return the gas variant, or blank if item no longer matches
     */
    @Override
    public @NotNull GasVariant getResource()
    {
        return context.getItemVariant().isOf(fullItem) ? containedGas : GasVariant.blank();
    }

    /**
     * Retrieves the stored gas droplet amount.
     *
     * @return the stored amount, or 0 if item no longer matches
     */
    @Override
    public long getAmount()
    {
        return context.getItemVariant().isOf(fullItem) ? containedAmount : 0;
    }

    /**
     * Retrieves the maximum gas capacity.
     *
     * @return the capacity in droplets
     */
    @Override
    public long getCapacity()
    {
        return getAmount();
    }

    /**
     * Returns a string representation of this storage.
     *
     * @return string representation
     */
    @Override
    public String toString()
    {
        return "FullItemGasStorage{" +
            "context=" + context +
            ", fullItem=" + fullItem +
            ", containedGas=" + containedGas +
            ", containedAmount=" + containedAmount +
            '}';
    }
}