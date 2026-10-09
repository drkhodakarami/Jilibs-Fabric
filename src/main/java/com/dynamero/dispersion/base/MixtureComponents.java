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

package com.dynamero.dispersion.base;

import com.dynamero.dispersion.base.enumerations.MixtureComponentType;
import com.dynamero.dispersion.base.records.MixtureComponent;
import com.dynamero.heat.base.interfaces.HeatUnit;
import com.dynamero.shared.annotations.*;

/**
 * Standard registry and repository of pre-configured {@link MixtureComponent} instances
 * grouped into solid, liquid, and gaseous phases with their physical and thermodynamic properties.
 */
@SuppressWarnings("unused")
@Developer("TheMentor")
@CreatedAt("2026-01-12")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
public class MixtureComponents
{
    /**
     * Pre-configured solid mixture components.
     */
    public static class Solid
    {
        /** Salt mixture component. */
        public static final MixtureComponent SALT;
        /** Sugar mixture component. */
        public static final MixtureComponent SUGAR;
        /** Sand mixture component. */
        public static final MixtureComponent SAND;
        /** Ruby mixture component. */
        public static final MixtureComponent RUBY;
        /** Sapphire mixture component. */
        public static final MixtureComponent SAPPHIRE;
        /** Citrine mixture component. */
        public static final MixtureComponent CITRINE;
        /** Diamond mixture component. */
        public static final MixtureComponent DIAMOND;
        /** Emerald mixture component. */
        public static final MixtureComponent EMERALD;
        /** Solid iron mixture component. */
        public static final MixtureComponent IRON;
        /** Solid gold mixture component. */
        public static final MixtureComponent GOLD;
        /** Solid silver mixture component. */
        public static final MixtureComponent SILVER;
        /** Solid bronze mixture component. */
        public static final MixtureComponent BRONZE;
        /** Solid tin mixture component. */
        public static final MixtureComponent TIN;
        /** Solid zinc mixture component. */
        public static final MixtureComponent ZINC;
        /** Solid copper mixture component. */
        public static final MixtureComponent COPPER;
        /** Solid lead mixture component. */
        public static final MixtureComponent LEAD;
        /** Solid aluminium mixture component. */
        public static final MixtureComponent ALUMINIUM;
        /** Stone mixture component. */
        public static final MixtureComponent STONE;
        /** Netherite mixture component. */
        public static final MixtureComponent NETHERITE;
        /** Obsidian mixture component. */
        public static final MixtureComponent OBSIDIAN;
        /** Glass mixture component. */
        public static final MixtureComponent GLASS;

