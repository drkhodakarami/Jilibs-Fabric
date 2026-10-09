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

package com.dynamero.pressure.base.interfaces;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Direction;

import com.dynamero.shared.annotations.*;
import com.dynamero.shared.enumerations.MappedDirection;

/**
 * Interface for providing directional pressure storage instances based on mapped or world directions and block facing.
 *
 * @param <T> the pressure storage type
 */
@SuppressWarnings("unused")
@Developer("TheMentor")
@CreatedAt("2026-01-12")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
public interface PressureStorageProvider<T>
{
    /**
     * Retrieves a pressure storage provider based on the specified {@link MappedDirection} and facing direction.
     *
     * @param direction the mapped direction
     * @param facing    the facing direction
     * @return the pressure storage provider, or null if none is found
     */
    @Nullable
    T getPressureStorageProvider(MappedDirection direction, Direction facing);

    /**
     * Retrieves a pressure storage provider based on the specified world direction and facing direction.
     *
     * @param direction the world direction
     * @param facing    the facing direction
     * @return the pressure storage provider, or null if none is found
     */
    @Nullable
    T getPressureStorageProvider(Direction direction, Direction facing);
}