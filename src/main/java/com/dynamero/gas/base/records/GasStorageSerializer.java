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

package com.dynamero.gas.base.records;

import net.fabricmc.fabric.api.transfer.v1.storage.Storage;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import com.dynamero.gas.base.interfaces.GasVariant;
import com.dynamero.gas.base.storage.SingleGasStorage;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.interfaces.DataSerializer;

/**
 * Data serializer record handling persistence for single gas storage instances.
 *
 * @param storage the gas storage to serialize or deserialize
 * @param <T>     the gas storage type
 */
@Developer("TheMentor")
@CreatedAt("2026-01-12")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
public record GasStorageSerializer<T extends Storage<GasVariant>>(T storage) implements DataSerializer
{
    /**
     * Serializes gas storage state into the given value output.
     *
     * @param view the value output to write to
     * @throws IllegalArgumentException if the storage is not an instance of SingleGasStorage
     */
    @Override
    public void saveAdditional(ValueOutput view)
    {
        if(storage instanceof SingleGasStorage singleGasStorage)
            singleGasStorage.writeData(view);
        else
            throw new IllegalArgumentException("Gas Storage type is not supported by default: " + storage.getClass().getName());
    }

    /**
     * Deserializes gas storage state from the given value input into the storage.
     *
     * @param view the value input to read from
     * @throws IllegalArgumentException if the storage is not an instance of SingleGasStorage
     */
    @Override
    public void loadAdditional(ValueInput view)
    {
        if(storage instanceof SingleGasStorage singleGasStorage)
            singleGasStorage.readData(view);
        else
            throw new IllegalArgumentException("Gas Storage type is not supported by default: " + storage.getClass().getName());
    }
}