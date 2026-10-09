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

package com.dynamero.registerars.interfaces;

import java.util.function.UnaryOperator;

import net.minecraft.core.component.DataComponentType;

import com.dynamero.shared.annotations.*;

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
public interface IComponentTypeRegisterar
{

    /**
     * Registers a new component type.
     *
     * @param <R>           the type of the component
     * @param name          the name of the component type
     * @param buildOperator an operator that builds the component type
     * @return the registered component type
     */
    <R> DataComponentType<R> register(String name, UnaryOperator<DataComponentType.Builder<R>> buildOperator);
}