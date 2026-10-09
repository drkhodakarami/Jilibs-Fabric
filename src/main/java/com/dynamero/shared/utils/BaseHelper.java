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

package com.dynamero.shared.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.NotNull;

import net.minecraft.advancements.triggers.CriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockItemTagId;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.attribute.AttributeType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.entity.npc.villager.VillagerType;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.DecoratedPotPattern;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;

import com.dynamero.shared.annotations.*;
import com.dynamero.shared.exceptions.Exceptions;

/**
 * Provides utility methods for working with various Minecraft registries and identifiers.
 */
@SuppressWarnings({"unused", "OptionalGetWithoutIsPresent"})
@Developer("TheMentor")
@CreatedAt("2025-04-18")
@ModifiedAt("2025-04-23")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")

public class BaseHelper
{
    /**
     * Private constructor to prevent instantiation.
     */
    public BaseHelper()
    {
        Exceptions.throwCtorAssertion();
    }

    public static class ResourceKeys
    {
        /**
         * The default constructor for the class
         */
        public ResourceKeys()
        {
        }

        /**
         * Creates a new registry key with the given name and registry type.
         *
         * @param <T>         The type of the registry.
         * @param modID       The mod ID.
         * @param name        The name for the registry key.
         * @param registryKey The registry key for the target registry.
         *
         * @return The created RegistryKey.
         */
        public static <T> ResourceKey<T> create(String modID, String name, ResourceKey<? extends Registry<T>> registryKey)
        {
            return ResourceKey.create(registryKey, id(modID, name));
        }

        public static ResourceKey<Fluid> get(Fluid fluid)
        {
            return BuiltInRegistries.FLUID.getResourceKey(fluid).get();
        }

        public static ResourceKey<Item> get(Item item)
        {
            return BuiltInRegistries.ITEM.getResourceKey(item).get();
        }

        public static ResourceKey<Item> get(ItemStack stack)
        {
            return BuiltInRegistries.ITEM.getResourceKey(stack.getItem()).get();
        }

        public static ResourceKey<Block> get(Block block)
        {
            return BuiltInRegistries.BLOCK.getResourceKey(block).get();
        }

        public static ResourceKey<BlockEntityType<?>> get(BlockEntityType<?> blockEntityType)
        {
            return BuiltInRegistries.BLOCK_ENTITY_TYPE.getResourceKey(blockEntityType).get();
        }

        public static ResourceKey<EntityType<?>> get(EntityType<?> entityType)
        {
            return BuiltInRegistries.ENTITY_TYPE.getResourceKey(entityType).get();
        }

        public static ResourceKey<Potion> get(Potion potion)
        {
            return BuiltInRegistries.POTION.getResourceKey(potion).get();
        }

        public static ResourceKey<ChunkStatus> get(ChunkStatus status)
        {
            return BuiltInRegistries.CHUNK_STATUS.getResourceKey(status).get();
        }

        public static ResourceKey<Attribute> get(Attribute attribute)
        {
            return BuiltInRegistries.ATTRIBUTE.getResourceKey(attribute).get();
        }

        public static ResourceKey<AttributeType<?>> get(AttributeType<?> attributeType)
        {
            return BuiltInRegistries.ATTRIBUTE_TYPE.getResourceKey(attributeType).get();
        }

        public static ResourceKey<MenuType<?>> get(MenuType<?> menuType)
        {
            return BuiltInRegistries.MENU.getResourceKey(menuType).get();
        }

        public static ResourceKey<RecipeType<?>> get(RecipeType<?> recipeType)
        {
            return BuiltInRegistries.RECIPE_TYPE.getResourceKey(recipeType).get();
        }

        public static ResourceKey<RecipeSerializer<?>> get(RecipeSerializer<?> recipeSerializer)
        {
            return BuiltInRegistries.RECIPE_SERIALIZER.getResourceKey(recipeSerializer).get();
        }

        public static ResourceKey<VillagerType> get(VillagerType villagerType)
        {
            return BuiltInRegistries.VILLAGER_TYPE.getResourceKey(villagerType).get();
        }

