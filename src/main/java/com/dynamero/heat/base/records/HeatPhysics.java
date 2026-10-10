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

package com.dynamero.heat.base.records;

import java.util.Objects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import com.dynamero.heat.base.interfaces.HeatUnit;
import com.dynamero.shared.annotations.*;

/**
 * Thermodynamic physics properties record containing unit, specific heat capacity in J/(g·K),
 * and thermal conductivity in W/(m·K).
 *
 * @param unit                    the heat measurement unit
 * @param specificThermalCapacity specific heat capacity in J/(g·K)
 * @param thermalConductivity     thermal conductivity in W/(m·K)
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
public record HeatPhysics(HeatUnit unit, double specificThermalCapacity, double thermalConductivity)
{
    /**
     * Canonical zero-valued heat physics instance in Kelvin.
     */
    public static final HeatPhysics ZERO = new Builder(HeatUnit.KELVIN).build();

    /**
     * Codec for serializing and deserializing HeatPhysics instances.
     */
    public static final Codec<HeatPhysics> CODEC = RecordCodecBuilder.create(inst -> inst.group(
    HeatUnit.CODEC.fieldOf("unit").forGetter(HeatPhysics::unit),
    Codec.DOUBLE.fieldOf("specificThermalCapacity").forGetter(HeatPhysics::specificThermalCapacity),
    Codec.DOUBLE.fieldOf("thermalConductivity").forGetter(HeatPhysics::thermalConductivity)
    ).apply(inst, HeatPhysics::new));

    /**
     * Stream codec for transmitting HeatPhysics instances over network byte buffers.
     */
    public static final StreamCodec<FriendlyByteBuf, HeatPhysics> STREAM_CODEC = StreamCodec.composite(
            HeatUnit.STREAM_CODEC, HeatPhysics::unit,
            ByteBufCodecs.DOUBLE, HeatPhysics::specificThermalCapacity,
            ByteBufCodecs.DOUBLE, HeatPhysics::thermalConductivity,
            HeatPhysics::new
    );

    /**
     * Builder for configuring and creating {@link HeatPhysics} instances.
     */
    public static class Builder
    {
        /**
         * The heat measurement unit.
         */
        private final HeatUnit heatUnit;

        /**
         * The specific thermal capacity in J/(g·K).
         */
        private double specificThermalCapacity;

        /**
         * The thermal conductivity in W/(m·K).
         */
        private double thermalConductivity;

        /**
         * Constructs a Builder with the specified base heat unit.
         *
         * @param heatUnit the heat measurement unit
         */
        public Builder(HeatUnit heatUnit)
        {
            Objects.requireNonNull(heatUnit, "Heat Unit cannot be null");

            this.heatUnit = heatUnit;
            this.specificThermalCapacity = 0.0;
            this.thermalConductivity = 0.0;
        }

        /**
         * Sets the specific thermal capacity in J/(g·K).
         *
         * @param value the specific heat capacity
         * @return this builder
         */
        public Builder specificThermalCapacity(double value)
        {
            this.specificThermalCapacity = value;
            return this;
        }

        /**
         * Sets the thermal conductivity in W/(m·K).
         *
         * @param value the thermal conductivity
         * @return this builder
         */
        public Builder thermalConductivity(double value)
        {
            this.thermalConductivity = value;
            return this;
        }

        /**
         * Builds a new {@link HeatPhysics} instance using configured values.
         *
         * @return the new HeatPhysics instance
         */
        public HeatPhysics build()
        {
            return new HeatPhysics(heatUnit, specificThermalCapacity, thermalConductivity);
        }
    }
}