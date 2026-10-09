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

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

@SuppressWarnings("unused")
public record ModToolSet(ModItem<?> axe, ModItem<?> hoe, ModItem<?> pickaxe, ModItem<?> shovel, ModItem<?> sword)
{
    public static class Builder
    {
        String name;
        ToolMaterial material;
        ItemRegisterar registerar;
        ModItem<?> axe, hoe, pickaxe, shovel, sword;
        float
                axe_attackDamage, axe_attackSpeed,
                hoe_attackDamage, hoe_attackSpeed,
                pickaxe_attackDamage, pickaxe_attackSpeed,
                shovel_attackDamage, shovel_attackSpeed,
                sword_attackDamage, sword_attackSpeed;

        public Builder(String modid, String name, ToolMaterial material)
        {
            registerar = new ItemRegisterar(modid);
            this.name = name;
            this.material = material;
        }

        public <R extends Item> Builder axe(float attackDamage, float attackSpeed, Function<Item.Properties, R> factory)
        {
            this.axe = registerar.registerAxe(name, material, attackDamage, attackSpeed, factory);
            return this;
        }

        public <R extends Item> Builder axe(float attackDamage, float attackSpeed, Item.Properties settings, Function<Item.Properties, R> factory)
        {
            this.axe = registerar.registerAxe(name, material, attackDamage, attackSpeed, settings, factory);
            return this;
        }

        public Builder axe(float attackDamage, float attackSpeed)
        {
            this.axe = registerar.registerAxe(name, material, attackDamage, attackSpeed, Item::new);
            return this;
        }

        public Builder axe(float attackDamage, float attackSpeed, Item.Properties settings)
        {
            this.axe = registerar.registerAxe(name, material, attackDamage, attackSpeed, settings, Item::new);
            return this;
        }

        public <R extends Item> Builder hoe(float attackDamage, float attackSpeed, Function<Item.Properties, R> factory)
        {
            this.hoe = registerar.registerAxe(name, material, attackDamage, attackSpeed, factory);
            return this;
        }

        public <R extends Item> Builder hoe(float attackDamage, float attackSpeed, Item.Properties settings, Function<Item.Properties, R> factory)
        {
            this.hoe = registerar.registerAxe(name, material, attackDamage, attackSpeed, settings, factory);
            return this;
        }

        public Builder hoe(float attackDamage, float attackSpeed)
        {
            this.hoe = registerar.registerAxe(name, material, attackDamage, attackSpeed);
            return this;
        }

        public Builder hoe(float attackDamage, float attackSpeed, Item.Properties settings)
        {
            this.hoe = registerar.registerAxe(name, material, attackDamage, attackSpeed, settings);
            return this;
        }

        public <R extends Item> Builder pickaxe(float attackDamage, float attackSpeed, Function<Item.Properties, R> factory)
        {
            this.pickaxe = registerar.registerAxe(name, material, attackDamage, attackSpeed, factory);
            return this;
        }

        public <R extends Item> Builder pickaxe(float attackDamage, float attackSpeed, Item.Properties settings, Function<Item.Properties, R> factory)
        {
            this.pickaxe = registerar.registerAxe(name, material, attackDamage, attackSpeed, settings, factory);
            return this;
        }

        public Builder pickaxe(float attackDamage, float attackSpeed)
        {
            this.pickaxe = registerar.registerAxe(name, material, attackDamage, attackSpeed);
            return this;
        }

        public Builder pickaxe(float attackDamage, float attackSpeed, Item.Properties settings)
        {
            this.pickaxe = registerar.registerAxe(name, material, attackDamage, attackSpeed, settings);
            return this;
        }

        public <R extends Item> Builder shovel(float attackDamage, float attackSpeed, Function<Item.Properties, R> factory)
        {
            this.shovel = registerar.registerAxe(name, material, attackDamage, attackSpeed, factory);
            return this;
        }

        public <R extends Item> Builder shovel(float attackDamage, float attackSpeed, Item.Properties settings, Function<Item.Properties, R> factory)
        {
            this.shovel = registerar.registerAxe(name, material, attackDamage, attackSpeed, settings, factory);
            return this;
        }

        public Builder shovel(float attackDamage, float attackSpeed)
        {
            this.shovel = registerar.registerAxe(name, material, attackDamage, attackSpeed);
            return this;
        }

        public Builder shovel(float attackDamage, float attackSpeed, Item.Properties settings)
        {
            this.shovel = registerar.registerAxe(name, material, attackDamage, attackSpeed, settings);
            return this;
        }

        public <R extends Item> Builder sword(float attackDamage, float attackSpeed, Function<Item.Properties, R> factory)
        {
            this.sword = registerar.registerAxe(name, material, attackDamage, attackSpeed, factory);
            return this;
        }

        public <R extends Item> Builder sword(float attackDamage, float attackSpeed, Item.Properties settings, Function<Item.Properties, R> factory)
        {
            this.sword = registerar.registerAxe(name, material, attackDamage, attackSpeed, settings, factory);
            return this;
        }

        public Builder sword(float attackDamage, float attackSpeed)
        {
            this.sword = registerar.registerAxe(name, material, attackDamage, attackSpeed, Item::new);
            return this;
        }

        public Builder sword(float attackDamage, float attackSpeed, Item.Properties settings)
        {
            this.sword = registerar.registerAxe(name, material, attackDamage, attackSpeed, settings);
            return this;
        }

        public ModToolSet build()
        {
            return new ModToolSet(axe, hoe, pickaxe, shovel, sword);
        }
    }
}