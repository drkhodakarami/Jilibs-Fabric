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

package com.dynamero.dispersion;

import com.dynamero.gas.GasConstants;
import com.dynamero.shared.annotations.*;

/**
 * Constants and utility conversions defining standard dispersion volume amounts measured in droplets.
 */
@SuppressWarnings("unused")
@Developer("TheMentor")
@CreatedAt("2026-08-10")
@ModifiedAt("2026-08-10")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public final class DispersionConstants
{
    /**
     * Standard dispersion bucket volume in droplets (81,000 droplets).
     */
    public static final long BUCKET = 81000L;

    /**
     * Volume of dispersion contained in a bottle in droplets (27,000 droplets).
     */
    public static final long BOTTLE = 27000L;

    /**
     * Volume of dispersion contained in a bowl in droplets (27,000 droplets).
     */
    public static final long BOWL = 27000L;

    /**
     * Volume of dispersion contained in a full block in droplets (81,000 droplets).
     */
    public static final long BLOCK = 81000L;

    /**
     * Volume of dispersion equivalent to an ingot in droplets (9,000 droplets).
     */
    public static final long INGOT = 9000L;

    /**
     * Volume of dispersion equivalent to a nugget in droplets (1,000 droplets).
     */
    public static final long NUGGET = 1000L;

    /**
     * Smallest discrete volume of dispersion: 1 droplet.
     */
    public static final long DROPLET = 1L;

    /**
     * Converts a fractional bucket quantity into an exact droplet amount.
     *
     * @param numerator   the numerator of the bucket fraction
     * @param denominator the denominator of the bucket fraction
     * @return the equivalent amount in droplets
     * @throws IllegalArgumentException if the fraction does not resolve to an integer number of droplets
     */
    public static long fromBucketFraction(long numerator, long denominator) {
        return GasConstants.fromUnitFraction(numerator, denominator);
    }

    /**
     * Private constructor preventing instantiation of utility constants class.
     */
    private DispersionConstants() {
    }
}