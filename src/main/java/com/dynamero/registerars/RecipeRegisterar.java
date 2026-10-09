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

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.*;

import com.dynamero.registerars.interfaces.IRecipeRegisterar;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.utils.BaseHelper;

/**
 * Registers custom recipes and recipe serializers for Minecraft.
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
public class RecipeRegisterar implements IRecipeRegisterar
{
    /**
     * The mod ID used for registering recipes and serializers.
     */
    private final String modId;

    /**
     * Constructs a new instance of RecipeRegisterer with the specified mod ID.
     *
     * @param modId the mod ID
     */
    public RecipeRegisterar(String modId)
    {
        this.modId = modId;
    }

    @Override
    public <R extends Recipe<@NotNull D>, D extends RecipeInput> RecipeSerializer<@NotNull R> register(String name, RecipeSerializer<@NotNull R> serializer)
    {
        ResourceKey<RecipeSerializer<?>> key = BaseHelper.ResourceKeys.create(this.modId, name, Registries.RECIPE_SERIALIZER);
        return Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, key, serializer);
    }

    @Override
    public <R extends Recipe<@NotNull D>, D extends RecipeInput> RecipeType<@NotNull R> register(String name, RecipeType<@NotNull R> recipeType)
    {
        ResourceKey<RecipeType<?>> key = BaseHelper.ResourceKeys.create(this.modId, name, Registries.RECIPE_TYPE);
        return Registry.register(BuiltInRegistries.RECIPE_TYPE, key, recipeType);
    }

    @Override
    public RecipeBookCategory registerCategory(String name)
    {
        return Registry.register(BuiltInRegistries.RECIPE_BOOK_CATEGORY, BaseHelper.id(this.modId, name), new RecipeBookCategory());
    }
}