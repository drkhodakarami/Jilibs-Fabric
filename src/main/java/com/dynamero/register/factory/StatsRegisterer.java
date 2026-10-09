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

package com.dynamero.register.factory;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;

import com.dynamero.shared.annotations.*;
import com.dynamero.shared.utils.BaseHelper;

/**
 * Provides utility methods for registering custom game statistics in Minecraft.
 */
@Developer("The Mentor")
@CreatedAt("2025-04-18")
@Repository("https://github.com/drkhodakarami/___PROJECTS___")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")

public class StatsRegisterer
{
    /**
     * The unique identifier for the mod.
     */
    private final String modId;

    /**
     * Constructs a new instance of StatsRegisterer with the specified mod ID.
     *
     * @param modId the mod ID
     */
    public StatsRegisterer(String modId)
    {
        this.modId = modId;
    }

    /**
     * Registers a custom statistic using the default number formatter.
     *
     * @param key the unique key of the statistic
     * @return the registered custom statistic instance
     */
    public Stat<?> register(String key)
    {
        return register(key, StatFormatter.DEFAULT);
    }

    /**
     * Registers a custom statistic with a specified statistic formatter.
     *
     * @param key       the unique key of the statistic
     * @param formatter the formatter used for rendering the statistic value
     * @return the registered custom statistic instance
     */
    public Stat<?> register(String key, StatFormatter formatter)
    {
        Identifier newStat = Registry.register(BuiltInRegistries.CUSTOM_STAT, key,
                                               BaseHelper.id(this.modId, key));
        return Stats.CUSTOM.get(newStat, formatter);
    }
}