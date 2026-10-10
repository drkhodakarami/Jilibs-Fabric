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

import static com.dynamero.Jilibs.MODID;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

import com.dynamero.Jilibs;
import com.dynamero.dispersion.base.enumerations.DispersionType;
import com.dynamero.dispersion.base.enumerations.MixtureComponentType;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.utils.BaseHelper;

/**
 * Definition record for a dispersion type containing identifier, classification type,
 * physical properties (viscosity, density, stability), and constituent mixture components.
 *
 * @param id                the unique string identifier of the dispersion
 * @param dispersionType    the dispersion classification type
 * @param viscosity         the dynamic viscosity in Pa·s
 * @param density           the density in kg/m³
 * @param stability         the colloidal stability score
 * @param mixtureComponents the list of constituent mixture components
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
public record Dispersion(String id, DispersionType dispersionType, double viscosity, double density, double stability, List<MixtureComponent> mixtureComponents)
{
    /**
     * Canonical empty dispersion definition.
     */
    public static final Dispersion EMPTY = register(new Builder("empty")
                                                            .type(DispersionType.EMPTY)
                                                            .build());

    /**
     * Checks whether the specified dispersion matches this dispersion instance.
     *
     * @param dispersion the dispersion to test
     * @return {@code true} if identical instance, {@code false} otherwise
     */
    public boolean matchingType(Dispersion dispersion)
    {
        return dispersion == this;
    }

    //region Register
    /**
     * Registers a dispersion definition under the specified string path.
     *
     * @param id                the path identifier
     * @param dispersionType    the dispersion type
     * @param viscosity         the viscosity in Pa·s
     * @param density           the density in kg/m³
     * @param stability         the stability score
     * @param mixtureComponents the constituent mixture components
     * @return the registered dispersion
     */
    public static Dispersion register(String id, DispersionType dispersionType, double viscosity, double density, double stability, List<MixtureComponent>mixtureComponents)
    {
        return register(BaseHelper.id(MODID, id), dispersionType, viscosity, density, stability, mixtureComponents);
    }

    /**
     * Registers a dispersion definition under the specified resource identifier.
     *
     * @param id                the resource identifier
     * @param dispersionType    the dispersion type
     * @param viscosity         the viscosity in Pa·s
     * @param density           the density in kg/m³
     * @param stability         the stability score
     * @param mixtureComponents the constituent mixture components
     * @return the registered dispersion
     */
    public static Dispersion register(Identifier id, DispersionType dispersionType, double viscosity, double density, double stability, List<MixtureComponent>mixtureComponents)
    {
        return Registry.register(Jilibs.DISPERSIONS, id,
                                 new Dispersion(id.getPath(), dispersionType, viscosity, density, stability, mixtureComponents));
    }

    /**
     * Registers a dispersion definition instance in the dispersions registry.
     *
     * @param dispersion the dispersion instance to register
     * @return the registered dispersion
     */
    public static Dispersion register(Dispersion dispersion)
    {
        return Registry.register(Jilibs.DISPERSIONS, BaseHelper.id(MODID, dispersion.id()), dispersion);
    }
    //endregion

    /**
     * Retrieves the list of MixtureComponents that contain the specified type.
     *
     * @param type the MixtureComponentType to filter by
     * @return a list of MixtureComponents that contain the specified type
     */
    public List<MixtureComponent> getComponentsByType(MixtureComponentType type)
    {
        return mixtureComponents.stream()
                .filter(component -> component.hasType(type))
                .toList();
    }

    /**
     * Physical interactions that can happen between dispersion's components.
     * Handle anything that should happen between components.
     */
    public void handleInteractions()
    {
        handleDesolution();
        handleDistribution();
        handleReaction();
    }

    /**
     * How quickly solutes dissolve in solvents.
     * Handle anything that should happen during dissolving phase.
     */
    public void handleDesolution()
    {}

    /**
     * Size and distribution of particles in suspensions.
     * Handle anything that should happen during particle distribution phase.
     */
    public void handleDistribution()
    {}

    /**
     * Chemical reactions between components.
     * Handle anything that should happen during chemical reaction phase.
     */
    public void handleReaction()
    {}

    /**
     * Builder helper class for constructing {@link Dispersion} instances.
     */
    public static class Builder
    {
        /**
         * The unique identifier string.
         */
        private final String id;

        /**
         * The dispersion classification type.
         */
        private DispersionType dispersionType;

        /**
         * The dynamic viscosity in Pa·s.
         */
        private double viscosity;

        /**
         * The density in kg/m³.
         */
        private double density;

        /**
         * The colloidal stability score.
         */
        private double stability;

        /**
         * The constituent mixture components list.
         */
        private List<MixtureComponent> mixtureComponents;

        /**
         * Constructs a Builder with the specified dispersion identifier.
         *
         * @param id the unique string identifier
         */
        public Builder(String id)
        {
            this.id = id;
            this.viscosity = 0.0;
            this.density = 0.0;
            this.stability = 1.0;
            this.mixtureComponents = new ArrayList<>();
            this.dispersionType = DispersionType.NONE;
        }

        /**
         * Sets the dispersion classification type.
         *
         * @param type the dispersion type
         * @return this builder
         */
        public Builder type(DispersionType type)
        {
            this.dispersionType = type;
            return this;
        }

        /**
         * Sets the dynamic viscosity in Pa·s.
         *
         * @param viscosity the viscosity value
         * @return this builder
         */
        public Builder viscosity(double viscosity)
        {
            this.viscosity = viscosity;
            return this;
        }

        /**
         * Sets the density in kg/m³.
         *
         * @param density the density value
         * @return this builder
         */
        public Builder density(double density)
        {
            this.density = density;
            return this;
        }

        /**
         * Sets the colloidal stability score.
         *
         * @param stability the stability score
         * @return this builder
         */
        public Builder stability(double stability)
        {
            this.stability = stability;
            return this;
        }

        /**
         * Sets the list of constituent mixture components.
         *
         * @param components the components list
         * @return this builder
         */
        public Builder mixtureComponents(List<MixtureComponent> components)
        {
            this.mixtureComponents = components;
            return this;
        }

        /**
         * Builds a new {@link Dispersion} instance using configured properties.
         *
         * @return the new Dispersion instance
         */
        public Dispersion build()
        {
            return new Dispersion(id, dispersionType, viscosity, density, stability, mixtureComponents);
        }
    }
}