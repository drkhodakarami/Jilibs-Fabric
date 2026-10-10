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

package com.dynamero.inventory.menu;

import net.fabricmc.fabric.api.transfer.v1.item.ContainerStorage;
import org.jetbrains.annotations.NotNull;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;

import com.dynamero.gui.AbstractBaseContainerScreen;
import com.dynamero.inventory.base.InventoryConnector;
import com.dynamero.inventory.base.InventoryConnectorCH;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.data.CachedBlockEntity;
import com.dynamero.shared.interfaces.StorageConnectorProvider;
import com.dynamero.shared.interfaces.StorageProvider;
import com.dynamero.shared.records.BlockPosPayload;

/**
 * Abstract screen handler base class for block entities managing inventories via {@link InventoryConnector}.
 * <p>
 * Handles client-side slot configuration, inventory opening/closing lifecycle notifications,
 * and block entity resolution via network payloads.
 * </p>
 *
 * @param <T> the block entity type providing container storage and connector access
 */
@SuppressWarnings({"unused", "unchecked", "DataFlowIssue"})
@Developer("The Mentor")
@CreatedAt("2026-10-08")
@ModifiedAt("2026-10-08")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public abstract class AbstractBaseInventoryMenu<T extends BlockEntity & StorageProvider<ContainerStorage> & StorageConnectorProvider<InventoryConnector<?>>> extends AbstractBaseContainerScreen<@NotNull T>
{
    /**
     * Cloned or referenced inventory connector managing the container slots for this screen handler.
     */
    protected final InventoryConnector<?> inventory;

    /**
     * Constructs an AbstractBaseInventoryCH resolving the block entity class dynamically from the level.
     *
     * @param type            the menu type
     * @param syncId          the synchronization identifier
     * @param playerInventory the player inventory
     * @param payload         the block position payload
     */
    public AbstractBaseInventoryMenu(MenuType<?> type, int syncId, Inventory playerInventory, BlockPosPayload payload)
    {
        this(type, syncId, playerInventory, payload, (Class<T>) playerInventory.player.level().getBlockEntity(payload.pos()).getClass());
    }

    /**
     * Constructs an AbstractBaseInventoryCH with a known block entity class.
     *
     * @param type             the menu type
     * @param syncID           the synchronization identifier
     * @param playerInventory  the player inventory
     * @param payload          the block position payload
     * @param blockEntityClass the block entity class
     */
    public AbstractBaseInventoryMenu(MenuType<?> type, int syncID, Inventory playerInventory, BlockPosPayload payload, Class<T> blockEntityClass)
    {
        this(type, syncID, playerInventory, payload, new CachedBlockEntity<>(blockEntityClass));
    }

    /**
     * Constructs an AbstractBaseInventoryCH using a cached block entity resolver.
     *
     * @param type            the menu type
     * @param syncID          the synchronization identifier
     * @param playerInventory the player inventory
     * @param payload         the block position payload
     * @param cachedBE        the cached block entity resolver
     */
    public AbstractBaseInventoryMenu(MenuType<?> type, int syncID, Inventory playerInventory, BlockPosPayload payload, CachedBlockEntity<@NotNull T> cachedBE)
    {
        this(type, syncID, playerInventory,
             InventoryConnectorCH.copyOf(cachedBE.apply(playerInventory, payload.pos()).getConnector()),
             cachedBE.apply(playerInventory, payload.pos()));
    }

    /**
     * Constructs an AbstractBaseInventoryCH with explicit inventory connector and block entity instances.
     *
     * @param type            the menu type
     * @param syncId          the synchronization identifier
     * @param playerInventory the player inventory
     * @param inventory       the inventory connector instance
     * @param blockEntity     the owning block entity instance
     */
    public AbstractBaseInventoryMenu(MenuType<?> type, int syncId, Inventory playerInventory, InventoryConnector<?> inventory, T blockEntity)
    {
        super(type, syncId, playerInventory, blockEntity);
        this.inventory = inventory;
        this.inventory.checkSize(getInventorySize());
        this.addSlots();
        this.inventory.onOpen(playerInventory.player);
    }

    /**
     * Handles screen handler closure and notifies the underlying inventory connector.
     *
     * @param player the player closing the screen handler
     */
    @Override
    public void removed(@NotNull Player player)
    {
        super.removed(player);
        this.inventory.removed(player);
    }

    /**
     * Configures and adds inventory-specific slots to this screen handler.
     */
    protected abstract void addSlots();

    /**
     * Indicates whether player inventory and hotbar slots should be automatically added.
     *
     * @return {@code true} to add player inventory slots, {@code false} otherwise
     */
    @Override
    public boolean addPlayerInventory()
    {
        return true;
    }
}