        public static ResourceKey<VillagerProfession> get(VillagerProfession villagerProfession)
        {
            return BuiltInRegistries.VILLAGER_PROFESSION.getResourceKey(villagerProfession).get();
        }

        public static ResourceKey<DecoratedPotPattern> get(DecoratedPotPattern decoratedPotPattern)
        {
            return BuiltInRegistries.DECORATED_POT_PATTERN.getResourceKey(decoratedPotPattern).get();
        }

        public static ResourceKey<CreativeModeTab> get(CreativeModeTab creativeModeTab)
        {
            return BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey(creativeModeTab).get();
        }

        public static ResourceKey<CriterionTrigger<?>> get(CriterionTrigger<?> trigger)
        {
            return BuiltInRegistries.TRIGGER_TYPES.getResourceKey(trigger).get();
        }

        public static ResourceKey<DataComponentType<?>> get(DataComponentType<?> componentType)
        {
            return BuiltInRegistries.DATA_COMPONENT_TYPE.getResourceKey(componentType).get();
        }

        public static ResourceKey<MapDecorationType> get(MapDecorationType mapDecorationType)
        {
            return BuiltInRegistries.MAP_DECORATION_TYPE.getResourceKey(mapDecorationType).get();
        }

        public static ResourceKey<RecipeBookCategory> get(RecipeBookCategory recipeBookCategory)
        {
            return BuiltInRegistries.RECIPE_BOOK_CATEGORY.getResourceKey(recipeBookCategory).get();
        }
    }

    public static class TagKeys
    {
        /**
         * The default constructor for the class
         */
        public TagKeys()
        {
        }

        public static <T> TagKey<T> getTag(String modID, String name, ResourceKey<? extends Registry<T>> registryKey)
        {
            return TagKey.create(registryKey, id(modID, name));
        }

        /**
         * Generates a tag check string for the given item tag.
         *
         * @param tag The item tag key.
         *
         * @return A tag check string.
         */
        public static @NotNull String getHasTag(@NotNull TagKey<Item> tag)
        {
            return "has_" + tag.location();
        }
    }

    public static class RegistryNames
    {
        /**
         * The default constructor for the class
         */
        public RegistryNames()
        {
            Exceptions.throwArgumentException();
        }

        /**
         * Retrieves the registry name for a given fluid.
         *
         * @param fluid The fluid to get the registry name for.
         *
         * @return The registry name of the fluid.
         */
        public static String get(Fluid fluid)
        {
            return BuiltInRegistries.FLUID.getKey(fluid).getPath();
        }

        /**
         * Retrieves the registry name for a given item.
         *
         * @param item The item to get the registry name for.
         *
         * @return The registry name of the item.
         */
        public static String get(Item item)
        {
            return BuiltInRegistries.ITEM.getKey(item).getPath();
        }

        /**
         * Retrieves the registry name for a given item stack.
         *
         * @param stack The item stack to get the registry name for.
         *
         * @return The registry name of the item in the stack.
         */
        public static String get(ItemStack stack)
        {
            return BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();
        }

        /**
         * Retrieves the registry name for a given block.
         *
         * @param block The block to get the registry name for.
         *
         * @return The registry name of the block.
         */
        public static String get(Block block)
        {
            return BuiltInRegistries.BLOCK.getKey(block).getPath();
        }

        /**
         * Retrieves the registry name for a given entity type.
         *
         * @param entityType The entity type to get the registry name for.
         *
         * @return The registry name of the entity type.
         */
        public static String get(EntityType<?> entityType)
        {
            return BuiltInRegistries.ENTITY_TYPE.getKey(entityType).getPath();
        }

        /**
         * Retrieves the registry name for a given potion.
         *
         * @param potion The potion to get the registry name for.
         *
         * @return The registry name of the potion.
         */
        public static String get(Potion potion)
        {
            return Objects.requireNonNull(BuiltInRegistries.POTION.getKey(potion)).getPath();
        }

