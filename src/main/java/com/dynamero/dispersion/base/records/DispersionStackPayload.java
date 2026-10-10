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

import static com.dynamero.Jilibs.MODID;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.NotNull;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import com.dynamero.dispersion.base.interfaces.DispersionVariant;
import com.dynamero.dispersion.base.storage.SingleDispersionStorage;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.utils.BaseHelper;

/**
 * Custom packet payload and data model representing a dispersion variant quantity and droplet amount,
 * supporting serialization, network transmission, and recipe matching.
 *
 * @param variant the dispersion variant
 * @param amount  the droplet amount
 */
@Developer("TheMentor")
@CreatedAt("2026-10-08")
@ModifiedAt("2026-10-08")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public record DispersionStackPayload(DispersionVariant variant, long amount)
        implements CustomPacketPayload
{
    /**
     * Custom packet payload type identifier for dispersion stack payloads.
     */
    public static final Type<@NotNull DispersionStackPayload> ID = new Type<>(BaseHelper.id(MODID, "dispersion_stack_payload"));

    /**
     * Canonical empty dispersion stack payload.
     */
    public static final DispersionStackPayload EMPTY = new DispersionStackPayload(DispersionVariant.blank(), 0);

    /**
     * Codec for serializing and deserializing DispersionStackPayload instances.
     */
    public static final Codec<DispersionStackPayload> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            DispersionVariant.CODEC.fieldOf("variant").forGetter(DispersionStackPayload::variant),
            Codec.LONG.fieldOf("amount").forGetter(DispersionStackPayload::amount)
    ).apply(inst, DispersionStackPayload::new));

    /**
     * Stream codec for transmitting DispersionStackPayload over registry friendly byte buffers.
     */
    public static final StreamCodec<RegistryFriendlyByteBuf, DispersionStackPayload> STREAM_CODEC = StreamCodec.composite(
            DispersionVariant.STREAM_CODEC, DispersionStackPayload::variant,
            ByteBufCodecs.LONG, DispersionStackPayload::amount,
            DispersionStackPayload::new
    );

    /**
     * Codec for serializing a list of DispersionStackPayload instances.
     */
    public static final Codec<List<DispersionStackPayload>> LIST_CODEC = CODEC.listOf();

    /**
     * Retrieves the packet payload type identifier.
     *
     * @return the payload type
     */
    @Override
    public @NotNull Type<? extends @NotNull CustomPacketPayload> type()
    {
        return ID;
    }

    /**
     * Checks whether this dispersion stack payload represents an empty amount.
     *
     * @return {@code true} if amount is zero, {@code false} otherwise
     */
    public boolean isEmpty()
    {
        return this.amount == 0;
    }

    /**
     * Checks whether this dispersion stack matches another in variant and has at least the required amount.
     *
     * @param stack the stack payload to compare against
     * @return {@code true} if matching, {@code false} otherwise
     */
    public boolean matches(DispersionStackPayload stack)
    {
        if(this == EMPTY && stack == EMPTY)
            return true;

        return stack == null ? isEmpty() : this.variant().equals(stack.variant()) &&
                                        this.amount() >= stack.amount();
    }

    /**
     * Tests whether this dispersion stack satisfies a recipe ingredient requirement.
     *
     * @param stack the recipe required stack payload
     * @return {@code true} if matching variant and sufficient amount, {@code false} otherwise
     */
    public boolean testForRecipe(DispersionStackPayload stack)
    {
        return this.variant().equals(stack.variant()) &&
            this.amount() >= stack.amount();
    }

    /**
     * Tests whether this dispersion stack satisfies a recipe requirement against a single dispersion storage.
     *
     * @param storage the storage to test against
     * @return {@code true} if matching variant and sufficient amount in storage, {@code false} otherwise
     */
    public boolean testForRecipe(SingleDispersionStorage storage)
    {
        if(storage == null)
            return false;

        return this.variant().equals(storage.getResource()) &&
            storage.getAmount() >= this.amount();
    }

    /**
     * Creates a DispersionStackPayload from the current resource and amount of a single dispersion storage.
     *
     * @param storage the storage
     * @return a new DispersionStackPayload
     */
    public static DispersionStackPayload of(SingleDispersionStorage storage)
    {
        return new DispersionStackPayload(storage.getResource(), storage.getAmount());
    }
}