package com.dynamero.testworld.client;

import com.dynamero.testworld.TestWorld;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.WorldDataConfiguration;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.WorldDimensions;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.flat.FlatLayerInfo;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class TestWorldLauncher
{
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Component VANILLA_BUTTON_LABEL = Component.literal("Create Test World");
    private static final Component MOD_BUTTON_LABEL = Component.literal("Mod Test");
    private static final LevelSettings LEVEL_SETTINGS = new LevelSettings(
            TestWorld.WORLD_ID,
            GameType.CREATIVE,
            new LevelSettings.DifficultySettings(Difficulty.PEACEFUL, false, false),
            true,
            WorldDataConfiguration.DEFAULT);
    private static final WorldOptions WORLD_OPTIONS = new WorldOptions(0L, false, false);

    private static boolean useFlatAllDimensions = false;

    private static List<FlatLayerInfo> layers = new ArrayList<>();

    private TestWorldLauncher()
    {}

    public static void enableFlatAllDimensions() {
        useFlatAllDimensions = true;
    }

    public static void setUseFlatAllDimensions(boolean flatAll) {
        useFlatAllDimensions = flatAll;
    }

    public static void setDefaultLayers()
    {
        layers =  List.of(
                new FlatLayerInfo(1, Blocks.BEDROCK),
                new FlatLayerInfo(4, Blocks.STONE),
                new FlatLayerInfo(1, Blocks.WOOL.black()),
                new FlatLayerInfo(1, Blocks.WOOL.brown()),
                new FlatLayerInfo(1, Blocks.WOOL.white()),
                new FlatLayerInfo(1, Blocks.WOOL.lightBlue()),
                new FlatLayerInfo(1, Blocks.WOOL.blue()),
                new FlatLayerInfo(1, Blocks.WOOL.green()),
                new FlatLayerInfo(1, Blocks.WOOL.gray()),
                new FlatLayerInfo(1, Blocks.WOOL.lightGray()),
                new FlatLayerInfo(1, Blocks.WOOL.magenta()),
                new FlatLayerInfo(1, Blocks.WOOL.purple()),
                new FlatLayerInfo(1, Blocks.WOOL.red()),
                new FlatLayerInfo(1, Blocks.WOOL.orange()),
                new FlatLayerInfo(1, Blocks.WOOL.yellow()),
                new FlatLayerInfo(1, Blocks.WOOL.lime()),
                new FlatLayerInfo(1, Blocks.WOOL.cyan()),
                new FlatLayerInfo(1, Blocks.GRASS_BLOCK));
    }

    public static void setLayers(List<FlatLayerInfo> list)
    {
        layers = list;
    }

    public static void setLayers(FlatLayerInfo... list)
    {
        layers = Arrays.stream(list).toList();
    }

    public static boolean isVanillaTestWorldButton(Button button) {
        return button.getMessage().equals(VANILLA_BUTTON_LABEL);
    }

    public static Button replacementFor(Button original, Screen titleScreen) {
        return createButton(titleScreen, original.getX(), original.getY(), original.getWidth(), original.getHeight());
    }

    public static Button createButton(Screen titleScreen, int x, int y, int width, int height) {
        return Button.builder(MOD_BUTTON_LABEL, _ -> open(titleScreen))
                .bounds(x, y, width, height)
                .build();
    }

    public static void open(Screen titleScreen) {
        Minecraft minecraft = Minecraft.getInstance();
        try (LevelStorageSource.LevelStorageAccess access = minecraft.getLevelSource().createAccess(TestWorld.WORLD_ID)) {
            access.deleteLevel();
        } catch (IOException exception) {
            SystemToast.onWorldAccessFailure(minecraft, TestWorld.WORLD_ID);
            LOGGER.warn("Failed to access test world", exception);
            return;
        }

        minecraft.createWorldOpenFlows().createFreshLevel(
                TestWorld.WORLD_ID,
                LEVEL_SETTINGS,
                WORLD_OPTIONS,
                TestWorldLauncher::createDimensions,
                titleScreen);
    }

    private static WorldDimensions createDimensions(HolderLookup.Provider registries) {
        var settings = new FlatLevelGeneratorSettings(
                Optional.empty(),
                FlatLevelGeneratorSettings.getDefaultBiome(registries.lookupOrThrow(Registries.BIOME)),
                FlatLevelGeneratorSettings.createLakesList(registries.lookupOrThrow(Registries.PLACED_FEATURE)));
        settings.getLayersInfo().addAll(layers);
        settings.updateLayers();

        var targetPreset = useFlatAllDimensions
                           ? WorldPresets.FLAT_ALL_DIMENSIONS
                           : WorldPresets.FLAT;

        return registries.lookupOrThrow(Registries.WORLD_PRESET)
                         .getOrThrow(targetPreset)
                         .value()
                         .createWorldDimensions()
                         .replaceOverworldGenerator(registries, new FlatLevelSource(settings));
    }
}