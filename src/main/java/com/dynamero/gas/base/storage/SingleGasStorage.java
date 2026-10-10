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

package com.dynamero.gas.base.storage;

import java.util.Objects;

import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import org.jetbrains.annotations.NotNull;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import com.dynamero.gas.base.GasComponent;
import com.dynamero.gas.base.interfaces.GasVariant;
import com.dynamero.heat.base.HeatComponent;
import com.dynamero.heat.base.interfaces.HeatUnit;
import com.dynamero.pressure.base.PressureComponent;
import com.dynamero.pressure.base.interfaces.PressureUnit;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.constants.BEKeys;

/**
 * Abstract single-variant gas storage implementation providing thermal heat and pneumatic pressure state management,
 * value persistence, and payload extraction.
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
public abstract class SingleGasStorage extends SingleVariantStorage<@NotNull GasVariant>
{
    /**
     * Heat component tracking gas temperature and thermal units.
     */
    private HeatComponent heatComponent = HeatComponent.ROOM;

    /**
     * Pressure component tracking gas pressure and pneumatic units.
     */
    private PressureComponent pressureComponent = PressureComponent.ATMOSPHERE;

    /**
     * Creates an anonymous SingleGasStorage instance with a fixed capacity and change callback.
     *
     * @param capacity the maximum capacity in droplets
     * @param onChange the callback invoked upon transaction final commit
     * @return a new SingleGasStorage instance
     */
    public static SingleGasStorage withFixedCapacity(long capacity, Runnable onChange)
    {
        StoragePreconditions.notNegative(capacity);
        Objects.requireNonNull(onChange, "onChange may not be null.");

        return new SingleGasStorage()
        {
            @Override
            protected long getCapacity(@NotNull GasVariant gasVariant)
            {
                return capacity;
            }

            @Override
            protected void onFinalCommit()
            {
                onChange.run();
            }
        };
    }

    //region HEAT COMPONENT
    /**
     * Retrieves the heat component of the stored gas.
     *
     * @return the heat component
     */
    public HeatComponent getHeatComponent()
    {
        return heatComponent;
    }

    /**
     * Sets the heat component of the stored gas.
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
     * Sets the temperature value of the stored gas.
     *
     * @param heat the heat amount to set
     */
    public void setHeat(double heat)
    {
        heatComponent.setHeat(heat);
    }

    /**
     * Retrieves the temperature value of the stored gas.
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
     * Retrieves the pressure component of the stored gas.
     *
     * @return the pressure component
     */
    public PressureComponent getPressureComponent()
    {
        return pressureComponent;
    }

    /**
     * Sets the pressure component of the stored gas.
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
     * Sets the pressure value of the stored gas.
     *
     * @param heat the pressure amount to set
     */
    public void setPressure(double heat)
    {
        pressureComponent.setPressure(heat);
    }

    /**
     * Retrieves the pressure value of the stored gas.
     *
     * @return the pressure amount
     */
    public double getPressure()
    {
        return pressureComponent.getPressure();
    }
    //endregion

    /**
     * Returns the canonical blank gas variant.
     *
     * @return blank gas variant
     */
    @Override
    protected @NotNull GasVariant getBlankVariant()
    {
        return GasVariant.blank();
    }

    /**
     * Deserializes gas storage data along with heat and pressure components from the given value input.
     *
     * @param value the value input to read from
     */
    public void readData(ValueInput value)
    {
        SingleVariantStorage.readValue(this, GasVariant.CODEC, GasVariant::blank, value);
        heatComponent = value.read("be." + BEKeys.GAS_STORAGE + "." + BEKeys.HEAT_COMPONENT, HeatComponent.CODEC).orElse(HeatComponent.ROOM);
        pressureComponent = value.read("be." + BEKeys.GAS_STORAGE + "." + BEKeys.PRESSURE_COMPONENT, PressureComponent.CODEC).orElse(PressureComponent.ATMOSPHERE);
    }

    /**
     * Serializes gas storage data along with heat and pressure components to the given value output.
     *
     * @param value the value output to write to
     */
    public void writeData(ValueOutput value)
    {
        SingleVariantStorage.writeValue(this, GasVariant.CODEC, value);
        value.store("be." + BEKeys.GAS_STORAGE + "." + BEKeys.HEAT_COMPONENT, HeatComponent.CODEC, heatComponent);
        value.store("be." + BEKeys.GAS_STORAGE + "." + BEKeys.PRESSURE_COMPONENT, PressureComponent.CODEC, pressureComponent);
    }

    /**
     * Updates the stored gas variant and amount from a gas component stack.
     *
     * @param stack the gas component stack containing variant and droplet amount
     */
    public void setGas(GasComponent stack)
    {
        variant = stack.getGas();
        amount = stack.getAmount();
    }

    /**
     * Constructs a {@link GasComponent} payload capturing the current variant and stored amount.
     *
     * @return the gas component payload
     */
    public GasComponent getGasPayload()
    {
        return new GasComponent(variant, amount);
    }
}