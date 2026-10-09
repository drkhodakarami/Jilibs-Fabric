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


import com.dynamero.gas.base.records.Gas;
import com.dynamero.shared.annotations.*;

/**
 * Standard pre-registered {@link Gas} instances with physical constants (molar mass, specific heat capacity, thermal conductivity).
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
public class Gases
{
    /** Air gas definition. */
    public static final Gas AIR = Gas.register(new Gas.Builder("air")
                                                .molarMass(28.97)
                                                .specificThermalCapacity(29.14)
                                                .thermalConductivity(0.026)
                                                .build());

    /** Oxygen gas definition. */
    public static final Gas OXYGEN = Gas.register(new Gas.Builder("oxygen")
                                                    .molarMass(32.0)
                                                    .specificThermalCapacity(29.14)
                                                    .thermalConductivity(0.0265)
                                                    .build());

    /** Nitrogen gas definition. */
    public static final Gas NITROGEN = Gas.register(new Gas.Builder("nitrogen")
                                                        .molarMass(28.02)
                                                        .specificThermalCapacity(29.12)
                                                        .thermalConductivity(0.0257)
                                                        .build());

    /** Argon gas definition. */
    public static final Gas ARGON = Gas.register(new Gas.Builder("argon")
                                                    .molarMass(39.95)
                                                    .specificThermalCapacity(20.80)
                                                    .thermalConductivity(0.0177)
                                                    .build());

    /** Neon gas definition. */
    public static final Gas NEON = Gas.register(new Gas.Builder("neon")
                                                    .molarMass(20.18)
                                                    .specificThermalCapacity(20.79)
                                                    .thermalConductivity(0.0491)
                                                    .build());

    /** Helium gas definition. */
    public static final Gas HELIUM = Gas.register(new Gas.Builder("helium")
                                                    .molarMass(4.0)
                                                    .specificThermalCapacity(5.193)
                                                    .thermalConductivity(0.1513)
                                                    .build());

    /** Krypton gas definition. */
    public static final Gas KRYPTON = Gas.register(new Gas.Builder("krypton")
                                                    .molarMass(83.798)
                                                    .specificThermalCapacity(24.86)
                                                    .thermalConductivity(0.0151)
                                                    .build());

    /** Xenon gas definition. */
    public static final Gas XENON = Gas.register(new Gas.Builder("xenon")
                                                    .molarMass(131.293)
                                                    .specificThermalCapacity(21.79)
                                                    .thermalConductivity(0.0147)
                                                    .build());

    /** Radon gas definition. */
    public static final Gas RADON = Gas.register(new Gas.Builder("radon")
                                                    .molarMass(222.018)
                                                    .specificThermalCapacity(93.62)
                                                    .thermalConductivity(0.00356)
                                                    .build());

    /** Carbon dioxide gas definition. */
    public static final Gas CARBON_DIOXIDE = Gas.register(new Gas.Builder("carbon_dioxide")
                                                        .molarMass(44.01)
                                                        .specificThermalCapacity(847.1)
                                                        .thermalConductivity(0.0158)
                                                        .build());

    /** Methane gas definition. */
    public static final Gas METHANE = Gas.register(new Gas.Builder("methane")
                                                        .molarMass(16.04)
                                                        .specificThermalCapacity(2.229)
                                                        .thermalConductivity(0.0372)
                                                        .build());

    /** Propane gas definition. */
    public static final Gas PROPANE = Gas.register(new Gas.Builder("propane")
                                                        .molarMass(44.10)
                                                        .specificThermalCapacity(186.5)
                                                        .thermalConductivity(0.0162)
                                                        .build());

    /** Butane gas definition. */
    public static final Gas BUTANE = Gas.register(new Gas.Builder("butane")
                                                        .molarMass(58.12)
                                                        .specificThermalCapacity(176.3)
                                                        .thermalConductivity(0.0147)
                                                        .build());
}