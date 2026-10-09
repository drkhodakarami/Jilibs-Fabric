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

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

import com.dynamero.shared.annotations.*;

/**
 * Represents a custom payload containing an ItemStack.
 * @param stack The item stack for the payload
 */
@SuppressWarnings("unused")
@Developer("TurtyWurty")
@CreatedAt("2026-08-10")
@ModifiedAt("2026-08-10")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public record ItemStackPayload(ItemStack stack) implements CustomPacketPayload
{
    /**
     * The unique identifier for this custom payload.
     */
    public static final Type<@NotNull ItemStackPayload> ID = new Type<>(Identifier.fromNamespaceAndPath(MODID, "item_stack_payload"));

    public static final ItemStackPayload EMPTY = new ItemStackPayload(ItemStack.EMPTY);

    /**
     * The codec used to serialize and deserialize the ItemStackPayload.
     */
    public static final Codec<ItemStackPayload> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            ItemStack.CODEC.fieldOf("stack").forGetter(ItemStackPayload::stack)
    ).apply(inst, ItemStackPayload::new));

    /**
     * The packet codec used to send and receive the ItemStackPayload.
     */
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemStackPayload> STREAM_CODEC =
            StreamCodec.composite(ItemStack.STREAM_CODEC, ItemStackPayload::stack, ItemStackPayload::new);

    public static final Codec<List<ItemStackPayload>> LIST_CODEC = CODEC.listOf();

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

    public boolean matches(ItemStackPayload stack)
    {
        if(this == EMPTY && stack == EMPTY)
            return true;
        return stack == null ? isEmpty() : this.stack().equals(stack.stack()) && this.stack().getCount() >= stack.stack().getCount();
    }

    public boolean isEmpty()
    {
        return this.stack.isEmpty() || this.stack.getCount() == 0;
    }

    public boolean testForRecipe(ItemStackPayload stackPayload)
    {
        return this.stack().equals(stackPayload.stack()) && this.stack().getCount() >= stackPayload.stack().getCount();
    }
}