        /**
         * Retrieves the registry name for a given block entity type.
         *
         * @param blockEntityType The block entity type to get the registry name for.
         *
         * @return The registry name of the block entity type.
         */
        public static String get(BlockEntityType<?> blockEntityType)
        {
            return Objects.requireNonNull(BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(blockEntityType)).getPath();
        }

        /**
         * Retrieves the registry name for a given custom stat identifier.
         *
         * @param identifier The custom stat identifier to get the registry name for.
         *
         * @return The registry name of the custom stat.
         */
        public static String get(Identifier identifier)
        {
            return Objects.requireNonNull(BuiltInRegistries.CUSTOM_STAT.getKey(identifier)).getPath();
        }

        /**
         * Retrieves the registry name for a given chunk status.
         *
         * @param chunkStatus The chunk status to get the registry name for.
         *
         * @return The registry name of the chunk status.
         */
        public static String get(ChunkStatus chunkStatus)
        {
            return BuiltInRegistries.CHUNK_STATUS.getKey(chunkStatus).getPath();
        }

        /**
         * Retrieves the registry name for a given entity attribute.
         *
         * @param entityAttribute The entity attribute to get the registry name for.
         *
         * @return The registry name of the entity attribute.
         */
        public static String get(Attribute entityAttribute)
        {
            return Objects.requireNonNull(BuiltInRegistries.ATTRIBUTE.getKey(entityAttribute)).getPath();
        }

        /**
         * Retrieves the registry name for a given screen handler type.
         *
         * @param screenHandlerType The screen handler type to get the registry name for.
         *
         * @return The registry name of the screen handler type.
         */
        public static String get(MenuType<?> screenHandlerType)
        {
            return Objects.requireNonNull(BuiltInRegistries.MENU.getKey(screenHandlerType)).getPath();
        }

        /**
         * Retrieves the registry name for a given recipe type.
         *
         * @param recipeType The recipe type to get the registry name for.
         *
         * @return The registry name of the recipe type.
         */
        public static String get(RecipeType<?> recipeType)
        {
            return Objects.requireNonNull(BuiltInRegistries.RECIPE_TYPE.getKey(recipeType)).getPath();
        }

        /**
         * Retrieves the registry name for a given recipe serializer.
         *
         * @param recipeSerializer The recipe serializer to get the registry name for.
         *
         * @return The registry name of the recipe serializer.
         */
        public static String get(RecipeSerializer<?> recipeSerializer)
        {
            return Objects.requireNonNull(BuiltInRegistries.RECIPE_SERIALIZER.getKey(recipeSerializer)).getPath();
        }

        /**
         * Retrieves the registry name for a given villager type.
         *
         * @param villagerType The villager type to get the registry name for.
         *
         * @return The registry name of the villager type.
         */
        public static String get(VillagerType villagerType)
        {
            return BuiltInRegistries.VILLAGER_TYPE.getKey(villagerType).getPath();
        }

        /**
         * Retrieves the registry name for a given villager profession.
         *
         * @param villagerProfession The villager profession to get the registry name for.
         *
         * @return The registry name of the villager profession.
         */
        public static String get(VillagerProfession villagerProfession)
        {
            return BuiltInRegistries.VILLAGER_PROFESSION.getKey(villagerProfession).getPath();
        }

        /**
         * Retrieves the registry name for a given float provider codec.
         *
         * @param floatProviderCodec The float provider codec to get the registry name for.
         *
         * @return The registry name of the float provider codec.
         */
        public static String getFlaotProvider(MapCodec<? extends FloatProvider> floatProviderCodec)
        {
            return Objects.requireNonNull(BuiltInRegistries.FLOAT_PROVIDER_TYPE.getKey(floatProviderCodec)).getPath();
        }

        /**
         * Retrieves the registry name for a given int provider codec.
         *
         * @param intProviderCodec The int provider codec to get the registry name for.
         *
         * @return The registry name of the int provider codec.
         */
        public static String getIntProvider(MapCodec<? extends IntProvider> intProviderCodec)
        {
            return Objects.requireNonNull(BuiltInRegistries.INT_PROVIDER_TYPE.getKey(intProviderCodec)).getPath();
        }

