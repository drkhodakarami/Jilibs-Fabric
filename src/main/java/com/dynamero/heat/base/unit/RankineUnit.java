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
 * Rankine (°R) temperature unit implementation.
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
public class RankineUnit implements HeatUnit
{
    /**
     * Returns the name of this unit.
     *
     * @return "Rankine"
     */
    @Override
    public String getUnitName()
    {
        return "Rankine";
    }

    /**
     * Returns the symbol of this unit.
     *
     * @return "°R"
     */
    @Override
    public String getUnitSymbol()
    {
        return "°R";
    }

    /**
     * Converts the Rankine value to base unit.
     *
     * @param value the temperature value in Rankine
     * @return value in base unit
     */
    @Override
    public double convertToBaseUnit(double value)
    {
        return (value - 491.67) * 5.0 / 9.0;
    }

    /**
     * Converts the value from base unit to Rankine.
     *
     * @param value the temperature value in base unit
     * @return value in Rankine
     */
    @Override
    public double convertFromBaseUnit(double value)
    {
        return value * 9.0 / 5.0 + 491.67;
    }
}