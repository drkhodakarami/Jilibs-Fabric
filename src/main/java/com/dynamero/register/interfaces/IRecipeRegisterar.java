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

package com.dynamero.register.interfaces;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.item.crafting.*;

import com.dynamero.shared.annotations.*;

/**
 * Registers custom recipes and recipe serializers for Minecraft.
 */
@SuppressWarnings("unused")
@Developer("The Mentor")
@CreatedAt("2025-04-18")
@Repository("https://github.com/drkhodakarami/___PROJECTS___")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")

public interface IRecipeRegisterar
{
    /**
     * Registers a recipe serializer.
     *
     * @param <R>             the type of the recipe
     * @param <D>             the type of the recipe input
     * @param name            the name of the recipe serializer
     * @param serializer      the recipe serializer to register
     * @return the registered recipe serializer
     */
    <R extends Recipe<@NotNull D>, D extends RecipeInput> RecipeSerializer<@NotNull R> register(String name, RecipeSerializer<@NotNull R> serializer);

    /**
     * Registers a recipe type.
     *
     * @param <R>             the type of the recipe
     * @param name            the name of the recipe type
     * @param recipeType      the recipe type to register
     * @return the registered recipe type
     */
    <R extends Recipe<@NotNull D>, D extends RecipeInput> RecipeType<@NotNull R> register(String name, RecipeType<@NotNull R> recipeType);

    /**
     * Registers a new recipe book category.
     *
     * @param name the name of the recipe book category
     * @return the registered RecipeBookCategory instance
     */
    RecipeBookCategory registerCategory(String name);
}