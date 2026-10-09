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
 * Torr (torr) pressure unit implementation.
 */
@SuppressWarnings("unused")
@Developer("TheMentor")
@CreatedAt("2026-01-12")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
public class TorrUnit implements PressureUnit
{
    /**
     * Returns the name of this unit.
     *
     * @return "Torr"
     */
    @Override
    public String getUnitName()
    {
        return "Torr";
    }

    /**
     * Returns the symbol of this unit.
     *
     * @return "torr"
     */
    @Override
    public String getUnitSymbol()
    {
        return "torr";
    }

    /**
     * Converts the Torr value to base unit (Pascal).
     *
     * @param value the pressure value in Torr
     * @return value in Pascal
     */
    @Override
    public double convertToBaseUnit(double value)
    {
        return value * 133.322;
    }

    /**
     * Converts the value from base unit (Pascal) to Torr.
     *
     * @param value the pressure value in Pascal
     * @return value in Torr
     */
    @Override
    public double convertFromBaseUnit(double value)
    {
        return value / 133.322;
    }
}