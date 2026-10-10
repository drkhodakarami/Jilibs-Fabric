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

package com.dynamero.dispersion.base.records;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.dynamero.dispersion.base.enumerations.MixtureComponentType;
import com.dynamero.heat.base.interfaces.HeatUnit;
import com.dynamero.heat.base.records.HeatPhysics;
import com.dynamero.shared.annotations.*;

/**
 * Physical component definition within a chemical mixture or dispersion,
 * detailing thermodynamic, electrical, kinetic, and transport properties.
 *
 * @param name                   the component name
 * @param molarMass              molecular weight in g/mol
 * @param heatPhysics            thermal physics properties record
 * @param boilingPoint           boiling point in Kelvin
 * @param meltingPoint           melting point in Kelvin
 * @param electricalConductivity electrical conductivity in S/m
 * @param diffusivity            mass diffusivity in m²/s
 * @param viscosity              dynamic viscosity in Pa·s
 * @param density                mass density in kg/m³
 * @param types                  set of role and phase classifications
 */
@SuppressWarnings("unused")
@Developer("TheMentor")
@CreatedAt("2026-10-08")
@ModifiedAt("2026-10-08")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public record MixtureComponent(String name,
                            double molarMass,  // in g/mol
                            HeatPhysics heatPhysics,
                            double boilingPoint,  // in K
                            double meltingPoint,  // in K
                            double electricalConductivity,  // in S/m
                            double diffusivity,  // in m^2/s
                            double viscosity,  // in Pa*s
                            double density,  // in kg/m^3
                            Set<MixtureComponentType> types)
{
    /**
     * Creates a new MixtureComponent with the specified name, amount, and types.
     *
     * @param name   the name of the component
     * @param molarMass the molecular weight of the component
     * @param heatPhysics the heat physics used for the component, default unit is Kelvin
     * @param boilingPoint the boiling point of the component
     * @param meltingPoint the melting point of the component
     * @param electricalConductivity the electrical conductivity of the component
     * @param diffusivity the diffusivity of the component
     * @param viscosity the viscosity of the component
     * @param density the density of the component
     * @param types  the set of types for the component
     */
    public MixtureComponent
    {
        if(name == null || name.isEmpty())
            throw new IllegalArgumentException("Name cannot be null or empty");

        if(molarMass < 0)
            throw new IllegalArgumentException("Molecular Weight cacnnot be negative");
        if (boilingPoint < 0)
            throw new IllegalArgumentException("Boiling point cannot be negative");
        if (meltingPoint < 0)
            throw new IllegalArgumentException("Melting point cannot be negative");
        if (electricalConductivity < 0)
            throw new IllegalArgumentException("Electrical conductivity cannot be negative");
        if (diffusivity < 0)
            throw new IllegalArgumentException("Diffusivity cannot be negative");
        if (viscosity < 0)
            throw new IllegalArgumentException("Viscosity cannot be negative");
        if (density < 0)
            throw new IllegalArgumentException("Density cannot be negative");

        Objects.requireNonNull(heatPhysics, "Heat Physics cannot be null");
        Objects.requireNonNull(types, "Types cannot be null");

        if(types.isEmpty())
            throw new IllegalArgumentException("Types set cannot be empty");
    }

    /**
     * Checks if the component is of a specific type.
     *
     * @param type the type to check
     * @return true if the component has the specified type, false otherwise
     */
    public boolean hasType(MixtureComponentType type)
    {
        return types.contains(type);
    }

    /**
     * Checks if the component is a solute.
     *
     * @return true if the component is a solute, false otherwise
     */
    public boolean isSolute()
    {
        return hasType(MixtureComponentType.SOLUTE);
    }

    /**
     * Checks if the component is a solvent.
     *
     * @return true if the component is a solvent, false otherwise
     */
    public boolean isSolvent()
    {
        return hasType(MixtureComponentType.SOLVENT);
    }

    /**
     * Checks if the component is part of the disperse phase.
     *
     * @return true if the component is part of the disperse phase, false otherwise
     */
    public boolean isDispersePhase()
    {
        return hasType(MixtureComponentType.DISPERSE_PHASE);
    }

    /**
     * Checks if the component is part of the continuous phase.
     *
     * @return true if the component is part of the continuous phase, false otherwise
     */
    public boolean isContinuousPhase()
    {
        return hasType(MixtureComponentType.CONTINUOUS_PHASE);
    }

    /**
     * Checks if the component is a particle.
     *
     * @return true if the component is a particle, false otherwise
     */
    public boolean isParticle()
    {
        return hasType(MixtureComponentType.PARTICLE);
    }

    /**
     * Checks if the component is a droplet.
     *
     * @return true if the component is a droplet, false otherwise
     */
    public boolean isDroplet()
    {
        return hasType(MixtureComponentType.DROPLET);
    }

    /**
     * Checks if the component is a bubble.
     *
     * @return true if the component is a bubble, false otherwise
     */
    public boolean isBubble()
    {
        return hasType(MixtureComponentType.BUBBLE);
    }

    /**
     * Checks if the component is a solid.
     *
     * @return true if the component is a solid, false otherwise
     */
    public boolean isSolid()
    {
        return hasType(MixtureComponentType.SOLID);
    }

    /**
     * Checks if the component is a liquid.
     *
     * @return true if the component is a liquid, false otherwise
     */
    public boolean isLiquid()
    {
        return hasType(MixtureComponentType.LIQUID);
    }

    /**
     * Checks if the component is a gel.
     *
     * @return true if the component is a gel, false otherwise
     */
    public boolean isGel()
    {
        return hasType(MixtureComponentType.GEL);
    }

    /**
     * Checks if the component is a polymer.
     *
     * @return true if the component is a polymer, false otherwise
     */
    public boolean isPolymer()
    {
        return hasType(MixtureComponentType.POLYMER);
    }

    /**
     * Checks if the component is a colloid.
     *
     * @return true if the component is a colloid, false otherwise
     */
    public boolean isColloid()
    {
        return hasType(MixtureComponentType.COLLOID);
    }

    /**
     * Builder helper class for configuring and creating {@link MixtureComponent} instances.
     */
    public static class Builder
    {
        /**
         * The component name.
         */
        private final String name;

        /**
         * The molar mass in g/mol.
         */
        private double molarMass;  // in g/mol

        /**
         * Thermal physics properties record.
         */
        private HeatPhysics heatPhysics;

        /**
         * Boiling point in Kelvin.
         */
        private double boilingPoint;  // in K

        /**
         * Melting point in Kelvin.
         */
        private double meltingPoint;  // in K

        /**
         * Electrical conductivity in S/m.
         */
        private double electricalConductivity;  // in S/m

        /**
         * Mass diffusivity in m²/s.
         */
        private double diffusivity;  // in m^2/s

        /**
         * Dynamic viscosity in Pa·s.
         */
        private double viscosity;  // in Pa*s

        /**
         * Mass density in kg/m³.
         */
        private double density;  // in kg/m^3

        /**
         * Classification types set.
         */
        private Set<MixtureComponentType> types = new HashSet<>();

        /**
         * Constructs a Builder with the specified component name.
         *
         * @param name the component name
         */
        public Builder(String name)
        {
            this.name = name;
        }

        /**
         * Sets the heat measurement unit for thermal calculations.
         *
         * @param heatUnit the heat unit
         * @return this builder
         */
        public Builder heatUnit(HeatUnit heatUnit)
        {
            Objects.requireNonNull(heatUnit, "Heat Unit cannot be null");

            this.heatPhysics = new HeatPhysics(heatUnit, heatPhysics.specificThermalCapacity(), heatPhysics.thermalConductivity());
            return this;
        }

        /**
         * Sets the molar mass in grams per mole.
         *
         * @param molarMass the molar mass in g/mol
         * @return this builder
         */
        public Builder molarMass(double molarMass)
        {
            this.molarMass = molarMass;
            return this;
        }

        /**
         * Sets the specific thermal capacity of the component.
         *
         * @param specificThermalCapacity the specific heat capacity in J/(g·K)
         * @return this builder
         * @throws IllegalArgumentException if capacity is negative
         */
        public Builder specificThermalCapacity(double specificThermalCapacity)
        {
            if(specificThermalCapacity < 0)
                throw new IllegalArgumentException("Specific Heat Capacity cacnnot be negative");

            this.heatPhysics  = new HeatPhysics(heatPhysics.unit(), specificThermalCapacity, heatPhysics.thermalConductivity());
            return this;
        }

        /**
         * Sets the boiling point temperature in Kelvin.
         *
         * @param boilingPoint boiling point in K
         * @return this builder
         */
        public Builder boilingPoint(double boilingPoint)
        {
            this.boilingPoint = boilingPoint;
            return this;
        }

        /**
         * Sets the melting point temperature in Kelvin.
         *
         * @param meltingPoint melting point in K
         * @return this builder
         */
        public Builder meltingPoint(double meltingPoint)
        {
            this.meltingPoint = meltingPoint;
            return this;
        }

        /**
         * Sets the thermal conductivity in W/(m·K).
         *
         * @param thermalConductivity thermal conductivity
         * @return this builder
         * @throws IllegalArgumentException if conductivity is negative
         */
        public Builder thermalConductivity(double thermalConductivity)
        {
            if (thermalConductivity < 0)
                throw new IllegalArgumentException("Thermal conductivity cannot be negative");

            this.heatPhysics  = new HeatPhysics(heatPhysics.unit(), heatPhysics.specificThermalCapacity(), thermalConductivity);
            return this;
        }

        /**
         * Sets the electrical conductivity in S/m.
         *
         * @param electricalConductivity electrical conductivity
         * @return this builder
         */
        public Builder electricalConductivity(double electricalConductivity)
        {
            this.electricalConductivity = electricalConductivity;
            return this;
        }

        /**
         * Sets the mass diffusivity in m²/s.
         *
         * @param diffusivity diffusivity
         * @return this builder
         */
        public Builder diffusivity(double diffusivity)
        {
            this.diffusivity = diffusivity;
            return this;
        }

        /**
         * Sets the dynamic viscosity in Pa·s.
         *
         * @param viscosity viscosity
         * @return this builder
         */
        public Builder viscosity(double viscosity)
        {
            this.viscosity = viscosity;
            return this;
        }

        /**
         * Sets the mass density in kg/m³.
         *
         * @param density density
         * @return this builder
         */
        public Builder density(double density)
        {
            this.density = density;
            return this;
        }

        /**
         * Adds additional classification types to the component.
         *
         * @param additionalTypes additional types
         * @return this builder
         */
        public Builder additionalTypes(MixtureComponentType... additionalTypes)
        {
            Set<MixtureComponentType> newTypes = new HashSet<>(types);
            newTypes.addAll(Arrays.asList(additionalTypes));
            this.types = newTypes;
            return this;
        }

        /**
         * Sets the classification types set.
         *
         * @param types the types set
         * @return this builder
         * @throws IllegalArgumentException if types set is null or empty
         */
        public Builder types(Set<MixtureComponentType> types)
        {
            if(types == null || types.isEmpty())
                throw new IllegalArgumentException("New types set cannot be empty");

            this.types = types;
            return this;
        }

        /**
         * Sets the classification types from an array of types.
         *
         * @param types array of types
         * @return this builder
         * @throws IllegalArgumentException if types array is null or empty
         */
        public Builder types(MixtureComponentType... types)
        {
            if(types == null || types.length == 0)
                throw new IllegalArgumentException("New types list cannot be null or empty");

            this.types = new HashSet<>(Arrays.asList(types));
            return this;
        }

        /**
         * Builds a new {@link MixtureComponent} instance using configured properties.
         *
         * @return the new MixtureComponent instance
         */
        public MixtureComponent build()
        {
            return new MixtureComponent(name, molarMass, heatPhysics, boilingPoint, meltingPoint,
                                        electricalConductivity, diffusivity, viscosity, density, types);
        }
    }
}