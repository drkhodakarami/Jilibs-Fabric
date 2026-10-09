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

package com.dynamero.dispersion.base.records;

import java.util.List;

import com.mojang.serialization.Codec;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import com.dynamero.shared.annotations.*;

/**
 * Data record wrapping a list of {@link DispersionStackPayload} instances, providing serialization codecs,
 * and recipe matching helpers.
 *
 * @param values the list of dispersion stack payloads
 */
@Developer("TheMentor")
@CreatedAt("2026-01-12")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
public record DispersionStackList(List<DispersionStackPayload> values)
{
    /**
     * Codec for serializing and deserializing DispersionStackList instances.
     */
    public static Codec<DispersionStackList> CODEC = DispersionStackPayload.LIST_CODEC.xmap(DispersionStackList::new, DispersionStackList::values);

    /**
     * Stream codec for transmitting DispersionStackList instances over network buffers.
     */
    public static StreamCodec<RegistryFriendlyByteBuf, DispersionStackList> STREAM_CODEC =
            DispersionStackPayload.STREAM_CODEC.apply(ByteBufCodecs.list())
                    .map(DispersionStackList::new, DispersionStackList::values);

    /**
     * Canonical empty dispersion stack list.
     */
    public static final DispersionStackList EMPTY = new DispersionStackList(List.of());

    /**
     * Checks whether this dispersion stack list is empty or contains only empty payloads.
     *
     * @return {@code true} if empty, {@code false} otherwise
     */
    public boolean isEmpty()
    {
        if(values.isEmpty())
            return true;

        for(DispersionStackPayload value : values)
            if(!value.isEmpty())
                return false;

        return true;
    }

    /**
     * Checks whether this dispersion stack list matches another in count and payload matching.
     *
     * @param dispersions the other dispersion stack list
     * @return {@code true} if all payloads match, {@code false} otherwise
     */
    public boolean matches(DispersionStackList dispersions)
    {
        if(values.isEmpty() && dispersions.isEmpty())
            return true;

        if(values.size() != dispersions.values.size())
            return false;

        for (int i = 0; i < values.size(); i++)
            if(!values.get(i).matches(dispersions.values.get(i)))
                return false;

        return true;
    }

    /**
     * Tests whether this dispersion stack list satisfies a recipe ingredient requirement list.
     *
     * @param dispersions the recipe required dispersion stack list
     * @return {@code true} if each payload satisfies the requirement, {@code false} otherwise
     */
    public boolean testForRecipe(DispersionStackList dispersions)
    {
        if(values.isEmpty() || dispersions.isEmpty())
            return false;

        if(values.size() != dispersions.values.size())
            return false;

        for (int i = 0; i < values.size(); i++)
            if(!values.get(i).testForRecipe(dispersions.values.get(i)))
                return false;

        return true;
    }
}