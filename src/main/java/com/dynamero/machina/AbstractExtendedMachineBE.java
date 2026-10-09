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

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import com.dynamero.Jilibs;
import com.dynamero.fluid.base.SingleFluidStorage;
import com.dynamero.gas.base.GasComponent;
import com.dynamero.gas.base.GasConnector;
import com.dynamero.gas.base.interfaces.GasConnectorProvider;
import com.dynamero.gas.base.interfaces.GasSpreadHandler;
import com.dynamero.gas.base.interfaces.GasStorageProvider;
import com.dynamero.gas.base.records.GasStackList;
import com.dynamero.gas.base.storage.SingleGasStorage;
import com.dynamero.heat.base.HeatComponent;
import com.dynamero.heat.base.HeatConnector;
import com.dynamero.heat.base.interfaces.HeatConnectorProvider;
import com.dynamero.heat.base.interfaces.HeatSpreadHandler;
import com.dynamero.heat.base.interfaces.HeatStorage;
import com.dynamero.heat.base.interfaces.HeatStorageProvider;
import com.dynamero.heat.base.records.HeatComponentList;
import com.dynamero.heat.base.storage.SimpleHeatStorage;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.enumerations.MappedDirection;
import com.dynamero.shared.utils.DirectionHelper;

/**
 * Extended machine block entity extending {@link AbstractBaseMachineBE} with integrated heat and gas storage subsystems,
 * supporting data component serialization and sided access for inventory, fluids, energy, heat, and gases.
 *
 * @param <T> the concrete extended machine block entity type
 * @param <B> the container inventory type
 * @param <C> the fluid storage type
 * @param <D> the energy storage type
 * @param <E> the heat storage type
 * @param <F> the gas storage type
 */