        static
        {
            OBSIDIAN = new MixtureComponent.Builder("Obsidian")
                    .heatUnit(HeatUnit.KELVIN)
                    .molarMass(201.8)                                       // Molecular weight of obsidian in g/mol
                    .specificThermalCapacity(753)                   // Specific heat capacity in J/g*K
                    .boilingPoint(2200.15)                               // Boiling point in K (1927°C)
                    .meltingPoint(1728.15)                               // Melting point in K (1455°C)
                    .thermalConductivity(1.6)                     // Thermal conductivity in W/m*K
                    .electricalConductivity(1e-9)              // Electrical conductivity in S/m
                    .diffusivity(1e-7)                                    // Diffusivity in m^2/s
                    .viscosity(0.0)                                         // Viscosity in Pa*s
                    .density(2650)                                            // Density in kg/m^3
                    .types(MixtureComponentType.SOLID, MixtureComponentType.PARTICLE)
                    .build();

            STONE = new MixtureComponent.Builder("Stone")
                    .heatUnit(HeatUnit.KELVIN)
                    .molarMass(176.12)                                      // Molecular weight of silicate rocks in g/mol
                    .specificThermalCapacity(790)                   // Specific heat capacity in J/g*K
                    .boilingPoint(3050.15)                               // Boiling point in K (2777°C)
                    .meltingPoint(1648.15)                               // Melting point in K (1375°C)
                    .thermalConductivity(2.1)                     // Thermal conductivity in W/m*K
                    .electricalConductivity(1e-9)              // Electrical conductivity in S/m
                    .diffusivity(1e-8)                                    // Diffusivity in m^2/s
                    .viscosity(0.0)                                         // Viscosity in Pa*s
                    .density(2700)                                            // Density in kg/m^3
                    .types(MixtureComponentType.SOLID, MixtureComponentType.PARTICLE)
                    .build();

            GLASS = new MixtureComponent.Builder("Glass")
                    .heatUnit(HeatUnit.KELVIN)
                    .molarMass(60.08)                                       // Molecular weight of SiO2 in g/mol
                    .specificThermalCapacity(840)                   // Specific heat capacity in J/g*K
                    .boilingPoint(1750.15)                               // Boiling point in K (1477°C)
                    .meltingPoint(1673.15)                               // Melting point in K (1400°C)
                    .thermalConductivity(1.05)                    // Thermal conductivity in W/m*K
                    .electricalConductivity(1e-9)              // Electrical conductivity in S/m
                    .diffusivity(2e-7)                                    // Diffusivity in m^2/s
                    .viscosity(0.0)                                         // Viscosity in Pa*s
                    .density(2500)                                            // Density in kg/m^3
                    .types(MixtureComponentType.SOLID, MixtureComponentType.PARTICLE)
                    .build();

            IRON = new MixtureComponent.Builder("Iron")
                    .heatUnit(HeatUnit.KELVIN)
                    .molarMass(55.845)                                      // Molecular weight of Fe in g/mol
                    .specificThermalCapacity(450)                   // Specific heat capacity in J/g*K
                    .boilingPoint(3134.15)                               // Boiling point in K (2861°C)
                    .meltingPoint(1811.15)                               // Melting point in K (1538°C)
                    .thermalConductivity(80.2)                    // Thermal conductivity in W/m*K
                    .electricalConductivity(9.94e7)            // Electrical conductivity in S/m
                    .diffusivity(1.3e-6)                                  // Diffusivity in m^2/s
                    .viscosity(0.0)                                         // Viscosity in Pa*s
                    .density(7874)                                            // Density in kg/m^3
                    .types(MixtureComponentType.SOLID, MixtureComponentType.PARTICLE)
                    .build();

            GOLD = new MixtureComponent.Builder("Gold")
                    .heatUnit(HeatUnit.KELVIN)
                    .molarMass(196.97)                                      // Molecular weight of Au in g/mol
                    .specificThermalCapacity(129)                   // Specific heat capacity in J/g*K
                    .boilingPoint(3124.15)                               // Boiling point in K (2851°C)
                    .meltingPoint(1337.73)                               // Melting point in K (1064.18°C)
                    .thermalConductivity(319.3)                   // Thermal conductivity in W/m*K
                    .electricalConductivity(4.2e7)             // Electrical conductivity in S/m
                    .diffusivity(3.5e-6)                                  // Diffusivity in m^2/s
                    .viscosity(0.0)                                         // Viscosity in Pa*s
                    .density(19300)                                           // Density in kg/m^3
                    .types(MixtureComponentType.SOLID, MixtureComponentType.PARTICLE)
                    .build();

            BRONZE = new MixtureComponent.Builder("Bronze")
                    .heatUnit(HeatUnit.KELVIN)
                    .molarMass(83.2)                                        // Molecular weight of bronze (Cu-Sn alloy) in g/mol
                    .specificThermalCapacity(370)                   // Specific heat capacity in J/g*K
                    .boilingPoint(1650.15)                               // Boiling point in K (1377°C)
                    .meltingPoint(1220.15)                               // Melting point in K (947°C)
                    .thermalConductivity(89)                      // Thermal conductivity in W/m*K
                    .electricalConductivity(1.6e7)             // Electrical conductivity in S/m
                    .diffusivity(2.5e-6)                                  // Diffusivity in m^2/s
                    .viscosity(0.0)                                         // Viscosity in Pa*s
                    .density(8930)                                            // Density in kg/m^3
                    .types(MixtureComponentType.SOLID, MixtureComponentType.PARTICLE)
                    .build();

            SILVER = new MixtureComponent.Builder("Silver")
                    .heatUnit(HeatUnit.KELVIN)
                    .molarMass(107.868)                                     // Molecular weight of Ag in g/mol
                    .specificThermalCapacity(234)                   // Specific heat capacity in J/g*K
                    .boilingPoint(2595.15)                               // Boiling point in K (2322°C)
                    .meltingPoint(1234.93)                               // Melting point in K (961.78°C)
                    .thermalConductivity(429)                     // Thermal conductivity in W/m*K
                    .electricalConductivity(6.3e7)             // Electrical conductivity in S/m
                    .diffusivity(1.7e-6)                                  // Diffusivity in m^2/s
                    .viscosity(0.0)                                         // Viscosity in Pa*s
                    .density(10490)                                           // Density in kg/m^3
                    .types(MixtureComponentType.SOLID, MixtureComponentType.PARTICLE)
                    .build();

            TIN = new MixtureComponent.Builder("Tin")
                    .heatUnit(HeatUnit.KELVIN)
                    .molarMass(118.71)                                      // Molecular weight of Sn in g/mol
                    .specificThermalCapacity(230)                   // Specific heat capacity in J/g*K
                    .boilingPoint(2856.15)                               // Boiling point in K (2583°C)
                    .meltingPoint(505.08)                                // Melting point in K (231.93°C)
                    .thermalConductivity(67)                      // Thermal conductivity in W/m*K
                    .electricalConductivity(8.9e6)             // Electrical conductivity in S/m
                    .diffusivity(4.5e-6)                                  // Diffusivity in m^2/s
                    .viscosity(0.0)                                         // Viscosity in Pa*s
                    .density(7310)                                            // Density in kg/m^3
                    .types(MixtureComponentType.SOLID, MixtureComponentType.PARTICLE)
                    .build();

            ZINC = new MixtureComponent.Builder("Zinc")
                    .heatUnit(HeatUnit.KELVIN)
                    .molarMass(65.38)                                       // Molecular weight of Zn in g/mol
                    .specificThermalCapacity(388)                   // Specific heat capacity in J/g*K
                    .boilingPoint(1240.15)                               // Boiling point in K (967°C)
                    .meltingPoint(692.68)                                // Melting point in K (419.53°C)
                    .thermalConductivity(116)                     // Thermal conductivity in W/m*K
                    .electricalConductivity(1.7e7)             // Electrical conductivity in S/m
                    .diffusivity(3.0e-6)                                  // Diffusivity in m^2/s
                    .viscosity(0.0)                                         // Viscosity in Pa*s
                    .density(7140)                                            // Density in kg/m^3
                    .types(MixtureComponentType.SOLID, MixtureComponentType.PARTICLE)
                    .build();

            COPPER = new MixtureComponent.Builder("Copper")
                    .heatUnit(HeatUnit.KELVIN)
                    .molarMass(63.546)                                      // Molecular weight of Cu in g/mol
                    .specificThermalCapacity(385)                   // Specific heat capacity in J/g*K
                    .boilingPoint(2835.15)                               // Boiling point in K (2562°C)
                    .meltingPoint(1357.77)                               // Melting point in K (1084.62°C)
                    .thermalConductivity(401)                     // Thermal conductivity in W/m*K
                    .electricalConductivity(5.9e7)             // Electrical conductivity in S/m
                    .diffusivity(1.3e-6)                                  // Diffusivity in m^2/s
                    .viscosity(0.0)                                         // Viscosity in Pa*s
                    .density(8940)                                            // Density in kg/m^3
                    .types(MixtureComponentType.SOLID, MixtureComponentType.PARTICLE)
                    .build();

            LEAD = new MixtureComponent.Builder("Lead")
                    .heatUnit(HeatUnit.KELVIN)
                    .molarMass(207.2)                                       // Molecular weight of Pb in g/mol
                    .specificThermalCapacity(128)                   // Specific heat capacity in J/g*K
                    .boilingPoint(2022.15)                               // Boiling point in K (1749°C)
                    .meltingPoint(600.61)                                // Melting point in K (327.46°C)
                    .thermalConductivity(34.7)                    // Thermal conductivity in W/m*K
                    .electricalConductivity(4.8e7)             // Electrical conductivity in S/m
                    .diffusivity(1.0e-6)                                  // Diffusivity in m^2/s
                    .viscosity(0.0)                                         // Viscosity in Pa*s
                    .density(11340)                                           // Density in kg/m^3
                    .types(MixtureComponentType.SOLID, MixtureComponentType.PARTICLE)
                    .build();

            ALUMINIUM = new MixtureComponent.Builder("Aluminum")
                    .heatUnit(HeatUnit.KELVIN)
                    .molarMass(26.9815)                                     // Molecular weight of Al in g/mol
                    .specificThermalCapacity(897)                   // Specific heat capacity in J/g*K
                    .boilingPoint(2740.15)                               // Boiling point in K (2467°C)
                    .meltingPoint(933.47)                                // Melting point in K (660.32°C)
                    .thermalConductivity(237)                     // Thermal conductivity in W/m*K
                    .electricalConductivity(38e7)              // Electrical conductivity in S/m
                    .diffusivity(1.5e-6)                                  // Diffusivity in m^2/s
                    .viscosity(0.0)                                         // Viscosity in Pa*s
                    .density(2700)                                            // Density in kg/m^3
                    .types(MixtureComponentType.SOLID, MixtureComponentType.PARTICLE)
                    .build();

            SALT = new MixtureComponent.Builder("Salt")
                    .heatUnit(HeatUnit.KELVIN)
                    .molarMass(58.44)                                       // Molecular weight of NaCl in g/mol
                    .specificThermalCapacity(879)                   // Specific heat capacity in J/g*K
                    .boilingPoint(309.15)                                // Boiling point in K (80°C)
                    .meltingPoint(283.15)                                // Melting point in K (10°C)
                    .thermalConductivity(0.802)                   // Thermal conductivity in W/m*K
                    .electricalConductivity(4.76e-8)           // Electrical conductivity in S/m
                    .diffusivity(1.99e-9)                                 // Diffusivity in m^2/s
                    .viscosity(15.4)                                        // Viscosity in Pa*s
                    .density(2170)                                            // Density in kg/m^3
                    .types(MixtureComponentType.SOLUTE, MixtureComponentType.DISPERSE_PHASE, MixtureComponentType.SOLID, MixtureComponentType.PARTICLE)
                    .build();

            SUGAR = new MixtureComponent.Builder("Sugar")
                    .heatUnit(HeatUnit.KELVIN)
                    .molarMass(342.3)                                       // Molecular weight of C12H22O11 in g/mol
                    .specificThermalCapacity(1650)                  // Specific heat capacity in J/g*K
                    .boilingPoint(487.15)                                // Boiling point in K (214°C)
                    .meltingPoint(589.15)                                // Melting point in K (316°C)
                    .thermalConductivity(0.16)                    // Thermal conductivity in W/m*K
                    .electricalConductivity(5e-7)              // Electrical conductivity in S/m
                    .diffusivity(2e-9)                                    // Diffusivity in m^2/s
                    .viscosity(0.0)                                         // Viscosity in Pa*s
                    .density(1586)                                            // Density in kg/m^3
                    .types(MixtureComponentType.SOLUTE, MixtureComponentType.DISPERSE_PHASE, MixtureComponentType.SOLID, MixtureComponentType.PARTICLE)
                    .build();

            SAND = new MixtureComponent.Builder("Sand")
                    .heatUnit(HeatUnit.KELVIN)
                    .molarMass(60.08)                                       // Molecular weight of SiO2 in g/mol
                    .specificThermalCapacity(710)                   // Specific heat capacity in J/g*K
                    .boilingPoint(2355.15)                               // Boiling point in K (2082°C)
                    .meltingPoint(1923.15)                               // Melting point in K (1650°C)
                    .thermalConductivity(0.71)                    // Thermal conductivity in W/m*K
                    .electricalConductivity(2e-8)              // Electrical conductivity in S/m
                    .diffusivity(1e-9)                                    // Diffusivity in m^2/s
                    .viscosity(0.0)                                         // Viscosity in Pa*s
                    .density(2650)                                            // Density in kg/m^3
                    .types(MixtureComponentType.SOLID, MixtureComponentType.PARTICLE)
                    .build();

            RUBY = new MixtureComponent.Builder("Ruby")
                    .heatUnit(HeatUnit.KELVIN)
                    .molarMass(176.12)                                      // Molecular weight of Al2O3 in g/mol
                    .specificThermalCapacity(709)                   // Specific heat capacity in J/g*K
                    .boilingPoint(4515.15)                               // Boiling point in K (4242°C)
                    .meltingPoint(2287.15)                               // Melting point in K (2014°C)
                    .thermalConductivity(23.6)                    // Thermal conductivity in W/m*K
                    .electricalConductivity(1e-7)              // Electrical conductivity in S/m
                    .diffusivity(1e-9)                                    // Diffusivity in m^2/s
                    .viscosity(0.0)                                         // Viscosity in Pa*s
                    .density(3985)                                            // Density in kg/m^3
                    .types(MixtureComponentType.SOLID, MixtureComponentType.PARTICLE)
                    .build();

            SAPPHIRE = new MixtureComponent.Builder("Sapphire")
                    .heatUnit(HeatUnit.KELVIN)
                    .molarMass(176.12)                                      // Molecular weight of Al2O3 in g/mol
                    .specificThermalCapacity(712)                   // Specific heat capacity in J/g*K
                    .boilingPoint(4515.15)                               // Boiling point in K (4242°C)
                    .meltingPoint(2045.15)                               // Melting point in K (1772°C)
                    .thermalConductivity(38.5)                    // Thermal conductivity in W/m*K
                    .electricalConductivity(1e-7)              // Electrical conductivity in S/m
                    .diffusivity(1e-9)                                    // Diffusivity in m^2/s
                    .viscosity(0.0)                                         // Viscosity in Pa*s
                    .density(3985)                                            // Density in kg/m^3
                    .types(MixtureComponentType.SOLID, MixtureComponentType.PARTICLE)
                    .build();

            CITRINE = new MixtureComponent.Builder("Citrine")
                    .heatUnit(HeatUnit.KELVIN)
                    .molarMass(176.12)                                      // Molecular weight of SiO2 in g/mol
                    .specificThermalCapacity(703)                   // Specific heat capacity in J/g*K
                    .boilingPoint(4515.15)                               // Boiling point in K (4242°C)
                    .meltingPoint(1685.15)                               // Melting point in K (1412°C)
                    .thermalConductivity(0.93)                    // Thermal conductivity in W/m*K
                    .electricalConductivity(1e-7)              // Electrical conductivity in S/m
                    .diffusivity(1e-9)                                    // Diffusivity in m^2/s
                    .viscosity(0.0)                                         // Viscosity in Pa*s
                    .density(3435)                                            // Density in kg/m^3
                    .types(MixtureComponentType.SOLID, MixtureComponentType.PARTICLE)
                    .build();

            DIAMOND = new MixtureComponent.Builder("Diamond")
                    .heatUnit(HeatUnit.KELVIN)
                    .molarMass(12.01)                                       // Molecular weight of C in g/mol
                    .specificThermalCapacity(509)                   // Specific heat capacity in J/g*K
                    .boilingPoint(3825.15)                               // Boiling point in K (3552°C)
                    .meltingPoint(3825.15)                               // Melting point in K (3552°C)
                    .thermalConductivity(2200)                    // Thermal conductivity in W/m*K
                    .electricalConductivity(1e-7)              // Electrical conductivity in S/m
                    .diffusivity(1e-9)                                    // Diffusivity in m^2/s
                    .viscosity(0.0)                                         // Viscosity in Pa*s
                    .density(3514)                                            // Density in kg/m^3
                    .types(MixtureComponentType.SOLID, MixtureComponentType.PARTICLE)
                    .build();

            EMERALD = new MixtureComponent.Builder("Emerald")
                    .heatUnit(HeatUnit.KELVIN)
                    .molarMass(176.12)                                      // Molecular weight of Be3Al2Si6O18 in g/mol
                    .specificThermalCapacity(710)                   // Specific heat capacity in J/g*K
                    .boilingPoint(4515.15)                               // Boiling point in K (4242°C)
                    .meltingPoint(2797.15)                               // Melting point in K (2524°C)
                    .thermalConductivity(8.3)                     // Thermal conductivity in W/m*K
                    .electricalConductivity(1e-7)              // Electrical conductivity in S/m
                    .diffusivity(1e-9)                                    // Diffusivity in m^2/s
                    .viscosity(0.0)                                         // Viscosity in Pa*s
                    .density(3120)                                            // Density in kg/m^3
                    .types(MixtureComponentType.SOLID, MixtureComponentType.PARTICLE)
                    .build();

            NETHERITE = new MixtureComponent.Builder("Netherite")
                    .heatUnit(HeatUnit.KELVIN)
                    .molarMass(290.0)                                       // Molecular weight of netherite in g/mol
                    .specificThermalCapacity(800)                   // Specific heat capacity in J/g*K
                    .boilingPoint(4500.15)                               // Boiling point in K (4227°C)
                    .meltingPoint(3900.15)                               // Melting point in K (3627°C)
                    .thermalConductivity(35.0)                    // Thermal conductivity in W/m*K
                    .electricalConductivity(8e-9)              // Electrical conductivity in S/m
                    .diffusivity(1.4e-6)                                  // Diffusivity in m^2/s
                    .viscosity(0.0)                                         // Viscosity in Pa*s
                    .density(5000)                                            // Density in kg/m^3
                    .types(MixtureComponentType.SOLID, MixtureComponentType.PARTICLE)
                    .build();
        }
    }

