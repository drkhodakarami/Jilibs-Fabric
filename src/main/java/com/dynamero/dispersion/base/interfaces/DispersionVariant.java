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

package com.dynamero.dispersion.base.interfaces;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant;
import org.jetbrains.annotations.NotNull;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import com.dynamero.Jilibs;
import com.dynamero.dispersion.DispersionVariantImpl;
import com.dynamero.dispersion.base.enumerations.DispersionType;
import com.dynamero.dispersion.base.records.Dispersion;
import com.dynamero.dispersion.base.records.MixtureComponent;
import com.dynamero.shared.annotations.*;

/**
 * Transfer variant representation for dispersions in the Fabric transfer API,
 * integrating data component patches, dispersion types, and physical mixture properties.
 */
@SuppressWarnings("unused")
@Developer("TheMentor")
@CreatedAt("2026-01-12")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
public interface DispersionVariant extends TransferVariant<Dispersion>
{
    /**
     * Codec for serializing and deserializing DispersionVariant instances.
     */
    Codec<DispersionVariant> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Jilibs.DISPERSIONS.holderByNameCodec().fieldOf("dispersion").forGetter(DispersionVariant::typeHolder),
            DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(DispersionVariant::getComponentsPatch)
    ).apply(inst, DispersionVariantImpl::of));

    /**
     * Stream codec for transmitting DispersionVariant instances over network buffers.
     */
    StreamCodec<RegistryFriendlyByteBuf, DispersionVariant> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.holderRegistry(Jilibs.DISPERSION_REGISTRY_KEY), DispersionVariant::typeHolder,
            DataComponentPatch.STREAM_CODEC, DispersionVariant::getComponentsPatch,
            DispersionVariantImpl::of
    );

    /**
     * Returns the canonical blank (empty) dispersion variant.
     *
     * @return the blank dispersion variant
     */
    static DispersionVariant blank()
    {
        return of(Dispersion.EMPTY);
    }

    /**
     * Creates a DispersionVariant with an empty component patch.
     *
     * @param dispersion the dispersion definition
     * @return the new DispersionVariant
     */
    static DispersionVariant of(Dispersion dispersion)
    {
        return of(dispersion, DataComponentPatch.EMPTY);
    }

    /**
     * Creates a DispersionVariant with a custom component patch.
     *
     * @param dispersion     the dispersion definition
     * @param componentPatch the data component patch
     * @return the new DispersionVariant
     */
    static DispersionVariant of(Dispersion dispersion, DataComponentPatch componentPatch)
    {
        return DispersionVariantImpl.of(dispersion, componentPatch);
    }

    /**
     * Retrieves the underlying dispersion object.
     *
     * @return the dispersion
     */
    default Dispersion getDispersion()
    {
        return getObject();
    }

    /**
     * Wraps the dispersion in a registry holder.
     *
     * @return the holder for this dispersion
     */
    @Override
    default @NotNull Holder<Dispersion> typeHolder()
    {
        return Jilibs.DISPERSIONS.wrapAsHolder(getDispersion());
    }

    /**
     * Handles interactions between dispersion phases.
     */
    default void handleInteractions()
    {}

    /**
     * Handles dissolution of solute in the dispersion medium.
     */
    default void handleDesolution()
    {}

    /**
     * Handles particle or droplet distribution throughout the continuous phase.
     */
    default void handleDistribution()
    {}

    /**
     * Handles chemical reactions within the dispersion mixture.
     */
    default void handleReaction()
    {}

    /**
     * Retrieves the identifier of this dispersion variant.
     *
     * @return the dispersion identifier string
     */
    String getId();

    /**
     * Retrieves the dispersion classification type.
     *
     * @return the dispersion type
     */
    DispersionType getType();

    /**
     * Retrieves the dynamic viscosity in Pa·s.
     *
     * @return the viscosity in Pa·s
     */
    double viscosity();

    /**
     * Retrieves the density in kg/m³.
     *
     * @return the density in kg/m³
     */
    double density();

    /**
     * Retrieves the kinetic/colloidal stability score of the dispersion.
     *
     * @return the stability score
     */
    double stability();

    /**
     * Retrieves the list of mixture components forming this dispersion.
     *
     * @return list of mixture components
     */
    List<MixtureComponent> mixtureComponents();
}