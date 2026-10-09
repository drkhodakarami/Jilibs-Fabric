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
 * Rømer (°Rø) temperature unit implementation.
 */
@Developer("TheMentor")
@CreatedAt("2026-01-12")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
public class RomerUnit implements HeatUnit
{
    /**
     * Returns the name of this unit.
     *
     * @return "Rømer"
     */
    @Override
    public String getUnitName()
    {
        return "Rømer";
    }

    /**
     * Returns the symbol of this unit.
     *
     * @return "°Rø"
     */
    @Override
    public String getUnitSymbol()
    {
        return "°Rø";
    }

    /**
     * Converts the Rømer value to base unit.
     *
     * @param value the temperature value in Rømer
     * @return value in base unit
     */
    @Override
    public double convertToBaseUnit(double value)
    {
        return (value - 7.5) * 40 / 21;
    }

    /**
     * Converts the value from base unit to Rømer.
     *
     * @param value the temperature value in base unit
     * @return value in Rømer
     */
    @Override
    public double convertFromBaseUnit(double value)
    {
        return value * 21 / 40 + 7.5;
    }
}