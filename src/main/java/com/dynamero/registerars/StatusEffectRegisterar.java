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

package com.dynamero.registerars;

import java.util.function.BiFunction;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

import com.dynamero.registerars.interfaces.IStatusEffectRegisterar;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.utils.BaseHelper;

/**
 * Registers custom status effects for Minecraft.
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
public class StatusEffectRegisterar implements IStatusEffectRegisterar
{
    /**
     * The mod ID used for registering status effects.
     */
    private final String modId;

    /**
     * Constructs a new instance of StatusEffectRegisterer with the specified mod ID.
     *
     * @param modId the mod ID
     */
    public StatusEffectRegisterar(String modId)
    {
        this.modId = modId;
    }

    @Override
    public Holder<MobEffect> register(String name, MobEffectCategory category, int color,
                                            BiFunction<MobEffectCategory, Integer, MobEffect> factory)
    {
        ResourceKey<MobEffect> key = BaseHelper.ResourceKeys.create(this.modId, name, Registries.MOB_EFFECT);
        MobEffect effect = factory.apply(category, color);
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, key, effect);
    }
}