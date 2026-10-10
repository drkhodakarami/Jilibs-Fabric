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

package com.dynamero.treefamily;

import com.dynamero.registerars.ModBlock;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;

import net.minecraft.core.registries.Registries;
import net.minecraft.references.BlockItemId;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

import com.dynamero.registerars.BlockItemRegisterar;
import com.dynamero.registerars.BlockRegisterar;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.utils.BaseHelper;

/**
 * Container and registry holder for all standard blocks and corresponding block items within a {@link TreeFamilyBuilder}.
 *
 * @param log Log block for this wood set.
 * @param strippedLog Stripped log block for this wood set.
 * @param wood Wood block (6-sided log bark) for this wood set.
 * @param strippedWood Stripped wood block for this wood set.
 * @param planks Planks block for this wood set.
 * @param leaves Leaves block for this wood set.
 * @param sapling Sapling block for this wood set.
 * @param stairs Stairs block for this wood set.
 * @param slab Slab block for this wood set.
 * @param fence Fence block for this wood set.
 * @param fenceGate Fence gate block for this wood set.
 * @param door Door block for this wood set.
 * @param trapdoor Trapdoor block for this wood set.
 * @param pressurePlate Pressure plate block for this wood set.
 * @param button Button block for this wood set.
 */
@SuppressWarnings("unused")
@Developer("TurtyWurty")
@CreatedAt("2026-10-08")
@ModifiedAt("2026-10-08")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public record WoodsetBlocks (ModBlock<?, ?> log, ModBlock<?, ?> strippedLog, ModBlock<?, ?> wood, ModBlock<?, ?> strippedWood, ModBlock<?, ?> planks,
                             ModBlock<?, ?> leaves, ModBlock<?, ?> sapling, ModBlock<?, ?> stairs, ModBlock<?, ?> slab,
                             ModBlock<?, ?> fence, ModBlock<?, ?> fenceGate, ModBlock<?, ?> door, ModBlock<?, ?> trapdoor,
                             ModBlock<?, ?> pressurePlate, ModBlock<?, ?> button, WoodType woodType, BlockSetType blockSetType)
{

    /**
     * Constructs and registers all standard blocks and block items for a wood set.
     *
     * @param modid             the mod identifier
     * @param name              the base wood name
     * @param blockRegister     the block registerer helper
     * @param blockItemRegister the block-item registerer helper
     * @param saplingGenerator  the tree grower generator for saplings
     * @param woodType          supplier providing the wood type
     * @param blockSetType      the block set type
     */
    public static WoodsetBlocks build(String modid, String name,
                         BlockRegisterar blockRegister, BlockItemRegisterar blockItemRegister,
                         TreeGrower saplingGenerator, WoodType woodType, BlockSetType blockSetType)
    {
        //region Item IDs
        var woodItemId = BaseHelper.ResourceKeys.create(modid, name + "_wood", Registries.ITEM);
        var logItemId = BaseHelper.ResourceKeys.create(modid, name + "_log", Registries.ITEM);
        var plankItemId = BaseHelper.ResourceKeys.create(modid, name + "_planks", Registries.ITEM);
        var leaveItemId = BaseHelper.ResourceKeys.create(modid, name + "_leaves", Registries.ITEM);
        var saplingItemId = BaseHelper.ResourceKeys.create(modid, name + "_sapling", Registries.ITEM);
        var strippedWoodItemId = BaseHelper.ResourceKeys.create(modid, "stripped_" + name + "_wood", Registries.ITEM);
        var strippedLogItemId = BaseHelper.ResourceKeys.create(modid, "stripped_" + name + "_log", Registries.ITEM);
        var stairsItemId = BaseHelper.ResourceKeys.create(modid, name + "_stairs", Registries.ITEM);
        var slabItemId = BaseHelper.ResourceKeys.create(modid, name + "_slab", Registries.ITEM);
        var fenceItemId = BaseHelper.ResourceKeys.create(modid, name + "_fence", Registries.ITEM);
        var fenceGateItemId = BaseHelper.ResourceKeys.create(modid, name + "_fence_gate", Registries.ITEM);
        var doorItemId = BaseHelper.ResourceKeys.create(modid, name + "_door", Registries.ITEM);
        var trapdoorItemId = BaseHelper.ResourceKeys.create(modid, name + "_trapdoor", Registries.ITEM);
        var pressurePlateItemId = BaseHelper.ResourceKeys.create(modid, name + "_pressure_plate", Registries.ITEM);
        var buttonItemId = BaseHelper.ResourceKeys.create(modid, name + "_button", Registries.ITEM);
        //endregion

        //region Block IDs
        var woodBlockId = BaseHelper.ResourceKeys.create(modid, name + "_wood", Registries.BLOCK);
        var logBlockId = BaseHelper.ResourceKeys.create(modid, name + "_log", Registries.BLOCK);
        var plankBlockId = BaseHelper.ResourceKeys.create(modid, name + "_planks", Registries.BLOCK);
        var leaveBlockId = BaseHelper.ResourceKeys.create(modid, name + "_leaves", Registries.BLOCK);
        var saplingBlockId = BaseHelper.ResourceKeys.create(modid, name + "_sapling", Registries.BLOCK);
        var strippedWoodBlockId = BaseHelper.ResourceKeys.create(modid, "stripped_" + name + "_wood", Registries.BLOCK);
        var strippedLogBlockId = BaseHelper.ResourceKeys.create(modid, "stripped_" + name + "_log", Registries.BLOCK);
        var stairsBlockId = BaseHelper.ResourceKeys.create(modid, name + "_stairs", Registries.BLOCK);
        var slabBlockId = BaseHelper.ResourceKeys.create(modid, name + "_slab", Registries.BLOCK);
        var fenceBlockId = BaseHelper.ResourceKeys.create(modid, name + "_fence", Registries.BLOCK);
        var fenceGateBlockId = BaseHelper.ResourceKeys.create(modid, name + "_fence_gate", Registries.BLOCK);
        var doorBlockId = BaseHelper.ResourceKeys.create(modid, name + "_door", Registries.BLOCK);
        var trapdoorBlockId = BaseHelper.ResourceKeys.create(modid, name + "_trapdoor", Registries.BLOCK);
        var pressurePlateBlockId = BaseHelper.ResourceKeys.create(modid, name + "_pressure_plate", Registries.BLOCK);
        var buttonBlockId = BaseHelper.ResourceKeys.create(modid, name + "_button", Registries.BLOCK);
        var signBlockId = BaseHelper.ResourceKeys.create(modid, name + "_sign", Registries.BLOCK);
        var wallSignBlockId = BaseHelper.ResourceKeys.create(modid, name + "_wall_sign", Registries.BLOCK);
        var hangingSignBlockId = BaseHelper.ResourceKeys.create(modid, name + "_hanging_sign", Registries.BLOCK);
        var wallHangingSignBlockId = BaseHelper.ResourceKeys.create(modid, name + "_wall_hanging_sign", Registries.BLOCK);
        //endregion

        //region BlockItem IDs
        var woodBlockItemId = new BlockItemId(woodBlockId, woodItemId);
        var logBlockItemId = new BlockItemId(logBlockId, logItemId);
        var plankBlockItemId = new BlockItemId(plankBlockId, plankItemId);
        var leaveBlockItemId = new BlockItemId(leaveBlockId, leaveItemId);
        var saplingBlockItemId = new BlockItemId(saplingBlockId, saplingItemId);
        var strippedWoodBlockItemId = new BlockItemId(strippedWoodBlockId, strippedWoodItemId);
        var strippedLogBlockItemId = new BlockItemId(strippedLogBlockId, strippedLogItemId);
        var stairsBlockItemId = new BlockItemId(stairsBlockId, stairsItemId);
        var slabBlockItemId = new BlockItemId(slabBlockId, slabItemId);
        var fenceBlockItemId = new BlockItemId(fenceBlockId, fenceItemId);
        var fenceGateBlockItemId = new BlockItemId(fenceGateBlockId, fenceGateItemId);
        var doorBlockItemId = new BlockItemId(doorBlockId, doorItemId);
        var trapdoorBlockItemId = new BlockItemId(trapdoorBlockId, trapdoorItemId);
        var pressurePlateBlockItemId = new BlockItemId(pressurePlateBlockId, pressurePlateItemId);
        var buttonBlockItemId = new BlockItemId(buttonBlockId, buttonItemId);
        //endregion

        //region Register
        var planks = ModBlock.register(blockRegister, blockItemRegister,
                               Blocks.OAK_PLANKS, plankBlockItemId,
                               Block::new);

        var log = ModBlock.register(blockRegister, blockItemRegister,
                            Blocks.OAK_LOG, logBlockItemId,
                            RotatedPillarBlock::new);

        var strippedLog = ModBlock.register(blockRegister, blockItemRegister,
                                    Blocks.STRIPPED_OAK_LOG, strippedLogBlockItemId,
                                    RotatedPillarBlock::new);

        var strippedWood = ModBlock.register(blockRegister, blockItemRegister,
                                     Blocks.STRIPPED_OAK_WOOD, strippedWoodBlockItemId,
                                     RotatedPillarBlock::new);

        var wood = ModBlock.register(blockRegister, blockItemRegister,
                             Blocks.OAK_WOOD, woodBlockItemId,
                             RotatedPillarBlock::new);

        var leaves = ModBlock.register(blockRegister, blockItemRegister,
                               Blocks.OAK_LEAVES, leaveBlockItemId,
                               settings ->
                                 new TintedParticleLeavesBlock(0.01F, settings));

        var sapling = ModBlock.register(blockRegister, blockItemRegister,
                                Blocks.OAK_SAPLING, saplingBlockItemId,
                                settings -> new SaplingBlock(saplingGenerator, settings));

        var stairs = ModBlock.register(blockRegister, blockItemRegister,
                               Blocks.OAK_STAIRS, stairsBlockItemId,
                               settings ->
                                 new StairBlock(planks.block().defaultBlockState(), settings));

        var slab = ModBlock.register(blockRegister, blockItemRegister,
                             Blocks.OAK_SLAB, slabBlockItemId,
                             SlabBlock::new);

        var fence = ModBlock.register(blockRegister, blockItemRegister,
                              Blocks.OAK_FENCE, fenceBlockItemId,
                              FenceBlock::new);

        var fenceGate = ModBlock.register(blockRegister, blockItemRegister,
                                  Blocks.OAK_FENCE_GATE, fenceGateBlockItemId,
                                  settings ->
                                    new FenceGateBlock(woodType, settings));

        var door = ModBlock.register(blockRegister, blockItemRegister,
                             Blocks.OAK_DOOR, doorBlockItemId,
                             settings -> new DoorBlock(blockSetType, settings));

        var trapdoor = ModBlock.register(blockRegister, blockItemRegister,
                                 Blocks.OAK_TRAPDOOR, trapdoorBlockItemId,
                                 settings -> new TrapDoorBlock(blockSetType, settings));

        var pressurePlate = ModBlock.register(blockRegister, blockItemRegister,
                                      Blocks.OAK_PRESSURE_PLATE, pressurePlateBlockItemId,
                                      settings ->
                                        new PressurePlateBlock(blockSetType, settings));

        var button = ModBlock.register(blockRegister, blockItemRegister,
                               Blocks.OAK_BUTTON, buttonBlockItemId,
                               settings ->
                                 new ButtonBlock(blockSetType, 30, settings));

        var blocks = new WoodsetBlocks(log, strippedLog, wood, strippedWood, planks, leaves, sapling, stairs, slab,
                                       fence, fenceGate, door, trapdoor, pressurePlate, button, woodType, blockSetType);
        //endregion

        //region Burnables
        FlammableBlockRegistry flammableBlockRegistry = FlammableBlockRegistry.getDefaultInstance();
        flammableBlockRegistry.add(blocks.planks.block(), 5, 20);
        flammableBlockRegistry.add(blocks.log.block(), 5, 5);
        flammableBlockRegistry.add(blocks.strippedLog.block(), 5, 5);
        flammableBlockRegistry.add(blocks.strippedWood.block(), 5, 5);
        flammableBlockRegistry.add(blocks.wood.block(), 5, 5);
        flammableBlockRegistry.add(blocks.leaves.block(), 30, 60);
        flammableBlockRegistry.add(blocks.sapling.block(), 60, 20);
        flammableBlockRegistry.add(blocks.stairs.block(), 5, 20);
        flammableBlockRegistry.add(blocks.slab.block(), 5, 20);
        flammableBlockRegistry.add(blocks.fence.block(), 5, 20);
        flammableBlockRegistry.add(blocks.fenceGate.block(), 5, 20);
        flammableBlockRegistry.add(blocks.door.block(), 5, 20);
        flammableBlockRegistry.add(blocks.trapdoor.block(), 5, 20);
        flammableBlockRegistry.add(blocks.pressurePlate.block(), 5, 20);
        flammableBlockRegistry.add(blocks.button.block(), 5, 20);
        //endregion

        return blocks;
    }
}