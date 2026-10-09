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

package com.dynamero.register;

import java.util.function.Function;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

import com.dynamero.register.factory.IToolFactory;
import com.dynamero.register.interfaces.IItemRegisterar;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.utils.BaseHelper;

/**
 * Registers custom items for Minecraft.
 */
@SuppressWarnings("unused")
@Developer("The Mentor")
@CreatedAt("2025-04-18")
@Repository("https://github.com/drkhodakarami/___PROJECTS___")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")

public class ItemRegisterar implements IItemRegisterar
{
    /**
     * The mod ID used for registering items.
     */
    private final String modId;

    /**
     * Constructs a new instance of JiItemRegister with the specified mod ID.
     *
     * @param mod_ID the mod ID
     */
    public ItemRegisterar(String mod_ID)
    {
        this.modId = mod_ID;
    }

    @Override
    public <R extends Item> ModItem<R> register(String name, Function<Item.Properties, ? extends R> factory)
    {
        var key = BaseHelper.ResourceKeys.create(this.modId, name, Registries.ITEM);
        return new ModItem<>(register(key, factory), key);
    }

    @Override
    public <R extends Item> ModItem<R> register(String name, int stackCount, Function<Item.Properties, ? extends R> factory)
    {
        var key = BaseHelper.ResourceKeys.create(this.modId, name, Registries.ITEM);
        return new ModItem<>(register(key, stackCount, factory), key);
    }

    @Override
    public <R extends Item> ModItem<R> register(String name, Item.Properties settings, Function<Item.Properties, ? extends R> factory)
    {
        var key = BaseHelper.ResourceKeys.create(this.modId, name, Registries.ITEM);
        return new ModItem<>(register(key, settings, factory), key);
    }

    @Override
    public <R extends Item> ModItem<R> register(String name, int stackCount, Item.Properties settings, Function<Item.Properties, ? extends R> factory)
    {
        var key = BaseHelper.ResourceKeys.create(this.modId, name, Registries.ITEM);
        return new ModItem<>(register(key, stackCount, settings, factory), key);
    }

    @Override
    public <R extends Item> ModItem<R> registerTool(String name, ToolMaterial material, float attackDamage, float attackSpeed,
                                                    IToolFactory<Item.Properties, ? extends R> factory)
    {
        ResourceKey<Item> key = BaseHelper.ResourceKeys.create(this.modId, name, Registries.ITEM);
        R item = factory.apply(material, attackDamage, attackSpeed, new Item.Properties().setId(key));
        return new ModItem<>(Registry.register(BuiltInRegistries.ITEM, key, item), key);
    }

    @Override
    public <R extends Item> ModItem<R> registerTool(String name, ToolMaterial material, float attackDamage, float attackSpeed,
                                                    Item.Properties settings, IToolFactory<Item.Properties, ? extends R> factory)
    {
        ResourceKey<Item> key = BaseHelper.ResourceKeys.create(this.modId, name, Registries.ITEM);
        R item = factory.apply(material, attackDamage, attackSpeed, settings.setId(key));
        return new ModItem<>(Registry.register(BuiltInRegistries.ITEM, key, item), key);
    }
}