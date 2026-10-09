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

package com.dynamero.inventory.base;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import net.fabricmc.fabric.api.transfer.v1.item.ContainerStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import oshi.util.tuples.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import com.dynamero.base.StorageConnector;
import com.dynamero.inventory.record.InventoryStorageSerializer;
import com.dynamero.inventory.record.PredicateInventoryStorage;
import com.dynamero.inventory.storage.RecipeInventory;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.constants.BEKeys;
import com.dynamero.shared.enumerations.MappedDirection;
import com.dynamero.shared.interfaces.StorageConnectorProvider;
import com.dynamero.shared.interfaces.StorageProvider;
import com.dynamero.shared.utils.DirectionHelper;
import com.dynamero.shared.utils.ValueIO;

/**
 * Storage connector managing multiple {@link SimpleContainer} inventories and their {@link ContainerStorage} wrappers,
 * providing sided mappings, predicate restrictions, combined transfer storage, and persistence.
 *
 * @param <T> the container inventory type
 */
@SuppressWarnings("unused")
@Developer("TurtyWurty")
@CreatedAt("2026-08-10")
@ModifiedAt("2026-08-10")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public class InventoryConnector<T extends SimpleContainer> extends StorageConnector<ContainerStorage>
    implements StorageConnectorProvider<InventoryConnector<T>>, StorageProvider<ContainerStorage>
{
    /**
     * List of all managed container inventories.
     */
    private final List<T> inventories = new ArrayList<>();

    /**
     * List of pairs associating mapped directions with their corresponding inventories.
     */
    private final List<Pair<MappedDirection, T>> sidedInventories = new ArrayList<>();

    /**
     * Combined storage view over all managed container storages.
     */
    private final CombinedStorage<ItemVariant, @NotNull ContainerStorage> combinedStorage = new CombinedStorage<>(this.storages);

    //region ADD INVENTORY
    /**
     * Registers an inventory without directional mapping.
     *
     * @param inventory the container inventory to add
     */
    public void addStorage(@NotNull T inventory) {
        addStorage(inventory, MappedDirection.NONE);
    }

    /**
     * Registers an inventory mapped to a specific mapped direction.
     *
     * @param inventory the container inventory to add
     * @param side      the mapped direction
     */
    public void addStorage(@NotNull T inventory, MappedDirection side)
    {
        this.inventories.add(inventory);
        this.sidedInventories.add(new Pair<>(side, inventory));
        var storage = ContainerStorage.of(inventory, MappedDirection.toDirection(side));
        addStorage(storage, side);
    }

    /**
     * Registers an inventory mapped to a specific Minecraft direction.
     *
     * @param inventory the container inventory to add
     * @param side      the Minecraft direction
     */
    public void addStorage(@NotNull T inventory, Direction side)
    {
        addStorage(inventory, MappedDirection.fromDirection(side));
    }

    /**
     * Registers an inventory without directional mapping, constrained by insertion and extraction suppliers.
     *
     * @param inventory the container inventory to add
     * @param canInsert insertion permission supplier
     * @param canExtract extraction permission supplier
     */
    public void addStorage(@NotNull T inventory, Supplier<Boolean> canInsert, Supplier<Boolean> canExtract) {
        addStorage(inventory, MappedDirection.NONE, canInsert, canExtract);
    }

    /**
     * Registers an inventory mapped to a specific mapped direction, constrained by insertion and extraction suppliers.
     *
     * @param inventory  the container inventory to add
     * @param side       the mapped direction
     * @param canInsert  insertion permission supplier
     * @param canExtract extraction permission supplier
     */
    public void addStorage(@NotNull T inventory, MappedDirection side, Supplier<Boolean> canInsert, Supplier<Boolean> canExtract)
    {
        this.inventories.add(inventory);
        this.sidedInventories.add(new Pair<>(side, inventory));
        var storage = PredicateInventoryStorage.of(ContainerStorage.of(inventory, MappedDirection.toDirection(side)), canInsert, canExtract);
        addStorage(storage, side);
    }

    /**
     * Registers an inventory mapped to a specific Minecraft direction, constrained by insertion and extraction suppliers.
     *
     * @param inventory  the container inventory to add
     * @param side       the Minecraft direction
     * @param canInsert  insertion permission supplier
     * @param canExtract extraction permission supplier
     */
    public void addStorage(@NotNull T inventory, Direction side, Supplier<Boolean> canInsert, Supplier<Boolean> canExtract)
    {
        this.addStorage(inventory, MappedDirection.fromDirection(side), canInsert, canExtract);
    }

    /**
     * Creates and registers an inventory using a size parameter and factory.
     *
     * @param size    the slot count
     * @param factory the factory creating the inventory
     */
    public void addStorage(int size, Function<Integer, T> factory)
    {
        addStorage(factory.apply(size));
    }

    /**
     * Creates and registers an inventory mapped to a mapped direction using size and factory.
     *
     * @param size    the slot count
     * @param side    the mapped direction
     * @param factory the factory creating the inventory
     */
    public void addStorage(int size, MappedDirection side, Function<Integer, T> factory)
    {
        addStorage(factory.apply(size), side);
    }

    /**
     * Creates and registers an inventory mapped to a Minecraft direction using size and factory.
     *
     * @param size    the slot count
     * @param side    the Minecraft direction
     * @param factory the factory creating the inventory
     */
    public void addStorage(int size, Direction side, Function<Integer, T> factory)
    {
        addStorage(factory.apply(size), side);
    }

    /**
     * Creates and registers an inventory populated with initial item stacks.
     *
     * @param factory the factory creating the inventory
     * @param items   the initial item stacks
     */
    public void addStorage(Function<ItemStack[], T> factory, ItemStack... items)
    {
        addStorage(factory.apply(items));
    }

    /**
     * Creates and registers an inventory mapped to a mapped direction with initial item stacks.
     *
     * @param side    the mapped direction
     * @param factory the factory creating the inventory
     * @param items   the initial item stacks
     */
    public void addStorage(MappedDirection side, Function<ItemStack[], T> factory, ItemStack... items)
    {
        addStorage(factory.apply(items), side);
    }

    /**
     * Creates and registers an inventory mapped to a Minecraft direction with initial item stacks.
     *
     * @param side    the Minecraft direction
     * @param factory the factory creating the inventory
     * @param items   the initial item stacks
     */
    public void addStorage(Direction side, Function<ItemStack[], T> factory, ItemStack... items)
    {
        addStorage(factory.apply(items), side);
    }

    /**
     * Creates and registers an inventory linked to a block entity.
     *
     * @param blockEntity the owning block entity
     * @param size        the slot count
     * @param factory     the factory creating the inventory
     */
    public void addStorage(BlockEntity blockEntity, int size, BiFunction<BlockEntity, Integer, T> factory)
    {
        addStorage(factory.apply(blockEntity, size));
    }

    /**
     * Creates and registers an inventory linked to a block entity and mapped to a mapped direction.
     *
     * @param blockEntity the owning block entity
     * @param size        the slot count
     * @param side        the mapped direction
     * @param factory     the factory creating the inventory
     */
    public void addStorage(BlockEntity blockEntity, int size, MappedDirection side, BiFunction<BlockEntity, Integer, T> factory)
    {
        addStorage(factory.apply(blockEntity, size), side);
    }

    /**
     * Creates and registers an inventory linked to a block entity and mapped to a Minecraft direction.
     *
     * @param blockEntity the owning block entity
     * @param size        the slot count
     * @param side        the Minecraft direction
     * @param factory     the factory creating the inventory
     */
    public void addStorage(BlockEntity blockEntity, int size, Direction side, BiFunction<BlockEntity, Integer, T> factory)
    {
        addStorage(factory.apply(blockEntity, size), side);
    }

    /**
     * Creates and registers an inventory linked to a block entity with initial item stacks.
     *
     * @param blockEntity the owning block entity
     * @param factory     the factory creating the inventory
     * @param items       the initial item stacks
     */
    public void addStorage(BlockEntity blockEntity, BiFunction<BlockEntity, ItemStack[], T> factory, ItemStack... items)
    {
        addStorage(factory.apply(blockEntity, items));
    }

    /**
     * Creates and registers an inventory linked to a block entity and mapped to a mapped direction with initial item stacks.
     *
     * @param blockEntity the owning block entity
     * @param side        the mapped direction
     * @param factory     the factory creating the inventory
     * @param items       the initial item stacks
     */
    public void addStorage(BlockEntity blockEntity, MappedDirection side, BiFunction<BlockEntity, ItemStack[], T> factory, ItemStack... items)
    {
        addStorage(factory.apply(blockEntity, items), side);
    }

    /**
     * Creates and registers an inventory linked to a block entity and mapped to a Minecraft direction with initial item stacks.
     *
     * @param blockEntity the owning block entity
     * @param side        the Minecraft direction
     * @param factory     the factory creating the inventory
     * @param items       the initial item stacks
     */
    public void addStorage(BlockEntity blockEntity, Direction side, BiFunction<BlockEntity, ItemStack[], T> factory, ItemStack... items)
    {
        addStorage(factory.apply(blockEntity, items), side);
    }
    //endregion

    //region ADD INSERT / EXTRACT ONLY
    /**
     * Registers an insertion-only inventory mapped to a mapped direction.
     *
     * @param inventory the container inventory
     * @param side      the mapped direction
     */
    public void addInsertOnlyInventory(@NotNull T inventory, MappedDirection side)
    {
        addInsertOnlyInventory(inventory, side, () -> true);
    }

    /**
     * Registers an insertion-only inventory mapped to a mapped direction with custom insertion condition.
     *
     * @param inventory the container inventory
     * @param side      the mapped direction
     * @param canInsert insertion permission supplier
     */
    public void addInsertOnlyInventory(@NotNull T inventory, MappedDirection side, Supplier<Boolean> canInsert)
    {
        addStorage(inventory, side, canInsert, () -> false);
    }

    /**
     * Registers an extraction-only inventory mapped to a mapped direction.
     *
     * @param inventory the container inventory
     * @param side      the mapped direction
     */
    public void addExtractOnlyInventory(@NotNull T inventory, MappedDirection side)
    {
        addExtractOnlyInventory(inventory, side, () -> true);
    }

    /**
     * Registers an extraction-only inventory mapped to a mapped direction with custom extraction condition.
     *
     * @param inventory  the container inventory
     * @param side       the mapped direction
     * @param canExtract extraction permission supplier
     */
    public void addExtractOnlyInventory(@NotNull T inventory, MappedDirection side, Supplier<Boolean> canExtract)
    {
        addStorage(inventory, side, () -> false, canExtract);
    }

    /**
     * Registers an insertion-only inventory mapped to a Minecraft direction.
     *
     * @param inventory the container inventory
     * @param side      the Minecraft direction
     */
    public void addInsertOnlyInventory(@NotNull T inventory, Direction side)
    {
        addInsertOnlyInventory(inventory, side, () -> true);
    }

    /**
     * Registers an insertion-only inventory mapped to a Minecraft direction with custom insertion condition.
     *
     * @param inventory the container inventory
     * @param side      the Minecraft direction
     * @param canInsert insertion permission supplier
     */
    public void addInsertOnlyInventory(@NotNull T inventory, Direction side, Supplier<Boolean> canInsert)
    {
        addStorage(inventory, side, canInsert, () -> false);
    }

    /**
     * Registers an extraction-only inventory mapped to a Minecraft direction.
     *
     * @param inventory the container inventory
     * @param side      the Minecraft direction
     */
    public void addExtractOnlyInventory(@NotNull T inventory, Direction side)
    {
        addExtractOnlyInventory(inventory, side, () -> true);
    }

    /**
     * Registers an extraction-only inventory mapped to a Minecraft direction with custom extraction condition.
     *
     * @param inventory  the container inventory
     * @param side       the Minecraft direction
     * @param canExtract extraction permission supplier
     */
    public void addExtractOnlyInventory(@NotNull T inventory, Direction side, Supplier<Boolean> canExtract)
    {
        addStorage(inventory, side, () -> false, canExtract);
    }
    //endregion

    /**
     * Retrieves the list of all registered container inventories.
     *
     * @return list of inventories
     */
    public List<T> getInventories()
    {
        return inventories;
    }

    /**
     * Retrieves the combined Fabric storage view over all managed inventories.
     *
     * @return the combined storage
     */
    public CombinedStorage<ItemVariant, @NotNull ContainerStorage> getCombinedStorage()
    {
        return combinedStorage;
    }

    //region GET INVENTORY
    /**
     * Retrieves the inventory at the specified index.
     *
     * @param index the inventory index
     * @return the container inventory, or null
     */
    public @Nullable T getInventory(int index)
    {
        return this.inventories.get(index);
    }

    /**
     * Retrieves the inventory mapped to the specified mapped direction.
     *
     * @param side the mapped direction
     * @return the container inventory, or null
     */
    public @Nullable T getInventory(MappedDirection side)
    {
        return this.inventories.get(this.storages.indexOf(getStorage(side)));
    }

    /**
     * Retrieves the inventory mapped to the specified Minecraft direction.
     *
     * @param side the Minecraft direction
     * @return the container inventory, or null
     */
    public @Nullable T getInventory(Direction side)
    {
        return this.inventories.get(this.storages.indexOf(getStorage(side)));
    }
    //endregion

    //region GET SLOT(S)
    /**
     * Retrieves a single slot storage at the specified slot index for the given mapped direction.
     *
     * @param slotIndex the slot index
     * @param side      the mapped direction
     * @return the slot storage, or null
     */
    public @Nullable SingleSlotStorage<ItemVariant> getSlot(int slotIndex, MappedDirection side)
    {
        return this.getStorage(side).getSlot(slotIndex);
    }

    /**
     * Retrieves a single slot storage at the specified slot index for the given Minecraft direction.
     *
     * @param slotIndex the slot index
     * @param side      the Minecraft direction
     * @return the slot storage, or null
     */
    public @Nullable SingleSlotStorage<ItemVariant> getSlot(int slotIndex, Direction side)
    {
        return this.getStorage(side).getSlot(slotIndex);
    }

    /**
     * Retrieves a single slot storage at the specified slot index from the storage at the given index.
     *
     * @param slotIndex the slot index
     * @param index     the storage index
     * @return the slot storage, or null
     */
    public @Nullable SingleSlotStorage<ItemVariant> getSlot(int slotIndex, int index)
    {
        return this.getStorage(index).getSlot(slotIndex);
    }

    /**
     * Retrieves all slot storages for the specified mapped direction.
     *
     * @param side the mapped direction
     * @return list of slot storages, or null
     */
    public @Nullable List<SingleSlotStorage<ItemVariant>> getSlots(MappedDirection side)
    {
        return this.getStorage(side).getSlots();
    }

    /**
     * Retrieves all slot storages for the specified Minecraft direction.
     *
     * @param side the Minecraft direction
     * @return list of slot storages, or null
     */
    public @Nullable List<SingleSlotStorage<ItemVariant>> getSlots(Direction side)
    {
        return this.getStorage(side).getSlots();
    }

    /**
     * Retrieves all slot storages from the storage at the given index.
     *
     * @param index the storage index
     * @return list of slot storages, or null
     */
    public @Nullable List<SingleSlotStorage<ItemVariant>> getSlots(int index)
    {
        return this.getStorage(index).getSlots();
    }
    //endregion

    /**
     * Collects all item stacks currently present across all managed inventories.
     *
     * @return list of item stacks
     */
    public @NotNull List<ItemStack> getStacks()
    {
        List<ItemStack> stacks = new ArrayList<>();
        for (T inventory : this.inventories)
            for(int i = 0; i < inventory.getContainerSize(); i++)
                stacks.add(inventory.getItem(i));
        return stacks;
    }

    /**
     * Asserts that the total container size across all inventories matches the expected size.
     *
     * @param size the expected total size
     * @throws IllegalArgumentException if size does not match
     */
    public void checkSize(int size)
    {
        int invSize = this.inventories.stream().map(Container::getContainerSize).reduce(0, Integer::sum);
        if( invSize != size)
            throw new IllegalArgumentException("Sie of inventories does not match the size provided: " + invSize + " => " + size);
    }

    /**
     * Notifies all managed inventories that a player opened them.
     *
     * @param player the player opening the inventories
     */
    public void onOpen(@NotNull Player player)
    {
        this.inventories.forEach(inventory -> inventory.startOpen(player));
    }

    /**
     * Notifies all managed inventories that a player closed them.
     *
     * @param player the player closing the inventories
     */
    public void removed(@NotNull Player player)
    {
        this.inventories.forEach(inventory -> inventory.stopOpen(player));
    }

    /**
     * Drops all contained items into the world at the given position.
     *
     * @param world the level
     * @param pos   the block position
     */
    public void dropContent(@NotNull Level world, @NotNull BlockPos pos)
    {
        this.inventories.forEach(inventory -> Containers.dropContents(world, pos, inventory));
    }

    /**
     * Constructs a {@link RecipeInventory} snapshot from all items in this connector.
     *
     * @return a recipe inventory instance
     */
    public RecipeInventory getRecipeInventory()
    {
        return new RecipeInventory(getStacks().toArray(new ItemStack[0]));
    }

    /**
     * Retrieves the list of pairs associating mapped directions with their inventories.
     *
     * @return list of direction-inventory pairs
     */
    public List<Pair<MappedDirection, T>> getSidedInventories()
    {
        return this.sidedInventories;
    }

    /**
     * Retrieves this inventory connector instance.
     *
     * @return this connector
     */
    @Override
    public InventoryConnector<T> getConnector()
    {
        return this;
    }

    /**
     * Retrieves the container storage provider for the given mapped direction and block facing.
     *
     * @param direction the mapped direction
     * @param facing    the block facing direction
     * @return the container storage, or null
     */
    @Override
    public ContainerStorage getStorageProvider(MappedDirection direction, Direction facing)
    {

        Direction side = DirectionHelper.relativeDirection(MappedDirection.toDirection(direction), facing);
        if(this.getSidedMap().containsKey(MappedDirection.fromDirection(side)))
            return getStorage(side);
        return null;
    }

    /**
     * Retrieves the container storage provider for the given world direction and block facing.
     *
     * @param direction the world direction
     * @param facing    the block facing direction
     * @return the container storage, or null
     */
    @Override
    public ContainerStorage getStorageProvider(Direction direction, Direction facing)
    {
        Direction side = DirectionHelper.relativeDirection(direction, facing);
        if(this.getSidedMap().containsKey(MappedDirection.fromDirection(side)))
            return getStorage(side);
        return null;
    }

    /**
     * Saves all managed inventories to the value output.
     *
     * @param writeView the value output to write to
     */
    @Override
    public void saveAdditional(ValueOutput writeView)
    {
        for (int i = 0; i < this.inventories.size(); i++)
            ValueIO.putChild(writeView, BEKeys.INVETORY_STORAGE + "." + i,
                             new InventoryStorageSerializer<>(this.inventories.get(i)));
    }

    /**
     * Loads all managed inventories from the value input.
     *
     * @param readView the value input to read from
     */
    @Override
    public void loadAdditional(ValueInput readView)
    {
        for (int i = 0; i < this.inventories.size(); i++)
            ValueIO.readChild(readView, BEKeys.INVETORY_STORAGE + "." + i,
                            new InventoryStorageSerializer<>(this.inventories.get(i)));
    }
}