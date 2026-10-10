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

package com.dynamero.shared.records;

import static com.dynamero.Jilibs.MODID;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import com.dynamero.shared.annotations.*;

/**
 * Represents a custom payload containing a Minecraft BlockPos and dimension information.
 *
 * @param pos       the BlockPos contained in this payload
 * @param dimension the dimension identifier of the world containing the BlockPos
 */
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
public record CoordinateDataPayload(BlockPos pos, String dimension) implements CustomPacketPayload
{
    /**
     * The unique identifier for this custom payload.
     */
    public static final Type<@NotNull CoordinateDataPayload> ID = new Type<>(Identifier.fromNamespaceAndPath(MODID, "coordinate_data_payload"));

    /**
     * The codec used to serialize and deserialize the CoordinateDataPayload.
     */
    public static final Codec<CoordinateDataPayload> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            BlockPos.CODEC.fieldOf("pos").forGetter(CoordinateDataPayload::pos),
            Codec.STRING.fieldOf("dimension").forGetter(CoordinateDataPayload::dimension)
    ).apply(inst, CoordinateDataPayload::new));

    /**
     * The packet codec used to send and receive the CoordinateDataPayload.
     */
    public static final StreamCodec<RegistryFriendlyByteBuf, CoordinateDataPayload> STREAM_CODEC =
            StreamCodec.composite(BlockPos.STREAM_CODEC, CoordinateDataPayload::pos,
                            ByteBufCodecs.STRING_UTF8, CoordinateDataPayload::dimension,
                            CoordinateDataPayload::new);

    public static final Codec<List<CoordinateDataPayload>> LIST_CODEC = CODEC.listOf();

    /**
     * Retrieves the unique identifier for this custom payload.
     *
     * @return the unique identifier
     */
    @Override
    public @NotNull Type<? extends @NotNull CustomPacketPayload> type()
    {
        return ID;
    }
}