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

package com.dynamero.dispersion.base;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import com.dynamero.base.StorageConnector;
import com.dynamero.dispersion.base.interfaces.DispersionVariant;
import com.dynamero.dispersion.base.records.DispersionStackList;
import com.dynamero.dispersion.base.records.DispersionStackPayload;
import com.dynamero.dispersion.base.records.DispersionStorageSerialier;
import com.dynamero.dispersion.base.storage.SingleDispersionStorage;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.constants.BEKeys;
import com.dynamero.shared.enumerations.MappedDirection;
import com.dynamero.shared.interfaces.StorageConnectorProvider;
import com.dynamero.shared.utils.ValueIO;

/**
 * Storage connector managing multiple {@link SingleDispersionStorage} instances, providing combined storage access,
 * factory registration methods, directional queries, and persistence.
 *
 * @param <T> the single dispersion storage type managed by this connector
 */
@SuppressWarnings("unused")
@Developer("TheMentor")
@CreatedAt("2026-01-12")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
public class DispersionConnector<T extends SingleDispersionStorage> extends StorageConnector<T> implements StorageConnectorProvider<DispersionConnector<T>>
{
    /**
     * Combined storage combining all individual managed storages into a unified Fabric transfer view.
     */
    private final CombinedStorage<DispersionVariant, T> combinedStorage = new CombinedStorage<>(this.storages);

    /**
     * Retrieves the unified combined storage view over all managed dispersion storages.
     *
     * @return the combined storage
     */
    public CombinedStorage<DispersionVariant, T> getCombinedStorage()
    {
        return combinedStorage;
    }

    /**
     * Collects a snapshot list of dispersion payloads representing current variants and droplet amounts across all storages.
     *
     * @return a dispersion stack list containing all stored dispersion payloads
     */
    public DispersionStackList getDispersions()
    {
        List<DispersionStackPayload> dispersions = new ArrayList<>();
        this.storages.forEach(storage ->
                                    dispersions.add(new DispersionStackPayload(storage.getResource(),
                                                                                storage.getAmount())));
        return new DispersionStackList(dispersions);
    }

    /**
     * Creates and registers a new dispersion storage using a factory function.
     *
     * @param blockEntity the owning block entity
     * @param size        the capacity parameter passed to the factory
     * @param factory     the factory producing the storage instance
     */
    public void addStorage(BlockEntity blockEntity, int size, BiFunction<BlockEntity, Integer, T> factory)
    {
        addStorage(factory.apply(blockEntity, size));
    }

    /**
     * Creates and registers a new dispersion storage mapped to a specific mapped direction.
     *
     * @param blockEntity the owning block entity
     * @param size        the capacity parameter passed to the factory
     * @param side        the mapped direction for sided access
     * @param factory     the factory producing the storage instance
     */
    public void addStorage(BlockEntity blockEntity, int size, MappedDirection side, BiFunction<BlockEntity, Integer, T> factory)
    {
        addStorage(factory.apply(blockEntity, size), side);
    }

    /**
     * Creates and registers a new dispersion storage mapped to a specific Minecraft direction.
     *
     * @param blockEntity the owning block entity
     * @param size        the capacity parameter passed to the factory
     * @param side        the Minecraft direction for sided access
     * @param factory     the factory producing the storage instance
     */
    public void addStorage(BlockEntity blockEntity, int size, Direction side, BiFunction<BlockEntity, Integer, T> factory)
    {
        addStorage(factory.apply(blockEntity, size), side);
    }

    /**
     * Retrieves the dispersion amount stored in the storage associated with the given mapped direction.
     *
     * @param direction the mapped direction
     * @return the dispersion amount in droplets
     */
    public long getAmount(MappedDirection direction)
    {
        return getStorage(direction).getAmount();
    }

    /**
     * Retrieves the maximum dispersion capacity of the storage associated with the given mapped direction.
     *
     * @param direction the mapped direction
     * @return the capacity in droplets
     */
    public long getCapacity(MappedDirection direction)
    {
        return getStorage(direction).getCapacity();
    }

    /**
     * Retrieves the dispersion variant stored in the storage associated with the given mapped direction.
     *
     * @param direction the mapped direction
     * @return the stored dispersion variant
     */
    public DispersionVariant getVariant(MappedDirection direction)
    {
        return getStorage(direction).getResource();
    }

    /**
     * Retrieves this dispersion connector instance.
     *
     * @return this connector
     */
    @Override
    public DispersionConnector<T> getConnector()
    {
        return this;
    }

    /**
     * Saves all managed dispersion storages into the given value output.
     *
     * @param writeView the value output to write to
     */
    @Override
    public void saveAdditional(ValueOutput writeView)
    {
        for (int i = 0; i < this.storages.size(); i++)
            ValueIO.putChild(writeView, BEKeys.DISPERSION_STORAGE + "." + i,
                             new DispersionStorageSerialier<>(this.storages.get(i)));
    }

    /**
     * Loads all managed dispersion storages from the given value input.
     *
     * @param readView the value input to read from
     */
    @Override
    public void loadAdditional(ValueInput readView)
    {
        for (int i = 0; i < this.storages.size(); i++)
            ValueIO.readChild(readView, BEKeys.GAS_STORAGE + "." + i,
                            new DispersionStorageSerialier<>(this.storages.get(i)));
    }
}