        /**
         * Retrieves the registry name for a given decorated pot pattern.
         *
         * @param decoratedPotPattern The decorated pot pattern to get the registry name for.
         *
         * @return The registry name of the decorated pot pattern.
         */
        public static String get(DecoratedPotPattern decoratedPotPattern)
        {
            return Objects.requireNonNull(BuiltInRegistries.DECORATED_POT_PATTERN.getKey(decoratedPotPattern)).getPath();
        }

        /**
         * Retrieves the registry name for a given item group.
         *
         * @param itemGroup The item group to get the registry name for.
         *
         * @return The registry name of the item group.
         */
        public static String get(CreativeModeTab itemGroup)
        {
            return Objects.requireNonNull(BuiltInRegistries.CREATIVE_MODE_TAB.getKey(itemGroup)).getPath();
        }

        /**
         * Retrieves the registry name for a given criterion.
         *
         * @param criterion The criterion to get the registry name for.
         *
         * @return The registry name of the criterion.
         */
        public static String get(CriterionTrigger<?> criterion)
        {
            return Objects.requireNonNull(BuiltInRegistries.TRIGGER_TYPES.getKey(criterion)).getPath();
        }

        /**
         * Retrieves the registry name for a given data component type or enchantment effect component type.
         *
         * @param componentType The component type to get the registry name for.
         * @param isEnchantment Whether this is an enchantment effect component type.
         *
         * @return The registry name of the component type.
         */
        public static String get(DataComponentType<?> componentType, boolean isEnchantment)
        {
            return isEnchantment
                ? Objects.requireNonNull(BuiltInRegistries.ENCHANTMENT_EFFECT_COMPONENT_TYPE.getKey(componentType)).getPath()
                : Objects.requireNonNull(BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(componentType)).getPath();
        }

        /**
         * Retrieves the registry name for a given map decoration type.
         *
         * @param mapDecorationType The map decoration type to get the registry name for.
         *
         * @return The registry name of the map decoration type.
         */
        public static String get(MapDecorationType mapDecorationType)
        {
            return Objects.requireNonNull(BuiltInRegistries.MAP_DECORATION_TYPE.getKey(mapDecorationType)).getPath();
        }

        /**
         * Retrieves the registry name for a given recipe book category.
         *
         * @param recipeBookCategory The recipe book category to get the registry name for.
         *
         * @return The registry name of the recipe book category.
         */
        public static String get(RecipeBookCategory recipeBookCategory)
        {
            return Objects.requireNonNull(BuiltInRegistries.RECIPE_BOOK_CATEGORY.getKey(recipeBookCategory)).getPath();
        }
    }

    public static class ListFromTags
    {
        /**
         * The default constructor for the class
         */
        public ListFromTags()
        {
            Exceptions.throwArgumentException();
        }

        /**
         * Retrieves a list of items that are in the given item tag.
         *
         * @param tagKey The item tag key to get the items for.
         *
         * @return A list of items in the tag.
         */
        public static List<Item> items(TagKey<Item> tagKey)
        {
            List<Item> list = new ArrayList<>();
            BuiltInRegistries.ITEM.getTagOrEmpty(tagKey).forEach(
                    holder -> list.add(holder.value()));
            return ImmutableList.copyOf(list);
        }

        /**
         * Retrieves a list of blocks that are in the given block tag.
         *
         * @param tagKey The block tag key to get the blocks for.
         *
         * @return A list of blocks in the tag.
         */
        public static List<Block> blocks(TagKey<Block> tagKey)
        {
            List<Block> list = new ArrayList<>();
            BuiltInRegistries.BLOCK.getTagOrEmpty(tagKey).forEach(
                    holder -> list.add(holder.value()));
            return ImmutableList.copyOf(list);
        }

        /**
         * Retrieves a list of block entity types that are in the given block entity type tag.
         *
         * @param tagKey The block entity type tag key to get the block entity types for.
         *
         * @return A list of block entity types in the tag.
         */
        public static List<BlockEntityType<?>> blockEntities(TagKey<BlockEntityType<?>> tagKey)
        {
            List<BlockEntityType<?>> list = new ArrayList<>();
            BuiltInRegistries.BLOCK_ENTITY_TYPE.getTagOrEmpty(tagKey).forEach(
                    holder -> list.add(holder.value()));
            return ImmutableList.copyOf(list);
        }

