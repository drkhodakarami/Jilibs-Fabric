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

package com.dynamero.pressure.base;

import static com.dynamero.Jilibs.MODID;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.NotNull;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import com.dynamero.pressure.PressureConstants;
import com.dynamero.pressure.base.interfaces.PressureUnit;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.utils.BaseHelper;

/**
 * Custom packet payload and data model representing a pressure quantity and its {@link PressureUnit},
 * supporting serialization, unit conversion, network synchronization, and recipe matching.
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
public class PressureComponent implements CustomPacketPayload
{
    /**
     * Numerical pressure value in terms of {@link #unit}.
     */
    private double pressure;

    /**
     * Unit of pressure measurement.
     */
    private PressureUnit unit;

    /**
     * Constant representing zero pressure (0.0 Pascal).
     */
    public static PressureComponent ZERO = new PressureComponent(0.0, PressureUnit.PASCAL);

    /**
     * Constant representing standard atmospheric pressure in Pascal.
     */
    public static PressureComponent ATMOSPHERE = new PressureComponent(PressureConstants.Pascal.ATMOSPHERE, PressureUnit.PASCAL);

    /**
     * Codec for serializing and deserializing PressureComponent instances.
     */
    public static final Codec<PressureComponent> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.DOUBLE.fieldOf("pressure").forGetter(PressureComponent::getPressure),
            PressureUnit.CODEC.fieldOf("unit").forGetter(PressureComponent::getUnit)
    ).apply(inst, PressureComponent::new));

    /**
     * Stream codec for transmitting PressureComponent over network byte buffers.
     */
    public static final StreamCodec<FriendlyByteBuf, PressureComponent> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE, PressureComponent::getPressure,
            PressureUnit.STREAM_CODEC, PressureComponent::getUnit,
            PressureComponent::new
    );

    /**
     * Codec for serializing a list of PressureComponent instances.
     */
    public static final Codec<List<PressureComponent>> LIST_CODEC = CODEC.listOf();

    /**
     * Custom packet payload type identifier for pressure components.
     */
    public static final Type<@NotNull PressureComponent> ID = new Type<>(BaseHelper.id(MODID, "pressure_component_payload"));

    /**
     * Constructs a PressureComponent with a given pressure value and unit.
     *
     * @param pressure the pressure value
     * @param unit     the pressure unit
     */
    public PressureComponent(double pressure, PressureUnit unit)
    {
        this.pressure = pressure;
        this.unit = unit;
    }

    /**
     * Retrieves the pressure value.
     *
     * @return the pressure value
     */
    public double getPressure()
    {
        return pressure;
    }

    /**
     * Sets the pressure value directly.
     *
     * @param pressure the pressure value to set
     */
    public void setPressure(double pressure)
    {
        this.pressure = pressure;
    }

    /**
     * Retrieves the pressure measurement unit.
     *
     * @return the pressure unit
     */
    public PressureUnit getUnit()
    {
        return unit;
    }

    /**
     * Converts the current pressure value to a new unit and updates the unit field.
     *
     * @param unit the new pressure unit
     */
    public void setUnit(PressureUnit unit)
    {
        this.pressure = unit.convertFromBaseUnit(this.unit.convertToBaseUnit(this.pressure));
        this.unit = unit;
    }

    /**
     * Checks equality between this pressure component and another object.
     *
     * @param obj the object to compare against
     * @return {@code true} if units match and this pressure value meets or exceeds the compared value
     */
    @Override
    public boolean equals(Object obj)
    {
        if(obj == this)
            return true;

        if(obj.getClass() != getClass())
            return false;

        PressureComponent other = (PressureComponent) obj;

        return this.pressure >= other.pressure && unit == other.unit;
    }

    /**
     * Tests whether this pressure component satisfies a recipe ingredient requirement.
     *
     * @param pressureComponent the recipe pressure requirement
     * @return {@code true} if matching unit and sufficient pressure
     */
    public boolean testForRecipe(PressureComponent pressureComponent)
    {
        return getPressure() >= pressureComponent.getPressure() && getUnit().equals(pressureComponent.getUnit());
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