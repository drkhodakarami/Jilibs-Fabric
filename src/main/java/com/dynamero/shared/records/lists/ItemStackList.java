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

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import com.dynamero.shared.annotations.*;
import com.dynamero.shared.records.ItemStackPayload;

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
public record ItemStackList(List<ItemStackPayload> values)
{
    public static Codec<ItemStackList> CODEC = ItemStackPayload.LIST_CODEC.xmap(ItemStackList::new, ItemStackList::values);

    public static StreamCodec<RegistryFriendlyByteBuf, ItemStackList> STREAM_CODEC =
            ItemStackPayload.STREAM_CODEC.apply(ByteBufCodecs.list())
                                        .map(ItemStackList::new, ItemStackList::values);

    public static final ItemStackList EMPTY = new ItemStackList(List.of());

    public static ItemStackList of(List<ItemStack> items)
    {
        if(items == null || items.isEmpty())
            return EMPTY;

        List<ItemStackPayload> list = new ArrayList<>();

        for (ItemStack item : items)
            list.add(new ItemStackPayload(item));

        return new ItemStackList(list);
    }

    public boolean isEmpty()
    {
        if(values.isEmpty())
            return true;

        for (ItemStackPayload value : values)
            if(!value.isEmpty())
                return false;

        return true;
    }

    public boolean matches(ItemStackList items)
    {
        if(values.isEmpty() && items.isEmpty())
            return true;

        if(values.size() != items.values.size())
            return false;

        for (int i = 0; i < values.size(); i++)
            if(!values.get(i).matches(items.values.get(i)))
                return false;

        return true;
    }

    public boolean testForRecipe(ItemStackList items)
    {
        if(values.isEmpty() || items.isEmpty())
            return false;

        if(values.size() != items.values.size())
            return false;

        for (int i = 0; i < values.size(); i++)
            if(!values.get(i).testForRecipe(items.values.get(i)))
                return false;

        return true;
    }
}