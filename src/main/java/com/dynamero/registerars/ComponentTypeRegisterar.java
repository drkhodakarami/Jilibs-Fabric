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

import java.util.function.UnaryOperator;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;

import com.dynamero.registerars.interfaces.IComponentTypeRegisterar;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.utils.BaseHelper;

/**
 * Registers custom component types for Minecraft data.
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
public class ComponentTypeRegisterar implements IComponentTypeRegisterar
{
    /**
     * The unique identifier for the mod.
     */
    private final String modId;

    /**
     * Constructs a new instance of JiComponentTypeRegister with the specified mod ID.
     *
     * @param modId the mod ID
     */
    public ComponentTypeRegisterar(String modId)
    {
        this.modId = modId;
    }

    @Override
    public <R> DataComponentType<R> register(String name, UnaryOperator<DataComponentType.Builder<R>> buildOperator)
    {
        ResourceKey<DataComponentType<?>> key = BaseHelper.ResourceKeys.create(this.modId, name, Registries.DATA_COMPONENT_TYPE);
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, key, buildOperator.apply(DataComponentType.builder()).build());
    }
}