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

package com.dynamero.gas.base;

import static com.dynamero.Jilibs.MODID;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.NotNull;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import com.dynamero.gas.base.interfaces.GasVariant;
import com.dynamero.gas.base.storage.SingleGasStorage;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.utils.BaseHelper;

/**
 * Custom packet payload and data model representing a {@link GasVariant} combined with a droplet volume amount,
 * supporting serialization, network transmission, and recipe ingredient matching.
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
public class GasComponent implements CustomPacketPayload
{
    /**
     * Droplet volume amount of the gas.
     */
    private long amount;

    /**
     * The gas variant.
     */
    private GasVariant gas;

    /**
     * Codec for serializing and deserializing GasComponent instances.
     */
    public static final Codec<GasComponent> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            GasVariant.CODEC.fieldOf("variant").forGetter(GasComponent::getGas),
            Codec.LONG.fieldOf("minimumAmount").forGetter(GasComponent::getAmount)
    ).apply(inst, GasComponent::new));

    /**
     * Stream codec for transmitting GasComponent over the network.
     */
    public static final StreamCodec<RegistryFriendlyByteBuf, GasComponent> STREAM_CODEC = StreamCodec.composite(
            GasVariant.STREAM_CODEC, GasComponent::getGas,
            ByteBufCodecs.LONG, GasComponent::getAmount,
            GasComponent::new
    );

    /**
     * Codec for serializing a list of GasComponent instances.
     */
    public static final Codec<List<GasComponent>> LIST_CODEC = CODEC.listOf();

    /**
     * Constant representing an empty gas component with 0 droplets.
     */
    public static final GasComponent EMPTY = new GasComponent(GasVariant.blank(), 0);

    /**
     * Custom packet payload type identifier for gas component network syncing.
     */
    public static final Type<@NotNull GasComponent> ID = new Type<>(BaseHelper.id(MODID, "gas_component_payload"));

    /**
     * Constructs a GasComponent with the specified variant and droplet amount.
     *
     * @param variant the gas variant
     * @param amount  the droplet amount
     */
    public GasComponent(GasVariant variant, long amount)
    {
        this.amount = amount;
        this.gas = variant;
    }

    /**
     * Retrieves the droplet volume amount.
     *
     * @return the droplet amount
     */
    public long getAmount()
    {
        return amount;
    }

    /**
     * Sets the droplet volume amount.
     *
     * @param amount the droplet amount to set
     */
    public void setAmount(long amount)
    {
        this.amount = amount;
    }

    /**
     * Retrieves the gas variant.
     *
     * @return the gas variant
     */
    public GasVariant getGas()
    {
        return gas;
    }

    /**
     * Sets the gas variant.
     *
     * @param variant the gas variant to set
     */
    public void setGas(GasVariant variant)
    {
        this.gas = variant;
    }

    /**
     * Compares this gas component to another object for equality.
     *
     * @param obj the object to compare against
     * @return {@code true} if equal and this component meets or exceeds the compared amount, {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj)
    {
        if(obj == this)
            return true;

        if(obj.getClass() != getClass())
            return false;

        GasComponent other = (GasComponent) obj;

        return this.amount >= other.amount && gas.equals(other.gas);
    }

    /**
     * Checks if the gas stack is empty (no gas or minimumAmount 0).
     *
     * @return true if the gas stack is empty, false otherwise
     */
    public boolean isEmpty()
    {
        return this.amount == 0 || this.gas.isBlank();
    }

    /**
     * Checks if this gas component matches another gas component.
     *
     * @param stack the gas component to compare with
     * @return true if the components match, false otherwise
     */
    public boolean matches(GasComponent stack)
    {
        if(this == EMPTY && stack == EMPTY)
            return true;

        return stack == null ? isEmpty() : this.gas.equals(stack.gas) && this.amount >= stack.getAmount();
    }

    /**
     * Tests whether this component satisfies a recipe ingredient requirement.
     *
     * @param gasComponent the recipe ingredient requirement
     * @return {@code true} if this component holds matching gas and sufficient amount
     */
    public boolean testForRecipe(GasComponent gasComponent)
    {
        return this.amount >= gasComponent.getAmount() && this.gas.equals(gasComponent.getGas());
    }

    /**
     * Tests whether a storage satisfies this component's recipe requirement.
     *
     * @param gasStorage the gas storage to test
     * @return {@code true} if the storage holds matching gas and sufficient amount
     */
    public boolean testForRecipe(SingleGasStorage gasStorage)
    {
        if(gasStorage == null)
            return false;

        return this.gas.equals(gasStorage.variant) && this.amount <= gasStorage.amount;
    }

    /**
     * Tests whether a storage satisfies this component's recipe requirement with a minimum threshold.
     *
     * @param gasStorage    the gas storage to test
     * @param minimumAmount the minimum amount required
     * @return {@code true} if the storage holds matching gas and sufficient amount meeting the threshold
     */
    public boolean testForRecipe(SingleGasStorage gasStorage, int minimumAmount)
    {
        if(gasStorage == null)
            return false;

        return this.gas.equals(gasStorage.variant) && this.amount <= gasStorage.amount && this.amount >= minimumAmount;
    }

    /**
     * Factory method creating a GasComponent snapshot from a {@link SingleGasStorage}.
     *
     * @param gasStorage the source gas storage
     * @return a new GasComponent instance
     */
    public static GasComponent of(SingleGasStorage gasStorage)
    {
        return new GasComponent(gasStorage.variant, gasStorage.amount);
    }

    /**
     * Retrieves the packet payload type identifier.
     *
     * @return the packet payload type
     */
    @Override
    public @NotNull Type<? extends @NotNull CustomPacketPayload> type()
    {
        return ID;
    }
}