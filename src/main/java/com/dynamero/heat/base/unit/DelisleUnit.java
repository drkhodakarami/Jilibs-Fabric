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

package com.dynamero.heat.base.unit;

import com.dynamero.heat.base.interfaces.HeatUnit;
import com.dynamero.shared.annotations.*;

/**
 * Delisle (°De) temperature unit implementation.
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
public class DelisleUnit implements HeatUnit
{
    /**
     * Returns the name of this unit.
     *
     * @return "Delisle"
     */
    @Override
    public String getUnitName()
    {
        return "Delisle";
    }

    /**
     * Returns the symbol of this unit.
     *
     * @return "°De"
     */
    @Override
    public String getUnitSymbol()
    {
        return "°De";
    }

    /**
     * Converts the Delisle value to base unit.
     *
     * @param value the temperature value in Delisle
     * @return value in base unit
     */
    @Override
    public double convertToBaseUnit(double value)
    {
        return 100 - value * 2 / 3;
    }

    /**
     * Converts the value from base unit to Delisle.
     *
     * @param value the temperature value in base unit
     * @return value in Delisle
     */
    @Override
    public double convertFromBaseUnit(double value)
    {
        return (100 - value) * 3 / 2;
    }
}