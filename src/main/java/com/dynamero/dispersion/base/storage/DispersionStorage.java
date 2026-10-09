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

import static com.dynamero.Jilibs.MODID;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import com.dynamero.dispersion.base.interfaces.DispersionVariant;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.utils.BaseHelper;

/**
 * Core registry lookup endpoints and API providers for block-sided and item dispersion storages.
 */
@SuppressWarnings("unused")
@Developer("TheMentor")
@CreatedAt("2026-01-12")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
public final class DispersionStorage
{
    /**
     * Block API lookup instance for querying sided dispersion storages at block positions.
     */
    public static final BlockApiLookup<Storage<DispersionVariant>, @Nullable Direction> SIDED =
            BlockApiLookup.get(BaseHelper.id(MODID, "sided_dispersion_storage"), Storage.asClass(), Direction.class);

    /**
     * Item API lookup instance for querying dispersion storages from item containers.
     */
    public static final ItemApiLookup<Storage<DispersionVariant>, ContainerItemContext> ITEM =
            ItemApiLookup.get(BaseHelper.id(MODID, "item_dispersion_storage"), Storage.asClass(), ContainerItemContext.class);

    /**
     * Supplier returning the singleton {@link EmptyDispersionStorage}.
     */
    public static final Supplier<Storage<DispersionVariant>> EMPTY = () -> Objects.requireNonNull(EmptyDispersionStorage.INSTANCE);

    /**
     * Retrieves or registers a combined item API provider event for the specified item.
     *
     * @param item the target item
     * @return the event managing combined providers
     */
    public static Event<CombinedItemApiProvider> combinedItemApiProvider(Item item)
    {
        return CombinedProvidersImpl.getOrCreateItemEvent(item);
    }

    /**
     * General combined item API provider event invoked as a fallback.
     */
    public static final Event<CombinedItemApiProvider> GENERAL_COMBINED_PROVIDER = CombinedProvidersImpl.createEvent(false);

    /**
     * Private constructor preventing instantiation of lookup utility class.
     */
    private DispersionStorage() {}

    static
    {
        DispersionStorage.ITEM.registerFallback((stack, context) -> GENERAL_COMBINED_PROVIDER.invoker().find(context));
    }

    /**
     * Functional interface for providing a dispersion storage from a container item context.
     */
    @FunctionalInterface
    public interface CombinedItemApiProvider
    {
        /**
         * Finds the dispersion storage for the given container item context.
         *
         * @param context the container item context
         * @return the dispersion storage, or null if none
         */
        Storage<DispersionVariant> find(ContainerItemContext context);
    }

    /**
     * Implementation helpers for combined item API provider events.
     */
    public static class CombinedProvidersImpl
    {
        /**
         * Creates an array-backed event combining dispersion storages across registered listeners.
         *
         * @param invokeFallBack whether to invoke the general fallback provider
         * @return the combined event
         */
        public static Event<CombinedItemApiProvider> createEvent(boolean invokeFallBack)
        {
            return EventFactory.createArrayBacked(CombinedItemApiProvider.class, listeners -> context ->
            {
                List<Storage<DispersionVariant>> storages = new ArrayList<>();

                for(CombinedItemApiProvider listener : listeners)
                {
                    Storage<DispersionVariant> found = listener.find(context);
                    if(found != null)
                        storages.add(found);
                }

                if(!storages.isEmpty() && invokeFallBack)
                {
                    Storage<DispersionVariant> fallbackFound = DispersionStorage.GENERAL_COMBINED_PROVIDER.invoker().find(context);

                    if(fallbackFound != null)
                        storages.add(fallbackFound);
                }

                return storages.isEmpty() ? null : new CombinedStorage<>(storages);
            });
        }

        /**
         * Internal provider implementation backed by an event.
         */
        private static class Provider implements ItemApiLookup.ItemApiProvider<Storage<DispersionVariant>, ContainerItemContext>
        {
            /**
             * Event dispatching provider lookups.
             */
            private final Event<CombinedItemApiProvider> event = createEvent(true);

            /**
             * Finds the dispersion storage for the item stack within the container item context.
             *
             * @param itemStack the item stack
             * @param context   the container item context
             * @return the dispersion storage or null
             * @throws IllegalArgumentException if the query stack and context variant do not match
             */
            @Override
            @Nullable
            public Storage<DispersionVariant> find(@NotNull ItemStack itemStack, @NotNull ContainerItemContext context)
            {
                if(!context.getItemVariant().matches(itemStack))
                {
                    String errorMessage = String.format(
                            "Query stack %s and ContainerItemContext variant %s don't match.",
                            itemStack, context.getItemVariant()
                    );
                    throw new IllegalArgumentException(errorMessage);
                }

                return event.invoker().find(context);
            }
        }

        /**
         * Obtains or creates the item event for combining providers for a given item.
         *
         * @param item the item to query
         * @return the event
         * @throws IllegalArgumentException if an incompatible provider is already registered
         */
        private static Event<CombinedItemApiProvider> getOrCreateItemEvent(Item item)
        {
            ItemApiLookup.ItemApiProvider<Storage<DispersionVariant>, ContainerItemContext> existingProvider = DispersionStorage.ITEM.getProvider(item);

            if(existingProvider == null)
            {
                DispersionStorage.ITEM.registerForItems(new Provider(), item);
                existingProvider = DispersionStorage.ITEM.getProvider(item);
            }

            if(existingProvider instanceof Provider registeredProvider)
                return registeredProvider.event;

            String errorMessage = String.format(
                    "An incompatible provider was already registered for item %s. provider: %s.",
                    item, existingProvider
                );
                throw new IllegalArgumentException(errorMessage);
        }
    }
}