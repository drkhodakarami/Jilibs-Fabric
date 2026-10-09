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

package com.dynamero.heat.be;

import java.util.ArrayList;
import java.util.List;

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
import com.dynamero.heat.base.HeatComponent;
import com.dynamero.heat.base.HeatConnector;
import com.dynamero.heat.base.interfaces.HeatConnectorProvider;
import com.dynamero.heat.base.interfaces.HeatSpreadHandler;
import com.dynamero.heat.base.interfaces.HeatStorage;
import com.dynamero.heat.base.interfaces.HeatStorageProvider;
import com.dynamero.heat.base.records.HeatComponentList;
import com.dynamero.heat.base.storage.SimpleHeatStorage;
import com.dynamero.inventory.be.AbstractBaseInventoryBE;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.enumerations.MappedDirection;
import com.dynamero.shared.utils.DirectionHelper;

/**
 * Abstract base heat block entity combining inventory and heat storage capabilities,
 * supporting sided heat access, heat component synchronization, and heat spreading.
 *
 * @param <T> the concrete heat block entity type
 * @param <B> the container inventory type
 * @param <C> the heat storage type
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
public abstract class AbstractBaseHeatBE <T extends AbstractBaseHeatBE<T, B, C>, B extends SimpleContainer, C extends HeatStorage>
    extends AbstractBaseInventoryBE<T, B>
    implements HeatStorageProvider<C>, HeatConnectorProvider<C>, HeatSpreadHandler<C>
{
    /**
     * Heat connector managing sided heat storage instances.
     */
    protected final HeatConnector<C> heatConnector;

    /**
     * Constructs an AbstractBaseHeatBE and enables block ticking.
     *
     * @param type  the block entity type
     * @param pos   the block position in the world
     * @param state the initial block state
     */
    public AbstractBaseHeatBE(BlockEntityType<@NotNull T> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
        heatConnector = new HeatConnector<>();
        this.properties.tick();
    }

    /**
     * Retrieves the heat connector managing heat storage instances for this block entity.
     *
     * @return the heat connector
     */
    @Override
    public HeatConnector<C> getHeatConnector()
    {
        return this.heatConnector;
    }

    /**
     * Loads additional persistent data including heat connector states from the given value input.
     *
     * @param view the value input to read from
     */
    @Override
    protected void loadAdditional(@NotNull ValueInput view)
    {
        super.loadAdditional(view);
        heatConnector.loadAdditional(view);
    }

    /**
     * Saves additional persistent data including heat connector states to the given value output.
     *
     * @param view the value output to write to
     */
    @Override
    protected void saveAdditional(@NotNull ValueOutput view)
    {
        super.saveAdditional(view);
        heatConnector.saveAdditional(view);
    }

    /**
     * Retrieves the heat storage provider for the given mapped direction and block facing.
     *
     * @param direction the mapped direction relative to facing
     * @param facing    the current facing direction of the block
     * @return the heat storage provider, or null if none is available
     */
    @Override
    public @Nullable C getHeatStorageProvider(MappedDirection direction, Direction facing)
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
    public @Nullable C getHeatStorageProvider(Direction direction, Direction facing)
    {
        Direction side = DirectionHelper.relativeDirection(direction, facing);
        return this.heatConnector.getSidedMap().containsKey(MappedDirection.fromDirection(side))
            ? this.heatConnector.getStorage(side)
            : this.heatConnector.getSidedMap().containsKey(MappedDirection.NONE)
                ? this.heatConnector.getStorage(MappedDirection.NONE)
                : null;
    }

    /**
     * Retrieves the heat storage instance for the given world direction taking block orientation into account.
     *
     * @param direction the world direction
     * @return the heat storage instance, or null if none is available
     */
    public C getHeatStorage(Direction direction)
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
     * Retrieves the heat storage instance for the given mapped direction taking block orientation into account.
     *
     * @param direction the mapped direction
     * @return the heat storage instance, or null if none is available
     */
    public C getHeatStorage(MappedDirection direction)
    {
        return getHeatStorage(MappedDirection.toDirection(direction));
    }

    /**
     * Applies implicit data components from item stacks onto this block entity, restoring heat values.
     *
     * @param dataComponentGetter the component getter containing item component data
     */
    @Override
    protected void applyImplicitComponents(@NotNull DataComponentGetter dataComponentGetter)
    {
        super.applyImplicitComponents(dataComponentGetter);

        if(heatConnector.getStorages().isEmpty())
            return;

        HeatComponentList list = dataComponentGetter.getOrDefault(Jilibs.Components.HEAT_LIST, HeatComponentList.EMPTY);

        if(list.values().isEmpty())
            return;

        int index = 0;

        for (HeatComponent component : list.values())
        {
            if(index >= heatConnector.getStorages().size())
                break;

            HeatStorage storage = heatConnector.getStorages().get(index);
            if(storage instanceof SimpleHeatStorage simpleEnergyStorage)
            {
                simpleEnergyStorage.amount = component.getHeat();
                update();
            }

            index++;
        }
    }

    /**
     * Collects implicit data components from this block entity into the item stack component builder, saving heat values.
     *
     * @param builder the component builder to collect data into
     */
    @Override
    protected void collectImplicitComponents(DataComponentMap.@NotNull Builder builder)
    {
        super.collectImplicitComponents(builder);

        List<HeatComponent> list = new ArrayList<>();

        for (HeatStorage storage : heatConnector.getStorages())
            list.add(new HeatComponent(storage.getAmount(), storage.getUnit()));

        builder.set(Jilibs.Components.HEAT_LIST, new HeatComponentList(list));
    }
}