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

package com.dynamero.gas.base.records;

import static com.dynamero.Jilibs.GASES;
import static com.dynamero.Jilibs.MODID;

import java.util.Objects;
import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

import com.dynamero.heat.base.interfaces.HeatUnit;
import com.dynamero.heat.base.records.HeatPhysics;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.utils.BaseHelper;

/**
 * Definition record for a gas type containing identifier, molar mass (g/mol), and thermal physics properties.
 *
 * @param id          the unique string identifier of the gas
 * @param molarMass   the molar mass in grams per mole (g/mol)
 * @param heatPhysics the thermodynamic properties record
 */
@SuppressWarnings("unused")
@Developer("TurtyWurty")
@CreatedAt("2026-10-08")
@ModifiedAt("2026-10-08")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public record Gas(String id,
                double molarMass,  // Molar mass in grams per mole (g/mol)
                HeatPhysics heatPhysics)
{
    /**
     * Canonical empty gas definition.
     */
    public static final Gas EMPTY = register("empty");

    /**
     * Codec for serializing and deserializing Gas instances.
     */
    public static final Codec<Gas> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.STRING.fieldOf("id").forGetter(Gas::id),
            Codec.DOUBLE.fieldOf("molarMass").forGetter(Gas::molarMass),
            HeatPhysics.CODEC.fieldOf("heatPhysics").forGetter(Gas::heatPhysics)
    ).apply(inst, Gas::new));

    /**
     * Stream codec for transmitting Gas instances over network buffers.
     */
    public static final StreamCodec<FriendlyByteBuf, Gas> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, Gas::id,
            ByteBufCodecs.DOUBLE, Gas::molarMass,
            HeatPhysics.STREAM_CODEC, Gas::heatPhysics,
            Gas::new
    );

    /**
     * Checks whether the specified gas matches this gas instance.
     *
     * @param gas the gas to test
     * @return {@code true} if identical instance, {@code false} otherwise
     */
    public boolean matchesType(Gas gas)
    {
        return gas == this;
    }

    /**
     * Resolves an optional pickup sound event for this gas.
     *
     * @return empty optional by default
     */
    public Optional<SoundEvent> getPickupSound()
    {
        return Optional.empty();
    }

    /**
     * Registers a gas instance in the gases registry under the mod namespace.
     *
     * @param gas the gas to register
     * @return the registered gas instance
     */
    public static Gas register(Gas gas)
    {
        return Registry.register(GASES, BaseHelper.id(MODID, gas.id()), gas);
    }

    /**
     * Registers an empty-property gas instance under the specified path ID.
     *
     * @param id the path identifier
     * @return the registered gas instance
     */
    public static Gas register(String id)
    {
        return register(BaseHelper.id(MODID, id));
    }

    /**
     * Registers an empty-property gas instance under the specified identifier.
     *
     * @param id the identifier
     * @return the registered gas instance
     */
    public static Gas register(Identifier id)
    {
        return register(new Builder(id.getPath()).build());
    }

    /**
     * Builder helper class for configuring and constructing {@link Gas} instances.
     */
    public static class Builder
    {
        /**
         * The gas identifier string.
         */
        private final String id;

        /**
         * The molar mass in g/mol.
         */
        private double molarMass;

        /**
         * The heat physics properties.
         */
        private HeatPhysics heatPhysics;

        /**
         * Constructs a Builder with the specified gas identifier.
         *
         * @param id the unique string identifier
         */
        public Builder(String id)
        {
            this.id = id;
            this.molarMass = 0.0;
            this.heatPhysics = HeatPhysics.ZERO;
        }

        /**
         * Sets the molar mass in grams per mole.
         *
         * @param molarMass the molar mass in g/mol
         * @return this builder
         */
        public Builder molarMass(double molarMass)
        {
            this.molarMass = molarMass;
            return this;
        }

        /**
         * Sets the heat measurement unit for thermal calculations.
         *
         * @param heatUnit the heat unit
         * @return this builder
         */
        public Builder heatUnit(HeatUnit heatUnit)
        {
            Objects.requireNonNull(heatUnit, "Heat Unit cannot be null");

            this.heatPhysics = new HeatPhysics(heatUnit, heatPhysics.specificThermalCapacity(), heatPhysics.thermalConductivity());
            return this;
        }

        /**
         * Sets the specific thermal capacity of the gas.
         *
         * @param specificThermalCapacity the specific heat capacity
         * @return this builder
         * @throws IllegalArgumentException if capacity is negative
         */
        public Builder specificThermalCapacity(double specificThermalCapacity)
        {
            if(specificThermalCapacity < 0)
                throw new IllegalArgumentException("Specific Heat Capacity cacnnot be negative");

            this.heatPhysics = new HeatPhysics(heatPhysics.unit(), specificThermalCapacity, heatPhysics.thermalConductivity());
            return this;
        }

        /**
         * Sets the thermal conductivity of the gas.
         *
         * @param thermalConductivity the thermal conductivity
         * @return this builder
         * @throws IllegalArgumentException if conductivity is negative
         */
        public Builder thermalConductivity(double thermalConductivity)
        {
            if (thermalConductivity < 0)
                throw new IllegalArgumentException("Thermal conductivity cannot be negative");

            this.heatPhysics = new HeatPhysics(heatPhysics.unit(), heatPhysics.specificThermalCapacity(), thermalConductivity);
            return this;
        }

        /**
         * Builds a new {@link Gas} instance using configured properties.
         *
         * @return the new Gas instance
         */
        public Gas build()
        {
            return new Gas(id, molarMass, heatPhysics);
        }
    }
}