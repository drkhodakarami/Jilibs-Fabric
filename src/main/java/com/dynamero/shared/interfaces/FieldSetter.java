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

package com.dynamero.shared.interfaces;

import net.minecraft.world.level.block.entity.BlockEntity;

import com.dynamero.shared.annotations.*;

/**
 * Represents a functional interface for setting a value on a block entity.
 *
 * @param <T> the type of the value to set
 * @param <B> the type of the block entity
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

@FunctionalInterface
public interface FieldSetter<T, B extends BlockEntity>
{
    /**
     * Sets the specified value on the given block entity.
     *
     * @param blockEntity the block entity on which to set the value
     * @param value the value to set
     */
    void set(B blockEntity, T value);
}