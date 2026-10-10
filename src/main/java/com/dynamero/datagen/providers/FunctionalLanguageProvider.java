package com.dynamero.datagen.providers;

import com.dynamero.datagen.base.LanguageGenerationContext;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;

/**
 * A small loader-neutral language provider.
 */
public final class FunctionalLanguageProvider implements DataProvider {
    private final PackOutput.PathProvider pathProvider;
    private final String modId;
    private final String locale;
    private final LanguageGenerator generator;

    public FunctionalLanguageProvider(PackOutput output, String modId, String locale, LanguageGenerator generator) {
        this.pathProvider = Objects.requireNonNull(output, "output")
            .createPathProvider(PackOutput.Target.RESOURCE_PACK, "lang");
        this.modId = Objects.requireNonNull(modId, "modId");
        this.locale = Objects.requireNonNull(locale, "locale");
        this.generator = Objects.requireNonNull(generator, "generator");
    }

    @Override
    public @NonNull CompletableFuture<?> run(@NonNull CachedOutput output) {
        Map<String, String> translations = new TreeMap<>();
        generator.generate(new LanguageGenerationContext(translations));

        JsonObject json = new JsonObject();
        translations.forEach(json::addProperty);
        Identifier id = Identifier.fromNamespaceAndPath(modId, locale);
        return DataProvider.saveStable(output, json, pathProvider.json(id));
    }

    @Override
    public @NonNull String getName() {
        return "Language " + locale + " for " + modId;
    }

    @FunctionalInterface
    public interface LanguageGenerator {
        void generate(LanguageGenerationContext translations);
    }
}