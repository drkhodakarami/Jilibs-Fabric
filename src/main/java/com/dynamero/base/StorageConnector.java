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

package com.dynamero.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dynamero.shared.annotations.*;
import com.dynamero.shared.enumerations.MappedDirection;
import com.dynamero.shared.interfaces.DataSerializer;
import com.dynamero.shared.interfaces.StorageHandler;
import com.dynamero.shared.interfaces.StorageProvider;
import com.dynamero.shared.utils.DirectionHelper;
import net.minecraft.core.Direction;

/**
 * Base abstract connector that manages storage instances mapped to block directions or faces.
 *
 * @param <T> the type of storage managed by this connector
 */
@SuppressWarnings("unused")
@Developer("TheMentor")
@CreatedAt("2026-10-08")
@ModifiedAt("2026-10-08")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public abstract class StorageConnector<T> implements DataSerializer, StorageHandler<T>, StorageProvider<T>
{
	/**
	 * The ordered list of storage instances.
	 */
	protected final List<T> storages = new ArrayList<>(MappedDirection.values().length);

	/**
	 * Map of directional mappings to their corresponding storage instances.
	 */
	protected final Map<MappedDirection, T> sidedMap = new HashMap<>(MappedDirection.values().length);

	/**
	 * Adds a storage instance with no specific directional mapping.
	 *
	 * @param storage the storage instance to add
	 */
	public void addStorage(T storage)
	{
		addStorage(storage, MappedDirection.NONE);
	}

	/**
	 * Adds a storage instance mapped to a specific {@link MappedDirection}.
	 *
	 * @param storage   the storage instance to add
	 * @param direction the mapped direction
	 */
	public void addStorage(T storage, MappedDirection direction)
	{
		this.storages.add(storage);
		this.sidedMap.put(direction, storage);
	}

	/**
	 * Adds a storage instance mapped to a Minecraft {@link Direction}.
	 *
	 * @param storage   the storage instance to add
	 * @param direction the Minecraft direction
	 */
	public void addStorage(T storage, Direction direction)
	{
		this.storages.add(storage);
		this.sidedMap.put(MappedDirection.fromDirection(direction), storage);
	}

	/**
	 * Retrieves the list of all registered storage instances.
	 *
	 * @return the list of storages
	 */
	public List<T> getStorages()
	{
		return this.storages;
	}

	/**
	 * Retrieves the directional storage mapping.
	 *
	 * @return the sided storage map
	 */
	public Map<MappedDirection, T> getSidedMap()
	{
		return this.sidedMap;
	}

	/**
	 * Retrieves the storage mapped to the specified {@link MappedDirection}.
	 *
	 * @param side the mapped direction
	 * @return the storage instance, or null if none mapped
	 */
	public T getStorage(MappedDirection side)
	{
		return this.sidedMap.get(side);
	}

	/**
	 * Retrieves the storage mapped to the specified Minecraft {@link Direction}.
	 *
	 * @param side the direction
	 * @return the storage instance, or null if none mapped
	 */
	public T getStorage(Direction side)
	{
		return this.sidedMap.get(MappedDirection.fromDirection(side));
	}

	/**
	 * Retrieves the storage at the given index in the list.
	 *
	 * @param index the index in the storage list
	 * @return the storage instance
	 */
	public T getStorage(int index)
	{
		return this.storages.get(index);
	}

	/**
	 * Resolves the relative storage provider taking block facing into account.
	 *
	 * @param direction the target mapped direction
	 * @param facing    the block's facing direction
	 * @return the matching storage instance, or null if not found
	 */
	public T getStorageProvider(MappedDirection direction, Direction facing)
	{
		Direction side = DirectionHelper.relativeDirection(MappedDirection.toDirection(direction), facing);
		if(this.getSidedMap().containsKey(MappedDirection.fromDirection(side)))
			return getStorage(side);
		return null;
	}

	/**
	 * Resolves the relative storage provider taking block facing into account.
	 *
	 * @param direction the target Minecraft direction
	 * @param facing    the block's facing direction
	 * @return the matching storage instance, or null if not found
	 */
	public T getStorageProvider(Direction direction, Direction facing)
	{
		Direction side = DirectionHelper.relativeDirection(direction, facing);
		if(this.getSidedMap().containsKey(MappedDirection.fromDirection(side)))
			return getStorage(side);
		return null;
	}
}