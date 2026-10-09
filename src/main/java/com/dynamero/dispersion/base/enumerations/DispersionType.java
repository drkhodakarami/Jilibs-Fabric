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
 * Categorical classification of dispersion mixture physical structures and phases.
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
public enum DispersionType
{
    /**
     * Homogeneous mixture where the solute is uniformly dispersed in the solvent.
     * Both are usually liquids, and the solute is typically dissolved in the solvent.
     * Example: Salt Water
     */
    SOLUTION,
    /**
     * Heterogeneous mixture whith solid particles (solute) suspended in a liquid or gas (solvent).
     * The particles can settle out over time if undisturbed.
     * Example: Paint, Muddy Water
     */
    SUSPENSION,
    /**
     * Colloidal dispersion of two immiscible liquids.
     * Droplets of one liquid are dispersed throughout another liquid.
     * Example: Milk, Mayonnaise
     */
    EMULSION,
    /**
     * Particles that are larger than solute particles in a solution but smaller than the particles in a suspension.
     * Intermediate between solutions and suspensions.
     * Examples: Mucus in blood, Fog droplets in Air, Gelatin
     */
    COLLOID,
    /**
     * Suspension of fine solid particles or liquid droplets in a gas.
     * Example: Smog, Clouds, Fog
     */
    AEROSOL,
    /**
     * Gas dispersed throughout a liquid or solid.
     * Consists of bubbles of gas surrounded by a film of liquid.
     * Example: Soup Foam, Whipped Cream
     */
    FOAM,
    /**
     * Solid-like substance composed of a liquid contained within a network of solid particles.
     * Often behaves like a colloid with properties of both solids and liquids.
     * Example: Jello, Mucus
     */
    GEL,
    /**
     * Thick, semi-solid mixture that is often viscous. Consists of fine particles suspended in a liquid medium.
     * Example: Toothpaste, Peanut Butter
     */
    PASTE,
    /**
     * Mixture where one solid is dispersed within another solid matrix.
     * Often used to improve solubility or stability.
     * Example: Amorphous Dispersions in Pharmaceuticals
     */
    SOLID,
    /**
     * Emulsion with droplet sizes ranging from 10 to 500 nanometers.
     * More stable and homogenous than conventional emulsions.
     * Example: Nanoemulsion-based Cosmetics, Drug delivery systems
     */
    NANOEMULSION,
    /** Custom dispersion type. */
    CUSTOM,
    /** Empty dispersion type. */
    EMPTY,
    /** Unspecified dispersion type. */
    NONE
}