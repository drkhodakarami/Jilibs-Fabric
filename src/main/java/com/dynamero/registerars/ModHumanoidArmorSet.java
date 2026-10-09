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

import java.util.function.Function;

import com.dynamero.shared.annotations.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterial;

@SuppressWarnings("unused")

@Developer("TheMentor")
@CreatedAt("2026-08-10")
@ModifiedAt("2026-08-10")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public record ModHumanoidArmorSet(ModItem<?> helmet, ModItem<?> chestplate, ModItem<?> leggings, ModItem<?> boots, ModItem<?> body)
{
    public static class Builder
    {
        String name;
        ArmorMaterial material;
        ItemRegisterar registerar;
        ModItem<?> helmet, chestplate, leggings, boots, body;

        public Builder(String modid, String name, ArmorMaterial material)
        {
            registerar = new ItemRegisterar(modid);
            this.name = name;
            this.material = material;
        }

        public ModHumanoidArmorSet.Builder helmet()
        {
            this.helmet = registerar.registerHorseArmor(name, material);
            return this;
        }

        public <R extends Item> ModHumanoidArmorSet.Builder helmet(Function<Item.Properties, R> factory)
        {
            this.helmet = registerar.registerHorseArmor(name, material, factory);
            return this;
        }

        public ModHumanoidArmorSet.Builder helmet(Item.Properties settings)
        {
            this.helmet = registerar.registerHorseArmor(name, material, settings);
            return this;
        }

        public <R extends Item> ModHumanoidArmorSet.Builder helmet(Item.Properties settings, Function<Item.Properties, R> factory)
        {
            this.helmet = registerar.registerHorseArmor(name, material, settings, factory);
            return this;
        }

        public ModHumanoidArmorSet.Builder chestplate()
        {
            this.chestplate = registerar.registerHorseArmor(name, material);
            return this;
        }

        public <R extends Item> ModHumanoidArmorSet.Builder chestplate(Function<Item.Properties, R> factory)
        {
            this.chestplate = registerar.registerHorseArmor(name, material, factory);
            return this;
        }

        public ModHumanoidArmorSet.Builder chestplate(Item.Properties settings)
        {
            this.chestplate = registerar.registerHorseArmor(name, material, settings);
            return this;
        }

        public <R extends Item> ModHumanoidArmorSet.Builder chestplate(Item.Properties settings, Function<Item.Properties, R> factory)
        {
            this.chestplate = registerar.registerHorseArmor(name, material, settings, factory);
            return this;
        }

        public ModHumanoidArmorSet.Builder leggings()
        {
            this.leggings = registerar.registerHorseArmor(name, material);
            return this;
        }

        public <R extends Item> ModHumanoidArmorSet.Builder leggings(Function<Item.Properties, R> factory)
        {
            this.leggings = registerar.registerHorseArmor(name, material, factory);
            return this;
        }

        public ModHumanoidArmorSet.Builder leggings(Item.Properties settings)
        {
            this.leggings = registerar.registerHorseArmor(name, material, settings);
            return this;
        }

        public <R extends Item> ModHumanoidArmorSet.Builder leggings(Item.Properties settings, Function<Item.Properties, R> factory)
        {
            this.leggings = registerar.registerHorseArmor(name, material, settings, factory);
            return this;
        }

        public ModHumanoidArmorSet.Builder boots()
        {
            this.boots = registerar.registerHorseArmor(name, material);
            return this;
        }

        public <R extends Item> ModHumanoidArmorSet.Builder boots(Function<Item.Properties, R> factory)
        {
            this.boots = registerar.registerHorseArmor(name, material, factory);
            return this;
        }

        public ModHumanoidArmorSet.Builder boots(Item.Properties settings)
        {
            this.boots = registerar.registerHorseArmor(name, material, settings);
            return this;
        }

        public <R extends Item> ModHumanoidArmorSet.Builder boots(Item.Properties settings, Function<Item.Properties, R> factory)
        {
            this.boots = registerar.registerHorseArmor(name, material, settings, factory);
            return this;
        }

        public ModHumanoidArmorSet.Builder body()
        {
            this.body = registerar.registerHorseArmor(name, material);
            return this;
        }

        public <R extends Item> ModHumanoidArmorSet.Builder body(Function<Item.Properties, R> factory)
        {
            this.body = registerar.registerHorseArmor(name, material, factory);
            return this;
        }

        public ModHumanoidArmorSet.Builder body(Item.Properties settings)
        {
            this.body = registerar.registerHorseArmor(name, material, settings);
            return this;
        }

        public <R extends Item> ModHumanoidArmorSet.Builder body(Item.Properties settings, Function<Item.Properties, R> factory)
        {
            this.body = registerar.registerHorseArmor(name, material, settings, factory);
            return this;
        }

        public ModHumanoidArmorSet build()
        {
            return new ModHumanoidArmorSet(helmet, chestplate, leggings, boots, body);
        }
    }
}