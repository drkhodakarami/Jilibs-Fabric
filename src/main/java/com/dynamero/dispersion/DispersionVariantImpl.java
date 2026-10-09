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

package com.dynamero.dispersion;

import java.util.List;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.PatchedDataComponentMap;

import com.dynamero.dispersion.base.enumerations.DispersionType;
import com.dynamero.dispersion.base.interfaces.DispersionVariant;
import com.dynamero.dispersion.base.records.Dispersion;
import com.dynamero.dispersion.base.records.MixtureComponent;
import com.dynamero.shared.annotations.*;

/**
 * Immutable implementation of {@link DispersionVariant} storing a dispersion definition and data components patch.
 */
@Developer("TheMentor")
@CreatedAt("2026-01-12")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
public class DispersionVariantImpl implements DispersionVariant
{
    /**
     * Factory method creating a new DispersionVariantImpl from a dispersion definition and component patch.
     *
     * @param dispersion     the dispersion definition
     * @param componentPatch the data component patch
     * @return a new DispersionVariantImpl instance
     */
    public static DispersionVariant of(Dispersion dispersion, DataComponentPatch componentPatch)
    {
        Objects.requireNonNull(dispersion, "Dispersion may not be null");
        Objects.requireNonNull(componentPatch, "Components may not be null");

        return new DispersionVariantImpl(dispersion, componentPatch);
    }

    /**
     * Factory method creating a new DispersionVariantImpl from a dispersion registry holder and component patch.
     *
     * @param dispersionHolder the dispersion holder
     * @param componentPatch   the data component patch
     * @return a new DispersionVariantImpl instance
     */
    public static DispersionVariant of(Holder<Dispersion> dispersionHolder, DataComponentPatch componentPatch)
    {
        Objects.requireNonNull(dispersionHolder, "Dispersion may not be null");
        return of(dispersionHolder.value(), componentPatch);
    }

    /**
     * The dispersion definition.
     */
    private final Dispersion dispersion;

    /**
     * Data component patch containing customized component data.
     */
    private final DataComponentPatch components;

    /**
     * Resolved data component map.
     */
    private final DataComponentMap componentMap;

    /**
     * Precomputed hash code.
     */
    private final int hashCode;

    /**
     * Private constructor initializing fields and precomputing hash code.
     *
     * @param dispersion the dispersion definition
     * @param components the data component patch
     */
    private DispersionVariantImpl(Dispersion dispersion, DataComponentPatch components)
    {
        this.dispersion = dispersion;
        this.components = components;
        this.componentMap = components == DataComponentPatch.EMPTY ? DataComponentMap.EMPTY : PatchedDataComponentMap.fromPatch(DataComponentMap.EMPTY, components);
        this.hashCode = Objects.hash(dispersion, components);
    }

    /**
     * Checks whether this variant represents the blank (empty) dispersion.
     *
     * @return {@code true} if empty, {@code false} otherwise
     */
    @Override
    public boolean isBlank()
    {
        return this.dispersion.matchingType(Dispersion.EMPTY);
    }

    /**
     * Retrieves the underlying dispersion definition object.
     *
     * @return the dispersion
     */
    @Override
    public @NotNull Dispersion getObject()
    {
        return this.dispersion;
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
     * Handles interactions between dispersion phases.
     */
    @Override
    public void handleInteractions()
    {
        this.dispersion.handleInteractions();
    }

    /**
     * Handles dissolution of solute in the dispersion medium.
     */
    @Override
    public void handleDesolution()
    {
        this.dispersion.handleDesolution();
    }

    /**
     * Handles particle or droplet distribution throughout the continuous phase.
     */
    @Override
    public void handleDistribution()
    {
        this.dispersion.handleDistribution();
    }

    /**
     * Handles chemical reactions within the dispersion mixture.
     */
    @Override
    public void handleReaction()
    {
        this.dispersion.handleReaction();
    }

    /**
     * Retrieves the dispersion classification type.
     *
     * @return the dispersion type
     */
    @Override
    public DispersionType getType()
    {
        return this.dispersion.dispersionType();
    }

    /**
     * Retrieves the identifier of this dispersion variant.
     *
     * @return the dispersion identifier string
     */
    @Override
    public String getId()
    {
        return this.dispersion.id();
    }

    /**
     * Retrieves the dynamic viscosity in Pa·s.
     *
     * @return the viscosity in Pa·s
     */
    @Override
    public double viscosity()
    {
        return this.dispersion.viscosity();
    }

    /**
     * Retrieves the density in kg/m³.
     *
     * @return the density in kg/m³
     */
    @Override
    public double density()
    {
        return this.dispersion.density();
    }

    /**
     * Retrieves the colloidal stability score of the dispersion.
     *
     * @return the stability score
     */
    @Override
    public double stability()
    {
        return this.dispersion.stability();
    }

    /**
     * Retrieves the list of mixture components forming this dispersion.
     *
     * @return list of mixture components
     */
    @Override
    public List<MixtureComponent> mixtureComponents()
    {
        return this.dispersion.mixtureComponents();
    }

    /**
     * Returns a string representation of this dispersion variant.
     *
     * @return string representation
     */
    @Override
    public String toString()
    {
        return "DispersionVariantImpl[dispersion=" + dispersion + ", components=" + components + "]";
    }

    /**
     * Checks equality between this dispersion variant and another object.
     *
     * @param obj the object to compare against
     * @return {@code true} if equal, {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;

        if(obj == null || obj.getClass() != getClass())
            return false;

        DispersionVariantImpl that = (DispersionVariantImpl) obj;

        return this.hashCode == that.hashCode && this.dispersion.matchingType(that.dispersion) && componentsMatch(that.components);
    }

    /**
     * Computes the hash code for this dispersion variant.
     *
     * @return precomputed hash code
     */
    @Override
    public int hashCode()
    {
        return this.hashCode;
    }
}