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

package com.dynamero.machines;

import java.util.Objects;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

import com.dynamero.energy.storage.SyncedEnergyStorage;
import com.dynamero.fluid.storage.SyncedFluidStorage;
import com.dynamero.inventory.storage.OutputInventory;
import com.dynamero.inventory.storage.SyncedInventory;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.enumerations.MappedDirection;
import com.dynamero.shared.interfaces.ContainerDataProvider;

/**
 * Basic machine block entity providing predefined input, output, and upgrade inventory slots,
 * synchronized fluid and energy storage, recipe processing progress tracking, and container data synchronization.
 *
 * @param <T> the concrete machine block entity type extending this base class
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
public abstract class AbstractBasicMachineBE<T extends AbstractBasicMachineBE<T>>
        extends AbstractBaseMachineBE<T, @NotNull SyncedInventory, @NotNull SyncedFluidStorage, SyncedEnergyStorage>
        implements ContainerDataProvider
{
    /**
     * Synced fluid storage instance.
     */
    protected SyncedFluidStorage fluidStorage;

    /**
     * Synced energy storage instance.
     */
    protected SyncedEnergyStorage energyStorage;

    /**
     * Container data delegate syncing progress and max progress with the client screen handler.
     */
    protected final ContainerData containerData;

    /**
     * Count of installed energy upgrades.
     */
    protected int energyUpgradeCount;

    /**
     * Count of installed fluid capacity upgrades.
     */
    protected int fluidUpgradeCount;

    /**
     * Count of installed speed upgrades.
     */
    protected int speedUpgradeCount;

    /**
     * Current machine operation progress in ticks.
     */
    protected int progress;

    /**
     * Total ticks required to complete the current operation.
     */
    protected int maxProgress;

    /**
     * Number of integer properties tracked by {@link #containerData}.
     */
    public static final int DELEGATE_SIZE = 2;

    //region INVENTORIES
    /**
     * Index of the output inventory in the inventory connector.
     */
    public static final int OUTPUT_INVENTORY_INDEX = 0;

    /**
     * Index of the input inventory in the inventory connector.
     */
    public static final int INPUT_INVENTORY_INDEX = 1;

    /**
     * Index of the upgrade inventory in the inventory connector.
     */
    public static final int UPGRADE_INVENTORY_INDEX = 2;

    //OUTPUT INVENTORY
    /**
     * Slot index for the primary crafted result item in the output inventory.
     */
    public static final int RESULT_OUTPUT_SLOT = 0;

    /**
     * Slot index for empty container/bucket returns in the output inventory.
     */
    public static final int BUCKET_OUTPUT_SLOT = 1;

    //INPUT INVENTORY
    /**
     * Slot index for raw input items in the input inventory.
     */
    public static final int INGREDIENT_INPUT_SLOT = 0;

    /**
     * Slot index for fluid containers/buckets in the input inventory.
     */
    public static final int BUCKET_INPUT_SLOT = 1;

    //UPGRADE INVENTORY
    /**
     * Slot index for energy efficiency upgrades in the upgrade inventory.
     */
    public static final int ENERGY_UPGRADE_SLOT = 0;

    /**
     * Slot index for speed upgrades in the upgrade inventory.
     */
    public static final int SPEED_UPGRADE_SLOT = 1;

    /**
     * Slot index for fluid capacity upgrades in the upgrade inventory.
     */
    public static final int FLUID_UPGRADE_SLOT = 2;
    //endregion

    /**
     * Constructs an AbstractBasicMachineBE initializing inventories, fluid/energy storage, and container data.
     *
     * @param type  the block entity type
     * @param pos   the block position in the world
     * @param state the initial block state
     */
    public AbstractBasicMachineBE(BlockEntityType<@NotNull T> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);

        this.energyUpgradeCount = 0;
        this.fluidUpgradeCount = 0;
        this.speedUpgradeCount = 0;

        this.progress = 0;
        this.maxProgress = 0;

        if(outputInventorySize() > 0)
            this.inventoryConnector.addStorage(this, outputInventorySize(), inventoryOutputDirection(), OutputInventory::new);
        if(inputInventorySize() > 0)
            this.inventoryConnector.addStorage(this, inputInventorySize(), inventoryInputDirection(), SyncedInventory::new);
        if(upgradeInventorySize() > 0)
            this.inventoryConnector.addStorage(this, upgradeInventorySize(), inventoryUpgradeDirection(), SyncedInventory::new);

        if(hasFluidStorage())
        {
            fluidStorage = createFluidStorage();
            this.fluidConnector.addStorage(fluidStorage, fluidIODirection());
        }
        if(hasEnergyStorage())
        {
            energyStorage = createEnergyStorage();
            this.energyConnector.addStorage(energyStorage, energyIODirection());
        }

        containerData = createContainerData();
    }

    //region overrides
    /**
     * Retrieves the container data delegate syncing machine state with client screens.
     *
     * @return the container data delegate
     */
    @Override
    public ContainerData getContainerData()
    {
        return containerData;
    }

    /**
     * Retrieves the number of integer properties tracked by the container data delegate.
     *
     * @return the delegate size
     */
    @Override
    public int getContainerDataSize()
    {
        return DELEGATE_SIZE;
    }
    //endregion

    //region public methods
    //region FLUID
    /**
     * Retrieves the synced fluid storage instance.
     *
     * @return the fluid storage
     */
    public SyncedFluidStorage getFluidStorage()
    {
        return fluidStorage;
    }

    /**
     * Retrieves the current stored fluid amount in droplet units.
     *
     * @return fluid amount in droplets
     */
    public long getFluidAmount()
    {
        return hasFluidStorage() && this.fluidStorage != null ? this.fluidStorage.getAmount() : 0;
    }

    /**
     * Sets the stored fluid amount directly.
     *
     * @param amount fluid amount in droplets
     */
    public void setFluidAmount(long amount)
    {
        if(hasFluidStorage() && this.fluidStorage != null)
            this.fluidStorage.amount = amount;
    }

    /**
     * Retrieves the maximum fluid capacity in droplet units.
     *
     * @return fluid capacity in droplets
     */
    public long getFluidCapacity()
    {
        return hasFluidStorage() && this.fluidStorage != null ? this.fluidStorage.getCapacity() : 0;
    }
    //endregion
    //region ENERGY
    /**
     * Retrieves the synced energy storage instance.
     *
     * @return the energy storage
     */
    public SyncedEnergyStorage getEnergyStorage()
    {
        return energyStorage;
    }

    /**
     * Retrieves the currently stored energy amount.
     *
     * @return the stored energy amount
     */
    public long getEnergyAmount()
    {
        return hasEnergyStorage() && this.energyStorage != null ? this.energyStorage.getAmount() : 0;
    }

    /**
     * Sets the stored energy amount directly.
     *
     * @param amount energy amount to set
     */
    public void setEnergyAmount(long amount)
    {
        if(hasEnergyStorage() && this.energyStorage != null)
            this.energyStorage.amount = amount;
    }

    /**
     * Retrieves the maximum energy capacity.
     *
     * @return the energy capacity
     */
    public long getEnergyCapacity()
    {
        return hasEnergyStorage() && this.energyStorage != null ? this.energyStorage.getCapacity() : 0;
    }
    //endregion
    //region PROGRESS
    /**
     * Retrieves the current processing progress in ticks.
     *
     * @return current progress ticks
     */
    public int getProgress()
    {
        return this.progress;
    }

    /**
     * Retrieves the maximum processing ticks needed to complete the current operation.
     *
     * @return maximum progress ticks
     */
    public int getMaxProgress()
    {
        return this.maxProgress;
    }

    /**
     * Increments the current processing progress by one tick and marks the block entity dirty.
     */
    public void increaseProgress()
    {
        this.progress++;
        update();
    }

    /**
     * Resets both progress and max progress to zero and marks the block entity dirty.
     */
    public void resetProgress()
    {
        this.progress = 0;
        this.maxProgress = 0;
        update();
    }

    /**
     * Sets the maximum processing ticks and marks the block entity dirty.
     *
     * @param amount maximum progress ticks
     */
    public void setMaxProgress(int amount)
    {
        this.maxProgress = amount;
        update();
    }
    //endregion
    //region UPGRADES
    /**
     * Retrieves the count of fluid upgrade items in the upgrade inventory.
     *
     * @return count of fluid upgrades
     */
    public long getFluidUpgradeCount()
    {
        if(getUpgradeInventory() == null ||
        getUpgradeInventory().getSlot(FLUID_UPGRADE_SLOT) == null)
                return 0;

        ItemStack stack = Objects.requireNonNull(getUpgradeInventory().getSlot(FLUID_UPGRADE_SLOT)).get();

        if(stack.isEmpty())
            return 0;

        return stack.getCount();
    }

    /**
     * Retrieves the count of energy upgrade items in the upgrade inventory.
     *
     * @return count of energy upgrades
     */
    public long getEnergyUpgradeCount()
    {
        if(getUpgradeInventory() == null ||
        getUpgradeInventory().getSlot(ENERGY_UPGRADE_SLOT) == null)
            return 0;

        var stack = Objects.requireNonNull(getUpgradeInventory().getSlot(ENERGY_UPGRADE_SLOT)).get();

        if(stack.isEmpty())
            return 0;

        return stack.getCount();
    }

    /**
     * Retrieves the count of speed upgrade items in the upgrade inventory.
     *
     * @return count of speed upgrades
     */
    public int getSpeedUpgradeCount()
    {
        if(getUpgradeInventory() == null ||
        getUpgradeInventory().getSlot(SPEED_UPGRADE_SLOT) == null)
            return 0;

        var stack = Objects.requireNonNull(getUpgradeInventory().getSlot(SPEED_UPGRADE_SLOT)).get();

        if(stack.isEmpty())
            return 0;

        return stack.getCount();
    }
    //endregion
    //region INVENTORIES
    /**
     * Retrieves the output inventory instance.
     *
     * @return the output inventory
     */
    public OutputInventory getOutputInventory()
    {
        return (OutputInventory) this.inventoryConnector.getInventory(OUTPUT_INVENTORY_INDEX);
    }

    /**
     * Retrieves the input inventory instance.
     *
     * @return the input inventory
     */
    public SyncedInventory getInputInventory()
    {
        return this.inventoryConnector.getInventory(INPUT_INVENTORY_INDEX);
    }

    /**
     * Retrieves the upgrade inventory instance.
     *
     * @return the upgrade inventory
     */
    public SyncedInventory getUpgradeInventory()
    {
        return this.inventoryConnector.getInventory(UPGRADE_INVENTORY_INDEX);
    }

    //region INVENTORY DIRECTION
    /**
     * Specifies the mapped direction for automatic extraction of output inventory items.
     *
     * @return the output inventory direction (defaults to {@link MappedDirection#DOWN})
     */
    public MappedDirection inventoryOutputDirection()
    {
        return MappedDirection.DOWN;
    }

    /**
     * Specifies the mapped direction for inserting/extracting upgrade items.
     *
     * @return the upgrade inventory direction (defaults to {@link MappedDirection#EAST})
     */
    public MappedDirection inventoryUpgradeDirection()
    {
        return MappedDirection.EAST;
    }

    /**
     * Specifies the mapped direction for automatic insertion of input inventory items.
     *
     * @return the input inventory direction (defaults to {@link MappedDirection#UP})
     */
    public MappedDirection inventoryInputDirection()
    {
        return MappedDirection.UP;
    }
    //endregion
    /**
     * Computes the total number of inventory slots across output, input, and upgrade inventories.
     *
     * @return total slot count
     */
    public int totalInventorySize()
    {
        return outputInventorySize() + inputInventorySize() + upgradeInventorySize();
    }

    //region STORAGE DIRECTION
    /**
     * Specifies the mapped direction for energy transfer.
     *
     * @return the energy IO direction (defaults to {@link MappedDirection#WEST})
     */
    public MappedDirection energyIODirection()
    {
        return MappedDirection.WEST;
    }

    /**
     * Specifies the mapped direction for fluid transfer.
     *
     * @return the fluid IO direction (defaults to {@link MappedDirection#SOUTH})
     */
    public MappedDirection fluidIODirection()
    {
        return MappedDirection.SOUTH;
    }
    //endregion
    //endregion
    //endregion

    //region protected methods
    /**
     * Creates the ContainerData delegate instance for syncing progress and maxProgress with client menus.
     *
     * @return the container data delegate
     */
    protected ContainerData createContainerData()
    {
        return new ContainerData()
        {
            @Override
            public int get(int index)
            {
                return switch (index)
                {
                    case 0 -> AbstractBasicMachineBE.this.progress;
                    case 1 -> AbstractBasicMachineBE.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value)
            {
                switch (index)
                {
                    case 0 -> AbstractBasicMachineBE.this.progress = value;
                    case 1 -> AbstractBasicMachineBE.this.maxProgress = value;
                }
            }

            @Override
            public int getCount()
            {
                return DELEGATE_SIZE;
            }
        };
    }

    //region INVENTORY SIZES
    /**
     * Declares the size of the upgrade inventory.
     *
     * @return upgrade slot count (defaults to 0)
     */
    protected int upgradeInventorySize()
    {
        return 0;
    }

    /**
     * Declares the size of the input inventory.
     *
     * @return input slot count (defaults to 0)
     */
    protected int inputInventorySize()
    {
        return 0;
    }

    /**
     * Declares the size of the output inventory.
     *
     * @return output slot count (defaults to 0)
     */
    protected int outputInventorySize()
    {
        return 0;
    }
    //endregion
    //region FLUID
    /**
     * Indicates whether this machine possesses a fluid storage container.
     *
     * @return true if fluid storage is enabled, false otherwise
     */
    protected boolean hasFluidStorage()
    {
        return false;
    }

    /**
     * Instantiates the synchronized fluid storage for this machine.
     *
     * @return a new SyncedFluidStorage instance
     */
    protected SyncedFluidStorage createFluidStorage()
    {
        return new SyncedFluidStorage(this, getBaseFluidCapacity())
        {
            @SuppressWarnings("unchecked")
            @Override
            public @NotNull FluidVariant getResource()
            {
                T be = ((T)getBlockEntity());
                return be.fluidStorageVariant();
            }
        };
    }

    /**
     * Declares the accepted/stored fluid variant for the fluid container.
     *
     * @return the fluid variant (defaults to water)
     */
    protected FluidVariant fluidStorageVariant()
    {
        return FluidVariant.of(Fluids.WATER);
    }

    /**
     * Declares the baseline fluid storage capacity in droplet units.
     *
     * @return baseline fluid capacity in droplets
     */
    protected long getBaseFluidCapacity()
    {
        return FluidConstants.BUCKET * 10;
    }
    //endregion
    //region ENERGY
    /**
     * Indicates whether this machine possesses an energy storage container.
     *
     * @return true if energy storage is enabled, false otherwise
     */
    protected boolean hasEnergyStorage()
    {
        return false;
    }

    /**
     * Instantiates the synchronized energy storage for this machine.
     *
     * @return a new SyncedEnergyStorage instance
     */
    protected SyncedEnergyStorage createEnergyStorage()
    {
        return new SyncedEnergyStorage(this, getBaseEnergyCapacity(), getBaseEnergyInsertionAmount(), getBaseEnergyExtractionAmount());
    }

    /**
     * Declares the maximum energy insertion rate per transaction.
     *
     * @return baseline max energy insertion rate
     */
    protected long getBaseEnergyInsertionAmount()
    {
        return 1_000;
    }

    /**
     * Declares the maximum energy extraction rate per transaction.
     *
     * @return baseline max energy extraction rate
     */
    protected long getBaseEnergyExtractionAmount()
    {
        return 0;
    }

    /**
     * Declares the baseline energy capacity.
     *
     * @return baseline energy capacity
     */
    protected long getBaseEnergyCapacity()
    {
        return 100_000;
    }
    //endregion
    //region UPGRADE ITEMS
    /**
     * Returns the Item used as an energy efficiency upgrade.
     *
     * @return the energy upgrade Item
     */
    protected Item getEnergyUpgradeItem()
    {
        return ItemStack.EMPTY.getItem();
    }

    /**
     * Returns the Item used as a fluid capacity upgrade.
     *
     * @return the fluid upgrade Item
     */
    protected Item getFluidUpgradeItem()
    {
        return ItemStack.EMPTY.getItem();
    }

    /**
     * Returns the Item used as an operating speed upgrade.
     *
     * @return the speed upgrade Item
     */
    protected Item getSpeedUpgradeItem()
    {
        return ItemStack.EMPTY.getItem();
    }
    //endregion
    //endregion
}