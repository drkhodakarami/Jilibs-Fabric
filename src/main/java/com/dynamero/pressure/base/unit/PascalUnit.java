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
 * Pascal (Pa) base pressure unit implementation.
 */
@Developer("TheMentor")
@CreatedAt("2026-01-12")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
public class PascalUnit implements PressureUnit
{
    /**
     * Returns the name of this unit.
     *
     * @return "Pascal"
     */
    @Override
    public String getUnitName() {
        return "Pascal";
    }

    /**
     * Returns the symbol of this unit.
     *
     * @return "Pa"
     */
    @Override
    public String getUnitSymbol() {
        return "Pa";
    }

    /**
     * Converts the Pascal value to base unit (identity conversion).
     *
     * @param value the pressure value in Pascal
     * @return value in Pascal
     */
    @Override
    public double convertToBaseUnit(double value) {
        return value;
    }

    /**
     * Converts the value from base unit to Pascal (identity conversion).
     *
     * @param value the pressure value in base unit
     * @return value in Pascal
     */
    @Override
    public double convertFromBaseUnit(double value) {
        return value;
    }
}