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

package com.dynamero.pressure.base.unit;

import com.dynamero.pressure.base.interfaces.PressureUnit;
import com.dynamero.shared.annotations.*;

/**
 * Technical Atmosphere (at) pressure unit implementation.
 */
@Developer("TheMentor")
@CreatedAt("2026-08-10")
@ModifiedAt("2026-08-10")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public class TechnicalAtmosphereUnit implements PressureUnit
{
    /**
     * Returns the name of this unit.
     *
     * @return "Technical Atmosphere"
     */
    @Override
    public String getUnitName()
    {
        return "Technical Atmosphere";
    }

    /**
     * Returns the symbol of this unit.
     *
     * @return "at"
     */
    @Override
    public String getUnitSymbol()
    {
        return "at";
    }

    /**
     * Converts the at value to base unit (Pascal).
     *
     * @param value the pressure value in at
     * @return value in Pascal
     */
    @Override
    public double convertToBaseUnit(double value)
    {
        return value * 98066.5;
    }

    /**
     * Converts the value from base unit (Pascal) to at.
     *
     * @param value the pressure value in Pascal
     * @return value in at
     */
    @Override
    public double convertFromBaseUnit(double value)
    {
        return value / 98066.5;
    }
}