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

package com.dynamero.energy.base;

import team.reborn.energy.api.EnergyStorage;

import net.minecraft.core.Direction;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import com.dynamero.base.StorageConnector;
import com.dynamero.energy.base.records.EnergyStorageSerializer;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.constants.BEKeys;
import com.dynamero.shared.enumerations.MappedDirection;
import com.dynamero.shared.interfaces.StorageConnectorProvider;
import com.dynamero.shared.utils.ValueIO;

/**
 * Manages multiple sided energy storage instances with serialization, directional lookup, and capacity queries.
 *
 * @param <T> the type of energy storage managed by this connector
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
public class EnergyConnector<T extends EnergyStorage> extends StorageConnector<T> implements StorageConnectorProvider<EnergyConnector<T>>
{
    /**
     * Retrieves the stored energy amount for the given mapped direction.
     *
     * @param direction the mapped direction
     * @return the energy amount
     */
    public long getAmount(MappedDirection direction)
    {
        return getStorage(direction).getAmount();
    }

    /**
     * Retrieves the stored energy amount for the given Minecraft direction.
     *
     * @param direction the Minecraft direction
     * @return the energy amount
     */
    public long getAmount(Direction direction)
    {
        return getStorage(direction).getAmount();
    }

    /**
     * Retrieves the stored energy amount for the storage at the specified index.
     *
     * @param index the storage index
     * @return the energy amount
     */
    public long getAmount(int index)
    {
        return getStorage(index).getAmount();
    }

    /**
     * Retrieves the maximum energy capacity for the given mapped direction.
     *
     * @param direction the mapped direction
     * @return the energy capacity
     */
    public long getCapacity(MappedDirection direction)
    {
        return getStorage(direction).getCapacity();
    }

    /**
     * Retrieves the maximum energy capacity for the given Minecraft direction.
     *
     * @param direction the Minecraft direction
     * @return the energy capacity
     */
    public long getCapacity(Direction direction)
    {
        return getStorage(direction).getCapacity();
    }

    /**
     * Retrieves the maximum energy capacity for the storage at the specified index.
     *
     * @param index the storage index
     * @return the energy capacity
     */
    public long getCapacity(int index)
    {
        return getStorage(index).getCapacity();
    }

    /**
     * Retrieves this energy connector instance.
     *
     * @return this connector
     */
    @Override
    public EnergyConnector<T> getConnector()
    {
        return this;
    }

    /**
     * Saves energy storage states into the given value output.
     *
     * @param writeView the value output to write to
     */
    @Override
    public void saveAdditional(ValueOutput writeView)
    {
        for (int i = 0; i < this.storages.size(); i++)
            ValueIO.putChild(writeView, BEKeys.ENERGY_STORAGE + "." + i,
                             new EnergyStorageSerializer(this.storages.get(i)));
    }

    /**
     * Loads energy storage states from the given value input.
     *
     * @param readView the value input to read from
     */
    @Override
    public void loadAdditional(ValueInput readView)
    {
        for (int i = 0; i < this.storages.size(); i++)
            ValueIO.readChild(readView, BEKeys.ENERGY_STORAGE + "." + i,
                            new EnergyStorageSerializer(this.storages.get(i)));
    }
}