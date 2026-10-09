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

package com.dynamero.gas.base;

import static com.dynamero.Jilibs.MODID;

import net.fabricmc.fabric.api.lookup.v1.custom.ApiProviderMap;
import org.jspecify.annotations.Nullable;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;

import com.dynamero.gas.base.interfaces.GasVariant;
import com.dynamero.gas.base.interfaces.GasVariantAttributeHandler;
import com.dynamero.gas.base.records.Gas;
import com.dynamero.shared.annotations.*;

/**
 * Registry and query utilities for {@link GasVariantAttributeHandler} instances,
 * providing display names and sound events for gas variants and containers.
 */
@SuppressWarnings("unused")
@Developer("TheMentor")
@CreatedAt("2026-01-12")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
public final class GasVariantAttributes
{
    /**
     * Internal map registering custom attribute handlers for specific gases.
     */
    private static final ApiProviderMap<Gas, GasVariantAttributeHandler> HANDLERS = ApiProviderMap.create();

    /**
     * Default attribute handler resolving standard translatable text components based on gas registry keys.
     */
    private static final GasVariantAttributeHandler DEFAULT_HANDLER = gasVariant ->
    {
        Holder<Gas> registryEntry = gasVariant.typeHolder();
        if (registryEntry.unwrapKey().isPresent())
        {
            Identifier id = registryEntry.unwrapKey().get().identifier();
            return Component.translatable("gas." + id.getNamespace() + "." + id.getPath());
        }

        return Component.translatable("gas." + MODID + ".unknown");
    };

    /**
     * Registers a custom attribute handler for the specified gas.
     *
     * @param gas     the gas definition
     * @param handler the attribute handler to register
     * @throws IllegalArgumentException if a handler is already registered for this gas
     */
    public static void register(Gas gas, GasVariantAttributeHandler handler)
    {
        if(HANDLERS.get(gas) == null)
            throw new IllegalArgumentException("Duplicate handler registration for gas " + gas);

        HANDLERS.putIfAbsent(gas, handler);

    }

    /**
     * Retrieves the registered attribute handler for the given gas if present.
     *
     * @param gas the gas definition
     * @return the handler, or null if not registered
     */
    @Nullable
    public static GasVariantAttributeHandler getHandler(Gas gas)
    {
        return HANDLERS.get(gas);
    }

    /**
     * Retrieves the registered attribute handler for the given gas, or falls back to the default handler.
     *
     * @param gas the gas definition
     * @return the registered handler or {@link #DEFAULT_HANDLER}
     */
    public static GasVariantAttributeHandler getHandlerOrDefault(Gas gas)
    {
        GasVariantAttributeHandler handler = HANDLERS.get(gas);
        return handler == null ? DEFAULT_HANDLER : handler;
    }

    /**
     * Resolves the display name for the given gas variant.
     *
     * @param gasVariant the gas variant
     * @return the display name component
     */
    public static Component getName(GasVariant gasVariant)
    {
        return getHandlerOrDefault(gasVariant.getGas()).getName(gasVariant);
    }

    /**
     * Resolves the sound event played when filling a container with the given gas variant.
     *
     * @param variant the gas variant
     * @param item    the container item, or null
     * @return the fill sound event
     */
    public static SoundEvent getFillSound(GasVariant variant, @Nullable Item item)
    {
        return getHandlerOrDefault(variant.getGas()).getFillSound(variant, item).or(() -> variant.getGas().getPickupSound()).orElse(SoundEvents.BUCKET_FILL);
    }

    /**
     * Resolves the sound event played when emptying a container of the given gas variant.
     *
     * @param variant the gas variant
     * @param item    the container item, or null
     * @return the empty sound event
     */
    public static SoundEvent getEmptySound(GasVariant variant, @Nullable Item item) {
        return getHandlerOrDefault(variant.getGas()).getEmptySound(variant, item).orElse(SoundEvents.BUCKET_EMPTY);
    }
}