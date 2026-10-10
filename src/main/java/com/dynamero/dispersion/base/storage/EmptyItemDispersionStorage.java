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

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.BlankVariantView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.InsertionOnlyStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import org.jetbrains.annotations.NotNull;

import net.minecraft.world.item.Item;

import com.dynamero.dispersion.base.interfaces.DispersionVariant;
import com.dynamero.dispersion.base.records.Dispersion;
import com.dynamero.shared.annotations.*;

/**
 * Insertion-only dispersion storage for empty container items,
 * which exchanges the empty item for a filled variant upon full dispersion insertion.
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
public class EmptyItemDispersionStorage implements InsertionOnlyStorage<DispersionVariant>
{
    /**
     * Context representing the container item in a player hand or slot.
     */
    private final ContainerItemContext context;

    /**
     * The expected empty item type.
     */
    private final Item emptyItem;

    /**
     * Function mapping an empty item variant to its filled counterpart.
     */
    private final Function<ItemVariant, ItemVariant> emptyToFullMapping;

    /**
     * The specific dispersion accepted to fill this container item.
     */
    private final Dispersion insertableDispersion;

    /**
     * The required droplet amount to fill the container.
     */
    private final long insertableAmount;

    /**
     * Immutable single-element list containing the blank view for this storage.
     */
    private final List<StorageView<DispersionVariant>> blankView;

    /**
     * Constructs an EmptyItemDispersionStorage using a fixed filled item definition.
     *
     * @param context              the container item context
     * @param fullItem             the item representing the filled container
     * @param insertableDispersion the dispersion accepted by the container
     * @param insertableAmount     the amount required to fill in droplets
     */
    public EmptyItemDispersionStorage(ContainerItemContext context, Item fullItem, Dispersion insertableDispersion, long insertableAmount)
    {
        this(context, emptyVariant -> ItemVariant.of(fullItem, emptyVariant.getComponentsPatch()), insertableDispersion, insertableAmount);
    }

    /**
     * Constructs an EmptyItemDispersionStorage using a mapping function from empty to filled item variant.
     *
     * @param context              the container item context
     * @param emptyToFullMapping   mapping function from empty to filled variant
     * @param insertableDispersion the dispersion accepted by the container
     * @param insertableAmount     the amount required to fill in droplets
     */
    public EmptyItemDispersionStorage(ContainerItemContext context, Function<ItemVariant, ItemVariant> emptyToFullMapping, Dispersion insertableDispersion, long insertableAmount)
    {
        StoragePreconditions.notNegative(insertableAmount);

        this.context = context;
        this.emptyItem = context.getItemVariant().getItem();
        this.emptyToFullMapping = emptyToFullMapping;
        this.insertableDispersion = insertableDispersion;
        this.insertableAmount = insertableAmount;
        this.blankView = List.of(new BlankVariantView<>(DispersionVariant.blank(), insertableAmount));
    }

    /**
     * Inserts dispersion into the empty container item, exchanging it for the filled item upon a complete fill.
     *
     * @param resource    the dispersion variant to insert
     * @param maxAmount   the maximum droplet amount to insert
     * @param transaction the transaction context
     * @return the inserted amount if exchange succeeds, 0 otherwise
     */
    @Override
    public long insert(@NotNull DispersionVariant resource, long maxAmount, @NotNull TransactionContext transaction)
    {
        StoragePreconditions.notBlankNotNegative(resource, maxAmount);

        if(!context.getItemVariant().isOf(emptyItem))
            return 0;

        if(resource.isOf(insertableDispersion) && maxAmount >= insertableAmount)
        {
            ItemVariant fullItem = emptyToFullMapping.apply(context.getItemVariant());
            if(context.exchange(fullItem, 1, transaction) == 1)
                return insertableAmount;
        }

        return 0;
    }

    /**
     * Returns an iterator over the storage views.
     *
     * @return view iterator
     */
    @Override
    public @NotNull Iterator<StorageView<DispersionVariant>> iterator()
    {
        return blankView.iterator();
    }

    /**
     * Returns a string representation of this storage.
     *
     * @return string representation
     */
    @Override
    public String toString()
    {
        return "EmptyItemDispersionSorage{" +
            "context=" + context +
            ", emptyItem=" + emptyItem +
            ", insertableDispersion=" + insertableDispersion +
            ", insertableAmount=" + insertableAmount +
            "}";
    }
}