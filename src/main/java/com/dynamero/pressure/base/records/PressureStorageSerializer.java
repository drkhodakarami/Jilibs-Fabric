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

package com.dynamero.pressure.base.records;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import com.dynamero.pressure.base.interfaces.PressureStorage;
import com.dynamero.pressure.base.storage.SimplePressureStorage;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.constants.BEKeys;
import com.dynamero.shared.interfaces.DataSerializer;

/**
 * Data serializer record handling persistence for pressure storage instances.
 *
 * @param storage the pressure storage to serialize or deserialize
 * @param <T>     the pressure storage type
 */
@Developer("TheMentor")
@CreatedAt("2026-08-10")
@ModifiedAt("2026-08-10")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public record PressureStorageSerializer<T extends PressureStorage>(T storage) implements DataSerializer
{
    /**
     * Serializes pressure storage amount into the given value output.
     *
     * @param view the value output to write to
     * @throws IllegalArgumentException if the storage is not an instance of SimplePressureStorage
     */
    @Override
    public void saveAdditional(ValueOutput view)
    {
        if(storage instanceof SimplePressureStorage simplePressureStorage)
            view.putDouble("be." + BEKeys.PRESSURE_AMOUNT, simplePressureStorage.getAmount());
        else
            throw new IllegalArgumentException("Pressure Storage type is not supported by default: " + storage.getClass().getName());
    }

    /**
     * Deserializes pressure storage amount from the given value input into the storage.
     *
     * @param view the value input to read from
     * @throws IllegalArgumentException if the storage is not an instance of SimplePressureStorage
     */
    @Override
    public void loadAdditional(ValueInput view)
    {
        double amount = view.getDoubleOr("be." + BEKeys.PRESSURE_AMOUNT, 0);
        if(storage instanceof SimplePressureStorage simplePressureStorage)
            simplePressureStorage.amount = amount;
        else
            throw new IllegalArgumentException("Pressure Storage type is not supported by default: " + storage.getClass().getName());
    }
}