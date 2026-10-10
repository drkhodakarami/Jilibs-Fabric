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

package com.dynamero.registerars.interfaces;

import net.fabricmc.fabric.api.menu.v1.ExtendedMenuType;
import org.jetbrains.annotations.NotNull;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.inventory.AbstractContainerMenu;

import com.dynamero.shared.annotations.*;

/**
 * Registers custom screens and screen handlers for Minecraft.
 */
@SuppressWarnings("unused")
@Developer("TheMentor")
@CreatedAt("2026-10-08")
@ModifiedAt("2026-10-08")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public interface IMenuRegisterar
{
    /**
     * Registers an extended screen handler type.
     *
     * @param <R>             the type of the screen handler
     * @param <D>             the type of the custom payload
     * @param name            the name of the screen handler type
     * @param factory         the factory used to create instances of the screen handler
     * @param codec           the packet codec for the custom payload
     * @return the registered extended screen handler type
     */
    <R extends AbstractContainerMenu, D extends CustomPacketPayload> ExtendedMenuType<@NotNull R, D>
            register(String name, ExtendedMenuType.ExtendedFactory<@NotNull R, D> factory,
                    StreamCodec<? super RegistryFriendlyByteBuf, D> codec);
}