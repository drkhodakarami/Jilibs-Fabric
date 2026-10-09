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

import java.util.List;

import com.mojang.serialization.Codec;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import com.dynamero.shared.annotations.*;
import com.dynamero.shared.records.FloatPayload;

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
public record FloatList(List<FloatPayload> values)
{
    public static Codec<FloatList> CODEC = FloatPayload.LIST_CODEC.xmap(FloatList::new, FloatList::values);
    public static StreamCodec<RegistryFriendlyByteBuf, FloatList> STREAM_CODEC =
            FloatPayload.STREAM_CODEC.apply(ByteBufCodecs.list())
                                     .map(FloatList::new, FloatList::values);
    public static final FloatList EMPTY = new FloatList(List.of());
}