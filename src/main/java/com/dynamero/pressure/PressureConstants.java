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

package com.dynamero.pressure;

import com.dynamero.shared.annotations.*;

/**
 * Pressure unit conversion constants across eight pressure measurement scales.
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
public class PressureConstants
{
    /** Conversion constants from Pascal (Pa). */
    public static class Pascal
    {
        /** Standard atmosphere conversion factor. */
        public static final double ATMOSPHERE = 101_325.0;
        /** Bar conversion factor. */
        public static final double BAR = 100_000.0;
        /** Torr conversion factor. */
        public static final double TORR = 133.322;
        /** Pound-force per square inch (psi) conversion factor. */
        public static final double POUND_FORCE_PER_SQUARE_INCH = 6_894.76;
        /** Technical atmosphere conversion factor. */
        public static final double TECHNICAL_ATMOSPHERE = 98_066.5;
        /** Inches of mercury (inHg) conversion factor. */
        public static final double INHG = 3_386.39;
        /** Centimetres of mercury (cmHg) conversion factor. */
        public static final double CMHG = 133.322;
    }

    /** Conversion constants from Bar. */
    public static class Bar
    {
        /** Standard atmosphere conversion factor. */
        public static final double ATMOSPHERE = 1.01325;
        /** Pascal conversion factor. */
        public static final double PASC = 100_000.0;
        /** Torr conversion factor. */
        public static final double TORR = 750.062;
        /** Pound-force per square inch (psi) conversion factor. */
        public static final double POUND_FORCE_PER_SQUARE_INCH = 14.5038;
        /** Technical atmosphere conversion factor. */
        public static final double TECHNICAL_ATMOSPHERE = 0.980665;
        /** Inches of mercury (inHg) conversion factor. */
        public static final double INHG = 29.5299;
        /** Centimetres of mercury (cmHg) conversion factor. */
        public static final double CMHG = 750.062;
    }

    /** Conversion constants from Standard Atmosphere (atm). */
    public static class Atmosphere
    {
        /** Standard atmosphere factor. */
        public static final double ATMOSPHERE = 1.0;
        /** Pascal conversion factor. */
        public static final double PASC = 101_325.0;
        /** Bar conversion factor. */
        public static final double BAR = 1.01325;
        /** Torr conversion factor. */
        public static final double TORR = 760.0;
        /** Pound-force per square inch (psi) conversion factor. */
        public static final double POUND_FORCE_PER_SQUARE_INCH = 14.6959;
        /** Technical atmosphere conversion factor. */
        public static final double TECHNICAL_ATMOSPHERE = 1.03323;
        /** Inches of mercury (inHg) conversion factor. */
        public static final double INHG = 33.8983;
        /** Centimetres of mercury (cmHg) conversion factor. */
        public static final double CMHG = 760.0;
    }

    /** Conversion constants from Torr. */
    public static class Torr
    {
        /** Standard atmosphere conversion factor. */
        public static final double ATMOSPHERE = 0.00131579;
        /** Pascal conversion factor. */
        public static final double PASC = 133.322;
        /** Bar conversion factor. */
        public static final double BAR = 0.00133322;
        /** Pound-force per square inch (psi) conversion factor. */
        public static final double POUND_FORCE_PER_SQUARE_INCH = 0.0193368;
        /** Technical atmosphere conversion factor. */
        public static final double TECHNICAL_ATMOSPHERE = 0.00135951;
        /** Inches of mercury (inHg) conversion factor. */
        public static final double INHG = 0.0401463;
        /** Centimetres of mercury (cmHg) conversion factor. */
        public static final double CMHG = 1.0;
    }

    /** Conversion constants from Pound-Force per Square Inch (psi). */
    public static class PoundForcePerSquareInch
    {
        /** Standard atmosphere conversion factor. */
        public static final double ATMOSPHERE = 0.0680459;
        /** Pascal conversion factor. */
        public static final double PASC = 6894.76;
        /** Bar conversion factor. */
        public static final double BAR = 0.0689476;
        /** Torr conversion factor. */
        public static final double TORR = 51.7149;
        /** Technical atmosphere conversion factor. */
        public static final double TECHNICAL_ATMOSPHERE = 0.0703069;
        /** Inches of mercury (inHg) conversion factor. */
        public static final double INHG = 2.03602;
        /** Centimetres of mercury (cmHg) conversion factor. */
        public static final double CMHG = 51.7149;
    }

    /** Conversion constants from Technical Atmosphere (at). */
    public static class TechnicalAtmosphere
    {
        /** Standard atmosphere conversion factor. */
        public static final double ATMOSPHERE = 0.967841;
        /** Pascal conversion factor. */
        public static final double PASCAL = 98066.5;
        /** Bar conversion factor. */
        public static final double BAR = 0.980665;
        /** Torr conversion factor. */
        public static final double TORR = 735.559;
        /** Pound-force per square inch (psi) conversion factor. */
        public static final double POUND_FORCE_PER_SQUARE_INCH = 14.2233;
        /** Inches of mercury (inHg) conversion factor. */
        public static final double INHG = 29.53;
        /** Centimetres of mercury (cmHg) conversion factor. */
        public static final double CMHG = 735.559;
    }

    /** Conversion constants from Inches of Mercury (inHg). */
    public static class InHg
    {
        /** Standard atmosphere conversion factor. */
        public static final double ATMOSPHERE = 0.0334211;
        /** Pascal conversion factor. */
        public static final double PASCAL = 3386.39;
        /** Bar conversion factor. */
        public static final double BAR = 0.0338639;
        /** Torr conversion factor. */
        public static final double TORR = 25.3332;
        /** Pound-force per square inch (psi) conversion factor. */
        public static final double POUND_FORCE_PER_SQUARE_INCH = 0.491154;
        /** Technical atmosphere conversion factor. */
        public static final double TECHNICAL_ATMOSPHERE = 0.0338637;
        /** Centimetres of mercury (cmHg) conversion factor. */
        public static final double CMHG = 25.3332;
    }

    /** Conversion constants from Centimetres of Mercury (cmHg). */
    public static class CmHg
    {
        /** Standard atmosphere conversion factor. */
        public static final double ATMOSPHERE = 0.0131579;
        /** Pascal conversion factor. */
        public static final double PASCAL = 1333.22;
        /** Bar conversion factor. */
        public static final double BAR = 0.0133322;
        /** Torr conversion factor. */
        public static final double TORR = 1.0;
        /** Pound-force per square inch (psi) conversion factor. */
        public static final double POUND_FORCE_PER_SQUARE_INCH = 0.0393701;
        /** Technical atmosphere conversion factor. */
        public static final double TECHNICAL_ATMOSPHERE = 0.00135951;
        /** Inches of mercury (inHg) conversion factor. */
        public static final double INHG = 0.0401463;
    }
}