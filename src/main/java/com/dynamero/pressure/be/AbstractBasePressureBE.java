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

package com.dynamero.pressure.be;

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
import com.dynamero.inventory.be.AbstractBaseInventoryBE;
import com.dynamero.pressure.base.PressureComponent;
import com.dynamero.pressure.base.PressureConnector;
import com.dynamero.pressure.base.interfaces.PressureConnectorProvider;
import com.dynamero.pressure.base.interfaces.PressureSpreadHandler;
import com.dynamero.pressure.base.interfaces.PressureStorage;
import com.dynamero.pressure.base.interfaces.PressureStorageProvider;
import com.dynamero.pressure.base.records.PressureComponentList;
import com.dynamero.pressure.base.storage.SimplePressureStorage;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.enumerations.MappedDirection;
import com.dynamero.shared.utils.DirectionHelper;

/**
 * Abstract base pressure block entity combining inventory and pressure storage capabilities,
 * supporting sided pressure access, pressure component synchronization, and pressure spreading.
 *
 * @param <T> the concrete pressure block entity type
 * @param <B> the container inventory type
 * @param <C> the pressure storage type
 */
@Developer("TheMentor")
@CreatedAt("2026-10-08")
@ModifiedAt("2026-10-08")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public abstract class AbstractBasePressureBE <T extends AbstractBasePressureBE<T, B, C>, B extends SimpleContainer, C extends PressureStorage>
    extends AbstractBaseInventoryBE<T, B>
    implements PressureStorageProvider<C>, PressureConnectorProvider<C>, PressureSpreadHandler<C>
{
    /**
     * Pressure connector managing sided pressure storage instances.
     */
    protected final PressureConnector<C> pressureConnector;

    /**
     * Constructs an AbstractBasePressureBE and enables block ticking.
     *
     * @param type  the block entity type
     * @param pos   the block position in the world
     * @param state the initial block state
     */
    public AbstractBasePressureBE(BlockEntityType<@NotNull T> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
        pressureConnector = new PressureConnector<>();
        this.properties.tick();
    }

    /**
     * Retrieves the pressure connector managing pressure storage instances for this block entity.
     *
     * @return the pressure connector
     */
    @Override
    public PressureConnector<C> getPressureConnector()
    {
        return this.pressureConnector;
    }

    /**
     * Loads additional persistent data including pressure connector states from the given value input.
     *
     * @param view the value input to read from
     */
    @Override
    protected void loadAdditional(@NotNull ValueInput view)
    {
        super.loadAdditional(view);
        pressureConnector.loadAdditional(view);
    }

    /**
     * Saves additional persistent data including pressure connector states to the given value output.
     *
     * @param view the value output to write to
     */
    @Override
    protected void saveAdditional(@NotNull ValueOutput view)
    {
        super.saveAdditional(view);
        pressureConnector.saveAdditional(view);
    }

    /**
     * Retrieves the pressure storage provider for the given mapped direction and block facing.
     *
     * @param direction the mapped direction relative to facing
     * @param facing    the current facing direction of the block
     * @return the pressure storage provider, or null if none is available
     */
    @Override
    public @Nullable C getPressureStorageProvider(MappedDirection direction, Direction facing)
    {
        return getPressureStorageProvider(MappedDirection.toDirection(direction), facing);
    }

    /**
     * Retrieves the pressure storage provider for the given world direction and block facing.
     *
     * @param direction the world direction
     * @param facing    the current facing direction of the block
     * @return the pressure storage provider, or null if none is available
     */
    @Override
    public @Nullable C getPressureStorageProvider(Direction direction, Direction facing)
    {
        Direction side = DirectionHelper.relativeDirection(direction, facing);
        return this.pressureConnector.getSidedMap().containsKey(MappedDirection.fromDirection(side))
            ? this.pressureConnector.getStorage(side)
            : this.pressureConnector.getSidedMap().containsKey(MappedDirection.NONE)
                ? this.pressureConnector.getStorage(MappedDirection.NONE)
                : null;
    }

    /**
     * Retrieves the pressure storage instance for the given world direction taking block orientation into account.
     *
     * @param direction the world direction
     * @return the pressure storage instance, or null if none is available
     */
    public C getPressureStorage(Direction direction)
    {
        if(level == null)
            return null;
        return this.level.getBlockState(this.worldPosition).getProperties().contains(BlockStateProperties.FACING)
            ? this.getPressureStorageProvider(direction, this.level.getBlockState(this.worldPosition).getValue(BlockStateProperties.FACING))
            : this.level.getBlockState(this.worldPosition).getProperties().contains(BlockStateProperties.HORIZONTAL_FACING)
                ? this.getPressureStorageProvider(direction, this.level.getBlockState(this.worldPosition).getValue(BlockStateProperties.HORIZONTAL_FACING))
                : this.pressureConnector.getStorage(direction);
    }

    /**
     * Retrieves the pressure storage instance for the given mapped direction taking block orientation into account.
     *
     * @param direction the mapped direction
     * @return the pressure storage instance, or null if none is available
     */
    public C getPressureStorage(MappedDirection direction)
    {
        return getPressureStorage(MappedDirection.toDirection(direction));
    }

    /**
     * Applies implicit data components from item stacks onto this block entity, restoring pressure values.
     *
     * @param dataComponentGetter the component getter containing item component data
     */
    @Override
    protected void applyImplicitComponents(@NotNull DataComponentGetter dataComponentGetter)
    {
        super.applyImplicitComponents(dataComponentGetter);

        if(pressureConnector.getStorages().isEmpty())
            return;

        PressureComponentList list = dataComponentGetter.getOrDefault(Jilibs.Components.PRESSURE_LIST, PressureComponentList.EMPTY);

        if(list.values().isEmpty())
            return;

        int index = 0;

        for (PressureComponent component : list.values())
        {
            if(index >= pressureConnector.getStorages().size())
                break;

            PressureStorage storage = pressureConnector.getStorages().get(index);
            if(storage instanceof SimplePressureStorage simplePressureStorage)
            {
                simplePressureStorage.amount = component.getPressure();
                update();
            }

            index++;
        }
    }

    /**
     * Collects implicit data components from this block entity into the item stack component builder, saving pressure values.
     *
     * @param builder the component builder to collect data into
     */
    @Override
    protected void collectImplicitComponents(DataComponentMap.@NotNull Builder builder)
    {
        super.collectImplicitComponents(builder);

        List<PressureComponent> list = new ArrayList<>();

        for (PressureStorage storage : pressureConnector.getStorages())
            list.add(new PressureComponent(storage.getAmount(), storage.getUnit()));

        builder.set(Jilibs.Components.PRESSURE_LIST, new PressureComponentList(list));
    }
}