@SuppressWarnings("unused")
@Developer("TheMentor")
@CreatedAt("2026-01-12")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
public abstract class AbstractExtendedMachineBE<T extends AbstractExtendedMachineBE<T, B, C, D, E, F>,
        B extends SimpleContainer,
        C extends SingleFluidStorage,
        D extends EnergyStorage,
        E extends HeatStorage,
        F extends SingleGasStorage>
        extends AbstractBaseMachineBE<T, B, C, D>
        implements HeatStorageProvider<E>, HeatConnectorProvider<E>, HeatSpreadHandler<E>,
        GasStorageProvider<F>, GasConnectorProvider<F>, GasSpreadHandler<F>, MachineBlockEntityHandler
{
    /**
     * Heat connector managing sided heat storage instances.
     */
    protected final HeatConnector<E> heatConnector;

    /**
     * Gas connector managing sided gas storage instances.
     */
    protected final GasConnector<F> gasConnector;

    /**
     * Constructs an AbstractExtendedMachineBE initializing heat and gas connectors.
     *
     * @param type  the block entity type
     * @param pos   the block position in the world
     * @param state the initial block state
     */
    public AbstractExtendedMachineBE(BlockEntityType<@NotNull T> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
        heatConnector = new HeatConnector<>();
        gasConnector = new GasConnector<>();
    }

    /**
     * Retrieves the heat connector managing heat storage instances for this block entity.
     *
     * @return the heat connector
     */
    @Override
    public HeatConnector<E> getHeatConnector()
    {
        return this.heatConnector;
    }

    /**
     * Retrieves the gas connector managing gas storage instances for this block entity.
     *
     * @return the gas connector
     */
    @Override
    public GasConnector<F> getGasConnector()
    {
        return this.gasConnector;
    }

    /**
     * Loads additional persistent data including heat and gas connector states from the given value input.
     *
     * @param view the value input to read from
     */
    @Override
    protected void loadAdditional(@NotNull ValueInput view)
    {
        super.loadAdditional(view);
        heatConnector.loadAdditional(view);
        gasConnector.loadAdditional(view);
    }

    /**
     * Saves additional persistent data including heat and gas connector states to the given value output.
     *
     * @param view the value output to write to
     */
    @Override
    protected void saveAdditional(@NotNull ValueOutput view)
    {
        super.saveAdditional(view);
        heatConnector.saveAdditional(view);
        gasConnector.saveAdditional(view);
    }

    /**
     * Retrieves the heat storage provider for the given mapped direction and block facing.
     *
     * @param direction the mapped direction relative to facing
     * @param facing    the current facing direction of the block
     * @return the heat storage provider, or null if none is available
     */
    @Override
    public @Nullable E getHeatStorageProvider(MappedDirection direction, Direction facing)
    {
        return getHeatStorageProvider(MappedDirection.toDirection(direction), facing);
    }

    /**
     * Retrieves the heat storage provider for the given world direction and block facing.
     *
     * @param direction the world direction
     * @param facing    the current facing direction of the block
     * @return the heat storage provider, or null if none is available
     */
    @Override
    public @Nullable E getHeatStorageProvider(Direction direction, Direction facing)
    {
        Direction side = DirectionHelper.relativeDirection(direction, facing);
        return this.heatConnector.getSidedMap().containsKey(MappedDirection.fromDirection(side))
            ? this.heatConnector.getStorage(side)
            : this.heatConnector.getSidedMap().containsKey(MappedDirection.NONE)
                ? this.heatConnector.getStorage(MappedDirection.NONE)
                : null;
    }

    /**
     * Retrieves the gas storage provider for the given mapped direction and block facing.
     *
     * @param direction the mapped direction relative to facing
     * @param facing    the current facing direction of the block
     * @return the gas storage provider, or null if none is available
     */
    @Override
    public @Nullable F getGasStorageProvider(MappedDirection direction, Direction facing)
    {
        return getGasStorageProvider(MappedDirection.toDirection(direction), facing);
    }

    /**
     * Retrieves the gas storage provider for the given world direction and block facing.
     *
     * @param direction the world direction
     * @param facing    the current facing direction of the block
     * @return the gas storage provider, or null if none is available
     */
    @Override
    public @Nullable F getGasStorageProvider(Direction direction, Direction facing)
    {
        Direction side = DirectionHelper.relativeDirection(direction, facing);
        return this.gasConnector.getSidedMap().containsKey(MappedDirection.fromDirection(side))
            ? this.gasConnector.getStorage(side)
            : this.gasConnector.getSidedMap().containsKey(MappedDirection.NONE)
                ? this.gasConnector.getStorage(MappedDirection.NONE)
                : null;
    }

    /**
     * Retrieves the heat storage instance for the given mapped direction taking block orientation into account.
     *
     * @param direction the mapped direction
     * @return the heat storage instance, or null if none is available
     */
    public @Nullable E getHeatStorage(MappedDirection direction)
    {
        return getHeatStorage(MappedDirection.toDirection(direction));
    }

    /**
     * Retrieves the heat storage instance for the given world direction taking block orientation into account.
     *
     * @param direction the world direction
     * @return the heat storage instance, or null if none is available
     */
    public @Nullable E getHeatStorage(Direction direction)
    {
        if(level == null)
            return null;
        return this.level.getBlockState(this.worldPosition).getProperties().contains(BlockStateProperties.FACING)
            ? this.getHeatStorageProvider(direction, this.level.getBlockState(this.worldPosition).getValue(BlockStateProperties.FACING))
            : this.level.getBlockState(this.worldPosition).getProperties().contains(BlockStateProperties.HORIZONTAL_FACING)
                ? this.getHeatStorageProvider(direction, this.level.getBlockState(this.worldPosition).getValue(BlockStateProperties.HORIZONTAL_FACING))
                : this.heatConnector.getStorage(direction);
    }

    /**
     * Retrieves the gas storage instance for the given mapped direction taking block orientation into account.
     *
     * @param direction the mapped direction
     * @return the gas storage instance, or null if none is available
     */
    public @Nullable F getGasStorage(MappedDirection direction)
    {
        return getGasStorage(MappedDirection.toDirection(direction));
    }

    /**
     * Retrieves the gas storage instance for the given world direction taking block orientation into account.
     *
     * @param direction the world direction
     * @return the gas storage instance, or null if none is available
     */
    public @Nullable F getGasStorage(Direction direction)
    {
        if(level == null)
            return null;
        return this.level.getBlockState(this.worldPosition).getProperties().contains(BlockStateProperties.FACING)
            ? this.getGasStorageProvider(direction, this.level.getBlockState(this.worldPosition).getValue(BlockStateProperties.FACING))
            : this.level.getBlockState(this.worldPosition).getProperties().contains(BlockStateProperties.HORIZONTAL_FACING)
                ? this.getGasStorageProvider(direction, this.level.getBlockState(this.worldPosition).getValue(BlockStateProperties.HORIZONTAL_FACING))
                : this.gasConnector.getStorage(direction);
    }

    /**
     * Applies data components from item stacks onto this block entity, restoring heat and gas values.
     *
     * @param dataComponentGetter the component getter containing item component data
     */
    @Override
    protected void applyImplicitComponents(@NotNull DataComponentGetter dataComponentGetter)
    {
        super.applyImplicitComponents(dataComponentGetter);

        if(!heatConnector.getStorages().isEmpty())
        {
            HeatComponentList list = dataComponentGetter.getOrDefault(Jilibs.Components.HEAT_LIST, HeatComponentList.EMPTY);

            if (list.values().isEmpty())
                return;

            int index = 0;

            for (HeatComponent component : list.values())
            {
                if (index >= heatConnector.getStorages().size())
                    break;

                HeatStorage storage = heatConnector.getStorages().get(index);
                if (storage instanceof SimpleHeatStorage simpleHeatStorage)
                {
                    simpleHeatStorage.amount = component.getHeat();
                    update();
                }

                index++;
            }

        }

        if(!gasConnector.getStorages().isEmpty())
        {
            GasStackList list = dataComponentGetter.getOrDefault(Jilibs.Components.GAS_LIST, GasStackList.EMPTY);

            if (list.values().isEmpty())
                return;

            int index = 0;

            for (GasComponent component : list.values())
            {
                if (index >= heatConnector.getStorages().size())
                    break;

                HeatStorage storage = heatConnector.getStorages().get(index);
                if (storage instanceof SingleGasStorage singleGasStorage)
                {
                    singleGasStorage.variant = component.getGas();
                    singleGasStorage.amount = component.getAmount();
                    update();
                }

                index++;
            }

        }
    }

    /**
     * Collects implicit data components from this block entity into the item stack component builder, saving heat and gas values.
     *
     * @param builder the component builder to collect data into
     */
    @Override
    protected void collectImplicitComponents(DataComponentMap.@NotNull Builder builder)
    {
        super.collectImplicitComponents(builder);

        //region HEAT
        List<HeatComponent> heat_list = new ArrayList<>();

        for (HeatStorage storage : heatConnector.getStorages())
            heat_list.add(new HeatComponent(storage.getAmount(), storage.getUnit()));

        builder.set(Jilibs.Components.HEAT_LIST, new HeatComponentList(heat_list));
        //endregion

        //region GAS
        List<GasComponent> list = new ArrayList<>();

        for (var storage : gasConnector.getStorages())
            list.add(new GasComponent(storage.variant, storage.getAmount()));

        builder.set(Jilibs.Components.GAS_LIST, new GasStackList(list));
        //endregion
    }
}