    /**
     * Pre-configured liquid mixture components.
     */
    public static class Liquid
    {
        /** Water mixture component. */
        public static final MixtureComponent WATER;
        /** Oil mixture component. */
        public static final MixtureComponent OIL;
        /** Lava mixture component. */
        public static final MixtureComponent LAVA;
        /** Honey mixture component. */
        public static final MixtureComponent HONEY;
        /** Molten iron mixture component. */
        public static final MixtureComponent IRON;
        /** Molten gold mixture component. */
        public static final MixtureComponent GOLD;
        /** Molten silver mixture component. */
        public static final MixtureComponent SILVER;
        /** Molten bronze mixture component. */
        public static final MixtureComponent BRONZE;
        /** Molten tin mixture component. */
        public static final MixtureComponent TIN;
        /** Molten zinc mixture component. */
        public static final MixtureComponent ZINC;
        /** Molten copper mixture component. */
        public static final MixtureComponent COPPER;
        /** Molten lead mixture component. */
        public static final MixtureComponent LEAD;
        /** Molten aluminium mixture component. */
        public static final MixtureComponent ALUMINIUM;

        static
        {
            WATER = new MixtureComponent.Builder("Water")
                    .heatUnit(HeatUnit.KELVIN)
                    .molarMass(18.015)                                      // Molecular weight of H2O in g/mol
                    .specificThermalCapacity(4.186)                 // Specific heat capacity in J/g*K
                    .boilingPoint(373.15)                                // Boiling point in K (100°C)
                    .meltingPoint(273.15)                                // Melting point in K (0°C)
                    .thermalConductivity(0.598)                   // Thermal conductivity in W/m*K
                    .electricalConductivity(5.54e-6)           // Electrical conductivity in S/m
                    .diffusivity(7.92e-9)                                 // Diffusivity in m^2/s
                    .viscosity(1.002e-3)                                    // Viscosity in Pa*s (at room temperature)
                    .density(997)                                             // Density in kg/m^3  (at room temperature)
                    .types(MixtureComponentType.SOLVENT, MixtureComponentType.CONTINUOUS_PHASE, MixtureComponentType.LIQUID)
                    .build();

            OIL = new MixtureComponent.Builder("Oil")
                    .heatUnit(HeatUnit.KELVIN)
                    .molarMass(142.28)                                      // Molecular weight of C9H18 in g/mol
                    .specificThermalCapacity(1700)                  // Specific heat capacity in J/g*K
                    .boilingPoint(350.65)                                // Boiling point in K (77°C)
                    .meltingPoint(289.45)                                // Melting point in K (-1.5°C)
                    .thermalConductivity(0.14)                    // Thermal conductivity in W/m*K
                    .electricalConductivity(1e-7)              // Electrical conductivity in S/m
                    .diffusivity(2.3e-9)                                  // Diffusivity in m^2/s
                    .viscosity(0.028)                                       // Viscosity in Pa*s (at room temperature)
                    .density(916)                                             // Density in kg/m^3  (at room temperature)
                    .types(MixtureComponentType.DISPERSE_PHASE, MixtureComponentType.LIQUID, MixtureComponentType.DROPLET)
                    .build();

            LAVA = new MixtureComponent.Builder("Lava")
                    .heatUnit(HeatUnit.KELVIN)
                    .molarMass(176.12)                                      // Average molecular weight of silicate rocks in g/mol
                    .specificThermalCapacity(840)                   // Specific heat capacity in J/g*K
                    .boilingPoint(2355.15)                               // Boiling point in K (2082°C)
                    .meltingPoint(1185.15)                               // Melting point in K (912°C)
                    .thermalConductivity(2.2)                     // Thermal conductivity in W/m*K
                    .electricalConductivity(1e-7)              // Electrical conductivity in S/m
                    .diffusivity(1e-6)                                    // Diffusivity in m^2/s
                    .viscosity(5.9e-3)                                      // Viscosity in Pa*s (at 912°C)
                    .density(2400)                                            // Density in kg/m^3  (at 912°C)
                    .types(MixtureComponentType.CONTINUOUS_PHASE, MixtureComponentType.LIQUID, MixtureComponentType.DROPLET)
                    .build();

            IRON = new MixtureComponent.Builder("Molten.Iron")
                    .heatUnit(HeatUnit.KELVIN)
                    .molarMass(55.845)                                      // Average molecular weight of Fe in g/mol
                    .specificThermalCapacity(449)                   // Specific heat capacity in J/g*K
                    .boilingPoint(3134.15)                               // Boiling point in K (2861°C)
                    .meltingPoint(1811.15)                               // Melting point in K (1538°C)
                    .thermalConductivity(79.5)                    // Thermal conductivity in W/m*K
                    .electricalConductivity(2.0e6)             // Electrical conductivity in S/m
                    .diffusivity(4.4e-6)                                  // Diffusivity in m^2/s
                    .viscosity(1.78e-5)                                     // Viscosity in Pa*s (at 1600°C)
                    .density(7000)                                            // Density in kg/m^3  (at 1600°C)
                    .types(MixtureComponentType.CONTINUOUS_PHASE, MixtureComponentType.LIQUID)
                    .build();

            GOLD = new MixtureComponent.Builder("Molten.Gold")
                    .heatUnit(HeatUnit.KELVIN)
                    .molarMass(196.97)                                      // Average molecular weight of Au in g/mol
                    .specificThermalCapacity(124)                   // Specific heat capacity in J/g*K
                    .boilingPoint(3124.15)                               // Boiling point in K (2851°C)
                    .meltingPoint(1337.73)                               // Melting point in K (1064.18°C)
                    .thermalConductivity(317)                     // Thermal conductivity in W/m*K
                    .electricalConductivity(2.5e6)             // Electrical conductivity in S/m
                    .diffusivity(4.8e-6)                                  // Diffusivity in m^2/s
                    .viscosity(1.79e-5)                                     // Viscosity in Pa*s (at 1064°C)
                    .density(17300)                                           // Density in kg/m^3  (at 1064°C)
                    .types(MixtureComponentType.CONTINUOUS_PHASE, MixtureComponentType.LIQUID)
                    .build();

            BRONZE = new MixtureComponent.Builder("Molten.Bronze")
                    .heatUnit(HeatUnit.KELVIN)
                    .molarMass(83.2)                                        // Average molecular weight of bronze (Cu-Sn alloy) in g/mol
                    .specificThermalCapacity(365)                   // Specific heat capacity in J/g*K
                    .boilingPoint(1650.15)                               // Boiling point in K (1377°C)
                    .meltingPoint(1220.15)                               // Melting point in K (947°C)
                    .thermalConductivity(87)                      // Thermal conductivity in W/m*K
                    .electricalConductivity(1.3e6)             // Electrical conductivity in S/m
                    .diffusivity(3.0e-6)                                  // Diffusivity in m^2/s
                    .viscosity(8.5e-5)                                      // Viscosity in Pa*s (at 947°C)
                    .density(7800)                                            // Density in kg/m^3  (at 947°C)
                    .types(MixtureComponentType.CONTINUOUS_PHASE, MixtureComponentType.LIQUID)
                    .build();

            SILVER = new MixtureComponent.Builder("Molten.Silver")
                    .heatUnit(HeatUnit.KELVIN)
                    .molarMass(107.868)                                     // Average molecular weight of Ag in g/mol
                    .specificThermalCapacity(230)                   // Specific heat capacity in J/g*K
                    .boilingPoint(2595.15)                               // Boiling point in K (2322°C)
                    .meltingPoint(1234.93)                               // Melting point in K (961.78°C)
                    .thermalConductivity(425)                     // Thermal conductivity in W/m*K
                    .electricalConductivity(3.0e6)             // Electrical conductivity in S/m
                    .diffusivity(2.0e-6)                                  // Diffusivity in m^2/s
                    .viscosity(1.5e-5)                                      // Viscosity in Pa*s (at 962°C)
                    .density(9300)                                            // Density in kg/m^3  (at 962°C)
                    .types(MixtureComponentType.CONTINUOUS_PHASE, MixtureComponentType.LIQUID)
                    .build();

            TIN = new MixtureComponent.Builder("Molten.Tin")
                    .heatUnit(HeatUnit.KELVIN)
                    .molarMass(118.71)                                      // Average molecular weight of Sn in g/mol
                    .specificThermalCapacity(220)                   // Specific heat capacity in J/g*K
                    .boilingPoint(2856.15)                               // Boiling point in K (2583°C)
                    .meltingPoint(505.08)                                // Melting point in K (231.93°C)
                    .thermalConductivity(65)                      // Thermal conductivity in W/m*K
                    .electricalConductivity(4.0e6)             // Electrical conductivity in S/m
                    .diffusivity(5.0e-6)                                  // Diffusivity in m^2/s
                    .viscosity(1.8e-5)                                      // Viscosity in Pa*s (at 232°C)
                    .density(7000)                                            // Density in kg/m^3  (at 232°C)
                    .types(MixtureComponentType.CONTINUOUS_PHASE, MixtureComponentType.LIQUID)
                    .build();

            ZINC = new MixtureComponent.Builder("Molten.Zinc")
                    .heatUnit(HeatUnit.KELVIN)
                    .molarMass(65.38)                                       // Average molecular weight of Zn in g/mol
                    .specificThermalCapacity(370)                   // Specific heat capacity in J/g*K
                    .boilingPoint(1240.15)                               // Boiling point in K (967°C)
                    .meltingPoint(692.68)                                // Melting point in K (419.53°C)
                    .thermalConductivity(112)                     // Thermal conductivity in W/m*K
                    .electricalConductivity(1.0e6)             // Electrical conductivity in S/m
                    .diffusivity(3.5e-6)                                  // Diffusivity in m^2/s
                    .viscosity(4.8e-5)                                      // Viscosity in Pa*s (at 420°C)
                    .density(6570)                                            // Density in kg/m^3  (at 420°C)
                    .types(MixtureComponentType.CONTINUOUS_PHASE, MixtureComponentType.LIQUID)
                    .build();

            COPPER = new MixtureComponent.Builder("Molten.Copper")
                    .heatUnit(HeatUnit.KELVIN)
                    .molarMass(63.546)                                      // Average molecular weight of Cu in g/mol
                    .specificThermalCapacity(380)                   // Specific heat capacity in J/g*K
                    .boilingPoint(2835.15)                               // Boiling point in K (2562°C)
                    .meltingPoint(1357.77)                               // Melting point in K (1084.62°C)
                    .thermalConductivity(397)                     // Thermal conductivity in W/m*K
                    .electricalConductivity(3.0e6)             // Electrical conductivity in S/m
                    .diffusivity(1.5e-6)                                  // Diffusivity in m^2/s
                    .viscosity(1.4e-5)                                      // Viscosity in Pa*s (at 1085°C)
                    .density(7900)                                            // Density in kg/m^3  (at 1085°C)
                    .types(MixtureComponentType.CONTINUOUS_PHASE, MixtureComponentType.LIQUID)
                    .build();

            LEAD = new MixtureComponent.Builder("Molten.Lead")
                    .heatUnit(HeatUnit.KELVIN)
                    .molarMass(207.2)                                       // Average molecular weight of Pb in g/mol
                    .specificThermalCapacity(130)                   // Specific heat capacity in J/g*K
                    .boilingPoint(2022.15)                               // Boiling point in K (1749°C)
                    .meltingPoint(600.61)                                // Melting point in K (327.46°C)
                    .thermalConductivity(33)                      // Thermal conductivity in W/m*K
                    .electricalConductivity(3.5e6)             // Electrical conductivity in S/m
                    .diffusivity(1.2e-6)                                  // Diffusivity in m^2/s
                    .viscosity(4.8e-5)                                      // Viscosity in Pa*s (at 327°C)
                    .density(10500)                                           // Density in kg/m^3  (at 327°C)
                    .types(MixtureComponentType.CONTINUOUS_PHASE, MixtureComponentType.LIQUID)
                    .build();

            ALUMINIUM = new MixtureComponent.Builder("Molten.Aluminum")
                    .heatUnit(HeatUnit.KELVIN)
                    .molarMass(26.9815)                                     // Average molecular weight of Al in g/mol
                    .specificThermalCapacity(870)                   // Specific heat capacity in J/g*K
                    .boilingPoint(2740.15)                               // Boiling point in K (2467°C)
                    .meltingPoint(933.47)                                // Melting point in K (660.32°C)
                    .thermalConductivity(233)                     // Thermal conductivity in W/m*K
                    .electricalConductivity(2.5e6)             // Electrical conductivity in S/m
                    .diffusivity(1.8e-6)                                  // Diffusivity in m^2/s
                    .viscosity(3.8e-5)                                      // Viscosity in Pa*s (at 660°C)
                    .density(2400)                                            // Density in kg/m^3  (at 660°C)
                    .types(MixtureComponentType.CONTINUOUS_PHASE, MixtureComponentType.LIQUID)
                    .build();

            HONEY = new MixtureComponent.Builder("Honey")
                    .heatUnit(HeatUnit.KELVIN)
                    .molarMass(180.16)                                      // Average molecular weight of honey in g/mol
                    .specificThermalCapacity(3150)                  // Specific heat capacity in J/g*K
                    .boilingPoint(420.15)                                // Boiling point in K (147°C)
                    .meltingPoint(319.15)                                // Melting point in K (46°C)
                    .thermalConductivity(0.5)                     // Thermal conductivity in W/m*K
                    .electricalConductivity(2e-8)              // Electrical conductivity in S/m
                    .diffusivity(1.3e-9)                                  // Diffusivity in m^2/s
                    .viscosity(4700)                                        // Viscosity in Pa*s (at 25°C)
                    .density(1420)                                            // Density in kg/m^3  (at 25°C)
                    .types(MixtureComponentType.CONTINUOUS_PHASE, MixtureComponentType.LIQUID)
                    .build();
        }
    }

