package com.dynamero.events.client;

import com.dynamero.shared.annotations.*;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Loader-neutral callbacks and registration hooks used by Industria's client.
 */
@Developer("TurtyWurty")
@CreatedAt("2026-10-10")
@ModifiedAt("2026-10-10")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public final class ClientEvents {
    private ClientEvents() {
    }

    public static void onStartClientTick(Consumer<Minecraft> callback) {
        ClientEventService.get().onStartClientTick(require(callback));
    }

    public static void onEndClientTick(Consumer<Minecraft> callback) {
        ClientEventService.get().onEndClientTick(require(callback));
    }

    public static void onStartLevelTick(Consumer<ClientLevel> callback) {
        ClientEventService.get().onStartLevelTick(require(callback));
    }

    public static void onEndLevelTick(Consumer<ClientLevel> callback) {
        ClientEventService.get().onEndLevelTick(require(callback));
    }

    public static void onLevelEnter(Consumer<ClientLevel> callback) {
        ClientEventService.get().onLevelEnter(require(callback));
    }

    public static void onLevelLeave(Consumer<ClientLevel> callback) {
        ClientEventService.get().onLevelLeave(require(callback));
    }

    public static void onBlockEntityUnload(BiConsumer<BlockEntity, ClientLevel> callback) {
        ClientEventService.get().onBlockEntityUnload(require(callback));
    }

    public static void onConnection(Consumer<Minecraft> callback) {
        ClientEventService.get().onConnection(require(callback));
    }

    public static void onDisconnection(Consumer<Minecraft> callback) {
        ClientEventService.get().onDisconnection(require(callback));
    }

    public static void onTooltip(TooltipCallback callback) {
        ClientEventService.get().onTooltip(require(callback));
    }

    public static KeyMapping registerKeyMapping(KeyMapping keyMapping) {
        return ClientEventService.get().registerKeyMapping(require(keyMapping));
    }

    public static void registerResourceReloadListener(Identifier id, PreparableReloadListener listener) {
        ClientEventService.get().registerResourceReloadListener(require(id), require(listener));
    }

    public static void onRenderStage(RenderStage stage, Consumer<LevelRenderContext> callback) {
        ClientEventService.get().onRenderStage(require(stage), require(callback));
    }

    private static <T> T require(T value) {
        return Objects.requireNonNull(value, "value");
    }
}