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

import java.util.IdentityHashMap;
import java.util.Map;

import org.jspecify.annotations.Nullable;

import com.dynamero.gas.base.records.Gas;
import com.dynamero.gas.client.interfaces.GasRenderer;
import com.dynamero.shared.annotations.*;

/**
 * Client registry storing and retrieving {@link GasRenderer} instances for visual rendering of gas variants.
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
public final class GasRenderHandlerRegistry
{
    /**
     * Map indexing registered gas renderers by gas definition.
     */
    private static final Map<Gas, GasRenderer> HANDLERS = new IdentityHashMap<>();

    /**
     * Retrieves the renderer registered for the specified gas.
     *
     * @param gas the gas definition
     * @return the gas renderer, or null if none is registered
     */
    public static @Nullable GasRenderer get(Gas gas)
    {
        return HANDLERS.get(gas);
    }

    /**
     * Registers a gas renderer for the specified gas.
     *
     * @param gas      the gas definition
     * @param renderer the gas renderer
     * @throws IllegalStateException if a renderer has already been registered for this gas
     */
    public static void register(Gas gas, GasRenderer renderer)
    {
        if(HANDLERS.putIfAbsent(gas, renderer) != null)
            throw new IllegalStateException("Duplicate renderer for gas: " + gas);
    }
}