        /**
         * Retrieves a list of entity types that are in the given entity type tag.
         *
         * @param tagKey The entity type tag key to get the entity types for.
         *
         * @return A list of entity types in the tag.
         */
        public static List<EntityType<?>> entities(TagKey<EntityType<?>> tagKey)
        {
            List<EntityType<?>> list = new ArrayList<>();
            BuiltInRegistries.ENTITY_TYPE.getTagOrEmpty(tagKey).forEach(
                    holder -> list.add(holder.value()));
            return ImmutableList.copyOf(list);
        }
    }

    public static class HolderSetFromTags
    {
        /**
         * The default constructor for the class
         */
        public HolderSetFromTags()
        {
            Exceptions.throwArgumentException();
        }

        /**
         * Retrieves a list of items that are in the given item tag.
         *
         * @param tagKey The item tag key to get the items for.
         *
         * @return A list of items in the tag.
         */
        public static HolderSet<Item> items(HolderLookup.Provider registries, TagKey<Item> tagKey)
        {
            return registries.lookupOrThrow(Registries.ITEM).getOrThrow(tagKey);
        }

        /**
         * Retrieves a list of blocks that are in the given block tag.
         *
         * @param tagKey The block tag key to get the blocks for.
         *
         * @return A list of blocks in the tag.
         */
        public static HolderSet<Block> blocks(HolderLookup.Provider registries, TagKey<Block> tagKey)
        {
            return registries.lookupOrThrow(Registries.BLOCK).getOrThrow(tagKey);
        }

        /**
         * Retrieves a list of block entity types that are in the given block entity type tag.
         *
         * @param tagKey The block entity type tag key to get the block entity types for.
         *
         * @return A list of block entity types in the tag.
         */
        public static HolderSet<BlockEntityType<?>> blockEntities(HolderLookup.Provider registries, TagKey<BlockEntityType<?>> tagKey)
        {
            return registries.lookupOrThrow(Registries.BLOCK_ENTITY_TYPE).getOrThrow(tagKey);
        }

        /**
         * Retrieves a list of entity types that are in the given entity type tag.
         *
         * @param tagKey The entity type tag key to get the entity types for.
         *
         * @return A list of entity types in the tag.
         */
        public static HolderSet<EntityType<?>> entities(HolderLookup.Provider registries, TagKey<EntityType<?>> tagKey)
        {
            return registries.lookupOrThrow(Registries.ENTITY_TYPE).getOrThrow(tagKey);
        }
    }

    public static class Dimensions
    {
        /**
         * The default constructor for the class
         */
        public Dimensions()
        {
            Exceptions.throwArgumentException();
        }

        /**
         * Retrieves the dimension identifier for the given level.
         *
         * @param level The level to get the dimension identifier for.
         * @return The dimension identifier.
         */
        public static Identifier dimensionId(Level level)
        {
            return level.dimension().identifier();
        }

        /**
         * Retrieves the dimension name for the given level.
         *
         * @param level The level to get the dimension name for.
         * @return The dimension name.
         */
        public static String dimensionName(Level level)
        {
            return dimensionId(level).toString();
        }

        /**
         * Retrieves a cleaned, readable version of the dimension name.
         *
         * @param level The level to get the clean dimension name for.
         * @return A cleaned dimension name.
         */
        public static String dimensionNameClean(Level level)
        {
            return dimensionNameClean(dimensionName(level));
        }

        /**
         * Retrieves a cleaned, readable version of the dimension name from a string.
         *
         * @param dimensionName The dimension name as a string.
         * @return A cleaned dimension name.
         */
        public static String dimensionNameClean(String dimensionName)
        {
            return dimensionName.substring(dimensionName.indexOf(':') + 1).replace('_', ' ');
        }
    }

    public static class Foods
    {
        /**
         * The default constructor for the class
         */
        public Foods()
        {
            Exceptions.throwArgumentException();
        }

