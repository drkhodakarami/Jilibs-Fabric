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
import com.dynamero.shared.records.BlockPosPayload;

@SuppressWarnings("unused")
@Developer("TheMentor")
@CreatedAt("2026-10-08")
@ModifiedAt("2026-10-08")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public record BlockPosList(List<BlockPosPayload> values)
{
    public static Codec<BlockPosList> CODEC = BlockPosPayload.LIST_CODEC.xmap(BlockPosList::new, BlockPosList::values);
    public static StreamCodec<RegistryFriendlyByteBuf, BlockPosList> STREAM_CODEC =
            BlockPosPayload.STREAM_CODEC.apply(ByteBufCodecs.list())
                                        .map(BlockPosList::new, BlockPosList::values);
    public static final BlockPosList EMPTY = new BlockPosList(List.of());
}