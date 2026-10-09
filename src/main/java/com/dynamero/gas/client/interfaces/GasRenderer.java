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

import org.jspecify.annotations.Nullable;

import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;

import com.dynamero.shared.annotations.*;

/**
 * Client-side renderer interface defining visual properties such as tint colors for gases.
 */
@SuppressWarnings("unused")
@Developer("TurtyWurty")
@CreatedAt("2026-08-10")
@ModifiedAt("2026-08-10")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public interface GasRenderer
{
    /**
     * Resolves the tint color for the gas in the given world context.
     *
     * @param view optional block and tint getter
     * @param pos  optional block position
     * @return the ARGB color integer (defaults to -1 for uncolored)
     */
    default int getColor(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos)
    {
        return -1;
    }
}