        public static FoodProperties getProperties(int nutrition, float saturation, boolean alwaysEdibale)
        {
            //look into foods class
            return alwaysEdibale
                ? new FoodProperties.Builder().nutrition(nutrition).saturationModifier(saturation).alwaysEdible().build()
                : new FoodProperties.Builder().nutrition(nutrition).saturationModifier(saturation).build();
        }

        public static FoodProperties getProperties(int nutrition, float saturation)
        {
            return getProperties(nutrition, saturation, false);
        }

        public static Consumable getConsumable(Consumable.Builder builder, float seconds)
        {
            return builder.consumeSeconds(seconds).build();
        }

        public static Consumable getConsumable(Consumable.Builder builder, float seconds, ConsumeEffect effect)
        {
            return builder
                    .consumeSeconds(seconds)
                    .onConsume(effect)
                    .build();
        }

        public static Consumable getConsumable(Consumable.Builder builder, float seconds, MobEffectInstance effect)
        {
            return builder
                    .consumeSeconds(seconds)
                    .onConsume(new ApplyStatusEffectsConsumeEffect(effect))
                    .build();
        }

        public static Consumable getConsumable(Consumable.Builder builder, float seconds, List<MobEffectInstance> effects)
        {
            return builder
                    .consumeSeconds(seconds)
                    .onConsume(new ApplyStatusEffectsConsumeEffect(effects))
                    .build();
        }

        public static Consumable getConsumable(Consumable.Builder builder, float seconds, MobEffectInstance... effects)
        {
            return builder
                    .consumeSeconds(seconds)
                    .onConsume(new ApplyStatusEffectsConsumeEffect(List.of(effects)))
                    .build();
        }

