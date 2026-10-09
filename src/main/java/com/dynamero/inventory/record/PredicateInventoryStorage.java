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

package com.dynamero.inventory.record;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

import com.google.common.collect.MapMaker;
import net.fabricmc.fabric.api.transfer.v1.item.ContainerStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import com.dynamero.inventory.base.PredicateKey;
import com.dynamero.shared.annotations.*;

/**
 * Container storage wrapper that delegates transfer operations to an underlying {@link ContainerStorage}
 * while constraining insertion and extraction availability using conditional permission suppliers.
 *
 * @param storage    the underlying container storage
 * @param canInsert  supplier determining whether insertion is currently permitted
 * @param canExtract supplier determining whether extraction is currently permitted
 */
@Developer("TurtyWurty")
@ModifiedBy("The Mentor")
@CreatedAt("2025-04-18")
@Repository("https://github.com/DaRealTurtyWurty/Industria")
@Discord("https://discord.turtywurty.dev/")
@Youtube("https://www.youtube.com/@TurtyWurty")

@Experimental
@SuppressWarnings("NonExtendableApiUsage")
public record PredicateInventoryStorage(ContainerStorage storage, Supplier<Boolean> canInsert, Supplier<Boolean> canExtract) implements ContainerStorage
{
    /**
     * Weakly-referenced cache mapping container storages and predicate keys to cached storage wrappers.
     */
    private static final ConcurrentMap<ContainerStorage, ConcurrentMap<PredicateKey, PredicateInventoryStorage>> CACHE = new MapMaker().weakKeys().makeMap();

    /**
     * Obtains or creates a cached {@link PredicateInventoryStorage} wrapping the specified container storage.
     *
     * @param storage    the container storage to wrap
     * @param canInsert  supplier determining insertion permission
     * @param canExtract supplier determining extraction permission
     * @return a cached or newly created {@link PredicateInventoryStorage} instance
     */
    public static PredicateInventoryStorage of(ContainerStorage storage, Supplier<Boolean> canInsert, Supplier<Boolean> canExtract)
    {
        ConcurrentMap<PredicateKey, PredicateInventoryStorage> inventoryCache = CACHE.computeIfAbsent(storage, _ -> new MapMaker().makeMap());
        PredicateKey key = new PredicateKey(canInsert, canExtract);
        return inventoryCache.computeIfAbsent(key, _ -> new PredicateInventoryStorage(storage, canInsert, canExtract));
    }

    /**
     * Retrieves an unmodifiable list of single-slot storage views delegating to the underlying container.
     *
     * @return list of single-slot storages
     */
    @Override
    public @UnmodifiableView @NotNull List<SingleSlotStorage<ItemVariant>> getSlots()
    {
        return this.storage.getSlots();
    }

    /**
     * Checks whether insertion is supported by both the underlying storage and the insertion supplier.
     *
     * @return {@code true} if insertion is supported and allowed, {@code false} otherwise
     */
    @Override
    public boolean supportsInsertion()
    {
        return this.storage.supportsInsertion() && this.canInsert.get();
    }

    /**
     * Checks whether extraction is supported by both the underlying storage and the extraction supplier.
     *
     * @return {@code true} if extraction is supported and allowed, {@code false} otherwise
     */
    @Override
    public boolean supportsExtraction()
    {
        return this.storage.supportsExtraction() && this.canExtract.get();
    }

    /**
     * Inserts item variants into the underlying storage within a transaction context.
     *
     * @param resource           the item variant to insert
     * @param maxAmount          the maximum amount to insert
     * @param transactionContext the active transaction context
     * @return the actual amount inserted
     */
    @Override
    public long insert(@NotNull ItemVariant resource, long maxAmount, @NotNull TransactionContext transactionContext)
    {
        return storage.insert(resource, maxAmount, transactionContext);
    }

    /**
     * Extracts item variants from the underlying storage within a transaction context.
     *
     * @param resource           the item variant to extract
     * @param maxAmount          the maximum amount to extract
     * @param transactionContext the active transaction context
     * @return the actual amount extracted
     */
    @Override
    public long extract(@NotNull ItemVariant resource, long maxAmount, @NotNull TransactionContext transactionContext)
    {
        return storage.extract(resource, maxAmount, transactionContext);
    }

    /**
     * Returns an iterator over all storage views in the underlying container storage.
     *
     * @return storage view iterator
     */
    @Override
    public @NotNull Iterator<StorageView<ItemVariant>> iterator()
    {
        return storage.iterator();
    }

    /**
     * Retrieves the total number of slots in the underlying container storage.
     *
     * @return total slot count
     */
    @Override
    public int getSlotCount()
    {
        return storage.getSlotCount();
    }

    /**
     * Retrieves the single-slot storage at the specified slot index from the underlying storage.
     *
     * @param slotIndex the zero-based slot index
     * @return the single-slot storage
     */
    @Override
    public @NotNull SingleSlotStorage<ItemVariant> getSlot(int slotIndex)
    {
        return storage.getSlot(slotIndex);
    }

    /**
     * Checks equality between this storage wrapper and another object.
     *
     * @param obj the object to compare against
     * @return {@code true} if equal, {@code false} otherwise
     */
    @SuppressWarnings("EqualsDoesntCheckParameterClass")
    @Override
    public boolean equals(Object obj)
    {
        return storage.equals(obj);
    }

    /**
     * Computes the hash code for this storage wrapper based on the underlying storage.
     *
     * @return the hash code
     */
    @Override
    public int hashCode()
    {
        return storage.hashCode();
    }

    /**
     * Generates a string representation of this storage wrapper.
     *
     * @return formatted string representation
     */
    @Override
    public @NotNull String toString()
    {
        return "PredicateInventoryStorage[%s]".formatted(storage.toString());
    }

    /**
     * Returns an iterator over all non-empty storage views in the underlying storage.
     *
     * @return non-empty storage view iterator
     */
    @Override
    public @NotNull Iterator<StorageView<ItemVariant>> nonEmptyIterator()
    {
        return storage.nonEmptyIterator();
    }

    /**
     * Returns an iterable over all non-empty storage views in the underlying storage.
     *
     * @return non-empty storage views iterable
     */
    @Override
    public @NotNull Iterable<StorageView<ItemVariant>> nonEmptyViews()
    {
        return storage.nonEmptyViews();
    }

    /**
     * Retrieves the version counter of the underlying storage to track modifications.
     *
     * @return the current storage version
     */
    @Override
    public long getVersion()
    {
        return storage.getVersion();
    }
}