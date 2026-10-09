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

package com.dynamero.dispersion.base.storage;

import java.util.Objects;

import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import org.jetbrains.annotations.NotNull;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import com.dynamero.dispersion.base.interfaces.DispersionVariant;
import com.dynamero.dispersion.base.records.DispersionStackPayload;
import com.dynamero.heat.base.HeatComponent;
import com.dynamero.heat.base.interfaces.HeatUnit;
import com.dynamero.pressure.base.PressureComponent;
import com.dynamero.pressure.base.interfaces.PressureUnit;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.constants.BEKeys;

/**
 * Abstract single-variant dispersion storage implementation providing thermal heat and pneumatic pressure state management,
 * value persistence, and payload extraction.
 */
@SuppressWarnings("unused")
@Developer("TheMentor")
@CreatedAt("2026-01-12")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
public abstract class SingleDispersionStorage extends SingleVariantStorage<@NotNull DispersionVariant>
{
    /**
     * Heat component tracking dispersion temperature and thermal units.
     */
    private HeatComponent heatComponent = HeatComponent.ROOM;

    /**
     * Pressure component tracking dispersion pressure and pneumatic units.
     */
    private PressureComponent pressureComponent = PressureComponent.ATMOSPHERE;

    /**
     * Creates an anonymous SingleDispersionStorage instance with a fixed capacity and change callback.
     *
     * @param capacity the maximum capacity in droplets
     * @param onChange the callback invoked upon transaction final commit
     * @return a new SingleDispersionStorage instance
     */
    public static SingleDispersionStorage withFixedAmount(long capacity, Runnable onChange)
    {
        StoragePreconditions.notNegative(capacity);
        Objects.requireNonNull(onChange, "onChange may not be null");

        return new SingleDispersionStorage()
        {
            @Override
            protected long getCapacity(@NotNull DispersionVariant variant)
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

    //region HEAT
    /**
     * Sets the heat component of the stored dispersion.
     *
     * @param component the heat component to set
     */
    public void setHeatComponent(HeatComponent component)
    {
        setHeatUnit(component.getUnit());
        setHeat(component.getHeat());
    }

    /**
     * Retrieves the heat component of the stored dispersion.
     *
     * @return the heat component
     */
    public HeatComponent getHeatComponent()
    {
        return heatComponent;
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
     * Sets the temperature value of the stored dispersion.
     *
     * @param heat the heat amount to set
     */
    public void setHeat(double heat)
    {
        heatComponent.setHeat(heat);
    }

    /**
     * Retrieves the temperature value of the stored dispersion.
     *
     * @return the heat amount
     */
    public double getHeat()
    {
        return heatComponent.getHeat();
    }
    //endregion

    //region PRESSURE
    /**
     * Sets the pressure component of the stored dispersion.
     *
     * @param component the pressure component to set
     */
    public void setPressureComponent(PressureComponent component)
    {
        setPressureUnit(component.getUnit());
        setPressure(component.getPressure());
    }

    /**
     * Retrieves the pressure component of the stored dispersion.
     *
     * @return the pressure component
     */
    public PressureComponent getPressureComponent()
    {
        return pressureComponent;
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
     * Sets the pressure value of the stored dispersion.
     *
     * @param pressure the pressure amount to set
     */
    public void setPressure(double pressure)
    {
        pressureComponent.setPressure(pressure);
    }

    /**
     * Retrieves the pressure value of the stored dispersion.
     *
     * @return the pressure amount
     */
    public double getPressure()
    {
        return pressureComponent.getPressure();
    }
    //endregion

    /**
     * Returns the canonical blank dispersion variant.
     *
     * @return blank dispersion variant
     */
    @Override
    protected @NotNull DispersionVariant getBlankVariant()
    {
        return DispersionVariant.blank();
    }

    /**
     * Deserializes dispersion storage data along with heat and pressure components from the given value input.
     *
     * @param view the value input to read from
     */
    public void readData(ValueInput view)
    {
        SingleVariantStorage.readValue(this, DispersionVariant.CODEC, DispersionVariant::blank, view);

        heatComponent = view.read("be." + BEKeys.DISPERSION_STORAGE + "." + BEKeys.HEAT_COMPONENT, HeatComponent.CODEC).orElse(HeatComponent.ROOM);
        pressureComponent = view.read("be." + BEKeys.DISPERSION_STORAGE + "." + BEKeys.PRESSURE_COMPONENT, PressureComponent.CODEC).orElse(PressureComponent.ATMOSPHERE);
    }

    /**
     * Serializes dispersion storage data along with heat and pressure components to the given value output.
     *
     * @param view the value output to write to
     */
    public void writeData(ValueOutput view)
    {
        SingleVariantStorage.writeValue(this, DispersionVariant.CODEC, view);

        view.store("be." + BEKeys.DISPERSION_STORAGE + "." + BEKeys.HEAT_COMPONENT, HeatComponent.CODEC, heatComponent);
        view.store("be." + BEKeys.DISPERSION_STORAGE + "." + BEKeys.PRESSURE_COMPONENT, PressureComponent.CODEC, pressureComponent);
    }

    /**
     * Updates the stored dispersion variant and amount from a dispersion stack payload.
     *
     * @param stack the payload containing variant and droplet amount
     */
    public void setDispersion(DispersionStackPayload stack)
    {
        variant = stack.variant();
        amount = stack.amount();
    }

    /**
     * Constructs a {@link DispersionStackPayload} capturing the current variant and stored amount.
     *
     * @return the dispersion stack payload
     */
    public DispersionStackPayload getDispersionPayload()
    {
        return new DispersionStackPayload(variant, amount);
    }
}