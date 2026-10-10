package com.dynamero.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * Fabric adapter for a common {@link DataGen}.
 */
@SuppressWarnings("unused")
public final class DataGeneration
{
    private DataGeneration() {
    }

    /**
     * Registers all common providers. Call this from {@code DataGeneratorEntrypoint.onInitializeDataGenerator}.
     */
    public static void run(FabricDataGenerator generator, DataGenBlueprint blueprint) {
        Objects.requireNonNull(generator, "generator");
        Objects.requireNonNull(blueprint, "spec");
        if (!generator.getModId().equals(blueprint.modId())) {
            throw new IllegalArgumentException(
                "Data-generation spec for " + blueprint.modId() + " cannot run for Fabric mod " + generator.getModId()
            );
        }

        FabricDataGenerator.Pack pack = generator.createPack();
        for (DataGenBlueprint.ProviderDeclaration declaration : blueprint.providers()) {
            pack.addProvider((FabricDataGenerator.Pack.Factory<?>) output -> declaration.factory().create(
                new DataProviderContext(blueprint.modId(), output, generator.getRegistries())
            ));
        }

        if (!blueprint.recipeGenerators().isEmpty()) {
            pack.addProvider((output, registries) ->
                new RecipeProviderAdapter(output, registries, blueprint.recipeGenerators())
            );
        }

        if (blueprint.generatesDynamicRegistries()) {
            pack.addProvider((output, registries) -> new FabricDynamicRegistryProvider(output, registries) {
                @Override
                protected void configure(HolderLookup.@NonNull Provider lookup, @NonNull Entries entries) {
                    if (blueprint.generatesAllDynamicRegistries()) {
                        lookup.listRegistryKeys().forEach(registry ->
                            addAll(entries, lookup, registry.registryKey())
                        );
                    } else {
                        blueprint.dynamicRegistries().forEach(registry -> addAll(entries, lookup, registry));
                    }
                }

                @Override
                public @NonNull String getName() {
                    return "Dynamic registries for " + blueprint.modId();
                }
            });
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void addAll(
        FabricDynamicRegistryProvider.Entries entries,
        HolderLookup.Provider lookup,
        ResourceKey<? extends Registry<?>> registry
    ) {
        lookup.lookup(registry).ifPresent(registryLookup ->
            addAllUnchecked(entries, (HolderLookup.RegistryLookup) registryLookup)
        );
    }

    private static <T> void addAllUnchecked(
        FabricDynamicRegistryProvider.Entries entries,
        HolderLookup.RegistryLookup<T> registry
    ) {
        entries.addAll(registry);
    }
}