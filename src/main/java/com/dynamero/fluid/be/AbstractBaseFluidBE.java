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

package com.dynamero.fluid.be;

import java.util.ArrayList;
import java.util.List;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
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
import com.dynamero.fluid.base.FluidConnector;
import com.dynamero.fluid.base.SingleFluidStorage;
import com.dynamero.fluid.base.interfaces.FluidConnectorProvider;
import com.dynamero.fluid.base.interfaces.FluidSpreadHandler;
import com.dynamero.fluid.base.interfaces.FluidStorageProvider;
import com.dynamero.heat.base.HeatComponent;
import com.dynamero.heat.base.records.HeatComponentList;
import com.dynamero.inventory.be.AbstractBaseInventoryBE;
import com.dynamero.pressure.base.PressureComponent;
import com.dynamero.pressure.base.records.PressureComponentList;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.enumerations.MappedDirection;
import com.dynamero.fluid.base.FluidComponent;
import com.dynamero.shared.records.lists.FluidStackList;
import com.dynamero.shared.utils.DirectionHelper;

/**
 * Abstract base fluid block entity combining inventory and fluid storage capabilities,
 * supporting sided fluid access, heat and pressure synchronization, and fluid spreading.
 *
 * @param <T> the concrete fluid block entity type
 * @param <B> the container inventory type
 * @param <C> the single fluid storage type
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
public abstract class AbstractBaseFluidBE<T extends AbstractBaseFluidBE<T, B, C>, B extends SimpleContainer, C extends SingleFluidStorage>
        extends AbstractBaseInventoryBE<T, B>
        implements FluidStorageProvider<C>, FluidConnectorProvider<C>, FluidSpreadHandler<C>
{
    /**
     * Fluid connector managing sided single fluid storage instances.
     */
    protected final FluidConnector<C> fluidConnector;

    /**
     * Constructs an AbstractBaseFluidBE and enables block ticking.
     *
     * @param type  the block entity type
     * @param pos   the block position in the world
     * @param state the initial block state
     */
    public AbstractBaseFluidBE(BlockEntityType<@NotNull T> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
        fluidConnector = new FluidConnector<>();
        this.properties.tick();
    }

    /**
     * Retrieves the fluid connector managing fluid storage instances for this block entity.
     *
     * @return the fluid connector
     */
    @Override
    public FluidConnector<C> getFluidConnector()
    {
        return fluidConnector;
    }

    /**
     * Loads additional persistent data including fluid connector states from the given value input.
     *
     * @param view the value input to read from
     */
    @Override
    protected void loadAdditional(@NotNull ValueInput view)
    {
        super.loadAdditional(view);
        fluidConnector.loadAdditional(view);
    }

    /**
     * Saves additional persistent data including fluid connector states to the given value output.
     *
     * @param view the value output to write to
     */
    @Override
    protected void saveAdditional(@NotNull ValueOutput view)
    {
        super.saveAdditional(view);
        fluidConnector.saveAdditional(view);
    }

    /**
     * Retrieves the fluid storage provider for the given mapped direction and block facing.
     *
     * @param mappedDirection the mapped direction relative to facing
     * @param facing          the current facing direction of the block
     * @return the fluid storage provider, or null if none is available
     */
    @Override
    public @Nullable C getFluidStorageProvider(MappedDirection mappedDirection, Direction facing)
    {
        return getFluidStorageProvider(MappedDirection.toDirection(mappedDirection), facing);
    }

    /**
     * Retrieves the fluid storage provider for the given world direction and block facing.
     *
     * @param direction the world direction
     * @param facing    the current facing direction of the block
     * @return the fluid storage provider, or null if none is available
     */
    @Override
    public @Nullable C getFluidStorageProvider(Direction direction, Direction facing)
    {
        Direction side = DirectionHelper.relativeDirection(direction, facing);
        return this.fluidConnector.getSidedMap().containsKey(MappedDirection.fromDirection(side))
            ? this.fluidConnector.getStorage(side)
            : this.fluidConnector.getSidedMap().containsKey(MappedDirection.NONE)
                ? this.fluidConnector.getStorage(MappedDirection.NONE)
                : null;
    }

    /**
     * Retrieves the fluid storage instance for the given world direction taking block orientation into account.
     *
     * @param direction the world direction
     * @return the fluid storage instance, or null if none is available
     */
    public Storage<FluidVariant> getFluidStorage(Direction direction)
    {
        if(level == null)
            return null;
        return this.level.getBlockState(this.worldPosition).getProperties().contains(BlockStateProperties.FACING)
            ? this.getFluidStorageProvider(direction, this.level.getBlockState(this.worldPosition).getValue(BlockStateProperties.FACING))
            : this.level.getBlockState(this.worldPosition).getProperties().contains(BlockStateProperties.HORIZONTAL_FACING)
                ? this.getFluidStorageProvider(direction, this.level.getBlockState(this.worldPosition).getValue(BlockStateProperties.HORIZONTAL_FACING))
                : this.fluidConnector.getStorage(direction);
    }

    /**
     * Retrieves the fluid storage instance for the given mapped direction taking block orientation into account.
     *
     * @param direction the mapped direction
     * @return the fluid storage instance, or null if none is available
     */
    public Storage<FluidVariant> getFluidStorage(MappedDirection direction)
    {
        return getFluidStorage(MappedDirection.toDirection(direction));
    }

    /**
     * Applies data components from item stacks onto this block entity, restoring fluid, heat, and pressure states.
     *
     * @param dataComponentGetter the component getter containing item component data
     * @throws IllegalArgumentException if data component lists mismatch the count of fluid storages
     */
    @Override
    protected void applyImplicitComponents(@NotNull DataComponentGetter dataComponentGetter)
    {
        super.applyImplicitComponents(dataComponentGetter);

        if(fluidConnector.getStorages().isEmpty())
            return;

        FluidStackList list = dataComponentGetter.getOrDefault(Jilibs.Components.FLUID_LIST, FluidStackList.EMPTY);
        HeatComponentList heatList = dataComponentGetter.getOrDefault(Jilibs.Components.HEAT_LIST, HeatComponentList.EMPTY);
        PressureComponentList pressureList = dataComponentGetter.getOrDefault(Jilibs.Components.PRESSURE_LIST, PressureComponentList.EMPTY);

        if(list.values().size() != fluidConnector.getStorages().size())
            throw new IllegalArgumentException("Fluid List Data Component has different size compared to Fluid Storages");

        if(heatList.values().size() != fluidConnector.getStorages().size())
            throw new IllegalArgumentException("Heat List List Data Component has different size compared to Fluid Storages");

        if(pressureList.values().size() != fluidConnector.getStorages().size())
            throw new IllegalArgumentException("Pressure List List Data Component has different size compared to Fluid Storages");

        for (int i = 0; i < fluidConnector.getStorages().size(); i++)
        {
            if(fluidConnector.getStorage(i) instanceof SingleFluidStorage singleFluidStorage)
            {
                if(list.values().get(i) != null)
                    singleFluidStorage.setFluid(list.values().get(i));

                if(heatList.values().get(i) != null)
                    singleFluidStorage.setHeatComponent(heatList.values().get(i));

                if(pressureList.values().get(i) != null)
                    singleFluidStorage.setPressureComponent(pressureList.values().get(i));

                update();
            }
        }
    }

    /**
     * Collects implicit data components from this block entity into the item stack component builder, saving fluids, heat, and pressure.
     *
     * @param builder the component builder to collect data into
     */
    @Override
    protected void collectImplicitComponents(DataComponentMap.@NotNull Builder builder)
    {
        super.collectImplicitComponents(builder);

        List<FluidComponent> list = new ArrayList<>();
        List<HeatComponent> heatComponentList = new ArrayList<>();
        List<PressureComponent> pressureComponentList = new ArrayList<>();

        for (var storage : fluidConnector.getStorages())
        {
            list.add(new FluidComponent(storage.variant, storage.getAmount()));
            heatComponentList.add(new HeatComponent(storage.getHeat(), storage.getHeatUnit()));
            pressureComponentList.add(new PressureComponent(storage.getPressure(), storage.getPressureUnit()));
        }

        builder.set(Jilibs.Components.FLUID_LIST, new FluidStackList(list));
        builder.set(Jilibs.Components.HEAT_LIST, new HeatComponentList(heatComponentList));
        builder.set(Jilibs.Components.PRESSURE_LIST, new PressureComponentList(pressureComponentList));
    }
}