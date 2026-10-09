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

package com.dynamero.gas;

import com.dynamero.shared.annotations.*;

/**
 * Constants and utility conversions defining standard gas volume amounts measured in droplets.
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
public final class GasConstants
{
    /**
     * Standard gas unit volume in droplets (equivalent to one block/bucket).
     */
    public static final long UNIT = 81000L;

    /**
     * Volume of gas contained in a standard glass bottle in droplets.
     */
    public static final long BOTTLE = 27000L;

    /**
     * Volume of gas contained in a full block in droplets.
     */
    public static final long BLOCK = 81000L;

    /**
     * Smallest discrete volume of gas: 1 droplet.
     */
    public static final long DROPLET = 1L;

    /**
     * Converts a fractional unit quantity into an exact droplet amount.
     *
     * @param numerator   the numerator of the unit fraction
     * @param denominator the denominator of the unit fraction
     * @return the equivalent amount in droplets
     * @throws IllegalArgumentException if the fraction does not resolve to an integer number of droplets
     */
    public static long fromUnitFraction(long numerator, long denominator) {
        long total = numerator * 81000L;
        if (total % denominator != 0L) {
            throw new IllegalArgumentException("Not a valid number of droplets!");
        } else {
            return total / denominator;
        }
    }

    /**
     * Private constructor preventing instantiation of utility constants class.
     */
    private GasConstants() {
    }
}