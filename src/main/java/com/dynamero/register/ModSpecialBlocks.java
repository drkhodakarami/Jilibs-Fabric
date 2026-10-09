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

package com.dynamero.register;

import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

@SuppressWarnings("unused")
public record ModSpecialBlocks(ModBlock<?, ?> stair, ModBlock<?, ?> slab, ModBlock<?, ?> button, ModBlock<?, ?> pressurePlate,
                               ModBlock<?, ?> fence, ModBlock<?, ?> fenceGate, ModBlock<?, ?> wall,
                               ModBlock<?, ?> door, ModBlock<?, ?> trapdoor)
{
    public static class Builder
    {
        String name, modid;

        BlockRegisterar blockRegisterar;

        ModBlock<?, ?> stair, slab, button, pressurePlate, fence, fenceGate, wall, door, trapdoor;

        public Builder(String modid, String name)
        {
            this.modid = modid;
            this.name = name;
            blockRegisterar = new BlockRegisterar(modid);
        }

        public Builder stair(Block stateBlock)
        {
            this.stair = new ModBlock.Builder<>(name, modid)
                    .add(properties -> new StairBlock(stateBlock.defaultBlockState(), properties)).build();
            return this;
        }

        public <C extends Block> Builder stair(Block stateBlock, C copyBlock)
        {
            this.stair = new ModBlock.Builder<>(name, modid)
                    .add(_ -> new StairBlock(stateBlock.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(copyBlock))).build();
            return this;
        }

        public Builder slab()
        {
            this.stair = new ModBlock.Builder<>(name, modid).add(SlabBlock::new).build();
            return this;
        }

        public <C extends Block> Builder slab(C copyBlock)
        {
            this.stair = new ModBlock.Builder<>(name, modid)
                    .add(_ -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(copyBlock))).build();
            return this;
        }

        public Builder button(BlockSetType blockType, int pressureTicks)
        {
            this.stair = new ModBlock.Builder<>(modid, name).add(properties -> new ButtonBlock(blockType, pressureTicks, properties)).build();
            return this;
        }

        public <C extends Block> Builder button(BlockSetType blockType, int pressureTicks, C copyBlock)
        {
            this.stair = new ModBlock.Builder<>(modid, name)
                    .add(_ -> new ButtonBlock(blockType, pressureTicks, BlockBehaviour.Properties.ofFullCopy(copyBlock))).build();
            return this;
        }

        public Builder pressurePlate(BlockSetType blockType)
        {
            this.stair = new ModBlock.Builder<>(modid, name).add(properties -> new PressurePlateBlock(blockType, properties)).build();
            return this;
        }

        public <C extends Block> Builder pressurePlate(BlockSetType blockType, C copyBlock)
        {
            this.stair = new ModBlock.Builder<>(modid, name)
                    .add(_ -> new PressurePlateBlock(blockType, BlockBehaviour.Properties.ofFullCopy(copyBlock))).build();
            return this;
        }

        public Builder fence(ModBlock<?, ?> block)
        {
            this.stair = new ModBlock.Builder<>(modid, name).add(FenceBlock::new).build();
            return this;
        }

        public <C extends Block> Builder fence(C copyBlock)
        {
            this.stair = new ModBlock.Builder<>(modid, name).add(_ -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(copyBlock))).build();
            return this;
        }

        public Builder fenceGate(WoodType woodType)
        {
            this.stair = new ModBlock.Builder<>(modid, name).add(properties -> new FenceGateBlock(woodType, properties)).build();
            return this;
        }

        public <C extends Block> Builder fenceGate(WoodType woodType, C copyBlock)
        {
            this.stair = new ModBlock.Builder<>(modid, name)
                    .add(_ -> new FenceGateBlock(woodType, BlockBehaviour.Properties.ofFullCopy(copyBlock))).build();
            return this;
        }

        public Builder wall(ModBlock<?, ?> block)
        {
            this.stair = new ModBlock.Builder<>(modid, name).add(WallBlock::new).build();
            return this;
        }

        public <C extends Block> Builder wall(C copyBlock)
        {
            this.stair = new ModBlock.Builder<>(modid, name)
                    .add(_ -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(copyBlock))).build();
            return this;
        }

        public Builder door(BlockSetType blockType)
        {
            this.stair = new ModBlock.Builder<>(modid, name).add(properties -> new DoorBlock(blockType, properties)).build();
            return this;
        }

        public <C extends Block> Builder door(BlockSetType blockType, C copyBlock)
        {
            this.stair = new ModBlock.Builder<>(modid, name)
                    .add(_ -> new DoorBlock(blockType, BlockBehaviour.Properties.ofFullCopy(copyBlock))).build();
            return this;
        }

        public Builder trapdoor(BlockSetType blockType)
        {
            this.stair = new ModBlock.Builder<>(modid, name).add(properties -> new TrapDoorBlock(blockType, properties)).build();
            return this;
        }

        public <C extends Block> Builder trapdoor(BlockSetType blockType, C copyBlock)
        {
            this.stair = new ModBlock.Builder<>(modid, name)
                    .add(_ -> new TrapDoorBlock(blockType, BlockBehaviour.Properties.ofFullCopy(copyBlock))).build();
            return this;
        }

        public ModSpecialBlocks build()
        {
            return new ModSpecialBlocks(stair, slab, button, pressurePlate, fence, fenceGate, wall, door, trapdoor);
        }
    }
}