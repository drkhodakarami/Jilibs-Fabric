package com.dynamero.events;

import com.dynamero.shared.annotations.*;
import com.dynamero.shared.exceptions.Exceptions;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Loader-neutral callbacks for the common lifecycle events used by Industria.
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
public class Events
{
    public Events()
    {
        Exceptions.throwCtorAssertion();
    }

    public static void onServerStarting(Consumer<MinecraftServer> callback)
    {
        IEventService.get().onServerStarting(require(callback));
    }

    public static void onServerStarted(Consumer<MinecraftServer> callback)
    {
        IEventService.get().onServerStarted(require(callback));
    }

    public static void onServerStopping(Consumer<MinecraftServer> callback)
    {
        IEventService.get().onServerStopping(require(callback));
    }

    public static void onServerStopped(Consumer<MinecraftServer> callback)
    {
        IEventService.get().onServerStopped(require(callback));
    }

    public static void onLevelLoad(Consumer<ServerLevel> callback)
    {
        IEventService.get().onLevelLoad(require(callback));
    }

    public static void onLevelUnload(Consumer<ServerLevel> callback)
    {
        IEventService.get().onLevelUnload(require(callback));
    }

    public static void onStartServerTick(Consumer<MinecraftServer> callback)
    {
        IEventService.get().onStartServerTick(require(callback));
    }

    public static void onEndServerTick(Consumer<MinecraftServer> callback)
    {
        IEventService.get().onEndServerTick(require(callback));
    }

    public static void onStartLevelTick(Consumer<ServerLevel> callback)
    {
        IEventService.get().onStartLevelTick(require(callback));
    }

    public static void onEndLevelTick(Consumer<ServerLevel> callback)
    {
        IEventService.get().onEndLevelTick(require(callback));
    }

    public static void onPlayerJoin(Consumer<ServerPlayer> callback)
    {
        IEventService.get().onPlayerJoin(require(callback));
    }

    public static void onPlayerDisconnect(Consumer<ServerPlayer> callback)
    {
        IEventService.get().onPlayerDisconnect(require(callback));
    }

    public static void onPlayerRespawn(Consumer<ServerPlayer> callback)
    {
        IEventService.get().onPlayerRespawn(require(callback));
    }

    /**
     * Registers a callback that runs after a server player has entered another dimension.
     */
    public static void onPlayerDimensionChange(PlayerDimensionChangeCallback callback)
    {
        IEventService.get().onPlayerDimensionChange(require(callback));
    }

    public static void onBlockBroken(BlockBreakCallback callback)
    {
        IEventService.get().onBlockBroken(require(callback));
    }

    public static void onLivingDamaged(LivingDamageCallback callback)
    {
        IEventService.get().onLivingDamaged(require(callback));
    }

    public static void onLivingKilled(BiConsumer<LivingEntity, DamageSource> callback)
    {
        IEventService.get().onLivingKilled(require(callback));
    }

    public static void onCommandRegistration(Consumer<CommandDispatcher<CommandSourceStack>> callback)
    {
        IEventService.get().onCommandRegistration(require(callback));
    }

    public static void onDatapackReload(Consumer<MinecraftServer> callback)
    {
        IEventService.get().onDatapackReload(require(callback));
    }

    private static <T> T require(T callback)
    {
        return Objects.requireNonNull(callback, "callback");
    }
}