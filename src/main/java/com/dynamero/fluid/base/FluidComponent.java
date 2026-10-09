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

package com.dynamero.fluid.base;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage;
import org.jetbrains.annotations.NotNull;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import com.dynamero.shared.annotations.*;
import com.dynamero.shared.utils.BaseHelper;

/**
 * Represents a custom payload containing a fluid stack, including its variant and minimumAmount.
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
public class FluidComponent implements CustomPacketPayload
{
    private FluidVariant fluid;
    private long amount;

    /**
     * The unique identifier for this custom payload.
     */
    public static final Type<@NotNull FluidComponent> ID = new Type<>(BaseHelper.id("jilibs_fluid_api", "fluid_stack_payload"));

    /**
     * An empty instance of FluidStackPayload.
     */
    public static final FluidComponent EMPTY = new FluidComponent(FluidVariant.blank(), 0);

    /**
     * The codec used to serialize and deserialize the FluidStackPayload.
     */
    public static final Codec<FluidComponent> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            FluidVariant.CODEC.fieldOf("fluid").forGetter(FluidComponent::getFluid),
            Codec.LONG.fieldOf("minimumAmount").forGetter(FluidComponent::getAmount)
    ).apply(inst, FluidComponent::new));

    /**
     * The packet codec used to send and receive the FluidStackPayload.
     */
    public static final StreamCodec<RegistryFriendlyByteBuf, FluidComponent> STREAM_CODEC =
            StreamCodec.composite(FluidVariant.PACKET_CODEC, FluidComponent::getFluid,
                                ByteBufCodecs.LONG, FluidComponent::getAmount,
                                FluidComponent::new);

    public static final Codec<List<FluidComponent>> LIST_CODEC = CODEC.listOf();

    public FluidComponent(FluidVariant fluid, long amount)
    {
        this.fluid = fluid;
        this.amount = amount;
    }

    public FluidVariant getFluid()
    {
        return fluid;
    }

    public void setFluid(FluidVariant fluid)
    {
        this.fluid = fluid;
    }

    public long getAmount()
    {
        return amount;
    }

    public void setAmount(long amount)
    {
        this.amount = amount;
    }

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

    /**
     * Checks if the fluid stack is empty (no fluid or minimumAmount 0).
     *
     * @return true if the fluid stack is empty, false otherwise
     */
    public boolean isEmpty()
    {
        return this.amount == 0 || this.fluid.isBlank();
    }

    /**
     * Checks if this fluid stack matches another fluid stack.
     *
     * @param stack the fluid stack to compare with
     * @return true if the stacks match, false otherwise
     */
    public boolean matches(FluidComponent stack)
    {
        if(this == EMPTY && stack == EMPTY)
            return true;
        return stack == null ? isEmpty() : this.fluid.equals(stack.fluid) && this.amount >= stack.getAmount();
    }

    /**
     * Tests if this fluid stack can be used in a recipe.
     *
     * @param stack the fluid stack to test against
     * @return true if the stacks match and this stack has enough fluid for the recipe, false otherwise
     */
    public boolean testForRecipe(FluidComponent stack)
    {
        return this.fluid.equals(stack.getFluid()) && this.amount >= stack.getAmount();
    }

    /**
     * Tests if this fluid stack can be used in a recipe with the given SingleFluidStorage.
     *
     * @param fluidStorage the SingleFluidStorage to test against
     * @return true if the stacks match and there is enough fluid in the storage, false otherwise
     */
    public boolean testForRecipe(SingleFluidStorage fluidStorage)
    {
        if(fluidStorage == null)
            return false;

        return this.fluid.equals(fluidStorage.variant) && this.amount <= fluidStorage.amount;
    }

    /**
     * Tests if this fluid stack can be used in a recipe with the given SingleFluidStorage and minimum minimumAmount.
     *
     * @param fluidStorage the SingleFluidStorage to test against
     * @param minimumAmount the minimum minimumAmount of fluid required for the recipe
     * @return true if the stacks match, there is enough fluid in the storage, and it meets or exceeds the minimum minimumAmount, false otherwise
     */
    public boolean testForRecipe(SingleFluidStorage fluidStorage, int minimumAmount)
    {
        if(fluidStorage == null)
            return false;

        return this.fluid.equals(fluidStorage.variant) && this.amount <= fluidStorage.amount && this.amount >= minimumAmount;
    }

    /**
     * Creates a new FluidStackPayload from the given SingleFluidStorage.
     *
     * @param fluidStorage the SingleFluidStorage to convert
     * @return the new FluidStackPayload instance
     */
    public static FluidComponent of(SingleFluidStorage fluidStorage)
    {
        return new FluidComponent(fluidStorage.variant, fluidStorage.amount);
    }
}