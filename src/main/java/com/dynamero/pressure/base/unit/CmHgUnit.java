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
 * Centimetre of Mercury (cmHg) pressure unit implementation.
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
public class CmHgUnit implements PressureUnit
{
    /**
     * Returns the name of this unit.
     *
     * @return "Centimeter of Mercury"
     */
    @Override
    public String getUnitName()
    {
        return "Centimeter of Mercury";
    }

    /**
     * Returns the symbol of this unit.
     *
     * @return "cmHg"
     */
    @Override
    public String getUnitSymbol()
    {
        return "cmHg";
    }

    /**
     * Converts the cmHg value to base unit (Pascal).
     *
     * @param value the pressure value in cmHg
     * @return value in Pascal
     */
    @Override
    public double convertToBaseUnit(double value)
    {
        return value * 1333.22;
    }

    /**
     * Converts the value from base unit (Pascal) to cmHg.
     *
     * @param value the pressure value in Pascal
     * @return value in cmHg
     */
    @Override
    public double convertFromBaseUnit(double value)
    {
        return value / 1333.22;
    }
}