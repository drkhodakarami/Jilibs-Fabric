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

package com.dynamero.dispersion.base.enumerations;

import com.dynamero.shared.annotations.*;

/**
 * Enumeration of role and physical phase classifications for components in a dispersion mixture.
 */
@Developer("TheMentor")
@CreatedAt("2026-08-10")
@ModifiedAt("2026-08-10")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public enum MixtureComponentType
{
    /**
     * The substance that is dissolved in the solvent.
     * Example: Salt in Salt Water
     */
    SOLUTE,
    /**
     * The substance in which the solute is dissolved.
     * Example: Water in Salt Water
     */
    SOLVENT,
    /**
     * The phase being dispersed in a dispersion.
     * In solutions, this corresponds to the solute.
     * Example: Oil droplets in water-based smulsions.
     */
    DISPERSE_PHASE,
    /**
     * The phase that acts as the medium in which the disperse phase is dispersed.
     * In solutions, this corresponds to the solvent.
     * Example: Water in oil-based emulsions.
     */
    CONTINUOUS_PHASE,
    /**
     * Solid Particles (in suspensions).
     * Example: Clay particles in Muddy Water.
     */
    PARTICLE,
    /**
     * Small liquid droplets and particles (in emulsions).
     * Example: Oil droplets in milk.
     */
    DROPLET,
    /**
     * Gas bubbles dispersed in a liquid or solid.
     * Example: Bubbles in foam, Carbon Dioxide in Soda
     */
    BUBBLE,
    /**
     * Gas dispersed in a liquid or solid.
     * Example: Oxigen in Water
     */
    GAS,
    /**
     * Solid components tat are not dissolved but suspended in another medium.
     * Example: Mud particles in Muddy Water.
     */
    SOLID,
    /**
     * Liquid components that are dispersed within a gas or another liquid.
     * Example: Water droplets in Fog, oil droplets in emulsions.
     */
    LIQUID,
    /**
     * Gelatinous susbastances that can be part of the continuous phase or disperse phase.
     * Example: Gelatin in gelatin-based dispersions.
     */
    GEL,
    /**
     * Polymer susbastances that can be part of the continuous phase or disperse phase.
     * Example: Gelatin in gelatin-based dispersions.
     */
    POLYMER,
    /**
     * Particles that are larger than solute particles but smaller than suspended particles.
     * Example: Mucus in blood, Fog droplets in Air.
     */
    COLLOID
}