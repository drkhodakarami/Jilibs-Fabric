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

package com.dynamero.heat.base.records;

import java.util.ArrayList;
import java.util.List;

import com.mojang.serialization.Codec;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import com.dynamero.heat.base.HeatComponent;
import com.dynamero.heat.base.interfaces.HeatUnit;
import com.dynamero.shared.annotations.*;

/**
 * Data record wrapping a list of {@link HeatComponent} instances, providing serialization codecs,
 * factory construction methods, and recipe matching helpers.
 *
 * @param values the list of heat components
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
public record HeatComponentList(List<HeatComponent> values)
{
    /**
     * Codec for serializing and deserializing HeatComponentList instances.
     */
    public static Codec<HeatComponentList> CODEC = HeatComponent.LIST_CODEC.xmap(HeatComponentList::new, HeatComponentList::values);

    /**
     * Stream codec for transmitting HeatComponentList instances over network byte buffers.
     */
    public static StreamCodec<FriendlyByteBuf, HeatComponentList> STREAM_CODEC =
            HeatComponent.STREAM_CODEC.apply(ByteBufCodecs.list())
                    .map(HeatComponentList::new, HeatComponentList::values);

    /**
     * Canonical empty heat component list.
     */
    public static final HeatComponentList EMPTY = new HeatComponentList(List.of());

    /**
     * Creates a HeatComponentList from a list of heat components.
     *
     * @param heatComponent the list of heat components
     * @return a new HeatComponentList or {@link #EMPTY}
     */
    public static HeatComponentList of(List<HeatComponent> heatComponent)
    {
        if(heatComponent == null || heatComponent.isEmpty())
            return EMPTY;

        List<HeatComponent> list = new ArrayList<>();

        for (HeatComponent component : heatComponent)
            list.add(new HeatComponent(component.getHeat(), component.getUnit()));

        return new HeatComponentList(list);
    }

    /**
     * Creates a HeatComponentList by pairing corresponding heat units and heat values.
     *
     * @param heatUnits   the list of heat units
     * @param heatAmounts the list of heat amounts
     * @return a new HeatComponentList
     * @throws IllegalArgumentException if the two lists differ in size
     */
    public static HeatComponentList of(List<HeatUnit> heatUnits, List<Double> heatAmounts)
    {
        if(heatUnits == null || heatAmounts == null)
            return EMPTY;

        if(heatUnits.isEmpty() && heatAmounts.isEmpty())
            return EMPTY;

        if(heatAmounts.size() != heatUnits.size())
            throw new IllegalArgumentException("Heat Unit and Heat Amount lists do not match in size");

        List<HeatComponent> list = new ArrayList<>();

        for (int i = 0; i < heatUnits.size(); i++)
            list.add(new HeatComponent(heatAmounts.get(i), heatUnits.get(i)));

        return new HeatComponentList(list);
    }

    /**
     * Checks whether this heat component list is empty.
     *
     * @return {@code true} if empty, {@code false} otherwise
     */
    public boolean isEmpty()
    {
        return values.isEmpty();
    }

    /**
     * Checks whether this heat component list matches another in count and component values.
     *
     * @param heatComponents the other heat component list
     * @return {@code true} if all components match, {@code false} otherwise
     */
    public boolean matches(HeatComponentList heatComponents)
    {
        if(values.isEmpty() && heatComponents.isEmpty())
            return true;

        if(values.size() != heatComponents.values.size())
            return false;

        for (int i = 0; i < values.size(); i++)
            if(values.get(i).getHeat() != heatComponents.values.get(i).getHeat() ||
            !values.get(i).getUnit().equals(heatComponents.values.get(i).getUnit()))
                return false;

        return true;
    }

    /**
     * Tests whether this heat component list satisfies a recipe ingredient requirement list.
     *
     * @param heatComponents the recipe required heat component list
     * @return {@code true} if each component satisfies the requirement, {@code false} otherwise
     */
    public boolean testForRecipe(HeatComponentList heatComponents)
    {
        if(values.isEmpty() || heatComponents.isEmpty())
            return false;

        if(values.size() != heatComponents.values.size())
            return false;

        for (int i = 0; i < values.size(); i++)
            if(values.get(i).testForRecipe(heatComponents.values.get(i)))
                return false;

        return true;
    }
}