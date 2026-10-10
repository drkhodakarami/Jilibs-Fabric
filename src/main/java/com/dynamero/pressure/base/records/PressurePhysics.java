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

package com.dynamero.pressure.base.records;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import com.dynamero.pressure.base.interfaces.PressureUnit;
import com.dynamero.shared.annotations.*;

/**
 * Pressure physics properties record containing unit and compressibility.
 *
 * @param unit            the pressure measurement unit
 * @param compressibility compressibility value
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
public record PressurePhysics(PressureUnit unit, double compressibility)
{
    /**
     * Canonical zero-valued pressure physics instance in Pascal.
     */
    public static final PressurePhysics ZERO = new Builder(PressureUnit.PASCAL).build();

    /**
     * Codec for serializing and deserializing PressurePhysics instances.
     */
    public static final Codec<PressurePhysics> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            PressureUnit.CODEC.fieldOf("unit").forGetter(PressurePhysics::unit),
            Codec.DOUBLE.fieldOf("compressibility").forGetter(PressurePhysics::compressibility)
    ).apply(inst, PressurePhysics::new));

    /**
     * Stream codec for transmitting PressurePhysics instances over network byte buffers.
     */
    public static final StreamCodec<FriendlyByteBuf, PressurePhysics> STREAM_CODEC = StreamCodec.composite(
            PressureUnit.STREAM_CODEC, PressurePhysics::unit,
            ByteBufCodecs.DOUBLE, PressurePhysics::compressibility,
            PressurePhysics::new
    );

    /**
     * Builder for configuring and creating {@link PressurePhysics} instances.
     */
    public static class Builder {
        /**
         * The pressure measurement unit.
         */
        private final PressureUnit pressureUnit;

        /**
         * The compressibility value.
         */
        private double compressibility;

        /**
         * Constructs a Builder with the specified base pressure unit.
         *
         * @param pressureUnit the pressure measurement unit
         * @throws IllegalArgumentException if pressureUnit is null
         */
        public Builder(PressureUnit pressureUnit)
        {
            if (pressureUnit == null)
                throw new IllegalArgumentException("Pressure Unit cannot be null");

            this.pressureUnit = pressureUnit;
            this.compressibility = 0.0;
        }

        /**
         * Sets the compressibility value.
         *
         * @param value the compressibility value
         * @return this builder
         */
        public Builder compressibility(double value)
        {
            this.compressibility = value;
            return this;
        }

        /**
         * Builds a new {@link PressurePhysics} instance using configured values.
         *
         * @return the new PressurePhysics instance
         */
        public PressurePhysics build()
        {
            return new PressurePhysics(pressureUnit, compressibility);
        }
    }
}