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

package com.dynamero.gas.base.records;

import java.util.ArrayList;
import java.util.List;

import com.mojang.serialization.Codec;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import com.dynamero.gas.base.GasComponent;
import com.dynamero.gas.base.interfaces.GasVariant;
import com.dynamero.shared.annotations.*;

/**
 * Data record wrapping a list of {@link GasComponent} instances, providing serialization codecs,
 * factory construction methods, and recipe matching helpers.
 *
 * @param values the list of gas components
 */
@SuppressWarnings("unused")
@Developer("TheMentor")
@CreatedAt("2026-01-12")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
public record GasStackList(List<GasComponent> values)
{
    /**
     * Codec for serializing and deserializing GasStackList instances.
     */
    public static Codec<GasStackList> CODEC = GasComponent.LIST_CODEC.xmap(GasStackList::new, GasStackList::values);

    /**
     * Stream codec for transmitting GasStackList instances over network buffers.
     */
    public static StreamCodec<RegistryFriendlyByteBuf, GasStackList> STREAM_CODEC =
            GasComponent.STREAM_CODEC.apply(ByteBufCodecs.list())
                    .map(GasStackList::new, GasStackList::values);

    /**
     * Canonical empty gas stack list.
     */
    public static final GasStackList EMPTY = new GasStackList(List.of());

    /**
     * Creates a GasStackList from a list of gas components.
     *
     * @param gasComponents the list of gas components
     * @return a new GasStackList or {@link #EMPTY}
     */
    public static GasStackList of(List<GasComponent> gasComponents)
    {
        if(gasComponents == null || gasComponents.isEmpty())
            return EMPTY;

        return new GasStackList(gasComponents);
    }

    /**
     * Creates a GasStackList by pairing corresponding variants and droplet amounts.
     *
     * @param gasVariants the list of gas variants
     * @param gasAmounts  the list of droplet amounts
     * @return a new GasStackList
     * @throws IllegalArgumentException if the two lists differ in size
     */
    public static GasStackList of(List<GasVariant> gasVariants, List<Long> gasAmounts)
    {
        if(gasVariants == null || gasAmounts == null)
            return EMPTY;

        if(gasVariants.isEmpty() && gasAmounts.isEmpty())
            return EMPTY;

        if(gasAmounts.size() != gasVariants.size())
            throw new IllegalArgumentException("Gas Variant and Gas Amount lists do not match in size");

        List<GasComponent> list = new ArrayList<>();

        for (int i = 0; i < gasVariants.size(); i++)
            list.add(new GasComponent(gasVariants.get(i), gasAmounts.get(i)));

        return new GasStackList(list);
    }

    /**
     * Checks whether this gas stack list is empty or contains only empty gas components.
     *
     * @return {@code true} if empty, {@code false} otherwise
     */
    public boolean isEmpty()
    {
        if(values.isEmpty())
            return true;

        for (GasComponent value : values)
            if(!value.isEmpty())
                return false;

        return true;
    }

    /**
     * Checks whether this gas stack list matches another in count and component matching.
     *
     * @param gases the other gas stack list
     * @return {@code true} if all components match, {@code false} otherwise
     */
    public boolean matches(GasStackList gases)
    {
        if(values.isEmpty() && gases.isEmpty())
            return true;

        if(values.size() != gases.values.size())
            return false;

        for (int i = 0; i < values.size(); i++)
            if(!values.get(i).matches(gases.values.get(i)))
                return false;

        return true;
    }

    /**
     * Tests whether this gas stack list satisfies a recipe ingredient requirement list.
     *
     * @param gases the recipe required gas stack list
     * @return {@code true} if each component satisfies the requirement, {@code false} otherwise
     */
    public boolean testForRecipe(GasStackList gases)
    {
        if(values.isEmpty() || gases.isEmpty())
            return false;

        if(values.size() != gases.values.size())
            return false;

        for (int i = 0; i < values.size(); i++)
            if(!values.get(i).testForRecipe(gases.values.get(i)))
                return false;

        return true;
    }
}