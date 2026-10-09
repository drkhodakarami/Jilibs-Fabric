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

package com.dynamero.gas.base.interfaces;

import static com.dynamero.Jilibs.GASES;

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
import com.dynamero.gas.GasVariantImpl;
import com.dynamero.gas.base.records.Gas;
import com.dynamero.heat.base.HeatComponent;
import com.dynamero.heat.base.interfaces.HeatUnit;
import com.dynamero.pressure.base.PressureComponent;
import com.dynamero.shared.annotations.*;

/**
 * Transfer variant representation for gases in the Fabric transfer API,
 * integrating data component patches, thermodynamic temperature, and pneumatic pressure.
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
public interface GasVariant extends TransferVariant<Gas>
{
    /**
     * Universal gas constant R in J/(mol·K).
     */
    double GAS_CONSTANT = 8.314;  // Universal gas constant in J/(mol·K)

    /**
     * Retrieves the thermodynamic heat component of this gas variant.
     *
     * @return the heat component
     */
    HeatComponent getHeatComponent();

    /**
     * Retrieves the current temperature value of this gas variant.
     *
     * @return the temperature value
     */
    double getTemperature();

    /**
     * Retrieves the temperature unit of measurement for this gas variant.
     *
     * @return the heat unit
     */
    HeatUnit getHeatUnit();

    /**
     * Retrieves the pneumatic pressure component of this gas variant.
     *
     * @return the pressure component
     */
    PressureComponent getPressureComponent();

    /**
     * Codec for serializing and deserializing GasVariant instances.
     */
    Codec<GasVariant> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            GASES.holderByNameCodec().fieldOf("gas").forGetter(GasVariant::typeHolder),
            DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(GasVariant::getComponentsPatch),
            HeatComponent.CODEC.fieldOf("temperature").forGetter(GasVariant::getHeatComponent),
            PressureComponent.CODEC.fieldOf("pressure").forGetter(GasVariant::getPressureComponent)
        ).apply(instance, GasVariantImpl::of));

    /**
     * Stream codec for transmitting GasVariant instances over network buffers.
     */
    StreamCodec<RegistryFriendlyByteBuf, GasVariant> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.holderRegistry(Jilibs.GASES_REGISTRY_KEY), GasVariant::typeHolder,
            DataComponentPatch.STREAM_CODEC, GasVariant::getComponentsPatch,
            HeatComponent.STREAM_CODEC, GasVariant::getHeatComponent,
            PressureComponent.STREAM_CODEC, GasVariant::getPressureComponent,
            GasVariantImpl::of);

    /**
     * Returns the canonical blank (empty) gas variant.
     *
     * @return the blank gas variant
     */
    static GasVariant blank()
    {
        return of(Gas.EMPTY, HeatComponent.ZERO, PressureComponent.ATMOSPHERE);
    }

    /**
     * Creates a GasVariant with standard room temperature and atmospheric pressure.
     *
     * @param gas the gas definition
     * @return the new GasVariant
     */
    static GasVariant of(Gas gas)
    {
        return of(gas, DataComponentPatch.EMPTY, HeatComponent.ROOM, PressureComponent.ATMOSPHERE);
    }

    /**
     * Creates a GasVariant with custom heat and pressure components.
     *
     * @param gas               the gas definition
     * @param heatComponent     the heat component
     * @param pressureComponent the pressure component
     * @return the new GasVariant
     */
    static GasVariant of(Gas gas, HeatComponent heatComponent, PressureComponent pressureComponent)
    {
        return of(gas, DataComponentPatch.EMPTY, heatComponent, pressureComponent);
    }

    /**
     * Creates a GasVariant with custom components patch, heat component, and pressure component.
     *
     * @param gas               the gas definition
     * @param componentPatch    the data component patch
     * @param heatComponent     the heat component
     * @param pressureComponent the pressure component
     * @return the new GasVariant
     */
    static GasVariant of(Gas gas, DataComponentPatch componentPatch, HeatComponent heatComponent, PressureComponent pressureComponent)
    {
        return GasVariantImpl.of(gas, componentPatch, heatComponent, pressureComponent);
    }

    /**
     * Retrieves the underlying gas object.
     *
     * @return the gas
     */
    default Gas getGas()
    {
        return getObject();
    }

    /**
     * Wraps the gas in a registry holder.
     *
     * @return the holder for this gas
     */
    @Override
    default @NotNull Holder<Gas> typeHolder()
    {
        return GASES.wrapAsHolder(getGas());
    }

    // Thermodynamic Methods
    /**
     * Calculates gas volume using the ideal gas law (V = nRT / P).
     *
     * @param n number of moles
     * @return volume in cubic meters / liters
     */
    double calculateVolume(double n);  // Calculate volume using PV=nRT

    /**
     * Calculates gas pressure using the ideal gas law (P = nRT / V).
     *
     * @param n number of moles
     * @param V volume in cubic meters / liters
     * @return pressure in pascals
     */
    double calculatePressure(double n, double V);  // Calculate pressure using PV=nRT

    /**
     * Calculates gas temperature using the ideal gas law (T = PV / nR).
     *
     * @param n number of moles
     * @param V volume in cubic meters / liters
     * @return temperature in Kelvin
     */
    double calculateTemperature(double n, double V);  // Calculate temperature using PV=nRT

    /**
     * Calculates the substance amount (moles) using the ideal gas law (n = PV / RT).
     *
     * @param V volume in cubic meters / liters
     * @return substance amount in moles
     */
    double calculateAmountOfSubstance(double V);  // Calculate amount of substance using PV=nRT
}