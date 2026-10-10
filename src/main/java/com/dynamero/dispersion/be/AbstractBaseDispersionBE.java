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

package com.dynamero.dispersion.be;

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
import com.dynamero.dispersion.base.DispersionConnector;
import com.dynamero.dispersion.base.interfaces.DispersionConnectorProvider;
import com.dynamero.dispersion.base.interfaces.DispersionSpreadHandler;
import com.dynamero.dispersion.base.interfaces.DispersionStorageProvider;
import com.dynamero.dispersion.base.interfaces.DispersionVariant;
import com.dynamero.dispersion.base.records.DispersionStackList;
import com.dynamero.dispersion.base.records.DispersionStackPayload;
import com.dynamero.dispersion.base.storage.SingleDispersionStorage;
import com.dynamero.heat.base.HeatComponent;
import com.dynamero.heat.base.records.HeatComponentList;
import com.dynamero.inventory.be.AbstractBaseInventoryBE;
import com.dynamero.pressure.base.PressureComponent;
import com.dynamero.pressure.base.records.PressureComponentList;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.enumerations.MappedDirection;
import com.dynamero.shared.utils.DirectionHelper;

/**
 * Abstract base dispersion block entity combining inventory and dispersion storage capabilities,
 * supporting sided dispersion access, temperature (heat) and pressure synchronization, and dispersion spreading.
 *
 * @param <T> the concrete dispersion block entity type
 * @param <B> the container inventory type
 * @param <C> the single dispersion storage type
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
public abstract class AbstractBaseDispersionBE<T extends AbstractBaseDispersionBE<T, B, C>, B extends SimpleContainer, C extends SingleDispersionStorage>
        extends AbstractBaseInventoryBE<T, B>
        implements DispersionStorageProvider<C>, DispersionConnectorProvider<C>, DispersionSpreadHandler<C>
{
    /**
     * Dispersion connector managing sided single dispersion storage instances.
     */
    protected final DispersionConnector<C> dispersionConnector;

    /**
     * Constructs an AbstractBaseDispersionBE and enables block ticking.
     *
     * @param type  the block entity type
     * @param pos   the block position in the world
     * @param state the initial block state
     */
    public AbstractBaseDispersionBE(BlockEntityType<@NotNull T> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
        dispersionConnector = new DispersionConnector<>();
        this.properties.tick();
    }

    /**
     * Retrieves the dispersion connector managing dispersion storage instances for this block entity.
     *
     * @return the dispersion connector
     */
    @Override
    public DispersionConnector<C> getDispersionConnector()
    {
        return dispersionConnector;
    }

    /**
     * Loads additional persistent data including dispersion connector states from the given value input.
     *
     * @param view the value input to read from
     */
    @Override
    protected void loadAdditional(@NotNull ValueInput view)
    {
        super.loadAdditional(view);
        dispersionConnector.loadAdditional(view);
    }

    /**
     * Saves additional persistent data including dispersion connector states to the given value output.
     *
     * @param view the value output to write to
     */
    @Override
    protected void saveAdditional(@NotNull ValueOutput view)
    {
        super.saveAdditional(view);
        dispersionConnector.saveAdditional(view);
    }

    /**
     * Retrieves the dispersion storage provider for the given mapped direction and block facing.
     *
     * @param direction the mapped direction relative to facing
     * @param facing    the current facing direction of the block
     * @return the dispersion storage provider, or null if none is available
     */
    @Override
    public @Nullable C getDispersionStorageProvider(MappedDirection direction, Direction facing)
    {
        return getDispersionStorageProvider(MappedDirection.toDirection(direction), facing);
    }

    /**
     * Retrieves the dispersion storage provider for the given world direction and block facing.
     *
     * @param direction the world direction
     * @param facing    the current facing direction of the block
     * @return the dispersion storage provider, or null if none is available
     */
    @Override
    public @Nullable C getDispersionStorageProvider(Direction direction, Direction facing)
    {
        Direction side = DirectionHelper.relativeDirection(direction, facing);
        return this.dispersionConnector.getSidedMap().containsKey(MappedDirection.fromDirection(side))
                ? this.dispersionConnector.getStorage(side)
                : this.dispersionConnector.getSidedMap().containsKey(MappedDirection.NONE)
                ? this.dispersionConnector.getStorage(MappedDirection.NONE)
                : null;
    }

    /**
     * Retrieves the dispersion storage instance for the given world direction taking block orientation into account.
     *
     * @param direction the world direction
     * @return the dispersion storage instance, or null if none is available
     */
    public Storage<DispersionVariant> getDispersionStorage(Direction direction)
    {
        if(level == null)
            return null;
        return this.level.getBlockState(this.worldPosition).getProperties().contains(BlockStateProperties.FACING)
                ? this.getDispersionStorageProvider(direction, this.level.getBlockState(this.worldPosition).getValue(BlockStateProperties.FACING))
                : this.level.getBlockState(this.worldPosition).getProperties().contains(BlockStateProperties.HORIZONTAL_FACING)
                ? this.getDispersionStorageProvider(direction, this.level.getBlockState(this.worldPosition).getValue(BlockStateProperties.HORIZONTAL_FACING))
                : this.dispersionConnector.getStorage(direction);
    }

    /**
     * Retrieves the dispersion storage instance for the given mapped direction taking block orientation into account.
     *
     * @param direction the mapped direction
     * @return the dispersion storage instance, or null if none is available
     */
    public Storage<DispersionVariant> getDispersionStorage(MappedDirection direction)
    {
        return getDispersionStorage(MappedDirection.toDirection(direction));
    }

    /**
     * Applies implicit data components from item stacks onto this block entity, restoring dispersion, heat, and pressure states.
     *
     * @param dataComponentGetter the component getter containing item component data
     * @throws IllegalArgumentException if data component lists mismatch the count of dispersion storages
     */
    @Override
    protected void applyImplicitComponents(@NotNull DataComponentGetter dataComponentGetter)
    {
        super.applyImplicitComponents(dataComponentGetter);

        if(dispersionConnector.getStorages().isEmpty())
            return;

        DispersionStackList list = dataComponentGetter.getOrDefault(Jilibs.Components.DISPERSION_LIST, DispersionStackList.EMPTY);
        HeatComponentList heatList = dataComponentGetter.getOrDefault(Jilibs.Components.HEAT_LIST, HeatComponentList.EMPTY);
        PressureComponentList pressureList = dataComponentGetter.getOrDefault(Jilibs.Components.PRESSURE_LIST, PressureComponentList.EMPTY);

        if(list.values().size() != dispersionConnector.getStorages().size())
            throw new IllegalArgumentException("Gas List Data Component has different size compared to Gas Storages");

        if(heatList.values().size() != dispersionConnector.getStorages().size())
            throw new IllegalArgumentException("Heat List List Data Component has different size compared to Gas Storages");

        if(pressureList.values().size() != dispersionConnector.getStorages().size())
            throw new IllegalArgumentException("Pressure List List Data Component has different size compared to Gas Storages");

        for (int i = 0; i < dispersionConnector.getStorages().size(); i++)
        {
            if (list.values().get(i) != null)
                dispersionConnector.getStorage(i).setDispersion(list.values().get(i));

            if(heatList.values().get(i) != null)
                dispersionConnector.getStorage(i).setHeatComponent(heatList.values().get(i));

            if(pressureList.values().get(i) != null)
                dispersionConnector.getStorage(i).setPressureComponent(pressureList.values().get(i));

            update();
        }
    }

    /**
     * Collects implicit data components from this block entity into the item stack component builder, saving dispersions, heat, and pressure.
     *
     * @param builder the component builder to collect data into
     */
    @Override
    protected void collectImplicitComponents(DataComponentMap.@NotNull Builder builder)
    {
        super.collectImplicitComponents(builder);

        List<DispersionStackPayload> list = new ArrayList<>();
        List<HeatComponent> heatComponentList = new ArrayList<>();
        List<PressureComponent> pressureComponentList = new ArrayList<>();

        for (var storage : dispersionConnector.getStorages())
        {
            list.add(new DispersionStackPayload(storage.variant, storage.getAmount()));
            heatComponentList.add(new HeatComponent(storage.getHeat(), storage.getHeatUnit()));
            pressureComponentList.add(new PressureComponent(storage.getPressure(), storage.getPressureUnit()));
        }

        builder.set(Jilibs.Components.DISPERSION_LIST, new DispersionStackList(list));
        builder.set(Jilibs.Components.HEAT_LIST, new HeatComponentList(heatComponentList));
        builder.set(Jilibs.Components.PRESSURE_LIST, new PressureComponentList(pressureComponentList));
    }
}