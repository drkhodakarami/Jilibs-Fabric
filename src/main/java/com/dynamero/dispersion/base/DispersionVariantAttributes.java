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

package com.dynamero.dispersion.base;


import static com.dynamero.Jilibs.MODID;

import net.fabricmc.fabric.api.lookup.v1.custom.ApiProviderMap;
import org.jspecify.annotations.Nullable;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;

import com.dynamero.dispersion.base.interfaces.DispersionVariant;
import com.dynamero.dispersion.base.interfaces.DispersionVariantAttributeHandler;
import com.dynamero.dispersion.base.records.Dispersion;
import com.dynamero.shared.annotations.*;

/**
 * Registry and query utilities for {@link DispersionVariantAttributeHandler} instances,
 * providing display names and sound events for dispersion variants and containers.
 */
@SuppressWarnings("unused")
@Developer("TheMentor")
@CreatedAt("2026-08-10")
@ModifiedAt("2026-08-10")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public final class DispersionVariantAttributes
{
    /**
     * Internal map registering custom attribute handlers for specific dispersions.
     */
    private static final ApiProviderMap<Dispersion, DispersionVariantAttributeHandler> HANDLERS = ApiProviderMap.create();

    /**
     * Default attribute handler resolving standard translatable text components based on dispersion registry keys.
     */
    private static final DispersionVariantAttributeHandler DEFAULT_HANDLER = dispersionVariant ->
    {
        Holder<Dispersion> registryEntry = dispersionVariant.typeHolder();
        if(registryEntry.unwrapKey().isPresent())
        {
            Identifier id = registryEntry.unwrapKey().get().identifier();
            return Component.translatable("dispersion." + id.getNamespace() + "." + id.getPath());
        }
        return Component.translatable("dispersion." + MODID, ".unknown");
    };

    /**
     * Registers a custom attribute handler for the specified dispersion.
     *
     * @param dispersion the dispersion definition
     * @param handler    the attribute handler to register
     * @throws IllegalArgumentException if a handler is already registered for this dispersion
     */
    public static void register(Dispersion dispersion, DispersionVariantAttributeHandler handler)
    {
        if(HANDLERS.get(dispersion) != null)
            throw new IllegalArgumentException("Duplicate handler registration for dispersion " + dispersion);

        HANDLERS.putIfAbsent(dispersion, handler);
    }

    /**
     * Retrieves the registered attribute handler for the given dispersion if present.
     *
     * @param dispersion the dispersion definition
     * @return the handler, or null if not registered
     */
    @Nullable
    public static DispersionVariantAttributeHandler getHandler(Dispersion dispersion)
    {
        return HANDLERS.get(dispersion);
    }

    /**
     * Retrieves the registered attribute handler for the given dispersion, or falls back to the default handler.
     *
     * @param dispersion the dispersion definition
     * @return the registered handler or {@link #DEFAULT_HANDLER}
     */
    public static DispersionVariantAttributeHandler getHandlerOrDefault(Dispersion dispersion)
    {
        DispersionVariantAttributeHandler handler = HANDLERS.get(dispersion);
        return handler != null ? handler : DEFAULT_HANDLER;
    }

    /**
     * Resolves the display name for the given dispersion variant.
     *
     * @param variant the dispersion variant
     * @return the display name component
     */
    public static Component getName(DispersionVariant variant)
    {
        return getHandlerOrDefault(variant.getDispersion()).getName(variant);
    }

    /**
     * Resolves the sound event played when filling a container with the given dispersion variant.
     *
     * @param variant the dispersion variant
     * @param item    the container item, or null
     * @return the fill sound event
     */
    public static SoundEvent getFillSound(DispersionVariant variant, @Nullable Item item)
    {
        return getHandlerOrDefault(variant.getDispersion())
                .getFillSound(variant, item)
                .orElse(SoundEvents.BUCKET_FILL);
    }

    /**
     * Resolves the sound event played when emptying a container of the given dispersion variant.
     *
     * @param variant the dispersion variant
     * @param item    the container item, or null
     * @return the empty sound event
     */
    public static SoundEvent getEmptySound(DispersionVariant variant, @Nullable Item item)
    {
        return getHandlerOrDefault(variant.getDispersion())
                .getEmptySound(variant, item)
                .orElse(SoundEvents.BUCKET_EMPTY);
    }
}