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

package com.dynamero.shared.client.datagen;

import java.util.List;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import com.dynamero.shared.annotations.*;
import com.dynamero.shared.utils.BaseHelper;

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
public class RecipeDatagenHelper
{
    public static void generateFamilyExcludeVariant(RecipeProvider provider, RecipeOutput output, BlockFamily family, FeatureFlagSet enabledFeatures, List<BlockFamily.Variant> excluded)
    {
        family.getVariants()
            .forEach((variant, block) ->
                    {
                        if (excluded.contains(variant))
                            return;

                        if (block.requiredFeatures().isSubsetOf(enabledFeatures))
                        {
                            ItemLike itemConvertible = provider.getBaseBlockForCrafting(family, variant);
                            if (RecipeProvider.SHAPE_BUILDERS.get(variant) != null)
                            {
                                RecipeBuilder craftingRecipeJsonBuilder =
                                        RecipeProvider.SHAPE_BUILDERS.get(variant)
                                                                    .create(provider, block, itemConvertible);
                                family.getRecipeGroupPrefix().ifPresent(
                                        (group) ->
                                                craftingRecipeJsonBuilder
                                                        .group(group + (variant == BlockFamily.Variant.CUT ? "" : "_" + variant.getRecipeGroup())));
                                craftingRecipeJsonBuilder
                                        .unlockedBy(family.getRecipeUnlockedBy()
                                                        .orElseGet(
                                                                () ->
                                                                        RecipeProvider.getHasName(itemConvertible)),
                                                                                                    provider.has(itemConvertible));
                                craftingRecipeJsonBuilder.save(output);
                            }

                            if (variant == BlockFamily.Variant.CRACKED)
                                provider.smeltingResultFromBase(block, itemConvertible);
                        }
                    });
    }

    public static void blastingFromTag(String modid, RecipeProvider provider, RecipeOutput output, HolderLookup.Provider registries, TagKey<Item> tag, String tagName, ItemLike result, float experience, int cookingTime)
    {
        var input = BaseHelper.HolderSetFromTags.items(registries, ItemTags.PLANKS);

        SimpleCookingRecipeBuilder.blasting(
                                        Ingredient.of(input),
                                        RecipeCategory.MISC,
                                        CookingBookCategory.MISC,
                                        result,
                                        experience,
                                        cookingTime
                                )
                                .unlockedBy("has_" + tagName, provider.has(tag))
                                .save(output, BaseHelper.id(modid, "blasting_" + BaseHelper.RegistryNames.get(result.asItem()) + "_from_" + tagName).toString());
    }
}