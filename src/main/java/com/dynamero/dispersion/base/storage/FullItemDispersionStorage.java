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

package com.dynamero.dispersion.base.storage;

import java.util.function.Function;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.base.ExtractionOnlyStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import org.jetbrains.annotations.NotNull;

import net.minecraft.world.item.Item;

import com.dynamero.dispersion.base.interfaces.DispersionVariant;
import com.dynamero.shared.annotations.*;

/**
 * Extraction-only single slot dispersion storage for filled container items,
 * which exchanges the full container item for an empty variant upon full dispersion extraction.
 */
@SuppressWarnings("unused")
@Developer("TheMentor")
@CreatedAt("2026-10-08")
@ModifiedAt("2026-10-08")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public class FullItemDispersionStorage implements ExtractionOnlyStorage<DispersionVariant>, SingleSlotStorage<DispersionVariant>
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
     * The dispersion variant contained within the item.
     */
    private final DispersionVariant containedDispersion;

    /**
     * The droplet volume amount contained within the item.
     */
    private final long containedAmount;

    /**
     * Constructs a FullItemDispersionStorage using a fixed empty item definition.
     *
     * @param context             the container item context
     * @param fullItem            the item representing the filled container
     * @param containedDispersion the dispersion variant contained in the item
     * @param insertableAmount    the amount contained in droplets
     */
    public FullItemDispersionStorage(ContainerItemContext context, Item fullItem, DispersionVariant containedDispersion, long insertableAmount)
    {
        this(context, fullVariant -> ItemVariant.of(fullItem, fullVariant.getComponentsPatch()), containedDispersion, insertableAmount);
    }

    /**
     * Constructs a FullItemDispersionStorage using a mapping function from full to empty item variant.
     *
     * @param context             the container item context
     * @param emptyToFullMapping  mapping function from full to empty variant
     * @param containedDispersion the dispersion variant contained in the item
     * @param insertableAmount    the amount contained in droplets
     */
    public FullItemDispersionStorage(ContainerItemContext context, Function<ItemVariant, ItemVariant> emptyToFullMapping, DispersionVariant containedDispersion, long insertableAmount)
    {
        StoragePreconditions.notNegative(insertableAmount);

        this.context = context;
        this.fullItem = context.getItemVariant().getItem();
        this.fullToEmptyMapping = emptyToFullMapping;
        this.containedDispersion = containedDispersion;
        this.containedAmount = insertableAmount;
    }

    /**
     * Extracts dispersion from the full container item, exchanging it for an empty item upon complete extraction.
     *
     * @param resource    the dispersion variant to extract
     * @param maxAmount   the maximum droplet amount to extract
     * @param transaction the transaction context
     * @return the extracted amount if exchange succeeds, 0 otherwise
     */
    @Override
    public long extract(@NotNull DispersionVariant resource, long maxAmount, @NotNull TransactionContext transaction)
    {
        StoragePreconditions.notBlankNotNegative(resource, maxAmount);

        if(!context.getItemVariant().isOf(fullItem))
            return 0;

        if(resource.equals(containedDispersion) && maxAmount >= containedAmount)
        {
            ItemVariant emptyItem = fullToEmptyMapping.apply(context.getItemVariant());
            if(context.exchange(emptyItem, 1, transaction) == 1)
                return containedAmount;
        }

        return 0;
    }

    /**
     * Checks whether the contained dispersion resource is blank.
     *
     * @return {@code true} if blank, {@code false} otherwise
     */
    @Override
    public boolean isResourceBlank()
    {
        return getResource().isBlank();
    }

    /**
     * Retrieves the contained dispersion variant.
     *
     * @return the dispersion variant, or blank if item no longer matches
     */
    @Override
    public @NotNull DispersionVariant getResource()
    {
        return context.getItemVariant().isOf(fullItem) ? containedDispersion : DispersionVariant.blank();
    }

    /**
     * Retrieves the stored dispersion droplet amount.
     *
     * @return the stored amount, or 0 if item no longer matches
     */
    @Override
    public long getAmount()
    {
        return context.getItemVariant().isOf(fullItem) ? containedAmount : 0;
    }

    /**
     * Retrieves the maximum dispersion capacity.
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
        return "FullItemDispersionSorage{" +
            "context=" + context +
            ", fullItem=" + fullItem +
            ", containedDispersion=" + containedDispersion +
            ", insertableAmount=" + containedAmount +
            "}";
    }
}