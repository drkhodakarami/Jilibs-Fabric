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

package com.dynamero.pressure.base;

import net.minecraft.core.Direction;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import com.dynamero.base.StorageConnector;
import com.dynamero.pressure.base.interfaces.PressureStorage;
import com.dynamero.pressure.base.records.PressureStorageSerializer;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.constants.BEKeys;
import com.dynamero.shared.enumerations.MappedDirection;
import com.dynamero.shared.interfaces.StorageConnectorProvider;
import com.dynamero.shared.utils.ValueIO;

/**
 * Storage connector managing multiple {@link PressureStorage} instances with directional lookup,
 * capacity queries, and persistence.
 *
 * @param <T> the pressure storage type
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
public class PressureConnector<T extends PressureStorage> extends StorageConnector<T> implements StorageConnectorProvider<PressureConnector<T>>
{
    /**
     * Retrieves the pressure amount in the storage mapped to the specified direction.
     *
     * @param direction the mapped direction
     * @return the pressure amount
     */
    public double getAmount(MappedDirection direction)
    {
        return getStorage(direction).getAmount();
    }

    /**
     * Retrieves the pressure amount in the storage mapped to the specified Minecraft direction.
     *
     * @param direction the Minecraft direction
     * @return the pressure amount
     */
    public double getAmount(Direction direction)
    {
        return getStorage(direction).getAmount();
    }

    /**
     * Retrieves the pressure amount in the storage at the specified index.
     *
     * @param index the storage index
     * @return the pressure amount
     */
    public double getAmount(int index)
    {
        return getStorage(index).getAmount();
    }

    /**
     * Retrieves the maximum pressure capacity of the storage mapped to the specified direction.
     *
     * @param direction the mapped direction
     * @return the pressure capacity
     */
    public double getCapacity(MappedDirection direction)
    {
        return getStorage(direction).getCapacity();
    }

    /**
     * Retrieves the maximum pressure capacity of the storage mapped to the specified Minecraft direction.
     *
     * @param direction the Minecraft direction
     * @return the pressure capacity
     */
    public double getCapacity(Direction direction)
    {
        return getStorage(direction).getCapacity();
    }

    /**
     * Retrieves the maximum pressure capacity of the storage at the specified index.
     *
     * @param index the storage index
     * @return the pressure capacity
     */
    public double getCapacity(int index)
    {
        return getStorage(index).getCapacity();
    }

    /**
     * Retrieves this pressure connector instance.
     *
     * @return this connector
     */
    @Override
    public PressureConnector<T> getConnector()
    {
        return this;
    }

    /**
     * Saves all managed pressure storages to the given value output.
     *
     * @param view the value output to write to
     */
    @Override
    public void saveAdditional(ValueOutput view)
    {
        for (int i = 0; i < this.storages.size(); i++)
            ValueIO.putChild(view, BEKeys.PRESSURE_STORAGE + "." + i,
                             new PressureStorageSerializer<>(this.storages.get(i)));
    }

    /**
     * Loads all managed pressure storages from the given value input.
     *
     * @param view the value input to read from
     */
    @Override
    public void loadAdditional(ValueInput view)
    {
        for (int i = 0; i < this.storages.size(); i++)
            ValueIO.readChild(view, BEKeys.PRESSURE_STORAGE + "." + i,
                            new PressureStorageSerializer<>(this.storages.get(i)));
    }
}