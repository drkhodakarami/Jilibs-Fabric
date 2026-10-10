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

package com.dynamero.dispersion.base.interfaces;

import java.util.Optional;

import org.jspecify.annotations.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;

import com.dynamero.shared.annotations.*;

/**
 * Handler interface for providing customized display names and container interaction sounds for dispersion variants.
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
public interface DispersionVariantAttributeHandler
{
    /**
     * Resolves the localized display name for the given dispersion variant.
     *
     * @param variant the dispersion variant
     * @return the text component name
     */
    Component getName(DispersionVariant variant);

    /**
     * Resolves an optional custom sound event played when filling a container with the dispersion variant.
     *
     * @param variant the dispersion variant
     * @param item    the container item, or null
     * @return an optional containing the fill sound if defined
     */
    default Optional<SoundEvent> getFillSound(DispersionVariant variant, @Nullable Item item)
    {
        return Optional.empty();
    }

    /**
     * Resolves an optional custom sound event played when emptying the dispersion variant from a container.
     *
     * @param variant the dispersion variant
     * @param item    the container item, or null
     * @return an optional containing the empty sound if defined
     */
    default Optional<SoundEvent> getEmptySound(DispersionVariant variant, @Nullable Item item)
    {
        return Optional.empty();
    }
}