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

package com.dynamero.fluid.base;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import com.dynamero.heat.base.HeatComponent;
import com.dynamero.heat.base.interfaces.HeatUnit;
import com.dynamero.pressure.base.PressureComponent;
import com.dynamero.pressure.base.interfaces.PressureUnit;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.constants.BEKeys;

/**
 * Extended single fluid storage incorporating thermodynamic heat and pneumatic pressure components
 * alongside standard fluid variant and droplet capacity tracking.
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
public abstract class SingleFluidStorage extends net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage
{
    /**
     * Heat component tracking fluid temperature and thermal units.
     */
    private HeatComponent heatComponent = HeatComponent.ROOM;

    /**
     * Pressure component tracking fluid pressure and pneumatic units.
     */
    private PressureComponent pressureComponent = PressureComponent.ATMOSPHERE;

    //region HEAT COMPONENT
    /**
     * Retrieves the heat component of the stored fluid.
     *
     * @return the heat component
     */
    public HeatComponent getHeatComponent()
    {
        return heatComponent;
    }

    /**
     * Sets the heat component of the stored fluid.
     *
     * @param heatComponent the heat component to set
     */
    public void setHeatComponent(HeatComponent heatComponent)
    {
        this.heatComponent = heatComponent;
    }

    /**
     * Sets the unit of measurement for the heat component.
     *
     * @param unit the heat unit
     */
    public void setHeatUnit(HeatUnit unit)
    {
        heatComponent.setUnit(unit);
    }

    /**
     * Retrieves the current unit of measurement for the heat component.
     *
     * @return the heat unit
     */
    public HeatUnit getHeatUnit()
    {
        return heatComponent.getUnit();
    }

    /**
     * Sets the temperature value of the stored fluid.
     *
     * @param heat the heat amount to set
     */
    public void setHeat(double heat)
    {
        heatComponent.setHeat(heat);
    }

    /**
     * Retrieves the temperature value of the stored fluid.
     *
     * @return the heat amount
     */
    public double getHeat()
    {
        return heatComponent.getHeat();
    }
    //endregion

    //region PRESSURE COMPONENT
    /**
     * Retrieves the pressure component of the stored fluid.
     *
     * @return the pressure component
     */
    public PressureComponent getPressureComponent()
    {
        return pressureComponent;
    }

    /**
     * Sets the pressure component of the stored fluid.
     *
     * @param pressureComponent the pressure component to set
     */
    public void setPressureComponent(PressureComponent pressureComponent)
    {
        this.pressureComponent = pressureComponent;
    }

    /**
     * Sets the unit of measurement for the pressure component.
     *
     * @param unit the pressure unit
     */
    public void setPressureUnit(PressureUnit unit)
    {
        pressureComponent.setUnit(unit);
    }

    /**
     * Retrieves the current unit of measurement for the pressure component.
     *
     * @return the pressure unit
     */
    public PressureUnit getPressureUnit()
    {
        return pressureComponent.getUnit();
    }

    /**
     * Sets the pressure value of the stored fluid.
     *
     * @param heat the pressure amount to set
     */
    public void setPressure(double heat)
    {
        pressureComponent.setPressure(heat);
    }

    /**
     * Retrieves the pressure value of the stored fluid.
     *
     * @return the pressure amount
     */
    public double getPressure()
    {
        return pressureComponent.getPressure();
    }
    //endregion

    /**
     * Deserializes fluid storage data along with heat and pressure components from the given value input.
     *
     * @param value the value input to read from
     */
    @Override
    public void readValue(@NotNull ValueInput value)
    {
        super.readValue(value);
        heatComponent = value.read("be." + BEKeys.FLUID_STORAGE + "." + BEKeys.HEAT_COMPONENT, HeatComponent.CODEC).orElse(HeatComponent.ROOM);
        pressureComponent = value.read("be." + BEKeys.FLUID_STORAGE + "." + BEKeys.PRESSURE_COMPONENT, PressureComponent.CODEC).orElse(PressureComponent.ATMOSPHERE);
    }

    /**
     * Serializes fluid storage data along with heat and pressure components to the given value output.
     *
     * @param value the value output to write to
     */
    @Override
    public void writeValue(@NotNull ValueOutput value)
    {
        super.writeValue(value);
        value.store("be." + BEKeys.FLUID_STORAGE + "." + BEKeys.HEAT_COMPONENT, HeatComponent.CODEC, heatComponent);
        value.store("be." + BEKeys.FLUID_STORAGE + "." + BEKeys.PRESSURE_COMPONENT, PressureComponent.CODEC, pressureComponent);

    }

    /**
     * Updates the stored fluid variant and amount from a fluid component stack.
     *
     * @param stack the fluid component stack containing variant and droplet amount
     */
    public void setFluid(FluidComponent stack)
    {
        variant = stack.getFluid();
        amount = stack.getAmount();
    }

    /**
     * Constructs a {@link FluidComponent} payload capturing the current variant and stored amount.
     *
     * @return the fluid component payload
     */
    public FluidComponent getFluidPayload()
    {
        return new FluidComponent(variant, amount);
    }
}