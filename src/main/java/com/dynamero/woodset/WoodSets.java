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

package com.dynamero.woodset;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.dynamero.shared.annotations.*;
import com.dynamero.shared.exceptions.Exceptions;

/**
 * Registry class for managing all registered {@link WoodSet} instances.
 */
@SuppressWarnings("unused")
@Developer("TheMentor")
@CreatedAt("2026-01-12")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
public class WoodSets
{
    /**
     * Internal list of all registered WoodSets.
     */
    private static final List<WoodSet> WOOD_SETS = new ArrayList<>();

    /**
     * Private constructor to prevent instantiation.
     *
     * @throws AssertionError if called directly
     */
    public WoodSets()
    {
        Exceptions.throwCtorAssertion();
    }

    /**
     * Gets the list of all registered WoodSets.
     *
     * @return The list of WoodSets.
     */
    public static List<WoodSet> get()
    {
        return WOOD_SETS;
    }

    /**
     * Gets a specific WoodSet by its name.
     *
     * @param name The name of the WoodSet (case-insensitive).
     * @return The matching WoodSet, or null if not found.
     */
    public static WoodSet get(@NotNull String name)
    {
        return WOOD_SETS.stream()
                .filter(set -> name.equalsIgnoreCase(set.getName()))
                .findFirst()
                .orElse(null);
    }
}