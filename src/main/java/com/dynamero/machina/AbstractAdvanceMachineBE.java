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

package com.dynamero.machina;

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
import com.dynamero.gas.storage.SyncedGasStorage;
import com.dynamero.heat.storage.SyncedHeatStorage;
import com.dynamero.inventory.storage.OutputInventory;
import com.dynamero.inventory.storage.SyncedInventory;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.enumerations.MappedDirection;
import com.dynamero.shared.interfaces.ContainerDataProvider;

/**
 * Advanced machine block entity extending {@link com.dynamero.machina.AbstractExtendedMachineBE} with full
 * multi-subsystem storage (Fluid, Energy, Heat, Gas), 5 upgrade slot types, recipe progress,
 * and combustion burn time tracking.
 *
 * @param <T> the concrete advanced machine block entity type
 */
@SuppressWarnings("unused")
@Developer("TheMentor")
@CreatedAt("2026-01-12")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
public abstract class AbstractAdvanceMachineBE<T extends AbstractAdvanceMachineBE<T>>
        extends AbstractExtendedMachineBE<T, @NotNull SyncedInventory, @NotNull SyncedFluidStorage, SyncedEnergyStorage, SyncedHeatStorage, @NotNull SyncedGasStorage>
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
     * Synced heat storage instance.
     */
    protected SyncedHeatStorage heatStorage;

    /**
     * Synced gas storage instance.
     */
    protected SyncedGasStorage gasStorage;

    /**
     * Container data delegate syncing progress, maxProgress, burnTime, and maxBurnTime with client menus.
     */
    protected final ContainerData containerData;

    /**
     * Count of installed energy efficiency upgrades.
     */
    protected int energyUpgradeCount;

    /**
     * Count of installed fluid capacity upgrades.
     */
    protected int fluidUpgradeCount;

    /**
     * Count of installed operational speed upgrades.
     */
    protected int speedUpgradeCount;

    /**
     * Count of installed heat efficiency/capacity upgrades.
     */
    protected int heatUpgradeCount;

    /**
     * Count of installed gas capacity upgrades.
     */
    protected int gasUpgradeCount;

    /**
     * Current machine operation progress in ticks.
     */
    protected int progress;

    /**
     * Total ticks required for the current operation.
     */
    protected int maxProgress;

    /**
     * Current fuel combustion ticks remaining.
     */
    protected int burnTime;

    /**
     * Total combustion ticks provided by the last consumed fuel.
     */
    protected int maxBurnTime;

    /**
     * Number of integer properties tracked by {@link #containerData}.
     */
    public static final int DELEGATE_SIZE = 4;

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
     * Slot index for the primary result item in the output inventory.
     */
    public static final int RESULT_OUTPUT_SLOT = 0;

    /**
     * Slot index for empty container/bucket returns in the output inventory.
     */
    public static final int BUCKET_OUTPUT_SLOT = 1;

    //INPUT INVENTORY
    /**
     * Slot index for raw input ingredient items in the input inventory.
     */
    public static final int INGREDIENT_INPUT_SLOT = 0;

    /**
     * Slot index for fluid/gas buckets or containers in the input inventory.
     */
    public static final int BUCKET_INPUT_SLOT = 1;

    //UPGRADE INVENTORY
    /**
     * Slot index for energy upgrades in the upgrade inventory.
     */
    public static final int ENERGY_UPGRADE_SLOT = 0;

    /**
     * Slot index for speed upgrades in the upgrade inventory.
     */
    public static final int SPEED_UPGRADE_SLOT = 1;

    /**
     * Slot index for fluid upgrades in the upgrade inventory.
     */
    public static final int FLUID_UPGRADE_SLOT = 2;

    /**
     * Slot index for heat upgrades in the upgrade inventory.
     */
    public static final int HEAT_UPGRADE_SLOT = 3;

    /**
     * Slot index for gas upgrades in the upgrade inventory.
     */
    public static final int GAS_UPGRADE_SLOT = 4;
    //endregion

    /**
     * Constructs an AbstractAdvanceMachineBE and configures inventories, storages, and container data.
     *
     * @param type  the block entity type
     * @param pos   the block position in the world
     * @param state the initial block state
     */
    public AbstractAdvanceMachineBE(BlockEntityType<@NotNull T> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);

        this.energyUpgradeCount = 0;
        this.fluidUpgradeCount = 0;
        this.speedUpgradeCount = 0;
        this.heatUpgradeCount = 0;
        this.gasUpgradeCount = 0;

        this.progress = 0;
        this.maxProgress = 0;
        this.burnTime = 0;
        this.maxBurnTime = 0;

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
        if(hasHeatStorage())
        {
            heatStorage = createHeatStorage();
            this.heatConnector.addStorage(heatStorage, heatIODirection());
        }
        if(hasGasStorage())
        {
            gasStorage = createGasStorage();
            this.gasConnector.addStorage(gasStorage, gasIODirection());
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
     * Retrieves the synced fluid storage.
     *
     * @return the fluid storage
     */
    public SyncedFluidStorage getFluidStorage()
    {
        return fluidStorage;
    }

    /**
     * Retrieves the stored fluid amount in droplets.
     *
     * @return fluid amount in droplets
     */
    public long getFluidAmount()
    {
        return hasFluidStorage() && this.fluidStorage != null ? this.fluidStorage.getAmount() : 0;
    }

    /**
     * Sets the stored fluid amount in droplets.
     *
     * @param amount fluid amount in droplets
     */
    public void setFluidAmount(long amount)
    {
        if(hasFluidStorage() && this.fluidStorage != null)
            this.fluidStorage.amount = amount;
    }

    /**
     * Retrieves the maximum fluid capacity in droplets.
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
     * Retrieves the synced energy storage.
     *
     * @return the energy storage
     */
    public SyncedEnergyStorage getEnergyStorage()
    {
        return energyStorage;
    }

    /**
     * Retrieves the stored energy amount.
     *
     * @return stored energy amount
     */
    public long getEnergyAmount()
    {
        return hasEnergyStorage() && this.energyStorage != null ? this.energyStorage.getAmount() : 0;
    }

    /**
     * Sets the stored energy amount.
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
     * @return energy capacity
     */
    public long getEnergyCapacity()
    {
        return hasEnergyStorage() && this.energyStorage != null ? this.energyStorage.getCapacity() : 0;
    }
    //endregion
    //region HEAT
    /**
     * Retrieves the synced heat storage.
     *
     * @return the heat storage
     */
    public SyncedHeatStorage getHeatStorage()
    {
        return heatStorage;
    }

    /**
     * Retrieves the current stored heat amount.
     *
     * @return heat amount
     */
    public double getHeatAmount()
    {
        return hasHeatStorage() && this.heatStorage != null ? this.heatStorage.getAmount() : 0;
    }

    /**
     * Sets the stored heat amount.
     *
     * @param amount heat amount to set
     */
    public void setHeatAmount(double amount)
    {
        if(hasHeatStorage() && this.heatStorage != null)
            this.heatStorage.amount = amount;
    }

    /**
     * Retrieves the maximum heat capacity.
     *
     * @return heat capacity
     */
    public double getHeatCapacity()
    {
        return hasHeatStorage() && this.heatStorage != null ? this.heatStorage.getCapacity() : 0;
    }
    //endregion
    //region GAS
    /**
     * Retrieves the synced gas storage.
     *
     * @return the gas storage
     */
    public SyncedGasStorage getGasStorage()
    {
        return gasStorage;
    }

    /**
     * Retrieves the stored gas amount.
     *
     * @return gas amount in millibuckets/droplets
     */
    public long getGasAmount()
    {
        return hasGasStorage() && this.gasStorage != null ? this.gasStorage.getAmount() : 0;
    }

    /**
     * Sets the stored gas amount.
     *
     * @param amount gas amount to set
     */
    public void setGasAmount(long amount)
    {
        if(hasHeatStorage() && this.heatStorage != null)
            this.gasStorage.amount = amount;
    }

    /**
     * Retrieves the maximum gas capacity.
     *
     * @return gas capacity
     */
    public long getGasCapacity()
    {
        return hasGasStorage() && this.gasStorage != null ? this.gasStorage.getCapacity() : 0;
    }
    //endregion
    //region PROGRESS
    /**
     * Retrieves the current operation progress in ticks.
     *
     * @return progress ticks
     */
    public int getProgress()
    {
        return this.progress;
    }

    /**
     * Retrieves the maximum operation progress in ticks.
     *
     * @return max progress ticks
     */
    public int getMaxProgress()
    {
        return this.maxProgress;
    }

    /**
     * Increments the operation progress by one tick and marks the block entity dirty.
     */
    public void increaseProgress()
    {
        this.progress++;
        update();
    }

    /**
     * Resets operation progress and max progress to zero and marks the block entity dirty.
     */
    public void resetProgress()
    {
        this.progress = 0;
        this.maxProgress = 0;
        update();
    }

    /**
     * Sets the maximum operation progress in ticks and marks the block entity dirty.
     *
     * @param amount maximum progress ticks
     */
    public void setMaxProgress(int amount)
    {
        this.maxProgress = amount;
        update();
    }
    //endregion
    //region BURN TIME
    /**
     * Retrieves the remaining combustion burn time in ticks.
     *
     * @return current burn time ticks
     */
    public int getBurnTime()
    {
        return this.burnTime;
    }

    /**
     * Retrieves the total burn time in ticks for the current fuel item.
     *
     * @return maximum burn time ticks
     */
    public int getMaxBurnTime()
    {
        return this.maxBurnTime;
    }

    /**
     * Increments the current burn time by one tick and marks the block entity dirty.
     */
    public void increaseBurnTime()
    {
        this.burnTime++;
        update();
    }

    /**
     * Resets burn time and max burn time to zero and marks the block entity dirty.
     */
    public void resetBurnTime()
    {
        this.burnTime = 0;
        this.maxBurnTime = 0;
        update();
    }

    /**
     * Sets the maximum burn time in ticks and marks the block entity dirty.
     *
     * @param amount maximum burn time ticks
     */
    public void setMaxBurnTime(int amount)
    {
        this.maxBurnTime = amount;
        update();
    }
    //endregion
    //region UPGRADES
    /**
     * Retrieves the count of fluid upgrades installed.
     *
     * @return fluid upgrade count
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
     * Retrieves the count of energy upgrades installed.
     *
     * @return energy upgrade count
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
     * Retrieves the count of speed upgrades installed.
     *
     * @return speed upgrade count
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

    /**
     * Retrieves the count of heat upgrades installed.
     *
     * @return heat upgrade count
     */
    public int getHeatUpgradeCount()
    {
        if(getUpgradeInventory() == null ||
        getUpgradeInventory().getSlot(HEAT_UPGRADE_SLOT) == null)
            return 0;

        var stack = Objects.requireNonNull(getUpgradeInventory().getSlot(HEAT_UPGRADE_SLOT)).get();

        if(stack.isEmpty())
            return 0;

        return stack.getCount();
    }

    /**
     * Retrieves the count of gas upgrades installed.
     *
     * @return gas upgrade count
     */
    public int getGasUpgradeCount()
    {
        if(getUpgradeInventory() == null ||
        getUpgradeInventory().getSlot(GAS_UPGRADE_SLOT) == null)
            return 0;

        var stack = Objects.requireNonNull(getUpgradeInventory().getSlot(GAS_UPGRADE_SLOT)).get();

        if(stack.isEmpty())
            return 0;

        return stack.getCount();
    }
    //endregion
    //region INVENTORIES
    /**
     * Retrieves the output inventory instance.
     *
     * @return output inventory
     */
    public OutputInventory getOutputInventory()
    {
        return (OutputInventory) this.inventoryConnector.getInventory(OUTPUT_INVENTORY_INDEX);
    }

    /**
     * Retrieves the input inventory instance.
     *
     * @return input inventory
     */
    public SyncedInventory getInputInventory()
    {
        return this.inventoryConnector.getInventory(INPUT_INVENTORY_INDEX);
    }

    /**
     * Retrieves the upgrade inventory instance.
     *
     * @return upgrade inventory
     */
    public SyncedInventory getUpgradeInventory()
    {
        return this.inventoryConnector.getInventory(UPGRADE_INVENTORY_INDEX);
    }

    //region INVENTORY DIRECTION
    /**
     * Specifies the mapped direction for automatic extraction of output inventory items.
     *
     * @return output direction (defaults to {@link MappedDirection#DOWN})
     */
    public MappedDirection inventoryOutputDirection()
    {
        return MappedDirection.DOWN;
    }

    /**
     * Specifies the mapped direction for inserting/extracting upgrade items.
     *
     * @return upgrade direction (defaults to {@link MappedDirection#EAST})
     */
    public MappedDirection inventoryUpgradeDirection()
    {
        return MappedDirection.EAST;
    }

    /**
     * Specifies the mapped direction for automatic insertion of input inventory items.
     *
     * @return input direction (defaults to {@link MappedDirection#UP})
     */
    public MappedDirection inventoryInputDirection()
    {
        return MappedDirection.UP;
    }
    //endregion

    /**
     * Computes total inventory slots across all inventories.
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
     * @return energy direction (defaults to {@link MappedDirection#WEST})
     */
    public MappedDirection energyIODirection()
    {
        return MappedDirection.WEST;
    }

    /**
     * Specifies the mapped direction for fluid transfer.
     *
     * @return fluid direction (defaults to {@link MappedDirection#SOUTH})
     */
    public MappedDirection fluidIODirection()
    {
        return MappedDirection.SOUTH;
    }

    /**
     * Specifies the mapped direction for heat transfer.
     *
     * @return heat direction (defaults to {@link MappedDirection#NONE})
     */
    public MappedDirection heatIODirection()
    {
        return MappedDirection.NONE;
    }

    /**
     * Specifies the mapped direction for gas transfer.
     *
     * @return gas direction (defaults to {@link MappedDirection#NONE})
     */
    public MappedDirection gasIODirection()
    {
        return MappedDirection.NONE;
    }
    //endregion
    //endregion
    //endregion

    //region protected methods
    /**
     * Creates the ContainerData delegate syncing progress, maxProgress, burnTime, and maxBurnTime with client menus.
     *
     * @return container data delegate
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
                    case 0 -> AbstractAdvanceMachineBE.this.progress;
                    case 1 -> AbstractAdvanceMachineBE.this.maxProgress;
                    case 2 -> AbstractAdvanceMachineBE.this.burnTime;
                    case 3 -> AbstractAdvanceMachineBE.this.maxBurnTime;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value)
            {
                switch (index)
                {
                    case 0 -> AbstractAdvanceMachineBE.this.progress = value;
                    case 1 -> AbstractAdvanceMachineBE.this.maxProgress = value;
                    case 2 -> AbstractAdvanceMachineBE.this.burnTime = value;
                    case 3 -> AbstractAdvanceMachineBE.this.maxBurnTime = value;
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
     * @return upgrade slot count
     */
    protected int upgradeInventorySize()
    {
        return 0;
    }

    /**
     * Declares the size of the input inventory.
     *
     * @return input slot count
     */
    protected int inputInventorySize()
    {
        return 0;
    }

    /**
     * Declares the size of the output inventory.
     *
     * @return output slot count
     */
    protected int outputInventorySize()
    {
        return 0;
    }
    //endregion
    //region FLUID
    /**
     * Indicates whether this machine has a fluid storage container.
     *
     * @return true if fluid storage is enabled
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
     * Declares the accepted/stored fluid variant.
     *
     * @return fluid variant (defaults to water)
     */
    protected FluidVariant fluidStorageVariant()
    {
        return FluidVariant.of(Fluids.WATER);
    }

    /**
     * Declares the baseline fluid capacity in droplet units.
     *
     * @return baseline fluid capacity
     */
    protected long getBaseFluidCapacity()
    {
        return FluidConstants.BUCKET * 10;
    }
    //endregion
    //region ENERGY
    /**
     * Indicates whether this machine has an energy storage container.
     *
     * @return true if energy storage is enabled
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
    //region HEAT
    /**
     * Indicates whether this machine has a heat storage container.
     *
     * @return true if heat storage is enabled
     */
    protected boolean hasHeatStorage()
    {
        return false;
    }

    /**
     * Instantiates the synchronized heat storage for this machine.
     *
     * @return a new SyncedHeatStorage instance
     */
    protected SyncedHeatStorage createHeatStorage()
    {
        return new SyncedHeatStorage(this, getBaseHeatCapacity(), getBaseHeatInsertionAmount(), getBaseHeatExtractionAmount());
    }

    /**
     * Declares the maximum heat insertion rate per transaction.
     *
     * @return baseline max heat insertion rate
     */
    protected double getBaseHeatInsertionAmount()
    {
        return 1;
    }

    /**
     * Declares the maximum heat extraction rate per transaction.
     *
     * @return baseline max heat extraction rate
     */
    protected double getBaseHeatExtractionAmount()
    {
        return 0;
    }

    /**
     * Declares the baseline heat capacity.
     *
     * @return baseline heat capacity
     */
    protected double getBaseHeatCapacity()
    {
        return 200;
    }
    //endregion
    //region GAS
    /**
     * Indicates whether this machine has a gas storage container.
     *
     * @return true if gas storage is enabled
     */
    protected boolean hasGasStorage()
    {
        return false;
    }

    /**
     * Instantiates the synchronized gas storage for this machine.
     *
     * @return a new SyncedGasStorage instance
     */
    protected SyncedGasStorage createGasStorage()
    {
        return new SyncedGasStorage(this, getBaseGasCapacity());
    }

    /**
     * Declares the baseline gas capacity.
     *
     * @return baseline gas capacity
     */
    protected long getBaseGasCapacity()
    {
        return 0;
    }
    //endregion
    //region UPGRADE ITEMS
    /**
     * Returns the Item used as an energy efficiency upgrade.
     *
     * @return energy upgrade Item
     */
    protected Item getEnergyUpgradeItem()
    {
        return ItemStack.EMPTY.getItem();
    }

    /**
     * Returns the Item used as a fluid capacity upgrade.
     *
     * @return fluid upgrade Item
     */
    protected Item getFluidUpgradeItem()
    {
        return ItemStack.EMPTY.getItem();
    }

    /**
     * Returns the Item used as an operational speed upgrade.
     *
     * @return speed upgrade Item
     */
    protected Item getSpeedUpgradeItem()
    {
        return ItemStack.EMPTY.getItem();
    }

    /**
     * Returns the Item used as a heat upgrade.
     *
     * @return heat upgrade Item
     */
    protected Item getHeatUpgradeItem()
    {
        return ItemStack.EMPTY.getItem();
    }

    /**
     * Returns the Item used as a gas upgrade.
     *
     * @return gas upgrade Item
     */
    protected Item getGasUpgradeItem()
    {
        return ItemStack.EMPTY.getItem();
    }
    //endregion
    //endregion
}