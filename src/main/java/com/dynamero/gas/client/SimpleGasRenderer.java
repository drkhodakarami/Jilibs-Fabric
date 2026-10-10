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

package com.dynamero.gas.client;

import org.jspecify.annotations.Nullable;

import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;

import com.dynamero.gas.client.interfaces.GasRenderer;
import com.dynamero.shared.annotations.*;

/**
 * Simple gas renderer providing a constant ARGB color tint for gas rendering.
 */
@SuppressWarnings("unused")
@Developer("TurtyWurty")
@CreatedAt("2026-10-08")
@ModifiedAt("2026-10-08")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public class SimpleGasRenderer implements GasRenderer
{
    /**
     * The ARGB color integer used to tint the gas.
     */
    protected final int color;

    /**
     * Constructs a SimpleGasRenderer with a fixed tint color.
     *
     * @param color the ARGB color integer
     */
    public SimpleGasRenderer(int color)
    {
        this.color = color;
    }

    /**
     * Returns the configured tint color for the gas.
     *
     * @param view optional world view
     * @param pos  optional block position
     * @return the ARGB color integer
     */
    @Override
    public int getColor(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos)
    {
        return color;
    }
}