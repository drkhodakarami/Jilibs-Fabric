package com.dynamero.events;

import com.dynamero.shared.annotations.*;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

import java.util.ServiceLoader;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Loader implementation for the deliberately small common event facade.
 * Consumers should normally register through {@link Events}.
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
public interface IEventService
{
    static IEventService get() {
        return ServiceHolder.INSTANCE;
    }

    void onServerStarting(Consumer<MinecraftServer> callback);

    void onServerStarted(Consumer<MinecraftServer> callback);

    void onServerStopping(Consumer<MinecraftServer> callback);

    void onServerStopped(Consumer<MinecraftServer> callback);

    void onLevelLoad(Consumer<ServerLevel> callback);

    void onLevelUnload(Consumer<ServerLevel> callback);

    void onStartServerTick(Consumer<MinecraftServer> callback);

    void onEndServerTick(Consumer<MinecraftServer> callback);

    void onStartLevelTick(Consumer<ServerLevel> callback);

    void onEndLevelTick(Consumer<ServerLevel> callback);

    void onPlayerJoin(Consumer<ServerPlayer> callback);

    void onPlayerDisconnect(Consumer<ServerPlayer> callback);

    void onPlayerRespawn(Consumer<ServerPlayer> callback);

    void onPlayerDimensionChange(PlayerDimensionChangeCallback callback);

    void onBlockBroken(BlockBreakCallback callback);

    void onLivingDamaged(LivingDamageCallback callback);

    void onLivingKilled(BiConsumer<LivingEntity, DamageSource> callback);

    void onCommandRegistration(Consumer<CommandDispatcher<CommandSourceStack>> callback);

    /**
     * Registers a callback that runs after a successful server datapack reload.
     */
    void onDatapackReload(Consumer<MinecraftServer> callback);

    final class ServiceHolder {
        private static final IEventService INSTANCE
                = ServiceLoader.load(IEventService.class, IEventService.class.getClassLoader())
                               .findFirst()
                               .orElseThrow(() -> new IllegalStateException("No event service is available"));

        private ServiceHolder() {
        }
    }
}