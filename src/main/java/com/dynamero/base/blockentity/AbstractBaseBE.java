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

package com.dynamero.base.blockentity;

import com.dynamero.shared.annotations.*;
import com.dynamero.shared.interfaces.SyncedTicking;
import com.dynamero.shared.interfaces.Updatable;
import com.dynamero.shared.properties.BEProperties;
import com.mojang.logging.LogUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueOutput;

/**
 * Abstract base class for modded block entities providing synchronized ticking (server and client),
 * dirty-tracking updates, modular properties, and environmental query utilities.
 *
 * @param <T> the concrete block entity type extending this base class
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
public abstract class AbstractBaseBE<T extends AbstractBaseBE<T>> extends BlockEntity implements Updatable, SyncedTicking
{
	/**
	 * Flag indicating whether this block entity has changed state and needs synchronization or disk save.
	 */
	protected boolean isDirty = false;

	/**
	 * Client-side dirty flag for tracking local client updates.
	 */
	protected boolean isDirtyClient = false;

	/**
	 * Server-side tick counter incremented on every tick.
	 */
	protected int ticks;

	/**
	 * Client-side tick counter incremented on every client tick.
	 */
	protected int clientTicks;

	/**
	 * Modular properties manager holding capabilities, fields, and tick logic for this block entity.
	 */
	protected BEProperties<@NotNull T> properties;

	/**
	 * Constructs a new AbstractBaseBE instance.
	 *
	 * @param type  the block entity type
	 * @param pos   the block position in the world
	 * @param state the initial block state
	 */
	@SuppressWarnings("unchecked")
	public AbstractBaseBE(BlockEntityType<@NotNull T> type, BlockPos pos, BlockState state)
	{
		super(type, pos, state);
		properties = new BEProperties<>((T)this);
	}

	@Override
	public void update()
	{
		this.isDirty = true;

		if(this.properties!= null && !this.properties.isWaitingEndTick())
		{
			setChanged();

			if(this.level != null && !this.level.isClientSide())
				this.level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
		}
	}

	@Override
	public void onTickEnd()
	{
		if(this.isDirty)
		{
			this.isDirty = false;
			setChanged();
			if(this.level != null && !this.level.isClientSide())
				this.level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
		}
	}

	@Override
	public @Nullable Packet<@NotNull ClientGamePacketListener> getUpdatePacket()
	{
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries)
	{
		CompoundTag nbt = super.getUpdateTag(registries);
		Logger logger = LogUtils.getLogger();
		try (ProblemReporter.ScopedCollector logging = new ProblemReporter.ScopedCollector(this.problemPath(), logger)) {
			TagValueOutput view = TagValueOutput.createWithContext(logging, registries);
			saveAdditional(view);
			return view.buildResult();
		}
	}

	@Override
	public void onTick()
	{
		if(this.ticks == 0)
			this.onInternalFirstTick();

		if(this.properties.getTickRate() == 0 || (this.ticks % this.properties.getTickRate() == 0))
			this.onInternalTick();

		this.ticks++;
	}

	@Override
	public void onTickClient()
	{
		if(this.clientTicks == 0)
			this.onInternalFirstTickClient();

		if(this.properties.getTickRate() == 0 || (this.clientTicks % this.properties.getTickRate() == 0))
			this.onInternalTickClient();

		this.clientTicks++;
	}

	@Override
	public boolean shouldSync()
	{
		return this.properties.isSynced();
	}

	/**
	 * Registers default tracked properties such as dirty flags, ticks, level, position, and cached state.
	 */
	protected void registerDefaultFields()
	{
		this.properties.fields().addField("isDirty", this.isDirty,
										blockEntity -> blockEntity.isDirty,
										((blockEntity, val) -> blockEntity.isDirty = val));
		this.properties.fields().addField("isDirtyClient", this.isDirtyClient,
										blockEntity -> blockEntity.isDirtyClient,
										((blockEntity, val) -> blockEntity.isDirtyClient = val));
		this.properties.fields().addField("ticks", this.ticks,
										blockEntity -> blockEntity.ticks,
										((blockEntity, val) -> blockEntity.ticks = val));
		this.properties.fields().addField("ticksClient", this.clientTicks,
										blockEntity -> blockEntity.clientTicks,
										((blockEntity, val) -> blockEntity.clientTicks = val));

		if(this.level != null)
			this.properties.fields().addField("world", this.level, AbstractBaseBE::getLevel, null);

		this.properties.fields().addField("pos", this.worldPosition, AbstractBaseBE::getBlockPos, null);
		this.properties.fields().addField("cachedState", getBlockState(), AbstractBaseBE::getBlockState, null);
	}

	/**
	 * Returns the current server-side tick count.
	 *
	 * @return the number of server ticks elapsed
	 */
	public int getTicks()
	{
		return this.ticks;
	}

	/**
	 * Returns the current client-side tick count.
	 *
	 * @return the number of client ticks elapsed
	 */
	public int getTicksClient()
	{
		return this.clientTicks;
	}

	private void onInternalFirstTick()
	{
		registerDefaultFields();
		registerFields();
		onFirstTick();
	}

	private void onInternalFirstTickClient()
	{
		onFirstTickClient();
	}

	private void onInternalTick()
	{
		if(this.properties.tickLogic() != null)
			this.properties.tickLogic().tick(this.properties);
	}

	private void onInternalTickClient()
	{
		if(this.properties.tickLogic() != null)
			this.properties.tickLogic().tickClient(this.properties);
	}

	/**
	 * Invoked once on the very first server tick after world placement/loading.
	 */
	protected void onFirstTick() {}

	/**
	 * Invoked once on the very first client tick after world placement/loading.
	 */
	protected void onFirstTickClient() {}

	/**
	 * Subclasses override this method to register custom syncable or persistent fields.
	 */
	protected void registerFields() {}

	/**
	 * Checks whether the current world is currently thundering.
	 *
	 * @return true if thundering, false otherwise
	 */
	public boolean isThundering()
	{
		return this.level != null && this.level.isThundering();
	}

	/**
	 * Checks whether the current world is currently raining.
	 *
	 * @return true if raining, false otherwise
	 */
	public boolean isRaining()
	{
		return this.level != null && this.level.isRaining();
	}

	/**
	 * Retrieves the current overworld clock time (0 - 24000).
	 *
	 * @return the day time in ticks
	 */
	public long getDayTime()
	{
		return this.level != null ? this.level.getOverworldClockTime() : 0;
	}

	/**
	 * Retrieves the total game time in ticks since world creation.
	 *
	 * @return the total game time in ticks
	 */
	public long getGameTime()
	{
		return this.level != null ? this.level.getGameTime() : 0;
	}

	/**
	 * Retrieves the sky light level directly above this block entity.
	 *
	 * @return the sky light brightness (0 - 15)
	 */
	public int getSkyLightLevelAbove()
	{
		return this.level != null ? this.level.getBrightness(LightLayer.SKY, getBlockPos().above()) : 0;
	}

	/**
	 * Checks whether the block directly above this block entity has unobstructed view of the sky.
	 *
	 * @return true if sky is visible from above, false otherwise
	 */
	public boolean canSeeSky()
	{
		return this.level != null && this.level.canSeeSky(getBlockPos().above());
	}
}