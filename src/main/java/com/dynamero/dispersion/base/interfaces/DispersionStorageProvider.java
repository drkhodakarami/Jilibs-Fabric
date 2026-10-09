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

package com.dynamero.dispersion.base.interfaces;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Direction;

import com.dynamero.shared.annotations.*;
import com.dynamero.shared.enumerations.MappedDirection;

/**
 * Interface for providing directional dispersion storage instances based on mapped or world directions and block facing.
 *
 * @param <T> the dispersion storage type
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
public interface DispersionStorageProvider<T>
{
    /**
     * Retrieves a dispersion storage provider based on the specified {@link MappedDirection} and facing direction.
     *
     * @param direction the mapped direction
     * @param facing    the facing direction
     * @return the dispersion storage provider, or null if none is found
     */
    @Nullable
    T getDispersionStorageProvider(MappedDirection direction, Direction facing);

    /**
     * Retrieves a dispersion storage provider based on the specified world direction and facing direction.
     *
     * @param direction the world direction
     * @param facing    the facing direction
     * @return the dispersion storage provider, or null if none is found
     */
    @Nullable
    T getDispersionStorageProvider(Direction direction, Direction facing);
}