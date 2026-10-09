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


import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

import com.dynamero.registerars.interfaces.IBlockRegisterar;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.utils.BaseHelper;

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
public class BlockRegisterar implements IBlockRegisterar
{
    /**
     * The unique identifier for the mod.
     */
    private final String modId;

    /**
     * Constructs a new JiBlockRegister with the specified mod ID.
     *
     * @param modId The unique identifier for the mod.
     */
    public BlockRegisterar(String modId)
    {
        this.modId = modId;
    }

    @Override
    public <R extends Block> R registerBlock(String name, R block)
    {
        return register(BaseHelper.ResourceKeys.create(this.modId, name, Registries.BLOCK), block);
    }

    @Override
    public StairBlock registerStair(String name, Block stateBlock, Block copyBlock)
    {
        ResourceKey<Block> key = BaseHelper.ResourceKeys.create(this.modId, name, Registries.BLOCK);
        return registerBlock(name, new StairBlock(stateBlock.defaultBlockState(),
                                                BlockBehaviour.Properties.ofFullCopy(copyBlock).setId(key)));
    }

    @Override
    public SlabBlock registerSlab(String name, Block copyBlock)
    {
        ResourceKey<Block> key = BaseHelper.ResourceKeys.create(this.modId, name, Registries.BLOCK);
        return registerBlock(name, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(copyBlock).setId(key)));
    }

    @Override
    public ButtonBlock registerButton(String name, BlockSetType blockType, int pressureTicks, Block copyBlock)
    {
        ResourceKey<Block> key = BaseHelper.ResourceKeys.create(this.modId, name, Registries.BLOCK);
        return registerBlock(name, new ButtonBlock(blockType, pressureTicks,
                                                BlockBehaviour.Properties.ofFullCopy(copyBlock).setId(key)));
    }

    @Override
    public PressurePlateBlock registerPressurePlate(String name, BlockSetType blockType, Block copyBlock)
    {
        ResourceKey<Block> key = BaseHelper.ResourceKeys.create(this.modId, name, Registries.BLOCK);
        return registerBlock(name, new PressurePlateBlock(blockType,
                                                        BlockBehaviour.Properties.ofFullCopy(copyBlock).setId(key)));
    }

    @Override
    public FenceBlock registerFence(String name, Block copyBlock)
    {
        ResourceKey<Block> key = BaseHelper.ResourceKeys.create(this.modId, name, Registries.BLOCK);
        return registerBlock(name, new FenceBlock(BlockBehaviour.Properties.ofFullCopy(copyBlock).setId(key)));
    }

    @Override
    public FenceGateBlock registerFenceGate(String name, WoodType woodType, Block copyBlock)
    {
        ResourceKey<Block> key = BaseHelper.ResourceKeys.create(this.modId, name, Registries.BLOCK);
        return registerBlock(name, new FenceGateBlock(woodType, BlockBehaviour.Properties.ofFullCopy(copyBlock).setId(key)));
    }

    @Override
    public WallBlock registerWall(String name, Block copyBlock)
    {
        ResourceKey<Block> key = BaseHelper.ResourceKeys.create(this.modId, name, Registries.BLOCK);
        return registerBlock(name, new WallBlock(BlockBehaviour.Properties.ofFullCopy(copyBlock).setId(key)));
    }

    @Override
    public DoorBlock registerDoor(String name, BlockSetType blockType, Block copyBlock)
    {
        ResourceKey<Block> key = BaseHelper.ResourceKeys.create(this.modId, name, Registries.BLOCK);
        return registerBlock(name, new DoorBlock(blockType,
                                                BlockBehaviour.Properties.ofFullCopy(copyBlock).setId(key)));
    }

    @Override
    public TrapDoorBlock registerTrapdoor(String name, BlockSetType blockType, Block copyBlock)
    {
        ResourceKey<Block> key = BaseHelper.ResourceKeys.create(this.modId, name, Registries.BLOCK);
        return registerBlock(name, new TrapDoorBlock(blockType,
                                                    BlockBehaviour.Properties.ofFullCopy(copyBlock).setId(key)));
    }
}