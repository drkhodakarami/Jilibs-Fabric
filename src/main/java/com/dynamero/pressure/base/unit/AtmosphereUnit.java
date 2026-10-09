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
 * Standard Atmosphere (atm) pressure unit implementation.
 */
@Developer("TheMentor")
@CreatedAt("2026-01-12")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
public class AtmosphereUnit implements PressureUnit
{
    /**
     * The default constructor for the class
     */
    public AtmosphereUnit()
    {
    }

    /**
     * Returns the name of this unit.
     *
     * @return "Atmosphere"
     */
    @Override
    public String getUnitName() {
        return "Atmosphere";
    }

    /**
     * Returns the symbol of this unit.
     *
     * @return "atm"
     */
    @Override
    public String getUnitSymbol() {
        return "atm";
    }

    /**
     * Converts the Atmosphere value to base unit (Pascal).
     *
     * @param value the pressure value in Atmosphere
     * @return value in Pascal
     */
    @Override
    public double convertToBaseUnit(double value) {
        return value * 101_325;
    }

    /**
     * Converts the value from base unit (Pascal) to Atmosphere.
     *
     * @param value the pressure value in Pascal
     * @return value in Atmosphere
     */
    @Override
    public double convertFromBaseUnit(double value) {
        return value / 101_325;
    }
}