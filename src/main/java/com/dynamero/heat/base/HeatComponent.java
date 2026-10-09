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

package com.dynamero.heat.base;

import static com.dynamero.Jilibs.MODID;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.NotNull;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import com.dynamero.heat.HeatConstants;
import com.dynamero.heat.base.interfaces.HeatUnit;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.utils.BaseHelper;

/**
 * Custom packet payload and data model representing a thermal quantity and its {@link HeatUnit},
 * supporting serialization, unit conversion, network synchronization, and recipe matching.
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
public class HeatComponent implements CustomPacketPayload
{
    /**
     * Numerical heat/temperature value in terms of {@link #unit}.
     */
    private double heat;

    /**
     * Unit of temperature measurement.
     */
    private HeatUnit unit;

    /**
     * Constant representing absolute zero (0.0 Kelvin).
     */
    public static HeatComponent ZERO = new HeatComponent(0.0, HeatUnit.KELVIN);

    /**
     * Constant representing standard room temperature in Kelvin.
     */
    public static HeatComponent ROOM = new HeatComponent(HeatConstants.Kelvin.ROOM_TEMPERATURE, HeatUnit.KELVIN);

    /**
     * Codec for serializing and deserializing HeatComponent instances.
     */
    public static final Codec<HeatComponent> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.DOUBLE.fieldOf("heat").forGetter(HeatComponent::getHeat),
            HeatUnit.CODEC.fieldOf("unit").forGetter(HeatComponent::getUnit)
    ).apply(inst, HeatComponent::new));

    /**
     * Stream codec for transmitting HeatComponent over network byte buffers.
     */
    public static final StreamCodec<FriendlyByteBuf, HeatComponent> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE, HeatComponent::getHeat,
            HeatUnit.STREAM_CODEC, HeatComponent::getUnit,
            HeatComponent::new
    );

    /**
     * Codec for serializing a list of HeatComponent instances.
     */
    public static final Codec<List<HeatComponent>> LIST_CODEC = CODEC.listOf();

    /**
     * Custom packet payload type identifier for heat components.
     */
    public static final Type<@NotNull HeatComponent> ID = new Type<>(BaseHelper.id(MODID, "heat_component_payload"));

    /**
     * Constructs a HeatComponent with a given heat value and unit.
     *
     * @param heat the heat value
     * @param unit the heat unit
     */
    public HeatComponent(double heat, HeatUnit unit)
    {
        this.heat = heat;
        this.unit = unit;
    }

    /**
     * Retrieves the heat/temperature value.
     *
     * @return the heat value
     */
    public double getHeat()
    {
        return heat;
    }

    /**
     * Sets the heat/temperature value directly.
     *
     * @param heat the heat value to set
     */
    public void setHeat(double heat)
    {
        this.heat = heat;
    }

    /**
     * Retrieves the heat measurement unit.
     *
     * @return the heat unit
     */
    public HeatUnit getUnit()
    {
        return unit;
    }

    /**
     * Converts the current heat value to a new unit and updates the unit field.
     *
     * @param unit the new heat unit
     */
    public void setUnit(HeatUnit unit)
    {

        this.heat = unit.convertFromBaseUnit(this.unit.convertToBaseUnit(this.heat));
        this.unit = unit;
    }

    /**
     * Checks equality between this heat component and another object.
     *
     * @param obj the object to compare against
     * @return {@code true} if units match and this heat value meets or exceeds the compared value
     */
    @Override
    public boolean equals(Object obj)
    {
        if(obj == this)
            return true;

        if(obj.getClass() != getClass())
            return false;

        HeatComponent other = (HeatComponent) obj;

        return this.heat >= other.heat && unit == other.unit;
    }

    /**
     * Tests whether this heat component satisfies a recipe ingredient requirement.
     *
     * @param heatComponent the recipe heat requirement
     * @return {@code true} if matching unit and sufficient heat
     */
    public boolean testForRecipe(HeatComponent heatComponent)
    {
        return getHeat() >= heatComponent.getHeat() && getUnit().equals(heatComponent.getUnit());
    }

    /**
     * Retrieves the packet payload type identifier.
     *
     * @return the packet payload type
     */
    @Override
    public @NotNull Type<? extends @NotNull CustomPacketPayload> type()
    {
        return ID;
    }
}