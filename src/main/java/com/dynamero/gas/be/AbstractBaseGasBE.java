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

package com.dynamero.gas.be;

import java.util.ArrayList;
import java.util.List;

import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
import com.dynamero.gas.base.GasComponent;
import com.dynamero.gas.base.GasConnector;
import com.dynamero.gas.base.interfaces.GasConnectorProvider;
import com.dynamero.gas.base.interfaces.GasSpreadHandler;
import com.dynamero.gas.base.interfaces.GasStorageProvider;
import com.dynamero.gas.base.interfaces.GasVariant;
import com.dynamero.gas.base.records.GasStackList;
import com.dynamero.gas.base.storage.SingleGasStorage;
import com.dynamero.heat.base.HeatComponent;
import com.dynamero.heat.base.records.HeatComponentList;
import com.dynamero.inventory.be.AbstractBaseInventoryBE;
import com.dynamero.pressure.base.PressureComponent;
import com.dynamero.pressure.base.records.PressureComponentList;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.enumerations.MappedDirection;
import com.dynamero.shared.utils.DirectionHelper;

/**
 * Abstract base gas block entity combining inventory and gas storage capabilities,
 * supporting sided gas access, temperature (heat) and pressure synchronization, and gas spreading.
 *
 * @param <T> the concrete gas block entity type
 * @param <B> the container inventory type
 * @param <C> the single gas storage type
 */
@SuppressWarnings("unused")
@Developer("TheMentor")
@CreatedAt("2026-01-12")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
public abstract class AbstractBaseGasBE<T extends AbstractBaseGasBE<T, B, C>, B extends SimpleContainer, C extends SingleGasStorage>
    extends AbstractBaseInventoryBE<T, B>
    implements GasStorageProvider<C>, GasConnectorProvider<C>, GasSpreadHandler<C>
{
    /**
     * Gas connector managing sided single gas storage instances.
     */
    protected final GasConnector<C> gasConnector;

    /**
     * Constructs an AbstractBaseGasBE and enables block ticking.
     *
     * @param type  the block entity type
     * @param pos   the block position in the world
     * @param state the initial block state
     */
    public AbstractBaseGasBE(BlockEntityType<@NotNull T> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
        gasConnector = new GasConnector<>();
        this.properties.tick();
    }

    /**
     * Retrieves the gas connector managing gas storage instances for this block entity.
     *
     * @return the gas connector
     */
    @Override
    public GasConnector<C> getGasConnector()
    {
        return gasConnector;
    }

    /**
     * Loads additional persistent data including gas connector states from the given value input.
     *
     * @param view the value input to read from
     */
    @Override
    protected void loadAdditional(@NotNull ValueInput view)
    {
        super.loadAdditional(view);
        gasConnector.loadAdditional(view);
    }

    /**
     * Saves additional persistent data including gas connector states to the given value output.
     *
     * @param view the value output to write to
     */
    @Override
    protected void saveAdditional(@NotNull ValueOutput view)
    {
        super.saveAdditional(view);
        gasConnector.saveAdditional(view);
    }

    /**
     * Retrieves the gas storage provider for the given mapped direction and block facing.
     *
     * @param direction the mapped direction relative to facing
     * @param facing    the current facing direction of the block
     * @return the gas storage provider, or null if none is available
     */
    @Override
    public @Nullable C getGasStorageProvider(MappedDirection direction, Direction facing)
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
    public @Nullable C getGasStorageProvider(Direction direction, Direction facing)
    {
        Direction side = DirectionHelper.relativeDirection(direction, facing);
        return this.gasConnector.getSidedMap().containsKey(MappedDirection.fromDirection(side))
            ? this.gasConnector.getStorage(side)
            : this.gasConnector.getSidedMap().containsKey(MappedDirection.NONE)
                ? this.gasConnector.getStorage(MappedDirection.NONE)
                : null;
    }

    /**
     * Retrieves the gas storage instance for the given world direction taking block orientation into account.
     *
     * @param direction the world direction
     * @return the gas storage instance, or null if none is available
     */
    public Storage<GasVariant> getGasStorage(Direction direction)
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
     * Retrieves the gas storage instance for the given mapped direction taking block orientation into account.
     *
     * @param direction the mapped direction
     * @return the gas storage instance, or null if none is available
     */
    public Storage<GasVariant> getGasStorage(MappedDirection direction)
    {
        return getGasStorage(MappedDirection.toDirection(direction));
    }

    /**
     * Applies data components from item stacks onto this block entity, restoring gas, heat, and pressure states.
     *
     * @param dataComponentGetter the component getter containing item component data
     * @throws IllegalArgumentException if data component lists mismatch the count of gas storages
     */
    @Override
    protected void applyImplicitComponents(@NotNull DataComponentGetter dataComponentGetter)
    {
        super.applyImplicitComponents(dataComponentGetter);

        if(gasConnector.getStorages().isEmpty())
            return;

        GasStackList list = dataComponentGetter.getOrDefault(Jilibs.Components.GAS_LIST, GasStackList.EMPTY);
        HeatComponentList heatList = dataComponentGetter.getOrDefault(Jilibs.Components.HEAT_LIST, HeatComponentList.EMPTY);
        PressureComponentList pressureList = dataComponentGetter.getOrDefault(Jilibs.Components.PRESSURE_LIST, PressureComponentList.EMPTY);

        if(list.values().size() != gasConnector.getStorages().size())
            throw new IllegalArgumentException("Gas List Data Component has different size compared to Gas Storages");

        if(heatList.values().size() != gasConnector.getStorages().size())
            throw new IllegalArgumentException("Heat List List Data Component has different size compared to Gas Storages");

        if(pressureList.values().size() != gasConnector.getStorages().size())
            throw new IllegalArgumentException("Pressure List List Data Component has different size compared to Gas Storages");

        for (int i = 0; i < gasConnector.getStorages().size(); i++)
        {
            if (list.values().get(i) != null)
                gasConnector.getStorage(i).setGas(list.values().get(i));

            if (heatList.values().get(i) != null)
                gasConnector.getStorage(i).setHeatComponent(heatList.values().get(i));

            if (pressureList.values().get(i) != null)
                gasConnector.getStorage(i).setPressureComponent(pressureList.values().get(i));

            update();
        }
    }

    /**
     * Collects implicit data components from this block entity into the item stack component builder, saving gases, heat, and pressure.
     *
     * @param builder the component builder to collect data into
     */
    @Override
    protected void collectImplicitComponents(DataComponentMap.@NotNull Builder builder)
    {
        super.collectImplicitComponents(builder);

        List<GasComponent> list = new ArrayList<>();
        List<HeatComponent> heatComponentList = new ArrayList<>();
        List<PressureComponent> pressureComponentList = new ArrayList<>();

        for (var storage : gasConnector.getStorages())
        {
            list.add(new GasComponent(storage.variant, storage.getAmount()));
            heatComponentList.add(new HeatComponent(storage.getHeat(), storage.getHeatUnit()));
            pressureComponentList.add(new PressureComponent(storage.getPressure(), storage.getPressureUnit()));
        }

        builder.set(Jilibs.Components.GAS_LIST, new GasStackList(list));
        builder.set(Jilibs.Components.HEAT_LIST, new HeatComponentList(heatComponentList));
        builder.set(Jilibs.Components.PRESSURE_LIST, new PressureComponentList(pressureComponentList));
    }
}