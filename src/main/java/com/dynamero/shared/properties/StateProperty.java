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

package com.dynamero.shared.properties;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.level.block.state.properties.Property;

import com.dynamero.shared.annotations.*;

/**
 * Represents a state property of a block, including its delegate Property and default value.
 *
 * @param <T> the type of the value stored in the state property
 */
@Developer("TurtyWurty")
@CreatedAt("2026-08-10")
@ModifiedAt("2026-08-10")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public class StateProperty<T extends Comparable<T>>
{
    /**
     * The delegate Property of the state property.
     */
    private final Property<@NotNull T> delegate;

    /**
     * The default value for the state property.
     */
    private T defaultValue;

    /**
     * Constructs a new instance of StateProperty with the specified delegate Property and default value.
     *
     * @param delegate  the delegate Property
     * @param defaultValue the default value for the state property
     */
    public StateProperty(Property<@NotNull T> delegate, T defaultValue)
    {
        this.delegate = delegate;
        this.defaultValue = defaultValue;
    }

    /**
     * Retrieves the delegate Property of the state property.
     *
     * @return the delegate Property
     */
    public Property<@NotNull T> delegate()
    {
        return this.delegate;
    }

    /**
     * Retrieves the default value for the state property.
     *
     * @return the default value
     */
    public T defaultValue()
    {
        return this.defaultValue;
    }

    /**
     * Sets a new default value for the state property.
     *
     * @param defaultValue the new default value
     */
    public void setDefaultValue(T defaultValue)
    {
        this.defaultValue = defaultValue;
    }
}