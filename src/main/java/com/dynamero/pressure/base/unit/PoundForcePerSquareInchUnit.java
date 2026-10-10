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
 * Pound-force per square inch (psi) pressure unit implementation.
 */
@Developer("TheMentor")
@CreatedAt("2026-10-08")
@ModifiedAt("2026-10-08")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public class PoundForcePerSquareInchUnit implements PressureUnit
{
    /**
     * Returns the name of this unit.
     *
     * @return "Pound Force per Square Inch"
     */
    @Override
    public String getUnitName()
    {
        return "Pound Force per Square Inch";
    }

    /**
     * Returns the symbol of this unit.
     *
     * @return "psi"
     */
    @Override
    public String getUnitSymbol()
    {
        return "psi";
    }

    /**
     * Converts the psi value to base unit (Pascal).
     *
     * @param value the pressure value in psi
     * @return value in Pascal
     */
    @Override
    public double convertToBaseUnit(double value)
    {
        return value * 6894.76;
    }

    /**
     * Converts the value from base unit (Pascal) to psi.
     *
     * @param value the pressure value in Pascal
     * @return value in psi
     */
    @Override
    public double convertFromBaseUnit(double value)
    {
        return value / 6894.76;
    }
}