package com.dynamero.datagen;

import com.dynamero.datagen.base.LanguageGenerationContext;
import com.dynamero.shared.utils.BaseHelper;
import com.dynamero.treefamily.TreeFamily;
import com.dynamero.treefamily.TreeFamilyBuilder;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * Reusable data-generator contribution for {@link TreeFamilyBuilder}.
 */
@SuppressWarnings("unchecked")
public final class WoodSetDataGeneration {
    private WoodSetDataGeneration() {
    }

    public static void contribute(DataGenBlueprint.Builder builder, TreeFamily set) {
        builder
            .recipes((registries, output) -> new Recipes(registries, output, set).generate())
            .lootTables(Set.of(), List.of(new LootTableProvider.SubProviderEntry(
                registries -> new DirectLoot(set, new LootBuilders(registries)), LootContextParamSets.BLOCK
            )))
            .blockTags((_, tags) -> {
                tags.tag(set.tags().block()).add(
                        BaseHelper.ResourceKeys.get(set.blocks().log().block()),
                        BaseHelper.ResourceKeys.get(set.blocks().strippedLog().block()),
                        BaseHelper.ResourceKeys.get(set.blocks().wood().block()),
                        BaseHelper.ResourceKeys.get(set.blocks().strippedWood().block()));
                tags.tag(BlockTags.PLANKS).add(set.blocks().planks().id().block());
                tags.tag(BlockTags.LEAVES).add(set.blocks().leaves().id().block());
                tags.tag(BlockTags.WOODEN_BUTTONS).add(set.blocks().button().id().block());
                tags.tag(BlockTags.WOODEN_DOORS).add(set.blocks().door().id().block());
                tags.tag(BlockTags.WOODEN_FENCES).add(set.blocks().fence().id().block());
                tags.tag(BlockTags.FENCE_GATES).add(set.blocks().fenceGate().id().block());
                tags.tag(BlockTags.WOODEN_PRESSURE_PLATES).add(set.blocks().pressurePlate().id().block());
                tags.tag(BlockTags.WOODEN_TRAPDOORS).add(set.blocks().trapdoor().id().block());
                tags.tag(BlockTags.WOODEN_STAIRS).add(set.blocks().stairs().id().block());
                tags.tag(BlockTags.WOODEN_SLABS).add(set.blocks().slab().id().block());
                tags.tag(BlockTags.STANDING_SIGNS).add(set.signs().sign().id().block());
                tags.tag(BlockTags.WALL_SIGNS).add(set.signs().wallSign().id().block());
                tags.tag(BlockTags.CEILING_HANGING_SIGNS).add(set.signs().hangingSign().id().block());
                tags.tag(BlockTags.WALL_HANGING_SIGNS).add(set.signs().wallHangingSign().id().block());
            })
            .itemTags((_, tags) -> {
                tags.tag(set.tags().item()).add(
                    set.blocks().log().id().item(), set.blocks().strippedLog().id().item(), set.blocks().wood().id().item(),
                    set.blocks().strippedWood().id().item());
                tags.tag(ItemTags.LOGS_THAT_BURN).addTag(set.tags().item());
                tags.tag(ItemTags.PLANKS).add(set.blocks().planks().id().item());
                tags.tag(ItemTags.LEAVES).add(set.blocks().leaves().id().item());
                tags.tag(ItemTags.SAPLINGS).add(set.blocks().sapling().id().item());
                tags.tag(ItemTags.WOODEN_BUTTONS).add(set.blocks().button().id().item());
                tags.tag(ItemTags.WOODEN_DOORS).add(set.blocks().door().id().item());
                tags.tag(ItemTags.WOODEN_FENCES).add(set.blocks().fence().id().item());
                tags.tag(ItemTags.FENCE_GATES).add(set.blocks().fenceGate().id().item());
                tags.tag(ItemTags.WOODEN_PRESSURE_PLATES).add(set.blocks().pressurePlate().id().item());
                tags.tag(ItemTags.WOODEN_TRAPDOORS).add(set.blocks().trapdoor().id().item());
                tags.tag(ItemTags.WOODEN_STAIRS).add(set.blocks().stairs().id().item());
                tags.tag(ItemTags.WOODEN_SLABS).add(set.blocks().slab().id().item());
                tags.tag(ItemTags.SIGNS).add(set.signs().items().sign().id());
                tags.tag(ItemTags.HANGING_SIGNS).add(set.signs().hangingSign().id().item());
                tags.tag(ItemTags.BOATS).add(set.boats().boatItem().id());
                tags.tag(ItemTags.CHEST_BOATS).add(set.boats().chestBoatItem().id());
            })
            .entityTypeTags((_, tags) ->
                                    tags.tag(EntityTypeTags.BOAT).add(set.boats().boat().id(), set.boats().chestBoat().id()))
            .language("en_us", language -> addLanguage(language, set))
            .vanillaModels((blocks, items) -> addModels(blocks, items, set));
    }

