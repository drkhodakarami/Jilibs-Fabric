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

package com.dynamero.shared.records.lists;

import java.util.ArrayList;
import java.util.List;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import com.dynamero.shared.annotations.*;
import com.dynamero.shared.network.FluidComponent;

@Developer("TheMentor")
@CreatedAt("2026-01-12")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
public record FluidStackList(List<FluidComponent> values)
{
    public static Codec<FluidStackList> CODEC = FluidComponent.LIST_CODEC.xmap(FluidStackList::new, FluidStackList::values);

    public static StreamCodec<RegistryFriendlyByteBuf, FluidStackList> STREAM_CODEC =
            FluidComponent.STREAM_CODEC.apply(ByteBufCodecs.list())
                                    .map(FluidStackList::new, FluidStackList::values);

    public static final FluidStackList EMPTY = new FluidStackList(List.of());

    public static FluidStackList of(List<FluidComponent> fluidComponents)
    {
        if(fluidComponents == null || fluidComponents.isEmpty())
            return EMPTY;

        List<FluidComponent> list = new ArrayList<>();

        for (FluidComponent component : fluidComponents)
            list.add(new FluidComponent(component.getFluid(), component.getAmount()));

        return new FluidStackList(list);
    }

    public static FluidStackList of(List<FluidVariant> fluidVariants, List<Long> fluidAmounts)
    {
        if(fluidVariants == null || fluidAmounts == null)
            return EMPTY;

        if(fluidVariants.isEmpty() && fluidAmounts.isEmpty())
            return EMPTY;

        if(fluidAmounts.size() != fluidVariants.size())
            throw new IllegalArgumentException("Fluid Variant and Fluid Amount lists do not match in size");

        List<FluidComponent> list = new ArrayList<>();

        for (int i = 0; i < fluidVariants.size(); i++)
            list.add(new FluidComponent(fluidVariants.get(i), fluidAmounts.get(i)));

        return new FluidStackList(list);
    }

    public boolean isEmpty()
    {
        if(values.isEmpty())
            return true;

        for (FluidComponent value : values)
            if(!value.isEmpty())
                return false;

        return true;
    }

    public boolean matches(FluidStackList fluids)
    {
        if(values.isEmpty() && fluids.isEmpty())
            return true;

        if(values.size() != fluids.values.size())
            return false;

        for (int i = 0; i < values.size(); i++)
            if(!values.get(i).matches(fluids.values.get(i)))
                return false;

        return true;
    }

    public boolean testForRecipe(FluidStackList fluids)
    {
        if(values.isEmpty() || fluids.isEmpty())
            return false;

        if(values.size() != fluids.values.size())
            return false;

        for (int i = 0; i < values.size(); i++)
            if(!values.get(i).testForRecipe(fluids.values.get(i)))
                return false;

        return true;
    }
}