    /**
     * Pre-configured gaseous mixture components.
     */
    public static class Gas
    {
        /** Steam mixture component. */
        public static final MixtureComponent STEAM;
        /** Oxygen mixture component. */
        public static final MixtureComponent OXYGEN;
        /** Nitrogen mixture component. */
        public static final MixtureComponent NITROGEN;
        /** Air mixture component. */
        public static final MixtureComponent AIR;

        static
        {
            STEAM = new MixtureComponent.Builder("Steam")
                    .heatUnit(HeatUnit.KELVIN)
                    .molarMass(18.015)                                      // Molecular weight of H2O in g/mol
                    .specificThermalCapacity(2030)                  // Specific heat capacity in J/g*K
                    .boilingPoint(373.15)                                // Boiling point in K (100°C)
                    .meltingPoint(273.15)                                // Melting point in K (0°C)
                    .thermalConductivity(0.026)                   // Thermal conductivity in W/m*K
                    .electricalConductivity(1e-8)              // Electrical conductivity in S/m
                    .diffusivity(2.45e-5)                                 // Diffusivity in m^2/s
                    .viscosity(9.73e-6)                                     // Viscosity in Pa*s (at 298 K)
                    .density(0.598)                                           // Density in kg/m^3  (at 298 K, 1 atm)
                    .types(MixtureComponentType.GAS, MixtureComponentType.BUBBLE)
                    .build();

            OXYGEN = new MixtureComponent.Builder("Oxygen")
                    .heatUnit(HeatUnit.KELVIN)
                    .molarMass(32.0)                                        // Molecular weight of O2 in g/mol
                    .specificThermalCapacity(918)                   // Specific heat capacity in J/g*K
                    .boilingPoint(90.2)                                  // Boiling point in K (-182.95°C)
                    .meltingPoint(54.36)                                 // Melting point in K (-218.79°C)
                    .thermalConductivity(2.6e-2)                  // Thermal conductivity in W/m*K
                    .electricalConductivity(3e-8)              // Electrical conductivity in S/m
                    .diffusivity(1.07e-5)                                 // Diffusivity in m^2/s
                    .viscosity(1.46e-5)                                     // Viscosity in Pa*s (at 298 K)
                    .density(1.33)                                            // Density in kg/m^3  (at 298 K, 1 atm)
                    .types(MixtureComponentType.GAS, MixtureComponentType.BUBBLE)
                    .build();

            NITROGEN = new MixtureComponent.Builder("Nitrogen")
                    .heatUnit(HeatUnit.KELVIN)
                    .molarMass(28.0)                                        // Molecular weight of N2 in g/mol
                    .specificThermalCapacity(1040)                  // Specific heat capacity in J/g*K
                    .boilingPoint(77.36)                                 // Boiling point in K (-195.79°C)
                    .meltingPoint(63.15)                                 // Melting point in K (-210.04°C)
                    .thermalConductivity(2.5e-2)                  // Thermal conductivity in W/m*K
                    .electricalConductivity(1e-8)              // Electrical conductivity in S/m
                    .diffusivity(1.79e-5)                                 // Diffusivity in m^2/s
                    .viscosity(1.83e-5)                                     // Viscosity in Pa*s (at 298 K)
                    .density(1.25)                                            // Density in kg/m^3  (at 298 K, 1 atm)
                    .types(MixtureComponentType.GAS, MixtureComponentType.BUBBLE)
                    .build();

            AIR = new MixtureComponent.Builder("Air")
                    .heatUnit(HeatUnit.KELVIN)
                    .molarMass(28.97)                                       // Molecular weight of Air in g/mol
                    .specificThermalCapacity(1005)                  // Specific heat capacity in J/g*K
                    .boilingPoint(273.15)                                // Boiling point in K (0°C, approx.)
                    .meltingPoint(273.15)                                // Melting point in K (0°C, approx.)
                    .thermalConductivity(2.6e-2)                  // Thermal conductivity in W/m*K
                    .electricalConductivity(1e-8)              // Electrical conductivity in S/m
                    .diffusivity(1.94e-5)                                 // Diffusivity in m^2/s
                    .viscosity(1.73e-5)                                     // Viscosity in Pa*s (at 298 K)
                    .density(1.225)                                           // Density in kg/m^3  (at 298 K, 1 atm)
                    .types(MixtureComponentType.GAS, MixtureComponentType.BUBBLE)
                    .build();
        }
    }
}