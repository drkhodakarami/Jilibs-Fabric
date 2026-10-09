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

package com.dynamero.pressure.base.records;

import java.util.ArrayList;
import java.util.List;

import com.mojang.serialization.Codec;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import com.dynamero.pressure.base.PressureComponent;
import com.dynamero.pressure.base.interfaces.PressureUnit;
import com.dynamero.shared.annotations.*;

/**
 * Data record wrapping a list of {@link PressureComponent} instances, providing serialization codecs,
 * factory construction methods, and recipe matching helpers.
 *
 * @param values the list of pressure components
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
public record PressureComponentList(List<PressureComponent> values)
{
    /**
     * Codec for serializing and deserializing PressureComponentList instances.
     */
    public static Codec<PressureComponentList> CODEC = PressureComponent.LIST_CODEC.xmap(PressureComponentList::new, PressureComponentList::values);

    /**
     * Stream codec for transmitting PressureComponentList instances over network byte buffers.
     */
    public static StreamCodec<FriendlyByteBuf, PressureComponentList> STREAM_CODEC =
            PressureComponent.STREAM_CODEC.apply(ByteBufCodecs.list())
                                          .map(PressureComponentList::new, PressureComponentList::values);

    /**
     * Canonical empty pressure component list.
     */
    public static final PressureComponentList EMPTY = new PressureComponentList(List.of());

    /**
     * Creates a PressureComponentList from a list of pressure components.
     *
     * @param pressureComponent the list of pressure components
     * @return a new PressureComponentList or {@link #EMPTY}
     */
    public static PressureComponentList of(List<PressureComponent> pressureComponent)
    {
        if(pressureComponent == null || pressureComponent.isEmpty())
            return EMPTY;

        List<PressureComponent> list = new ArrayList<>();

        for (PressureComponent component : pressureComponent)
            list.add(new PressureComponent(component.getPressure(), component.getUnit()));

        return new PressureComponentList(list);
    }

    /**
     * Creates a PressureComponentList by pairing corresponding pressure units and pressure values.
     *
     * @param pressureUnits   the list of pressure units
     * @param pressureAmounts the list of pressure amounts
     * @return a new PressureComponentList
     * @throws IllegalArgumentException if the two lists differ in size
     */
    public static PressureComponentList of(List<PressureUnit> pressureUnits, List<Double> pressureAmounts)
    {
        if(pressureUnits == null || pressureAmounts == null)
            return EMPTY;

        if(pressureUnits.isEmpty() && pressureAmounts.isEmpty())
            return EMPTY;

        if(pressureAmounts.size() != pressureUnits.size())
            throw new IllegalArgumentException("Heat Unit and Heat Amount lists do not match in size");

        List<PressureComponent> list = new ArrayList<>();

        for (int i = 0; i < pressureUnits.size(); i++)
            list.add(new PressureComponent(pressureAmounts.get(i), pressureUnits.get(i)));

        return new PressureComponentList(list);
    }

    /**
     * Checks whether this pressure component list is empty.
     *
     * @return {@code true} if empty, {@code false} otherwise
     */
    public boolean isEmpty()
    {
        return values.isEmpty();
    }

    /**
     * Checks whether this pressure component list matches another in count and component values.
     *
     * @param pressureComponents the other pressure component list
     * @return {@code true} if all components match, {@code false} otherwise
     */
    public boolean matches(PressureComponentList pressureComponents)
    {
        if(values.isEmpty() && pressureComponents.isEmpty())
            return true;

        if(values.size() != pressureComponents.values.size())
            return false;

        for (int i = 0; i < values.size(); i++)
            if(values.get(i).getPressure() != pressureComponents.values.get(i).getPressure() ||
            !values.get(i).getUnit().equals(pressureComponents.values.get(i).getUnit()))
                return false;

        return true;
    }

    /**
     * Tests whether this pressure component list satisfies a recipe ingredient requirement list.
     *
     * @param pressureComponents the recipe required pressure component list
     * @return {@code true} if each component satisfies the requirement, {@code false} otherwise
     */
    public boolean testForRecipe(PressureComponentList pressureComponents)
    {
        if(values.isEmpty() || pressureComponents.isEmpty())
            return false;

        if(values.size() != pressureComponents.values.size())
            return false;

        for (int i = 0; i < values.size(); i++)
            if(values.get(i).testForRecipe(pressureComponents.values.get(i)))
                return false;

        return true;
    }
}