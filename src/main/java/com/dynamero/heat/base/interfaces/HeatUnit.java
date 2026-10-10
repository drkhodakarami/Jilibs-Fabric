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

package com.dynamero.heat.base.interfaces;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import com.dynamero.heat.HeatConstants;
import com.dynamero.heat.base.unit.*;
import com.dynamero.shared.annotations.*;

/**
 * Interface defining temperature measurement units and conversion algorithms between different thermal scales,
 * with Kelvin as the canonical base unit.
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
public interface HeatUnit
{
    /** Degree symbol string constant. */
    String DEGREE_SYMBOL = "°";

    /** Celsius unit instance. */
    HeatUnit CELSIUS = new CelsiusUnit();
    /** Kelvin base unit instance. */
    HeatUnit KELVIN = new KelvinUnit();
    /** Fahrenheit unit instance. */
    HeatUnit FAHRENHEIT = new FahrenheitUnit();
    /** Rankine unit instance. */
    HeatUnit RANKINE = new RankineUnit();
    /** Delisle unit instance. */
    HeatUnit DELISLE = new DelisleUnit();
    /** Newton unit instance. */
    HeatUnit NEWTON = new NewtonUnit();
    /** Réaumur unit instance. */
    HeatUnit REAUMUR = new ReaumurUnit();
    /** Rømer unit instance. */
    HeatUnit ROMER = new RomerUnit();

    /** Codec for serializing and deserializing HeatUnit by name. */
    Codec<HeatUnit> CODEC = Codec.STRING.xmap(
            name -> switch (name.toLowerCase())
            {
                case "celsius" -> CELSIUS;
                case "kelvin" -> KELVIN;
                case "fahrenheit" -> FAHRENHEIT;
                case "rankine" -> RANKINE;
                case "delisle" -> DELISLE;
                case "newton" -> NEWTON;
                case "reaumur" -> REAUMUR;
                case "romer" -> ROMER;
                default -> throw new IllegalArgumentException("Unknown HeatUnit: " + name);
            },
            heatUnit -> heatUnit.getUnitName().toLowerCase()
    );

    /** Stream codec for transmitting HeatUnit by name over network byte buffers. */
    StreamCodec<ByteBuf, HeatUnit> STREAM_CODEC = ByteBufCodecs.STRING_UTF8.map(
            name -> switch (name.toLowerCase())
            {
                case "celsius" -> CELSIUS;
                case "kelvin" -> KELVIN;
                case "fahrenheit" -> FAHRENHEIT;
                case "rankine" -> RANKINE;
                case "delisle" -> DELISLE;
                case "newton" -> NEWTON;
                case "reaumur" -> REAUMUR;
                case "romer" -> ROMER;
                default -> throw new IllegalArgumentException("Unknown HeatUnit: " + name);
            },
            heatUnit -> heatUnit.getUnitName().toLowerCase());

    /**
     * Gets the room temperature in the specified unit.
     *
     * @param unit The unit to get the room temperature in
     * @return The room temperature in the specified unit
     */
    static double getRoomTemperature(HeatUnit unit)
    {
        return unit.convertFromBaseUnit(HeatConstants.Kelvin.ROOM_TEMPERATURE);
    }

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
     * <p>implNote: The base unit is {@link HeatUnit#KELVIN}</p>
     *
     * @param value The value to convert
     * @return The value in the base unit
     * @see HeatUnit#convertFromUnit(double, HeatUnit)
     * @see HeatUnit#convertToUnit(double, HeatUnit)
     */
    double convertToBaseUnit(double value);

    /**
     * Converts the specified value from the base unit.
     * <p>implNote: The base unit is {@link HeatUnit#KELVIN}</p>
     *
     * @param value The value to convert
     * @return The value from the base unit
     * @see HeatUnit#convertToUnit(double, HeatUnit)
     * @see HeatUnit#convertFromUnit(double, HeatUnit)
     */
    double convertFromBaseUnit(double value);

    /**
     * Gets the minimum value of this unit.
     *
     * @return The minimum value of this unit
     */
    default double getMinValue()
    {
        return HeatConstants.Kelvin.ABSOLUTE_ZERO;
    }

    /**
     * Converts the specified value to the specified unit.
     *
     * @param value The value to convert
     * @param unit  The unit to convert to
     * @return The value in the specified unit
     * @see HeatUnit#convertToBaseUnit(double)
     * @see HeatUnit#convertFromBaseUnit(double)
     */
    default double convertToUnit(double value, HeatUnit unit) {
        return unit.convertFromBaseUnit(convertToBaseUnit(value));
    }

    /**
     * Converts the specified value from the specified unit.
     *
     * @param value The value to convert
     * @param unit  The unit to convert from
     * @return The value from the specified unit
     * @see HeatUnit#convertFromBaseUnit(double)
     * @see HeatUnit#convertToBaseUnit(double)
     */
    default double convertFromUnit(double value, HeatUnit unit) {
        return convertFromBaseUnit(unit.convertToBaseUnit(value));
    }
}