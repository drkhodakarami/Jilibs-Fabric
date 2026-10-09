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

import java.util.function.Function;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

import com.dynamero.shared.annotations.*;

/**
 * Provides utility methods for registering blocks in Minecraft.
 */
@SuppressWarnings("unused")
@Developer("TheMentor")
@CreatedAt("2026-08-10")
@ModifiedAt("2026-08-10")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public interface IBlockRegisterar
{
    //region MAIN
    default <R extends Block> R register(ResourceKey<Block> key, R block)
    {
        return Registry.register(BuiltInRegistries.BLOCK, key, block);
    }

    default <R extends Block> R register(ResourceKey<Block> key, Function<BlockBehaviour.Properties, ? extends R> factory)
    {
        R block = factory.apply(BlockBehaviour.Properties.of().setId(key));
        return Registry.register(BuiltInRegistries.BLOCK, key, block);
    }

    default <R extends Block> R register(ResourceKey<Block> key, BlockBehaviour.Properties settings, Function<BlockBehaviour.Properties, ? extends R> factory)
    {
        R block = factory.apply(settings.setId(key));
        return Registry.register(BuiltInRegistries.BLOCK, key, block);
    }

    <R extends Block> R registerBlock(String name, R block);
    //ednregion

    //region HELPERS
    default Block register(ResourceKey<Block> key)
    {
        return register(key, (Function<BlockBehaviour.Properties, Block>) Block::new);
    }

    default Block register(BlockItemId blockItemId)
    {
        return register(blockItemId, (Function<BlockBehaviour.Properties, Block>) Block::new);
    }

    default <R extends Block> R register(BlockItemId blockItemId, Function<BlockBehaviour.Properties, ? extends R> factory)
    {
        return register(blockItemId.block(), factory);
    }

    default Block register(BlockItemId blockItemId, BlockBehaviour.Properties settings)
    {
        return register(blockItemId, settings, Block::new);
    }

    default <R extends Block> R register(BlockItemId blockItemId, R block)
    {
        return register(blockItemId.block(), block);
    }

    default <R extends Block> R register(BlockItemId blockItemId, BlockBehaviour.Properties settings, Function<BlockBehaviour.Properties, ? extends R> factory)
    {
        return register(blockItemId.block(), settings, factory);
    }
    //ednregion

    //region COPY
    default Block registerCopy(BlockItemId blockItemId, Block copyBlock)
    {
        return registerCopy(blockItemId, copyBlock, Block::new);
    }

    default Block registerCopy(ResourceKey<Block> key, Block copyBlock)
    {
        return registerCopy(copyBlock, key, Block::new);
    }

    default <R extends Block> R registerCopy(BlockItemId blockItemId, Block copyBlock, Function<BlockBehaviour.Properties, ? extends R> factory)
    {
        return registerCopy(copyBlock, blockItemId.block(), factory);
    }

    default <R extends Block> R registerCopy(ResourceKey<Block> key, Block copyBlock, Function<BlockBehaviour.Properties, ? extends R> factory)
    {
        return registerCopy(copyBlock, key, factory);
    }

    default <R extends Block> R registerCopy(Block copyBlock, ResourceKey<Block> key, Function<BlockBehaviour.Properties, ? extends R> factory)
    {
        R block = factory.apply(BlockBehaviour.Properties.ofFullCopy(copyBlock).setId(key));
        return Registry.register(BuiltInRegistries.BLOCK, key, block);
    }

    default <R extends Block, C extends Block> R registerCopy(C copyBlock, BlockItemId blockItemId, Function<BlockBehaviour.Properties, ? extends R> factory)
    {
        return registerCopy(copyBlock, blockItemId.block(), factory);
    }
    //endregion

    StairBlock registerStair(String name, Block stateBlock, Block copyBlock);
    SlabBlock registerSlab(String name, Block copyBlock);
    ButtonBlock registerButton(String name, BlockSetType blockType, int pressureTicks, Block copyBlock);
    PressurePlateBlock registerPressurePlate(String name, BlockSetType blockType, Block copyBlock);
    FenceBlock registerFence(String name, Block copyBlock);
    FenceGateBlock registerFenceGate(String name, WoodType woodType, Block copyBlock);
    WallBlock registerWall(String name, Block copyBlock);
    DoorBlock registerDoor(String name, BlockSetType blockType, Block copyBlock);
    TrapDoorBlock registerTrapdoor(String name, BlockSetType blockType, Block copyBlock);


    default Block registerXpBlock(String name, int minXp, int maxXp, BlockBehaviour.Properties settings)
    {
        return registerBlock(name, new DropExperienceBlock(UniformInt.of(minXp, maxXp), settings.requiresCorrectToolForDrops()));
    }

    default Block registerXpBlock(String name, int minXp, int maxXp)
    {
        return registerXpBlock(name, minXp, maxXp, BlockBehaviour.Properties.of());
    }
}