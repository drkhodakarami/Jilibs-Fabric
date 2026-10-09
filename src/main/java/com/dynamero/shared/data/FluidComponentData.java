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

package com.dynamero.shared.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.material.Fluids;

@SuppressWarnings("unused")
public class FluidComponentData
{
    private long amount;
    private FluidVariant variant;

    public static FluidComponentData WATER_BUCKET = new FluidComponentData(FluidConstants.BUCKET, FluidVariant.of(Fluids.WATER));
    public static FluidComponentData WATER_BLOCK = new FluidComponentData(FluidConstants.BLOCK, FluidVariant.of(Fluids.WATER));
    public static FluidComponentData WATER_BOTTLE = new FluidComponentData(FluidConstants.BOTTLE, FluidVariant.of(Fluids.WATER));
    public static FluidComponentData WATER_BOWL = new FluidComponentData(FluidConstants.BOWL, FluidVariant.of(Fluids.WATER));
    public static FluidComponentData WATER_DROPLET = new FluidComponentData(FluidConstants.DROPLET, FluidVariant.of(Fluids.WATER));

    public static FluidComponentData LAVA_BUCKET = new FluidComponentData(FluidConstants.BUCKET, FluidVariant.of(Fluids.LAVA));
    public static FluidComponentData LAVA_BLOCK = new FluidComponentData(FluidConstants.BLOCK, FluidVariant.of(Fluids.LAVA));
    public static FluidComponentData LAVA_BOTTLE = new FluidComponentData(FluidConstants.BOTTLE, FluidVariant.of(Fluids.LAVA));
    public static FluidComponentData LAVA_BOWL = new FluidComponentData(FluidConstants.BOWL, FluidVariant.of(Fluids.LAVA));
    public static FluidComponentData LAVA_DROPLET = new FluidComponentData(FluidConstants.DROPLET, FluidVariant.of(Fluids.LAVA));

    public static final Codec<FluidComponentData> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.LONG.fieldOf("amount").forGetter(FluidComponentData::getAmount),
            FluidVariant.CODEC.fieldOf("variant").forGetter(FluidComponentData::getVariant)
    ).apply(inst, FluidComponentData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, FluidComponentData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.LONG, FluidComponentData::getAmount,
            FluidVariant.PACKET_CODEC, FluidComponentData::getVariant,
            FluidComponentData::new
    );

    public FluidComponentData(long amount, FluidVariant variant)
    {
        this.amount = amount;
        this.variant = variant;
    }

    public long getAmount()
    {
        return amount;
    }

    public void setAmount(long amount)
    {
        this.amount = amount;
    }

    public FluidVariant getVariant()
    {
        return variant;
    }

    public void setVariant(FluidVariant variant)
    {
        this.variant = variant;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj == this)
            return true;

        if(obj.getClass() != getClass())
            return false;

        FluidComponentData other = (FluidComponentData) obj;

        return this.amount >= other.amount && variant.equals(other.variant);
    }
}