    private static void addLanguage(
        LanguageGenerationContext language,
        TreeFamily set
    ) {
        String name = displayName(set.name());
        language.add(set.blocks().planks().block(), name + " Planks");
        language.add(set.blocks().log().block(), name + " Log");
        language.add(set.blocks().strippedLog().block(), "Stripped " + name + " Log");
        language.add(set.blocks().strippedWood().block(), "Stripped " + name + " Wood");
        language.add(set.blocks().wood().block(), name + " Wood");
        language.add(set.blocks().leaves().block(), name + " Leaves");
        language.add(set.blocks().sapling().block(), name + " Sapling");
        language.add(set.blocks().stairs().block(), name + " Stairs");
        language.add(set.blocks().slab().block(), name + " Slab");
        language.add(set.blocks().fence().block(), name + " Fence");
        language.add(set.blocks().fenceGate().block(), name + " Fence Gate");
        language.add(set.blocks().door().block(), name + " Door");
        language.add(set.blocks().trapdoor().block(), name + " Trapdoor");
        language.add(set.blocks().pressurePlate().block(), name + " Pressure Plate");
        language.add(set.blocks().button().block(), name + " Button");
        language.add(set.signs().items().sign().item(), name + " Sign");
        language.add(set.signs().items().hangingSign().item(), name + " Hanging Sign");
        language.add(set.boats().boatItem().item(), name + " Boat");
        language.add(set.boats().chestBoatItem().item(), name + " Chest Boat");
        language.add(set.boats().boat().entity(), name + " Boat");
        language.add(set.boats().chestBoat().entity(), name + " Chest Boat");
    }

    private static void addModels(BlockModelGenerators models, ItemModelGenerators items, TreeFamily set) {
        models.woodProvider(set.blocks().log().block()).logWithHorizontal(set.blocks().log().block()).wood(set.blocks().wood().block());
        models.woodProvider(set.blocks().strippedLog().block())
            .logWithHorizontal(set.blocks().strippedLog().block()).wood(set.blocks().strippedWood().block());
        models.createTintedLeaves(set.blocks().leaves().block(), TexturedModel.LEAVES, 0x00BB0A);
        models.createCrossBlockWithDefaultItem(set.blocks().sapling().block(), BlockModelGenerators.PlantType.NOT_TINTED);
        models.family(set.blocks().planks().block()).generateFor(set.blockFamily());
        items.generateFlatItem(set.boats().boatItem().item(), ModelTemplates.FLAT_ITEM);
        items.generateFlatItem(set.boats().chestBoatItem().item(), ModelTemplates.FLAT_ITEM);
    }

    private static String displayName(String path) {
        StringBuilder result = new StringBuilder();
        for (String part : path.split("_")) {
            if (part.isEmpty()) continue;
            if (!result.isEmpty()) result.append(' ');
            result.append(part.substring(0, 1).toUpperCase(Locale.ROOT)).append(part.substring(1));
        }
        return result.toString();
    }

    private static final class Recipes extends RecipeProvider {
        private final TreeFamily set;

        private Recipes(HolderLookup.Provider registries, RecipeOutput output, TreeFamily set) {
            super(registries, output);
            this.set = set;
        }

        private void generate() {
            planksFromLogs(set.blocks().planks().block(), set.tags().item(), 4);
            woodFromLogs(set.blocks().wood().block(), set.blocks().log().block());
            woodFromLogs(set.blocks().strippedWood().block(), set.blocks().strippedLog().block());
            woodenBoat(set.boats().boatItem().item(), set.blocks().planks().block());
            chestBoat(set.boats().chestBoatItem().item(), set.boats().boatItem().item());
            hangingSignBuilder(set.signs().items().hangingSign().item(),
                               Ingredient.of(set.blocks().strippedLog().block()))
                    .save(output);
            generateRecipes(set.blockFamily(), FeatureFlags.DEFAULT_FLAGS);
        }

        @Override
        public void buildRecipes() {
        }
    }

    private record DirectLoot(TreeFamily set, LootBuilders builders) implements LootTableSubProvider
    {
        @Override
        public void generate(@NonNull BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
            for (var block : List.of(
                set.blocks().planks(), set.blocks().log(), set.blocks().strippedLog(), set.blocks().strippedWood(), set.blocks().wood(), set.blocks().sapling(),
                set.blocks().stairs(), set.blocks().fence(), set.blocks().fenceGate(), set.blocks().trapdoor(), set.blocks().pressurePlate(), set.blocks().button()
            ))
                accept(output, block.block(), builders.self(block.block()));

            accept(output, set.blocks().slab().block(), builders.slab(set.blocks().slab().block()));
            accept(output, set.blocks().door().block(), builders.door(set.blocks().door().block()));
            SignItem signItem = set.signs().items().sign().item();
            accept(output, set.signs().sign().block(), builders.other(signItem));
            accept(output, set.signs().wallSign().block(), builders.other(signItem));
            HangingSignItem hangingSignItem = set.signs().items().hangingSign().item();
            accept(output, set.signs().hangingSign().block(), builders.other(hangingSignItem));
            accept(output, set.signs().wallHangingSign().block(), builders.other(hangingSignItem));
            accept(output, set.blocks().leaves().block(), builders.leaves(set.blocks().leaves().block(), set.blocks().sapling().block()));
        }

        private static void accept(
            BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output,
            Block block,
            LootTable.Builder table
        ) {
            output.accept(block.getLootTable().orElseThrow(), table);
        }
    }

    /**
     * Uses vanilla's table builders without invoking its global block-validation pass.
     */
    private static final class LootBuilders extends BlockLootSubProvider {
        private LootBuilders(HolderLookup.Provider registries) {
            super(Set.of(), FeatureFlags.DEFAULT_FLAGS, registries);
        }

        private LootTable.Builder self(Block block) {
            return createSingleItemTable(block);
        }

        private LootTable.Builder slab(Block block) {
            return createSlabItemTable(block);
        }

        private LootTable.Builder door(Block block) {
            return createDoorTable(block);
        }

        private LootTable.Builder other(Item item) {
            return createSingleItemTable(item);
        }

        private LootTable.Builder leaves(Block leaves, Block sapling) {
            return createLeavesDrops(leaves, sapling, NORMAL_LEAVES_SAPLING_CHANCES);
        }

        @Override
        public void generate() {
        }
    }
}