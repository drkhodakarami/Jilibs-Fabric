package com.dynamero.datagen;

import net.minecraft.core.RegistrySetBuilder;

import java.util.Objects;

/**
 * Common data-generation entry points used from loader data-generator hooks.
 * <p></p>
 * Example Usage:
 * <pre><code>
 * public class ModDataGeneration
 * {
 *     public static final DataGenBlueprint BLUEPRINT = DataGen.blueprintBuilder("the_mod_id")
 *             .treeFamily(ModTreeFamilies.SOME_FAMILY)
 *             .recipes(ModRecipeProvider::generate)
 *             .lootTables(Set.of(), List.of(
 *                     new LootTableProvider.SubProviderEntry(
 *                             ModBlockLootTables::new,
 *                             LootContextParamSets.BLOCK)))
 *             .blockTags((registries, tags) ->
 *                                ModBlockTagProvider.generate(tags))
 *             .itemTags((registries, tags) ->
 *                               ModItemTagProvider.generate(tags))
 *             .fluidTags((registries, tags) ->
 *                                ModFluidTagProvider.generate(tags))
 *             .entityTypeTags((registries, tags) ->
 *                                     ModEntityTagProvider.generate(tags))
 *             .language("en_us", ModEnLangProvider::generate)
 *             .vanillaModels(ModModelProvider::generate)
 *             .damageTypes(ModDamageTypeProvider::bootstrap)
 *             .worldGeneration(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap)
 *             .worldGeneration(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap)
 *             .build();
 *
 *      public static void init()
 *      {}
 * }</code></pre>
 * Then:
 * <pre><code>
 * public class IndustriaDataGenerator implements DataGeneratorEntrypoint
 * {
 *      (Override)
 *     public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator)
 *     {
 *         FabricDataGeneration.run(fabricDataGenerator, ModDataGeneration.BLUEPRINT);
 *     }
 *
 *      (Override)
 *     public void buildRegistry(RegistrySetBuilder registryBuilder)
 *     {
 *         DataGeneration.addRegistryBootstraps(ModDataGeneration.BLUEPRINT, registryBuilder);
 *     }
 * }
 * </code></pre>
 * And, that's all to it!
 */
@SuppressWarnings("unused")
public final class DataGen
{
    private DataGen() {
    }

    public static DataGenBlueprint.Builder blueprintBuilder(String modId) {
        return DataGenBlueprint.builder(modId);
    }

    /**
     * Called from Fabric's {@code DataGeneratorEntrypoint.buildRegistry}. NeoForge runners call this automatically.
     */
    public static void addRegistryBootstraps(DataGenBlueprint spec, RegistrySetBuilder builder) {
        Objects.requireNonNull(spec, "spec").addRegistryBootstraps(Objects.requireNonNull(builder, "builder"));
    }
}