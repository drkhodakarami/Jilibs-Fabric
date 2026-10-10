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

package com.dynamero.dispersion.base.records;

import net.fabricmc.fabric.api.transfer.v1.storage.Storage;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import com.dynamero.dispersion.base.interfaces.DispersionVariant;
import com.dynamero.dispersion.base.storage.SingleDispersionStorage;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.interfaces.DataSerializer;

/**
 * Data serializer record handling persistence for single dispersion storage instances.
 *
 * @param storage the dispersion storage to serialize or deserialize
 * @param <T>     the dispersion storage type
 */
@Developer("TheMentor")
@CreatedAt("2026-10-08")
@ModifiedAt("2026-10-08")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public record DispersionStorageSerialier<T extends Storage<DispersionVariant>>(T storage) implements DataSerializer
{
    /**
     * Serializes dispersion storage state into the given value output.
     *
     * @param view the value output to write to
     * @throws UnsupportedOperationException if the storage is not an instance of SingleDispersionStorage
     */
    @Override
    public void saveAdditional(ValueOutput view)
    {
        if(storage instanceof SingleDispersionStorage singleDispersionStorage)
            singleDispersionStorage.writeData(view);
        else
            throw new UnsupportedOperationException("Cannot write dispersion storage of type: " + storage.getClass().getName());
    }

    /**
     * Deserializes dispersion storage state from the given value input into the storage.
     *
     * @param view the value input to read from
     * @throws UnsupportedOperationException if the storage is not an instance of SingleDispersionStorage
     */
    @Override
    public void loadAdditional(ValueInput view)
    {
        if(storage instanceof SingleDispersionStorage singleDispersionStorage)
            singleDispersionStorage.readData(view);
        else
            throw new UnsupportedOperationException("Cannot read dispersion storage of type: " + storage.getClass().getName());
    }
}