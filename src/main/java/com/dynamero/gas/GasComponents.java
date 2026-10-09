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

import com.dynamero.gas.base.GasComponent;
import com.dynamero.gas.base.interfaces.GasVariant;
import com.dynamero.shared.annotations.*;

/**
 * Pre-defined constant {@link GasComponent} instances for standard gases in unit, block, bottle, and droplet volumes.
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
public class GasComponents
{
    /** Air gas component with unit volume. */
    public static GasComponent AIR_UNIT = new GasComponent(GasVariant.of(Gases.AIR), GasConstants.UNIT);
    /** Air gas component with block volume. */
    public static GasComponent AIR_BLOCK = new GasComponent(GasVariant.of(Gases.AIR), GasConstants.BLOCK);
    /** Air gas component with bottle volume. */
    public static GasComponent AIR_BOTTLE = new GasComponent(GasVariant.of(Gases.AIR), GasConstants.BOTTLE);
    /** Air gas component with droplet volume. */
    public static GasComponent AIR_DROPLET = new GasComponent(GasVariant.of(Gases.AIR), GasConstants.DROPLET);

    /** Oxygen gas component with unit volume. */
    public static GasComponent OXYGEN_UNIT = new GasComponent(GasVariant.of(Gases.OXYGEN), GasConstants.UNIT);
    /** Oxygen gas component with block volume. */
    public static GasComponent OXYGEN_BLOCK = new GasComponent(GasVariant.of(Gases.OXYGEN), GasConstants.BLOCK);
    /** Oxygen gas component with bottle volume. */
    public static GasComponent OXYGEN_BOTTLE = new GasComponent(GasVariant.of(Gases.OXYGEN), GasConstants.BOTTLE);
    /** Oxygen gas component with droplet volume. */
    public static GasComponent OXYGEN_DROPLET = new GasComponent(GasVariant.of(Gases.OXYGEN), GasConstants.DROPLET);

    /** Nitrogen gas component with unit volume. */
    public static GasComponent NITROGEN_UNIT = new GasComponent(GasVariant.of(Gases.NITROGEN), GasConstants.UNIT);
    /** Nitrogen gas component with block volume. */
    public static GasComponent NITROGEN_BLOCK = new GasComponent(GasVariant.of(Gases.NITROGEN), GasConstants.BLOCK);
    /** Nitrogen gas component with bottle volume. */
    public static GasComponent NITROGEN_BOTTLE = new GasComponent(GasVariant.of(Gases.NITROGEN), GasConstants.BOTTLE);
    /** Nitrogen gas component with droplet volume. */
    public static GasComponent NITROGEN_DROPLET = new GasComponent(GasVariant.of(Gases.NITROGEN), GasConstants.DROPLET);

    /** Argon gas component with unit volume. */
    public static GasComponent ARGON_UNIT = new GasComponent(GasVariant.of(Gases.ARGON), GasConstants.UNIT);
    /** Argon gas component with block volume. */
    public static GasComponent ARGON_BLOCK = new GasComponent(GasVariant.of(Gases.ARGON), GasConstants.BLOCK);
    /** Argon gas component with bottle volume. */
    public static GasComponent ARGON_BOTTLE = new GasComponent(GasVariant.of(Gases.ARGON), GasConstants.BOTTLE);
    /** Argon gas component with droplet volume. */
    public static GasComponent ARGON_DROPLET = new GasComponent(GasVariant.of(Gases.ARGON), GasConstants.DROPLET);

    /** Neon gas component with unit volume. */
    public static GasComponent NEON_UNIT = new GasComponent(GasVariant.of(Gases.NEON), GasConstants.UNIT);
    /** Neon gas component with block volume. */
    public static GasComponent NEON_BLOCK = new GasComponent(GasVariant.of(Gases.NEON), GasConstants.BLOCK);
    /** Neon gas component with bottle volume. */
    public static GasComponent NEON_BOTTLE = new GasComponent(GasVariant.of(Gases.NEON), GasConstants.BOTTLE);
    /** Neon gas component with droplet volume. */
    public static GasComponent NEON_DROPLET = new GasComponent(GasVariant.of(Gases.NEON), GasConstants.DROPLET);

    /** Helium gas component with unit volume. */
    public static GasComponent HELIUM_UNIT = new GasComponent(GasVariant.of(Gases.HELIUM), GasConstants.UNIT);
    /** Helium gas component with block volume. */
    public static GasComponent HELIUM_BLOCK = new GasComponent(GasVariant.of(Gases.HELIUM), GasConstants.BLOCK);
    /** Helium gas component with bottle volume. */
    public static GasComponent HELIUM_BOTTLE = new GasComponent(GasVariant.of(Gases.HELIUM), GasConstants.BOTTLE);
    /** Helium gas component with droplet volume. */
    public static GasComponent HELIUM_DROPLET = new GasComponent(GasVariant.of(Gases.HELIUM), GasConstants.DROPLET);

    /** Krypton gas component with unit volume. */
    public static GasComponent KRYPTON_UNIT = new GasComponent(GasVariant.of(Gases.KRYPTON), GasConstants.UNIT);
    /** Krypton gas component with block volume. */
    public static GasComponent KRYPTON_BLOCK = new GasComponent(GasVariant.of(Gases.KRYPTON), GasConstants.BLOCK);
    /** Krypton gas component with bottle volume. */
    public static GasComponent KRYPTON_BOTTLE = new GasComponent(GasVariant.of(Gases.KRYPTON), GasConstants.BOTTLE);
    /** Krypton gas component with droplet volume. */
    public static GasComponent KRYPTON_DROPLET = new GasComponent(GasVariant.of(Gases.KRYPTON), GasConstants.DROPLET);

    /** Xenon gas component with unit volume. */
    public static GasComponent XENON_UNIT = new GasComponent(GasVariant.of(Gases.XENON), GasConstants.UNIT);
    /** Xenon gas component with block volume. */
    public static GasComponent XENON_BLOCK = new GasComponent(GasVariant.of(Gases.XENON), GasConstants.BLOCK);
    /** Xenon gas component with bottle volume. */
    public static GasComponent XENON_BOTTLE = new GasComponent(GasVariant.of(Gases.XENON), GasConstants.BOTTLE);
    /** Xenon gas component with droplet volume. */
    public static GasComponent XENON_DROPLET = new GasComponent(GasVariant.of(Gases.XENON), GasConstants.DROPLET);

    /** Radon gas component with unit volume. */
    public static GasComponent RADON_UNIT = new GasComponent(GasVariant.of(Gases.RADON), GasConstants.UNIT);
    /** Radon gas component with block volume. */
    public static GasComponent RADON_BLOCK = new GasComponent(GasVariant.of(Gases.RADON), GasConstants.BLOCK);
    /** Radon gas component with bottle volume. */
    public static GasComponent RADON_BOTTLE = new GasComponent(GasVariant.of(Gases.RADON), GasConstants.BOTTLE);
    /** Radon gas component with droplet volume. */
    public static GasComponent RADON_DROPLET = new GasComponent(GasVariant.of(Gases.RADON), GasConstants.DROPLET);

    /** Carbon dioxide gas component with unit volume. */
    public static GasComponent CARBON_DIOXIDE_UNIT = new GasComponent(GasVariant.of(Gases.CARBON_DIOXIDE), GasConstants.UNIT);
    /** Carbon dioxide gas component with block volume. */
    public static GasComponent CARBON_DIOXIDE_BLOCK = new GasComponent(GasVariant.of(Gases.CARBON_DIOXIDE), GasConstants.BLOCK);
    /** Carbon dioxide gas component with bottle volume. */
    public static GasComponent CARBON_DIOXIDE_BOTTLE = new GasComponent(GasVariant.of(Gases.CARBON_DIOXIDE), GasConstants.BOTTLE);
    /** Carbon dioxide gas component with droplet volume. */
    public static GasComponent CARBON_DIOXIDE_DROPLET = new GasComponent(GasVariant.of(Gases.CARBON_DIOXIDE), GasConstants.DROPLET);

    /** Methane gas component with unit volume. */
    public static GasComponent METHANE_UNIT = new GasComponent(GasVariant.of(Gases.METHANE), GasConstants.UNIT);
    /** Methane gas component with block volume. */
    public static GasComponent METHANE_BLOCK = new GasComponent(GasVariant.of(Gases.METHANE), GasConstants.BLOCK);
    /** Methane gas component with bottle volume. */
    public static GasComponent METHANE_BOTTLE = new GasComponent(GasVariant.of(Gases.METHANE), GasConstants.BOTTLE);
    /** Methane gas component with droplet volume. */
    public static GasComponent METHANE_DROPLET = new GasComponent(GasVariant.of(Gases.METHANE), GasConstants.DROPLET);

    /** Propane gas component with unit volume. */
    public static GasComponent PROPANE_UNIT = new GasComponent(GasVariant.of(Gases.PROPANE), GasConstants.UNIT);
    /** Propane gas component with block volume. */
    public static GasComponent PROPANE_BLOCK = new GasComponent(GasVariant.of(Gases.PROPANE), GasConstants.BLOCK);
    /** Propane gas component with bottle volume. */
    public static GasComponent PROPANE_BOTTLE = new GasComponent(GasVariant.of(Gases.PROPANE), GasConstants.BOTTLE);
    /** Propane gas component with droplet volume. */
    public static GasComponent PROPANE_DROPLET = new GasComponent(GasVariant.of(Gases.PROPANE), GasConstants.DROPLET);

    /** Butane gas component with unit volume. */
    public static GasComponent BUTANE_UNIT = new GasComponent(GasVariant.of(Gases.BUTANE), GasConstants.UNIT);
    /** Butane gas component with block volume. */
    public static GasComponent BUTANE_BLOCK = new GasComponent(GasVariant.of(Gases.BUTANE), GasConstants.BLOCK);
    /** Butane gas component with bottle volume. */
    public static GasComponent BUTANE_BOTTLE = new GasComponent(GasVariant.of(Gases.BUTANE), GasConstants.BOTTLE);
    /** Butane gas component with droplet volume. */
    public static GasComponent BUTANE_DROPLET = new GasComponent(GasVariant.of(Gases.BUTANE), GasConstants.DROPLET);
}