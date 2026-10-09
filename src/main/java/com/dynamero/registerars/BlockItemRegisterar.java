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

package com.dynamero.registerars;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import com.dynamero.registerars.factory.IBlockItemFactory;
import com.dynamero.registerars.interfaces.IBlockItemRegisterar;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.utils.BaseHelper;

/**
 * Provides utility methods for registering block items in Minecraft.
 */
@Developer("TheMentor")
@CreatedAt("2026-08-10")
@ModifiedAt("2026-08-10")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public class BlockItemRegisterar implements IBlockItemRegisterar
{
    /**
     * The unique identifier for the mod.
     */
    private final String modId;

    /**
     * Constructs a new JiBlockItemRegister with the specified mod ID.
     *
     * @param modId The unique identifier for the mod.
     */
    public BlockItemRegisterar(String modId)
    {
        this.modId = modId;
    }

    //region Helper Overloads

    public BlockItem register(Block block)
    {
        String name = BaseHelper.RegistryNames.get(block);
        return register(block, BaseHelper.ResourceKeys.create(this.modId, name, Registries.ITEM));
    }

    public <R extends BlockItem> R register(Block block, IBlockItemFactory<Item.Properties, R> factory)
    {

        String name = BaseHelper.RegistryNames.get(block);
        return register(block, BaseHelper.ResourceKeys.create(this.modId, name, Registries.ITEM), factory);
    }

    public <R extends BlockItem> R register(Block block, Item.Properties settings, IBlockItemFactory<Item.Properties, R> factory)
    {
        String name = BaseHelper.RegistryNames.get(block);
        return register(block, BaseHelper.ResourceKeys.create(this.modId, name, Registries.ITEM), settings, factory);
    }
    //endregion

    //region Main Overloads
    @Override
    public <B extends Block> BlockItem register(B block, ResourceKey<Item> key)
    {
        return Registry.register(BuiltInRegistries.ITEM, key, new BlockItem(block,
                                                                            new Item.Properties()
                                                                                    .useBlockDescriptionPrefix()
                                                                                    .setId(key)));
    }

    @Override
    public <B extends Block> BlockItem register(B block, BlockItemId blockItemId)
    {
        return Registry.register(BuiltInRegistries.ITEM, blockItemId.item(), new BlockItem(block,
                                                                            new Item.Properties()
                                                                                    .useBlockDescriptionPrefix()
                                                                                    .setId(blockItemId.item())));
    }

    @Override
    public <B extends Block, R extends BlockItem> R register(B block, ResourceKey<Item> key, IBlockItemFactory<Item.Properties, R> factory)
    {
        R item = factory.apply(block, new Item.Properties()
                .useBlockDescriptionPrefix()
                .setId(key));
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }

    @Override
    public <B extends Block, R extends BlockItem> R register(B block, BlockItemId blockItemId, IBlockItemFactory<Item.Properties, R> factory)
    {
        R item = factory.apply(block, new Item.Properties()
                .useBlockDescriptionPrefix()
                .setId(blockItemId.item()));
        return Registry.register(BuiltInRegistries.ITEM, blockItemId.item(), item);
    }

    @Override
    public <B extends Block, R extends BlockItem> R register(B block, ResourceKey<Item> key, Item.Properties settings, IBlockItemFactory<Item.Properties, R> factory)
    {
        R item = factory.apply(block, settings
                .useBlockDescriptionPrefix()
                .setId(key));
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }

    @Override
    public <B extends Block, R extends BlockItem> R register(B block, BlockItemId blockItemId, Item.Properties settings, IBlockItemFactory<Item.Properties, R> factory)
    {
        R item = factory.apply(block, settings
                .useBlockDescriptionPrefix()
                .setId(blockItemId.item()));
        return Registry.register(BuiltInRegistries.ITEM, blockItemId.item(), item);
    }
    //endregion
}