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

package com.dynamero.gas;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.PatchedDataComponentMap;

import com.dynamero.gas.base.interfaces.GasVariant;
import com.dynamero.gas.base.records.Gas;
import com.dynamero.heat.base.HeatComponent;
import com.dynamero.heat.base.interfaces.HeatUnit;
import com.dynamero.pressure.base.PressureComponent;
import com.dynamero.shared.annotations.*;

/**
 * Immutable implementation of {@link GasVariant} storing a gas definition, data components patch,
 * thermodynamic heat, and pneumatic pressure, with thermodynamic calculations.
 */
@SuppressWarnings("unused")
@Developer("TurtyWurty")
@CreatedAt("2026-08-10")
@ModifiedAt("2026-08-10")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public final class GasVariantImpl implements GasVariant
{
    /**
     * Factory method creating a new GasVariantImpl from a gas definition and component parameters.
     *
     * @param gas               the gas definition
     * @param componentPatch    the data component patch
     * @param heatComponent     the thermodynamic heat component
     * @param pressureComponent the pneumatic pressure component
     * @return a new GasVariantImpl instance
     * @throws IllegalArgumentException if heat units mismatch between gas physics and heat component
     */
    public static GasVariant of(Gas gas, DataComponentPatch componentPatch, HeatComponent heatComponent, PressureComponent pressureComponent)
    {
        Objects.requireNonNull(gas, "Gas may not be null.");
        Objects.requireNonNull(componentPatch, "Data Components may not be null.");
        Objects.requireNonNull(heatComponent, "Heat Component may not be null.");

        if(heatComponent.getUnit() != gas.heatPhysics().unit())
            throw new IllegalArgumentException("Different heat units between gas and variant is not allowed gas unit: " +
                                            gas.heatPhysics().unit().getUnitName() + " / variant: " + heatComponent.getUnit().getUnitName());

        return new GasVariantImpl(gas, componentPatch, heatComponent, pressureComponent);
    }

    /**
     * Factory method creating a new GasVariantImpl from a gas registry holder.
     *
     * @param gas               the gas holder
     * @param componentPatch    the data component patch
     * @param heatComponent     the thermodynamic heat component
     * @param pressureComponent the pneumatic pressure component
     * @return a new GasVariantImpl instance
     */
    public static GasVariant of(Holder<Gas> gas, DataComponentPatch componentPatch, HeatComponent heatComponent, PressureComponent pressureComponent)
    {
        Objects.requireNonNull(gas, "Gas may not be null.");

        return of(gas.value(), componentPatch, heatComponent, pressureComponent);
    }

    /**
     * The gas definition.
     */
    private final Gas gas;

    /**
     * Data component patch containing customized component data.
     */
    private final DataComponentPatch components;

    /**
     * Resolved data component map.
     */
    private final DataComponentMap componentMap;

    /**
     * Thermodynamic heat component.
     */
    private final HeatComponent heatComponent;

    /**
     * Pneumatic pressure component.
     */
    private final PressureComponent pressureComponent;

    /**
     * Precomputed hash code.
     */
    private final int hashCode;

    /**
     * Private constructor initializing fields and precomputing hash code.
     *
     * @param gas               the gas definition
     * @param components        the data component patch
     * @param heatComponent     the heat component
     * @param pressureComponent the pressure component
     */
    private GasVariantImpl(Gas gas, DataComponentPatch components, HeatComponent heatComponent, PressureComponent pressureComponent)
    {
        this.gas = gas;
        this.components = components;
        this.heatComponent = heatComponent;
        this.pressureComponent = pressureComponent;
        this.componentMap = components == DataComponentPatch.EMPTY ? DataComponentMap.EMPTY : PatchedDataComponentMap.fromPatch(DataComponentMap.EMPTY, components);
        this.hashCode = Objects.hash(gas, components);
    }

    /**
     * Checks whether this variant represents the blank (empty) gas.
     *
     * @return {@code true} if empty, {@code false} otherwise
     */
    @Override
    public boolean isBlank()
    {
        return this.gas.matchesType(Gas.EMPTY);
    }

    /**
     * Retrieves the underlying gas definition object.
     *
     * @return the gas
     */
    @Override
    public @NotNull Gas getObject()
    {
        return this.gas;
    }

    /**
     * Retrieves the resolved data component map.
     *
     * @return the data component map
     */
    @Override
    public @NotNull DataComponentMap getComponents()
    {
        return this.componentMap;
    }

    /**
     * Retrieves the data component patch.
     *
     * @return the data component patch
     */
    @Override
    public @NotNull DataComponentPatch getComponentsPatch()
    {
        return this.components;
    }

    /**
     * Retrieves the thermodynamic heat component.
     *
     * @return the heat component
     */
    @Override
    public HeatComponent getHeatComponent()
    {
        return heatComponent;
    }

    /**
     * Retrieves the temperature value of this gas variant.
     *
     * @return the temperature value
     */
    @Override
    public double getTemperature()
    {
        return heatComponent.getHeat();
    }

    /**
     * Retrieves the temperature measurement unit.
     *
     * @return the heat unit
     */
    @Override
    public HeatUnit getHeatUnit()
    {
        return heatComponent.getUnit();
    }

    /**
     * Retrieves the pneumatic pressure component.
     *
     * @return the pressure component
     */
    @Override
    public PressureComponent getPressureComponent()
    {
        return pressureComponent;
    }

    // Thermodynamic Calculations

    /**
     * Calculates gas volume using the ideal gas law: V = (nRT) / P.
     *
     * @param n number of moles
     * @return volume in cubic meters
     */
    @Override
    public double calculateVolume(double n)
    {
        // PV = nRT => V = (nRT) / P
        return (n * GasVariant.GAS_CONSTANT * this.gas.heatPhysics().unit().convertToBaseUnit(this.heatComponent.getHeat())) / pressureComponent.getPressure();
    }

    /**
     * Calculates gas pressure using the ideal gas law: P = (nRT) / V.
     *
     * @param n number of moles
     * @param V volume in cubic meters
     * @return pressure in pascals
     */
    @Override
    public double calculatePressure(double n, double V)
    {
        // PV = nRT => P = (nRT) / V
        return (n * GasVariant.GAS_CONSTANT * this.gas.heatPhysics().unit().convertToBaseUnit(this.heatComponent.getHeat())) / V;
    }

    /**
     * Calculates gas temperature using the ideal gas law: T = (PV) / (nR).
     *
     * @param n number of moles
     * @param V volume in cubic meters
     * @return temperature in configured heat unit
     */
    @Override
    public double calculateTemperature(double n, double V)
    {
        // PV = nRT => T = (PV) / (nR)
        return this.gas.heatPhysics().unit().convertFromBaseUnit((pressureComponent.getPressure() * V) / (n * GasVariant.GAS_CONSTANT));
    }

    /**
     * Calculates the substance amount in moles using the ideal gas law: n = (PV) / (RT).
     *
     * @param V volume in cubic meters
     * @return substance amount in moles
     */
    @Override
    public double calculateAmountOfSubstance(double V)
    {
        // PV = nRT => n = (PV) / (RT)
        return (pressureComponent.getPressure() * V) / (GasVariant.GAS_CONSTANT * this.gas.heatPhysics().unit().convertToBaseUnit(this.heatComponent.getHeat()));
    }

    /**
     * Returns a string representation of this gas variant.
     *
     * @return string representation
     */
    @Override
    public String toString()
    {
        return "GasVariantImpl{" +
            "gas=" + gas +
            ", components=" + components +
            '}';
    }

    /**
     * Checks equality between this gas variant and another object.
     *
     * @param obj the object to compare against
     * @return {@code true} if equal, {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj)
    {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;

        GasVariantImpl that = (GasVariantImpl) obj;
        return this.hashCode == that.hashCode && this.gas.matchesType(that.gas) && componentsMatch(that.components);
    }

    /**
     * Computes the hash code for this gas variant.
     *
     * @return precomputed hash code
     */
    @Override
    public int hashCode()
    {
        return this.hashCode;
    }
}