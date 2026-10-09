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

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.level.block.entity.BlockEntity;

import com.dynamero.shared.annotations.*;
import com.dynamero.shared.interfaces.FieldGetter;
import com.dynamero.shared.interfaces.FieldSetter;

/**
 * Represents a single field in a BlockEntity, including its value and optional getter and setter methods.
 *
 * @param <T> the type of the value stored in the field
 * @param <B> the type of BlockEntity this field belongs to
 */
@SuppressWarnings("unused")
@Developer("TurtyWurty")
@ModifiedBy("TheMentor")
@CreatedAt("2025-04-18")
@Repository("https://github.com/DaRealTurtyWurty/Industria")
@Discord("https://discord.turtywurty.dev/")
@Youtube("https://www.youtube.com/@TurtyWurty")

public class BlockEntityField<T, B extends BlockEntity>
{
    /**
     * The value stored in the field.
     */
    private final T value;

    /**
     * The optional getter method for the field value.
     */
    private final @Nullable FieldGetter<T, B> getter;

    /**
     * The optional setter method for the field value.
     */
    private final @Nullable FieldSetter<T, B> setter;

    //region CTOR
    /**
     * Constructs a new BlockEntityField with the specified value and both getter and setter methods.
     *
     * @param value  the value of the field
     * @param getter the method to retrieve the field's value from the BlockEntity
     * @param setter the method to set the field's value on the BlockEntity
     */
    public BlockEntityField(T value, @Nullable FieldGetter<T, B> getter, @Nullable FieldSetter<T, B> setter)
    {
        this.value = value;
        this.getter = getter;
        this.setter = setter;
    }

    /**
     * Constructs a new BlockEntityField with the specified value and only the getter method.
     *
     * @param value  the value of the field
     * @param getter the method to retrieve the field's value from the BlockEntity
     */
    public BlockEntityField(T value, @Nullable FieldGetter<T, B> getter)
    {
        this(value, getter, null);
    }

    /**
     * Constructs a new BlockEntityField with the specified value and only the setter method.
     *
     * @param value  the value of the field
     * @param setter the method to set the field's value on the BlockEntity
     */
    public BlockEntityField(T value, @Nullable FieldSetter<T, B> setter)
    {
        this(value, null, setter);
    }

    /**
     * Constructs a new BlockEntityField with the specified value and no getter or setter methods.
     *
     * @param value the value of the field
     */
    public BlockEntityField(T value)
    {
        this(value, null, null);
    }
    //endregion

    /**
     * Retrieves the stored value of the field.
     *
     * @return the value of the field
     */
    public T getValue()
    {
        return this.value;
    }

    /**
     * Retrieves the optional getter method for the field value.
     *
     * @return the getter method if it exists; null otherwise
     */
    public @Nullable FieldGetter<T, B> getGetter()
    {
        return this.getter;
    }

    /**
     * Retrieves the optional setter method for the field value.
     *
     * @return the setter method if it exists; null otherwise
     */
    public @Nullable FieldSetter<T, B> getSetter()
    {
        return this.setter;
    }

    /**
     * Retrieves the value of the field from the specified BlockEntity using an optional getter method.
     *
     * @param blockEntity the BlockEntity to retrieve the field's value from
     * @return the value of the field
     */
    public T get(B blockEntity)
    {
        return this.getter != null ? this.getter.get(blockEntity) : this.value;
    }

    /**
     * Sets the value of the field on the specified BlockEntity using an optional setter method.
     *
     * @param blockEntity the BlockEntity to set the field's value on
     * @param value       the new value for the field
     */
    public void set(B blockEntity, T value)
    {
        if(this.setter != null)
            this.setter.set(blockEntity, value);
    }
}