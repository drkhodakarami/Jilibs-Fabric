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

package com.dynamero.gas.client.interfaces;

import java.util.List;

import org.jspecify.annotations.Nullable;

import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;

import com.dynamero.gas.base.interfaces.GasVariant;
import com.dynamero.gas.client.GasRenderHandlerRegistry;
import com.dynamero.shared.annotations.*;

/**
 * Client-side renderer interface for {@link GasVariant} instances, providing tooltip line appending
 * and ARGB color tint evaluation using {@link GasRenderHandlerRegistry}.
 */
@SuppressWarnings("unused")
@Developer("TheMentor")
@CreatedAt("2026-01-12")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
public interface GasVariantRenderer
{
    /**
     * Appends gas variant information (such as temperature, pressure, or name) to tooltip lines.
     *
     * @param variant     the gas variant
     * @param tooltip     the list of tooltip lines to add to
     * @param tooltipFlag flags describing the tooltip context
     */
    default void appendTooltip(GasVariant variant, List<Component> tooltip, TooltipFlag tooltipFlag)
    {}

    /**
     * Resolves the ARGB color tint for the gas variant in the given world context.
     *
     * @param variant the gas variant
     * @param view    optional block and tint getter
     * @param pos     optional block position
     * @return the ARGB color integer with full opacity, or -1 if no renderer is found
     */
    default int getColor(GasVariant variant, @Nullable BlockAndTintGetter view, @Nullable BlockPos pos)
    {
        GasRenderer renderer = GasRenderHandlerRegistry.get(variant.getGas());
        return renderer != null ? renderer.getColor(view, pos) | 255 << 24 : -1;
    }
}