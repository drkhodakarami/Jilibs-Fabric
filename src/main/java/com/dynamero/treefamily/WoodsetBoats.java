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

package com.dynamero.treefamily;

import com.dynamero.registerars.ModEntity;
import com.dynamero.registerars.ModItem;
import net.minecraft.world.entity.animal.fish.TropicalFish;
import org.jetbrains.annotations.NotNull;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.entity.vehicle.boat.ChestBoat;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import com.dynamero.registerars.EntityRegisterar;
import com.dynamero.registerars.ItemRegisterar;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.utils.BaseHelper;

/**
 * Container and registry holder for boat and chest boat entities and items in a {@link TreeFamilyBuilder}.
 *
 * @param boatItem Item for placing the standard boat.
 * @param chestBoatItem Item for placing the chest boat.
 * @param boat Entity type definition for the standard boat.
 * @param chestBoat Entity type definition for the chest boat.
 */
@SuppressWarnings("unused")
@Developer("TurtyWurty")
@CreatedAt("2026-10-08")
@ModifiedAt("2026-10-08")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public record WoodsetBoats(ModEntity<EntityType<@NotNull Boat>> boat, ModEntity<EntityType<@NotNull ChestBoat>> chestBoat, ModItem<BoatItem> boatItem, ModItem<BoatItem> chestBoatItem)
{
    /**
     * Constructs and registers boat entities and items for a wood set.
     *
     * @param modid               the mod identifier
     * @param name                the base wood name
     * @param itemRegister        the item registerer helper
     * @param entityRegister      the entity registerer helper
     * @param planks              the planks block of this wood set
     */
    public static WoodsetBoats build(String modid, String name,
                        ItemRegisterar itemRegister, EntityRegisterar entityRegister,
                        Block planks)
    {
        var boatItemId = BaseHelper.ResourceKeys.create(modid, name + "_boat", Registries.ITEM);
        var chestBoatItemId = BaseHelper.ResourceKeys.create(modid, name + "_chest_boat", Registries.ITEM);

        //region Register
        var boatType = entityRegister
                .register(name + "_boat",
                          EntityType.Builder.<Boat>of((type, world) ->
                                                              new Boat(type, world, planks::asItem),
                                                      MobCategory.MISC)
                                            .noLootTable()
                                            .sized(1.375F, 0.5625F)
                                            .eyeHeight(0.5625F)
                                            .clientTrackingRange(10));

        var chestBoatType = entityRegister
                .register(name + "_chest_boat",
                          EntityType.Builder.<ChestBoat>of((type, world) ->
                                                                   new ChestBoat(type, world, planks::asItem),
                                                           MobCategory.MISC)
                                            .noLootTable()
                                            .sized(1.375F, 0.5625F)
                                            .eyeHeight(0.5625F)
                                            .clientTrackingRange(10));

        var boat = new ModEntity<>(boatType, BaseHelper.ResourceKeys.get(boatType));
        var chestBoat = new ModEntity<>(chestBoatType, BaseHelper.ResourceKeys.get(chestBoatType));

        var boatItem = new ModItem<>(itemRegister
                                              .register(name + "_boat",
                                                        settings ->
                                                                new BoatItem(boat.entity(),
                                                                             settings.stacksTo(1))).item(),
                                      boatItemId);

        var chestBoatItem = new ModItem<>(itemRegister
                                                   .register(name + "_boat",
                                                settings ->
                                                        new BoatItem(chestBoat.entity(),
                                                                     settings.stacksTo(1))).item(),
                                           chestBoatItemId);
        //endregion
        return new WoodsetBoats(boat, chestBoat, boatItem, chestBoatItem);
    }
}