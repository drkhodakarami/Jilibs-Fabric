package com.dynamero.events;

import com.dynamero.shared.annotations.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

/**
 * Runs after a server player has entered a different dimension.
 *
 * <p>The player is already in {@code destination} when this callback runs. This makes the callback suitable for
 * resending dimension-scoped state to the player.</p>
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

@FunctionalInterface
public interface PlayerDimensionChangeCallback
{
    void afterDimensionChange(ServerPlayer player, ServerLevel origin, ServerLevel destination);
}