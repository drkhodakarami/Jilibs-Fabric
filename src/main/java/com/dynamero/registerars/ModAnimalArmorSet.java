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
public record ModAnimalArmorSet(ModItem<?> horse, ModItem<?> nautilus, ModItem<?> wolf)
{
    public static class Builder
    {
        String name;
        ArmorMaterial material;
        ItemRegisterar registerar;
        ModItem<?> horse, nautilus, wolf;

        public Builder(String modid, String name, ArmorMaterial material)
        {
            registerar = new ItemRegisterar(modid);
            this.name = name;
            this.material = material;
        }

        public Builder horse()
        {
            this.horse = registerar.registerHorseArmor(name, material);
            return this;
        }

        public <R extends Item> Builder horse(Function<Item.Properties, R> factory)
        {
            this.horse = registerar.registerHorseArmor(name, material, factory);
            return this;
        }

        public Builder horse(Item.Properties settings)
        {
            this.horse = registerar.registerHorseArmor(name, material, settings);
            return this;
        }

        public <R extends Item> Builder horse(Item.Properties settings, Function<Item.Properties, R> factory)
        {
            this.horse = registerar.registerHorseArmor(name, material, settings, factory);
            return this;
        }

        public Builder nautilus()
        {
            this.nautilus = registerar.registerHorseArmor(name, material);
            return this;
        }

        public <R extends Item> Builder nautilus(Function<Item.Properties, R> factory)
        {
            this.nautilus = registerar.registerHorseArmor(name, material, factory);
            return this;
        }

        public Builder nautilus(Item.Properties settings)
        {
            this.nautilus = registerar.registerHorseArmor(name, material, settings);
            return this;
        }

        public <R extends Item> Builder nautilus(Item.Properties settings, Function<Item.Properties, R> factory)
        {
            this.nautilus = registerar.registerHorseArmor(name, material, settings, factory);
            return this;
        }

        public Builder wolf()
        {
            this.wolf = registerar.registerHorseArmor(name, material);
            return this;
        }

        public <R extends Item> Builder wolf(Function<Item.Properties, R> factory)
        {
            this.wolf = registerar.registerHorseArmor(name, material, factory);
            return this;
        }

        public Builder wolf(Item.Properties settings)
        {
            this.wolf = registerar.registerHorseArmor(name, material, settings);
            return this;
        }

        public <R extends Item> Builder wolf(Item.Properties settings, Function<Item.Properties, R> factory)
        {
            this.wolf = registerar.registerHorseArmor(name, material, settings, factory);
            return this;
        }

        public ModAnimalArmorSet build()
        {
            return new ModAnimalArmorSet(horse, nautilus, wolf);
        }
    }
}