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

package com.dynamero.base.client.interfaces;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import net.minecraft.server.level.ServerPlayer;

import com.dynamero.shared.annotations.*;

/**
 * Interface implemented by screen handlers (container menus) that require per-tick updates
 * on the server for players currently interacting with the menu.
 */
@SuppressWarnings("unused")
@Developer("TurtyWurty")
@ModifiedBy("The Mentor")
@ThanksTo(discordUsers = "TheWhyEvenHow")
@CreatedAt("2025-04-18")
@Repository("https://github.com/DaRealTurtyWurty/Industria")
@Discord("https://discord.turtywurty.dev/")
@Youtube("https://www.youtube.com/@TurtyWurty")
public interface ITickScreenHandler
{
    /**
     * Invoked every server tick for the player viewing this screen handler.
     *
     * @param player the server player viewing the container menu
     */
    void tick(ServerPlayer player);

    /**
     * Initializes the server tick listener hook to dispatch ticks to open {@link ITickScreenHandler} menus.
     */
    static void init()
    {
        ServerTickEvents.END_LEVEL_TICK.register(world ->
                                                {
                                                    for (ServerPlayer player : world.players())
                                                        if(player.containerMenu instanceof ITickScreenHandler tickableScreen)
                                                            tickableScreen.tick(player);
                                                });
    }
}