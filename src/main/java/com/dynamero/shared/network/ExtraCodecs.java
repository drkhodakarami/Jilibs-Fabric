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

package com.dynamero.shared.network;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import org.joml.Vector3d;

import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;

import com.dynamero.shared.annotations.*;

@SuppressWarnings("unused")
@Developer("TurtyWurty")
@CreatedAt("2025-08-06")
@Repository("https://github.com/DaRealTurtyWurty/Industria")
@Discord("https://discord.turtywurty.dev/")
@Youtube("https://www.youtube.com/@TurtyWurty")
public class ExtraCodecs
{
    public static final Codec<Set<BlockPos>> BLOCK_POS_SET_CODEC = setOf(BlockPos.CODEC);
    public static final Codec<AABB> BOX_CODEC = Codec.DOUBLE
            .listOf()
            .xmap(
                    list -> new AABB(
                            list.getFirst(),
                            list.get(1),
                            list.get(2),
                            list.get(3),
                            list.get(4),
                            list.get(5)
                    ),
                    box -> List.of(
                            box.minX,
                            box.minY,
                            box.minZ,
                            box.maxX,
                            box.maxY,
                            box.maxZ
                    )
            );

    public static final Codec<BlockPos> BLOCK_POS_STRING_CODEC = Codec.STRING.comapFlatMap(
            str -> {
                String[] parts = str.split(" ");
                if (parts.length != 3) {
                    return DataResult.error(() -> "Invalid BlockPos format: " + str);
                }

                try {
                    int x = Integer.parseInt(parts[0]);
                    int y = Integer.parseInt(parts[1]);
                    int z = Integer.parseInt(parts[2]);
                    return DataResult.success(new BlockPos(x, y, z));
                } catch (NumberFormatException e) {
                    return DataResult.error(() -> "Invalid BlockPos format: " + str);
                }
            },
            blockPos -> blockPos.getX() + " " + blockPos.getY() + " " + blockPos.getZ());
    public static final Codec<Map<BlockPos, UUID>> BLOCK_POS_TO_UUID_CODEC = Codec.unboundedMap(
            BLOCK_POS_STRING_CODEC, UUIDUtil.CODEC);
    public static final Codec<Character> CHAR_CODEC = Codec.STRING.xmap(s -> s.charAt(0), String::valueOf);
    public static final Codec<Block> BLOCK_CODEC = Codec.STRING.xmap(
            id -> BuiltInRegistries.BLOCK.getValue(Identifier.parse(id)),
            block -> BuiltInRegistries.BLOCK.getKey(block).toString()
    );
    public static final Codec<Vector3d> VECTOR_3D_CODEC = Codec.DOUBLE.listOf().xmap(
            list -> new Vector3d(list.getFirst(), list.get(1), list.get(2)),
            vec -> List.of(vec.x, vec.y, vec.z)
    );

    public static <T> Codec<Set<T>> setOf(Codec<T> codec) {
        return Codec.list(codec).xmap(Sets::newHashSet, Lists::newArrayList);
    }

    public static <T> Codec<Set<T>> setOf(MapCodec<T> codec) {
        return setOf(codec.codec());
    }

    public static <T> Codec<List<T>> listOf(MapCodec<T> codec) {
        return Codec.list(codec.codec());
    }

    public static <T> Codec<List<T>> listOf(Codec<T> codec) {
        return Codec.list(codec);
    }
}