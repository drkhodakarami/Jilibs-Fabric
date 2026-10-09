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

package com.dynamero.woodset;

import java.util.function.Function;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.entity.vehicle.boat.ChestBoat;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import com.dynamero.register.EntityRegisterar;
import com.dynamero.register.ItemRegisterar;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.utils.BaseHelper;

/**
 * Container and registry holder for boat and chest boat entities and items in a {@link WoodSet}.
 */
@SuppressWarnings("unused")
@Developer("TurtyWurty")
@ModifiedBy("The Mentor")
@CreatedAt("2025-04-15")
@Repository("https://github.com/DaRealTurtyWurty/Industria")
@Discord("https://discord.turtywurty.dev/")
@Youtube("https://www.youtube.com/@TurtyWurty")
public class WoodsetBoats
{
    /**
     * Item for placing the standard boat.
     */
    public final Item boatItem;

    /**
     * Item for placing the chest boat.
     */
    public final Item chestBoatItem;

    /**
     * Entity type definition for the standard boat.
     */
    public final EntityType<@NotNull Boat> boatEntityType;

    /**
     * Entity type definition for the chest boat.
     */
    public final EntityType<@NotNull ChestBoat> chestBoatEntityType;

    /**
     * Resource key for the boat item.
     */
    public final ResourceKey<Item> boatItemId;

    /**
     * Resource key for the chest boat item.
     */
    public final ResourceKey<Item> chestBoatItemId;

    /**
     * Constructs and registers boat entities and items for a wood set.
     *
     * @param modid               the mod identifier
     * @param name                the base wood name
     * @param itemRegister        the item registerer helper
     * @param entityRegister      the entity registerer helper
     * @param planks              the planks block of this wood set
     * @param boatEntityType      factory function for the boat entity builder
     * @param chestBoatEntityType factory function for the chest boat entity builder
     * @param boatItem            factory function for the boat item
     * @param chestBoatItem       factory function for the chest boat item
     */
    public WoodsetBoats(String modid, String name,
                        ItemRegisterar itemRegister, EntityRegisterar entityRegister,
                        Block planks,
                        Function<Supplier<Item>, EntityType.Builder<@NotNull Boat>> boatEntityType,
                        Function<Supplier<Item>, EntityType.Builder<@NotNull ChestBoat>> chestBoatEntityType,
                        Function<Item.Properties, Item> boatItem,
                        Function<Item.Properties, Item> chestBoatItem)
    {
        boatItemId = BaseHelper.ResourceKeys.create(modid, name + "_boat", Registries.ITEM);
        chestBoatItemId = BaseHelper.ResourceKeys.create(modid, name + "_chest_boat", Registries.ITEM);

        //region Register
        this.boatEntityType = entityRegister.register(name + "_boat",
                                                    boatEntityType == null
                                                    ? EntityType.Builder.<Boat>of(
                                                                        (type, world) ->
                                                                        new Boat(type, world, planks::asItem), MobCategory.MISC)
                                                                        .noLootTable()
                                                                        .sized(1.375F, 0.5625F)
                                                                        .eyeHeight(0.5625F)
                                                                        .clientTrackingRange(10)
                                                    : boatEntityType.apply(planks::asItem));

        this.chestBoatEntityType = entityRegister.register(name + "_chest_boat",
                                                        chestBoatEntityType == null
                                                        ? EntityType.Builder.<ChestBoat>of(
                                                                            (type, world) ->
                                                                            new ChestBoat(type, world, planks::asItem), MobCategory.MISC)
                                                                            .noLootTable()
                                                                            .sized(1.375F, 0.5625F)
                                                                            .eyeHeight(0.5625F)
                                                                            .clientTrackingRange(10)
                                                        : chestBoatEntityType.apply(planks::asItem));

        this.boatItem = itemRegister.register(name + "_boat",
                                            boatItem == null
                                            ? settings -> new BoatItem(this.boatEntityType, settings.stacksTo(1))
                                            : boatItem).item();

        this.chestBoatItem = itemRegister.register(name + "_boat",
                                                chestBoatItem == null
                                                ? settings -> new BoatItem(this.chestBoatEntityType, settings.stacksTo(1))
                                                : chestBoatItem).item();
        //endregion
    }
}