        public static Consumable getConsumableWithEffect(Consumable.Builder builder, float seconds, Holder<MobEffect> effect)
        {
            return builder
                    .consumeSeconds(seconds)
                    .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(effect)))
                    .build();
        }

        public static Consumable getConsumableWithEffect(Consumable.Builder builder, float seconds, Holder<MobEffect> effect, int duration)
        {
            return builder
                    .consumeSeconds(seconds)
                    .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(effect, duration)))
                    .build();
        }

        public static Consumable getConsumableWithEffect(Consumable.Builder builder, float seconds, Holder<MobEffect> effect, int duration, int amplifier)
        {
            return builder
                    .consumeSeconds(seconds)
                    .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(effect, duration, amplifier)))
                    .build();
        }

        public static Consumable getConsumableWithEffect(Consumable.Builder builder, float seconds, Holder<MobEffect> effect, int duration, int amplifier, boolean ambient, boolean visible)
        {
            return builder
                    .consumeSeconds(seconds)
                    .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(effect, duration, amplifier, ambient, visible)))
                    .build();
        }

        public static Consumable getConsumableWithEffect(Consumable.Builder builder, float seconds, Holder<MobEffect> effect, int duration, int amplifier, boolean ambient, boolean visible, boolean showIcon)
        {
            return builder
                    .consumeSeconds(seconds)
                    .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(effect, duration, amplifier, ambient, visible, showIcon)))
                    .build();
        }

        public static Consumable getConsumableWithEffect(Consumable.Builder builder, float seconds, Holder<MobEffect> effect, int duration, int amplifier, boolean ambient, boolean visible, boolean showIcon, MobEffectInstance hiddenEffect)
        {
            return builder
                    .consumeSeconds(seconds)
                    .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(effect, duration, amplifier, ambient, visible, showIcon, hiddenEffect)))
                    .build();
        }

        public static Consumable getConsumable(Consumable.Builder builder, float seconds, Holder<SoundEvent> sound)
        {
            return builder
                    .consumeSeconds(seconds)
                    .sound(sound)
                    .build();
        }

        public static Consumable getConsumable(Consumable.Builder builder, float seconds, Holder<SoundEvent> sound, ConsumeEffect effect)
        {
            return builder
                    .consumeSeconds(seconds)
                    .sound(sound)
                    .onConsume(effect)
                    .build();
        }

        public static Consumable getConsumable(Consumable.Builder builder, float seconds, Holder<SoundEvent> sound, MobEffectInstance effect)
        {
            return builder
                    .consumeSeconds(seconds)
                    .sound(sound)
                    .onConsume(new ApplyStatusEffectsConsumeEffect(effect))
                    .build();
        }

        public static Consumable getConsumable(Consumable.Builder builder, float seconds, Holder<SoundEvent> sound, List<MobEffectInstance> effects)
        {
            return builder
                    .consumeSeconds(seconds)
                    .sound(sound)
                    .onConsume(new ApplyStatusEffectsConsumeEffect(effects))
                    .build();
        }

        public static Consumable getConsumable(Consumable.Builder builder, float seconds, Holder<SoundEvent> sound, MobEffectInstance... effects)
        {
            return builder
                    .consumeSeconds(seconds)
                    .sound(sound)
                    .onConsume(new ApplyStatusEffectsConsumeEffect(List.of(effects)))
                    .build();
        }

        public static Consumable getConsumableWithEffect(Consumable.Builder builder, float seconds, Holder<SoundEvent> sound, Holder<MobEffect> effect)
        {
            return builder
                    .consumeSeconds(seconds)
                    .sound(sound)
                    .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(effect)))
                    .build();
        }

        public static Consumable getConsumableWithEffect(Consumable.Builder builder, float seconds, Holder<SoundEvent> sound, Holder<MobEffect> effect, int duration)
        {
            return builder
                    .consumeSeconds(seconds)
                    .sound(sound)
                    .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(effect, duration)))
                    .build();
        }

        public static Consumable getConsumableWithEffect(Consumable.Builder builder, float seconds, Holder<SoundEvent> sound, Holder<MobEffect> effect, int duration, int amplifier)
        {
            return builder
                    .consumeSeconds(seconds)
                    .sound(sound)
                    .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(effect, duration, amplifier)))
                    .build();
        }

        public static Consumable getConsumableWithEffect(Consumable.Builder builder, float seconds, Holder<SoundEvent> sound, Holder<MobEffect> effect, int duration, int amplifier, boolean ambient, boolean visible)
        {
            return builder
                    .consumeSeconds(seconds)
                    .sound(sound)
                    .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(effect, duration, amplifier, ambient, visible)))
                    .build();
        }

        public static Consumable getConsumableWithEffect(Consumable.Builder builder, float seconds, Holder<SoundEvent> sound, Holder<MobEffect> effect, int duration, int amplifier, boolean ambient, boolean visible, boolean showIcon)
        {
            return builder
                    .consumeSeconds(seconds)
                    .sound(sound)
                    .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(effect, duration, amplifier, ambient, visible, showIcon)))
                    .build();
        }

        public static Consumable getConsumableWithEffect(Consumable.Builder builder, float seconds, Holder<SoundEvent> sound, Holder<MobEffect> effect, int duration, int amplifier, boolean ambient, boolean visible, boolean showIcon, MobEffectInstance hiddenEffect)
        {
            return builder
                    .consumeSeconds(seconds)
                    .sound(sound)
                    .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(effect, duration, amplifier, ambient, visible, showIcon, hiddenEffect)))
                    .build();
        }
    }

    /**
     * Creates a new Minecraft identifier with the given mod ID and path.
     *
     * @param modID The mod ID.
     * @param path  The path for the identifier.
     * @return The created Identifier.
     */
    public static Identifier id(String modID, @NotNull String path)
    {
        return Identifier.fromNamespaceAndPath(modID, path);
    }

    public static BlockItemId blockItemId(String modID, @NotNull String name)
    {
        Identifier id = id(modID, name);
        return BlockItemId.create(id, id);
    }

    public static BlockItemTagId blockItemTagId(String modID, @NotNull String name)
    {
        return new BlockItemTagId(TagKey.create(Registries.BLOCK, id(modID, name)),
                                TagKey.create(Registries.ITEM, id(modID, name)));
    }

    public static boolean validateIdentifier(Identifier id)
    {
        return Identifier.isValidNamespace(id.getNamespace()) && Identifier.isValidPath(id.getPath());
    }
}