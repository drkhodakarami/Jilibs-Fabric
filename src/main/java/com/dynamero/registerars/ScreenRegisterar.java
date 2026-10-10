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

package com.dynamero.registerars;

import net.fabricmc.fabric.api.menu.v1.ExtendedMenuType;
import org.jetbrains.annotations.NotNull;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import com.dynamero.registerars.interfaces.IMenuRegisterar;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.utils.BaseHelper;

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
public class ScreenRegisterar implements IMenuRegisterar
{
    /**
     * The mod ID used for registering screens and handlers.
     */
    private final String modId;

    /**
     * Constructs a new instance of ScreenRegisterer with the specified mod ID.
     *
     * @param modId the mod ID
     */
    public ScreenRegisterar(String modId)
    {
        this.modId = modId;
    }

    @Override
    public <R extends AbstractContainerMenu, D extends CustomPacketPayload> ExtendedMenuType<@NotNull R, D>
            register(String name, ExtendedMenuType.ExtendedFactory<@NotNull R, D> factory,
                    StreamCodec<? super RegistryFriendlyByteBuf, D> codec)
    {
        ResourceKey<MenuType<?>> key = BaseHelper.ResourceKeys.create(this.modId, name, Registries.MENU);
        return Registry.register(BuiltInRegistries.MENU, key, new ExtendedMenuType<>(factory, codec));
    }
}