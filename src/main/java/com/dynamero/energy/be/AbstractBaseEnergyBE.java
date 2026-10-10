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

package com.dynamero.energy.be;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;

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
import com.dynamero.energy.base.EnergyConnector;
import com.dynamero.energy.base.interfaces.EnergyConnectorProvider;
import com.dynamero.energy.base.interfaces.EnergySpreadHandler;
import com.dynamero.energy.base.interfaces.EnergyStorageProvider;
import com.dynamero.inventory.be.AbstractBaseInventoryBE;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.enumerations.MappedDirection;
import com.dynamero.shared.records.LongPayload;
import com.dynamero.shared.records.lists.LongList;
import com.dynamero.shared.utils.DirectionHelper;

/**
 * Abstract base energy block entity combining inventory and energy storage capabilities,
 * supporting sided energy access, data component serialization, and energy spreading.
 *
 * @param <T> the concrete energy block entity type
 * @param <B> the container inventory type
 * @param <C> the energy storage type
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
@Developer("TheMentor")
@CreatedAt("2026-10-08")
@ModifiedAt("2026-10-08")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public abstract class AbstractBaseEnergyBE<T extends AbstractBaseEnergyBE<T, B, C>, B extends SimpleContainer, C extends EnergyStorage>
        extends AbstractBaseInventoryBE<T, B>
        implements EnergyStorageProvider<C>, EnergyConnectorProvider<C>, EnergySpreadHandler<C>
{
    /**
     * Energy connector managing sided energy storage instances.
     */
    protected final EnergyConnector<C> energyConnector;

    /**
     * Constructs an AbstractBaseEnergyBE and enables ticking.
     *
     * @param type  the block entity type
     * @param pos   the block position in the world
     * @param state the initial block state
     */
    public AbstractBaseEnergyBE(BlockEntityType<@NotNull T> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
        energyConnector = new EnergyConnector<>();
        this.properties.tick();
    }

    /**
     * Retrieves the energy connector managing energy storage instances for this block entity.
     *
     * @return the energy connector
     */
    @Override
    public EnergyConnector<C> getEnergyConnector()
    {
        return this.energyConnector;
    }

    /**
     * Loads additional persistent data including energy connector states from the given value input.
     *
     * @param view the value input to read from
     */
    @Override
    protected void loadAdditional(@NotNull ValueInput view)
    {
        super.loadAdditional(view);
        energyConnector.loadAdditional(view);
    }

    /**
     * Saves additional persistent data including energy connector states to the given value output.
     *
     * @param view the value output to write to
     */
    @Override
    protected void saveAdditional(@NotNull ValueOutput view)
    {
        super.saveAdditional(view);
        energyConnector.saveAdditional(view);
    }

    /**
     * Retrieves the energy storage provider for the given mapped direction and block facing.
     *
     * @param mappedDirection the mapped direction relative to facing
     * @param facing          the current facing direction of the block
     * @return the energy storage provider, or null if none is available
     */
    @Override
    public @Nullable C getEnergyStorageProvider(MappedDirection mappedDirection, Direction facing)
    {
        return getEnergyStorageProvider(MappedDirection.toDirection(mappedDirection), facing);
    }

    /**
     * Retrieves the energy storage provider for the given world direction and block facing.
     *
     * @param direction the world direction
     * @param facing    the current facing direction of the block
     * @return the energy storage provider, or null if none is available
     */
    @Override
    public @Nullable C getEnergyStorageProvider(Direction direction, Direction facing)
    {
        Direction side = DirectionHelper.relativeDirection(direction, facing);
        return this.energyConnector.getSidedMap().containsKey(MappedDirection.fromDirection(side))
            ? this.energyConnector.getStorage(side)
            : this.energyConnector.getSidedMap().containsKey(MappedDirection.NONE)
                ? this.energyConnector.getStorage(MappedDirection.NONE)
                : null;
    }

    /**
     * Retrieves the energy storage instance for the given world direction taking block orientation into account.
     *
     * @param direction the world direction
     * @return the energy storage instance, or null if none is available
     */
    public C getEnergyStorage(Direction direction)
    {
        if(level == null)
            return null;
        return this.level.getBlockState(this.worldPosition).getProperties().contains(BlockStateProperties.FACING)
            ? this.getEnergyStorageProvider(direction, this.level.getBlockState(this.worldPosition).getValue(BlockStateProperties.FACING))
            : this.level.getBlockState(this.worldPosition).getProperties().contains(BlockStateProperties.HORIZONTAL_FACING)
                ? this.getEnergyStorageProvider(direction, this.level.getBlockState(this.worldPosition).getValue(BlockStateProperties.HORIZONTAL_FACING))
                : this.energyConnector.getStorage(direction);
    }

    /**
     * Retrieves the energy storage instance for the given mapped direction taking block orientation into account.
     *
     * @param direction the mapped direction
     * @return the energy storage instance, or null if none is available
     */
    public C getEnergyStorage(MappedDirection direction)
    {
        return getEnergyStorage(MappedDirection.toDirection(direction));
    }

    /**
     * Applies data components from item stacks onto this block entity, restoring energy amounts.
     *
     * @param dataComponentGetter the component getter containing item component data
     */
    @Override
    protected void applyImplicitComponents(@NotNull DataComponentGetter dataComponentGetter)
    {
        super.applyImplicitComponents(dataComponentGetter);

        if(energyConnector.getStorages().isEmpty())
            return;

        LongList list = dataComponentGetter.getOrDefault(Jilibs.Components.ENERGY_LIST, LongList.EMPTY);

        if(list.values().isEmpty())
            return;

        int index = 0;

        for (LongPayload longPayload : list.values())
        {
            if(index >= energyConnector.getStorages().size())
                break;

            EnergyStorage storage = energyConnector.getStorages().get(index);
            if(storage instanceof SimpleEnergyStorage simpleEnergyStorage)
            {
                simpleEnergyStorage.amount = longPayload.value();
                update();
            }

            index++;
        }
    }

    /**
     * Collects implicit data components from this block entity into the item stack component builder, saving energy amounts.
     *
     * @param builder the component builder to collect data into
     */
    @Override
    protected void collectImplicitComponents(DataComponentMap.@NotNull Builder builder)
    {
        super.collectImplicitComponents(builder);

        List<LongPayload> list = new ArrayList<>();

        for (EnergyStorage storage : energyConnector.getStorages())
            list.add(new LongPayload(storage.getAmount()));

        builder.set(Jilibs.Components.ENERGY_LIST, new LongList(list));
    }
}