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

package com.dynamero.heat;

import com.dynamero.shared.annotations.*;

/**
 * Physical temperature benchmarks across eight temperature scales (freezing, boiling, absolute zero, room temp, lava temp).
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
public class HeatConstants
{
    /** Temperature constants in degrees Celsius (°C). */
    public static class Celsius
    {
        /** Water freezing point in Celsius. */
        public static final double WATER_FREEZ = 0;
        /** Water boiling point in Celsius. */
        public static final double WATER_BOIL = 100;
        /** Absolute zero in Celsius. */
        public static final double ABSOLUTE_ZERO = -273.15;
        /** Room temperature in Celsius. */
        public static final double ROOM_TEMPERATURE = 21;
        /** Lava temperature in Celsius. */
        public static final double LAVA_TEMPERATURE = 1000;
    }

    /** Temperature constants in degrees Delisle (°De). */
    public static class Delisle
    {
        /** Water freezing point in Delisle. */
        public static final double WATER_FREEZ = 150;
        /** Water boiling point in Delisle. */
        public static final double WATER_BOIL = 0;
        /** Absolute zero in Delisle. */
        public static final double ABSOLUTE_ZERO = -259.725;
        /** Room temperature in Delisle. */
        public static final double ROOM_TEMPERATURE = 118.5;
        /** Lava temperature in Delisle. */
        public static final double LAVA_TEMPERATURE = -1350;
    }

    /** Temperature constants in degrees Fahrenheit (°F). */
    public static class Fahrenheit
    {
        /** Water freezing point in Fahrenheit. */
        public static final double WATER_FREEZ = 32;
        /** Water boiling point in Fahrenheit. */
        public static final double WATER_BOIL = 212;
        /** Absolute zero in Fahrenheit. */
        public static final double ABSOLUTE_ZERO = -459.67;
        /** Room temperature in Fahrenheit. */
        public static final double ROOM_TEMPERATURE = 69.8;
        /** Lava temperature in Fahrenheit. */
        public static final double LAVA_TEMPERATURE = 1832;
    }

    /** Temperature constants in Kelvin (K). */
    public static class Kelvin
    {
        /** Water freezing point in Kelvin. */
        public static final double WATER_FREEZ = 273.15;
        /** Water boiling point in Kelvin. */
        public static final double WATER_BOIL = 373.15;
        /** Absolute zero in Kelvin. */
        public static final double ABSOLUTE_ZERO = 0.0;
        /** Room temperature in Kelvin. */
        public static final double ROOM_TEMPERATURE = 294.15;
        /** Lava temperature in Kelvin. */
        public static final double LAVA_TEMPERATURE = 1273.15;
    }

    /** Temperature constants in degrees Newton (°N). */
    public static class Newton
    {
        /** Water freezing point in Newton. */
        public static final double WATER_FREEZ = 0;
        /** Water boiling point in Newton. */
        public static final double WATER_BOIL = 33;
        /** Absolute zero in Newton. */
        public static final double ABSOLUTE_ZERO = -90.1395;
        /** Room temperature in Newton. */
        public static final double ROOM_TEMPERATURE = 6.93;
        /** Lava temperature in Newton. */
        public static final double LAVA_TEMPERATURE = 330;
    }

    /** Temperature constants in degrees Rankine (°R). */
    public static class Rankine
    {
        /** Water freezing point in Rankine. */
        public static final double WATER_FREEZ = 491.67;
        /** Water boiling point in Rankine. */
        public static final double WATER_BOIL = 671.67;
        /** Absolute zero in Rankine. */
        public static final double ABSOLUTE_ZERO = 0;
        /** Room temperature in Rankine. */
        public static final double ROOM_TEMPERATURE = 529.47;
        /** Lava temperature in Rankine. */
        public static final double LAVA_TEMPERATURE = 2291.67;
    }

    /** Temperature constants in degrees Réaumur (°Ré). */
    public static class Reaumur
    {
        /** Water freezing point in Réaumur. */
        public static final double WATER_FREEZ = 0;
        /** Water boiling point in Réaumur. */
        public static final double WATER_BOIL = 80;
        /** Absolute zero in Réaumur. */
        public static final double ABSOLUTE_ZERO = -218.52;
        /** Room temperature in Réaumur. */
        public static final double ROOM_TEMPERATURE = 16.8;
        /** Lava temperature in Réaumur. */
        public static final double LAVA_TEMPERATURE = 800;
    }

    /** Temperature constants in degrees Rømer (°Rø). */
    public static class Romer
    {
        /** Water freezing point in Rømer. */
        public static final double WATER_FREEZ = 7.5;
        /** Water boiling point in Rømer. */
        public static final double WATER_BOIL = 60;
        /** Absolute zero in Rømer. */
        public static final double ABSOLUTE_ZERO = -135.90375;
        /** Room temperature in Rømer. */
        public static final double ROOM_TEMPERATURE = 18.525;
        /** Lava temperature in Rømer. */
        public static final double LAVA_TEMPERATURE = 532.5;
    }
}