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

package com.dynamero.pressure.base.interfaces;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import com.dynamero.pressure.base.unit.AtmosphereUnit;
import com.dynamero.pressure.base.unit.BarUnit;
import com.dynamero.pressure.base.unit.PascalUnit;
import com.dynamero.shared.annotations.*;

/**
 * Interface defining pressure measurement units and conversion algorithms between different pressure scales,
 * with Pascal as the canonical base unit.
 */
@SuppressWarnings("unused")
@Developer("TheMentor")
@CreatedAt("2026-01-12")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
public interface PressureUnit
{
    /** Pascal base unit instance. */
    PressureUnit PASCAL = new PascalUnit();
    /** Bar unit instance. */
    PressureUnit BAR = new BarUnit();
    /** Standard Atmosphere unit instance. */
    PressureUnit ATMOSPHERE = new AtmosphereUnit();

    /** Codec for serializing and deserializing PressureUnit by name. */
    Codec<PressureUnit> CODEC = Codec.STRING.xmap(
            name -> switch (name.toLowerCase()) {
                case "pascal" -> PASCAL;
                case "bar" -> BAR;
                case "atmosphere" -> ATMOSPHERE;
                default -> throw new IllegalArgumentException("Unknown PressureUnit: " + name);
            },
            pressureUnit -> pressureUnit.getUnitName().toLowerCase()
    );

    /** Stream codec for transmitting PressureUnit by name over network byte buffers. */
    StreamCodec<ByteBuf, PressureUnit> STREAM_CODEC = ByteBufCodecs.STRING_UTF8.map(
            name -> switch (name.toLowerCase()) {
                case "pascal" -> PASCAL;
                case "bar" -> BAR;
                case "atmosphere" -> ATMOSPHERE;
                default -> throw new IllegalArgumentException("Unknown PressureUnit: " + name);
            },
            pressureUnit -> pressureUnit.getUnitName().toLowerCase());

    /**
     * Gets the name of this unit.
     *
     * @return The name of this unit
     */
    String getUnitName();

    /**
     * Gets the symbol of this unit.
     *
     * @return The symbol of this unit
     */
    String getUnitSymbol();

    /**
     * Converts the specified value to the base unit.
     * <p>implNote: The base unit is {@link PressureUnit#PASCAL}</p>
     *
     * @param value The value to convert
     * @return The value in the base unit
     */
    double convertToBaseUnit(double value);

    /**
     * Converts the specified value from the base unit.
     * <p>implNote: The base unit is {@link PressureUnit#PASCAL}</p>
     *
     * @param value The value to convert
     * @return The value from the base unit
     */
    double convertFromBaseUnit(double value);

    /**
     * Gets the minimum value of this unit.
     *
     * @return The minimum value of this unit
     */
    default double getMinValue() {
        return 0.0;
    }

    /**
     * Converts the specified value to the specified unit.
     *
     * @param value The value to convert
     * @param unit  The unit to convert to
     * @return The value in the specified unit
     */
    default double convertToUnit(double value, PressureUnit unit) {
        return unit.convertFromBaseUnit(convertToBaseUnit(value));
    }

    /**
     * Converts the specified value from the specified unit.
     *
     * @param value The value to convert
     * @param unit  The unit to convert from
     * @return The value from the specified unit
     */
    default double convertFromUnit(double value, PressureUnit unit) {
        return convertFromBaseUnit(unit.convertToBaseUnit(value));
    }
}