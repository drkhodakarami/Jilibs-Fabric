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

package com.dynamero.base.client;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.level.Level;

import com.dynamero.shared.annotations.*;

/**
 * Base block entity render state (BERS) storing the level context, item render state,
 * and display entity render state.
 */
@Developer("TheMentor")
@CreatedAt("2026-01-12")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
public class BaseBERS extends BlockEntityRenderState
{
    /**
     * The default constructor for the class
     */
    public BaseBERS()
    {
    }

    /**
     * The Minecraft level in which the block entity exists.
     */
    public Level world;

    /**
     * Render state for items displayed by the block entity.
     */
    public ItemStackRenderState itemRenderState = new ItemStackRenderState();

    /**
     * Render state for entities displayed by the block entity.
     */
    public EntityRenderState displayEntityRenderState = new EntityRenderState();
}