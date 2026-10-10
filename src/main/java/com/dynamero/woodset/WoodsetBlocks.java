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

package com.dynamero.woodset;

import java.util.function.Function;
import java.util.function.Supplier;

import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;

import net.minecraft.core.registries.Registries;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

import com.dynamero.registerars.BlockItemRegisterar;
import com.dynamero.registerars.BlockRegisterar;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.utils.BaseHelper;

/**
 * Container and registry holder for all standard blocks and corresponding block items within a {@link WoodSet}.
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
public class WoodsetBlocks
{
    /**
     * Log block for this wood set.
     */
    public final RotatedPillarBlock log;

    /**
     * Stripped log block for this wood set.
     */
    public final RotatedPillarBlock strippedLog;

    /**
     * Wood block (6-sided log bark) for this wood set.
     */
    public final RotatedPillarBlock wood;

    /**
     * Stripped wood block for this wood set.
     */
    public final RotatedPillarBlock strippedWood;

    /**
     * Planks block for this wood set.
     */
    public final Block planks;

    /**
     * Leaves block for this wood set.
     */
    public final Block leaves;

    /**
     * Sapling block for this wood set.
     */
    public final SaplingBlock sapling;

    /**
     * Stairs block for this wood set.
     */
    public final StairBlock stairs;

    /**
     * Slab block for this wood set.
     */
    public final SlabBlock slab;

    /**
     * Fence block for this wood set.
     */
    public final FenceBlock fence;

    /**
     * Fence gate block for this wood set.
     */
    public final FenceGateBlock fenceGate;

    /**
     * Door block for this wood set.
     */
    public final DoorBlock door;

    /**
     * Trapdoor block for this wood set.
     */
    public final TrapDoorBlock trapdoor;

    /**
     * Pressure plate block for this wood set.
     */
    public final PressurePlateBlock pressurePlate;

    /**
     * Button block for this wood set.
     */
    public final ButtonBlock button;

    /**
     * Resource key for the wood item.
     */
    public final ResourceKey<Item> woodItemId;

    /**
     * Resource key for the log item.
     */
    public final ResourceKey<Item> logItemId;

    /**
     * Resource key for the plank item.
     */
    public final ResourceKey<Item> plankItemId;

    /**
     * Resource key for the leaves item.
     */
    public final ResourceKey<Item> leaveItemId;

    /**
     * Resource key for the sapling item.
     */
    public final ResourceKey<Item> saplingItemId;

    /**
     * Resource key for the stripped wood item.
     */
    public final ResourceKey<Item> strippedWoodItemId;

    /**
     * Resource key for the stripped log item.
     */
    public final ResourceKey<Item> strippedLogItemId;

    /**
     * Resource key for the stairs item.
     */
    public final ResourceKey<Item> stairsItemId;

    /**
     * Resource key for the slab item.
     */
    public final ResourceKey<Item> slabItemId;

    /**
     * Resource key for the fence item.
     */
    public final ResourceKey<Item> fenceItemId;

    /**
     * Resource key for the fence gate item.
     */
    public final ResourceKey<Item> fenceGateItemId;

    /**
     * Resource key for the door item.
     */
    public final ResourceKey<Item> doorItemId;

    /**
     * Resource key for the trapdoor item.
     */
    public final ResourceKey<Item> trapdoorItemId;

    /**
     * Resource key for the pressure plate item.
     */
    public final ResourceKey<Item> pressurePlateItemId;

    /**
     * Resource key for the button item.
     */
    public final ResourceKey<Item> buttonItemId;

    /**
     * Resource key for the wood block.
     */
    public final ResourceKey<Block> woodBlockId;

    /**
     * Resource key for the log block.
     */
    public final ResourceKey<Block> logBlockId;

    /**
     * Resource key for the plank block.
     */
    public final ResourceKey<Block> plankBlockId;

    /**
     * Resource key for the leaves block.
     */
    public final ResourceKey<Block> leaveBlockId;

    /**
     * Resource key for the sapling block.
     */
    public final ResourceKey<Block> saplingBlockId;

    /**
     * Resource key for the stripped wood block.
     */
    public final ResourceKey<Block> strippedWoodBlockId;

    /**
     * Resource key for the stripped log block.
     */
    public final ResourceKey<Block> strippedLogBlockId;

    /**
     * Resource key for the stairs block.
     */
    public final ResourceKey<Block> stairsBlockId;

    /**
     * Resource key for the slab block.
     */
    public final ResourceKey<Block> slabBlockId;

    /**
     * Resource key for the fence block.
     */
    public final ResourceKey<Block> fenceBlockId;

    /**
     * Resource key for the fence gate block.
     */
    public final ResourceKey<Block> fenceGateBlockId;

    /**
     * Resource key for the door block.
     */
    public final ResourceKey<Block> doorBlockId;

    /**
     * Resource key for the trapdoor block.
     */
    public final ResourceKey<Block> trapdoorBlockId;

    /**
     * Resource key for the pressure plate block.
     */
    public final ResourceKey<Block> pressurePlateBlockId;

    /**
     * Resource key for the button block.
     */
    public final ResourceKey<Block> buttonBlockId;

    /**
     * Resource key for the standing sign block.
     */
    public final ResourceKey<Block> signBlockId;

    /**
     * Resource key for the wall sign block.
     */
    public final ResourceKey<Block> wallSignBlockId;

    /**
     * Resource key for the ceiling hanging sign block.
     */
    public final ResourceKey<Block> hangingSignBlockId;

    /**
     * Resource key for the wall hanging sign block.
     */
    public final ResourceKey<Block> wallHangingSignBlockId;

    /**
     * Block-item identifier pair for the wood block and item.
     */
    public final BlockItemId woodBlockItemId;

    /**
     * Block-item identifier pair for the log block and item.
     */
    public final BlockItemId logBlockItemId;

    /**
     * Block-item identifier pair for the plank block and item.
     */
    public final BlockItemId plankBlockItemId;

    /**
     * Block-item identifier pair for the leaves block and item.
     */
    public final BlockItemId leaveBlockItemId;

    /**
     * Block-item identifier pair for the sapling block and item.
     */
    public final BlockItemId saplingBlockItemId;

    /**
     * Block-item identifier pair for the stripped wood block and item.
     */
    public final BlockItemId strippedWoodBlockItemId;

    /**
     * Block-item identifier pair for the stripped log block and item.
     */
    public final BlockItemId strippedLogBlockItemId;

    /**
     * Block-item identifier pair for the stairs block and item.
     */
    public final BlockItemId stairsBlockItemId;

    /**
     * Block-item identifier pair for the slab block and item.
     */
    public final BlockItemId slabBlockItemId;

    /**
     * Block-item identifier pair for the fence block and item.
     */
    public final BlockItemId fenceBlockItemId;

    /**
     * Block-item identifier pair for the fence gate block and item.
     */
    public final BlockItemId fenceGateBlockItemId;

    /**
     * Block-item identifier pair for the door block and item.
     */
    public final BlockItemId doorBlockItemId;

    /**
     * Block-item identifier pair for the trapdoor block and item.
     */
    public final BlockItemId trapdoorBlockItemId;

    /**
     * Block-item identifier pair for the pressure plate block and item.
     */
    public final BlockItemId pressurePlateBlockItemId;

    /**
     * Block-item identifier pair for the button block and item.
     */
    public final BlockItemId buttonBlockItemId;

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
     * @param planks            factory creating planks blocks
     * @param log               factory creating log blocks
     * @param strippedLog       factory creating stripped log blocks
     * @param strippedWood      factory creating stripped wood blocks
     * @param wood              factory creating wood blocks
     * @param leaves            factory creating leaves blocks
     * @param sapling           factory creating sapling blocks
     * @param stairs            factory creating stairs blocks
     * @param slab              factory creating slab blocks
     * @param fence             factory creating fence blocks
     * @param fenceGate         factory creating fence gate blocks
     * @param door              factory creating door blocks
     * @param trapdoor          factory creating trapdoor blocks
     * @param pressurePlate     factory creating pressure plate blocks
     * @param button            factory creating button blocks
     */
    public WoodsetBlocks(String modid, String name,
                         BlockRegisterar blockRegister, BlockItemRegisterar blockItemRegister,
                         TreeGrower saplingGenerator, Supplier<WoodType> woodType, BlockSetType blockSetType,
                         Function<BlockBehaviour.Properties, Block> planks,
                         Function<BlockBehaviour.Properties, RotatedPillarBlock> log,
                         Function<BlockBehaviour.Properties, RotatedPillarBlock> strippedLog,
                         Function<BlockBehaviour.Properties, RotatedPillarBlock> strippedWood,
                         Function<BlockBehaviour.Properties, RotatedPillarBlock> wood,
                         Function<BlockBehaviour.Properties, Block> leaves,
                         Function<BlockBehaviour.Properties, SaplingBlock> sapling,
                         Function<BlockBehaviour.Properties, StairBlock> stairs,
                         Function<BlockBehaviour.Properties, SlabBlock> slab,
                         Function<BlockBehaviour.Properties, FenceBlock> fence,
                         Function<BlockBehaviour.Properties, FenceGateBlock> fenceGate,
                         Function<BlockBehaviour.Properties, DoorBlock> door,
                         Function<BlockBehaviour.Properties, TrapDoorBlock> trapdoor,
                         Function<BlockBehaviour.Properties, PressurePlateBlock> pressurePlate,
                         Function<BlockBehaviour.Properties, ButtonBlock> button)
    {
        //region Item IDs
        woodItemId = BaseHelper.ResourceKeys.create(modid, name + "_wood", Registries.ITEM);
        logItemId = BaseHelper.ResourceKeys.create(modid, name + "_log", Registries.ITEM);
        plankItemId = BaseHelper.ResourceKeys.create(modid, name + "_planks", Registries.ITEM);
        leaveItemId = BaseHelper.ResourceKeys.create(modid, name + "_leaves", Registries.ITEM);
        saplingItemId = BaseHelper.ResourceKeys.create(modid, name + "_sapling", Registries.ITEM);
        strippedWoodItemId = BaseHelper.ResourceKeys.create(modid, "stripped_" + name + "_wood", Registries.ITEM);
        strippedLogItemId = BaseHelper.ResourceKeys.create(modid, "stripped_" + name + "_log", Registries.ITEM);
        stairsItemId = BaseHelper.ResourceKeys.create(modid, name + "_stairs", Registries.ITEM);
        slabItemId = BaseHelper.ResourceKeys.create(modid, name + "_slab", Registries.ITEM);
        fenceItemId = BaseHelper.ResourceKeys.create(modid, name + "_fence", Registries.ITEM);
        fenceGateItemId = BaseHelper.ResourceKeys.create(modid, name + "_fence_gate", Registries.ITEM);
        doorItemId = BaseHelper.ResourceKeys.create(modid, name + "_door", Registries.ITEM);
        trapdoorItemId = BaseHelper.ResourceKeys.create(modid, name + "_trapdoor", Registries.ITEM);
        pressurePlateItemId = BaseHelper.ResourceKeys.create(modid, name + "_pressure_plate", Registries.ITEM);
        buttonItemId = BaseHelper.ResourceKeys.create(modid, name + "_button", Registries.ITEM);
        //endregion

        //region Block IDs
        woodBlockId = BaseHelper.ResourceKeys.create(modid, name + "_wood", Registries.BLOCK);
        logBlockId = BaseHelper.ResourceKeys.create(modid, name + "_log", Registries.BLOCK);
        plankBlockId = BaseHelper.ResourceKeys.create(modid, name + "_planks", Registries.BLOCK);
        leaveBlockId = BaseHelper.ResourceKeys.create(modid, name + "_leaves", Registries.BLOCK);
        saplingBlockId = BaseHelper.ResourceKeys.create(modid, name + "_sapling", Registries.BLOCK);
        strippedWoodBlockId = BaseHelper.ResourceKeys.create(modid, "stripped_" + name + "_wood", Registries.BLOCK);
        strippedLogBlockId = BaseHelper.ResourceKeys.create(modid, "stripped_" + name + "_log", Registries.BLOCK);
        stairsBlockId = BaseHelper.ResourceKeys.create(modid, name + "_stairs", Registries.BLOCK);
        slabBlockId = BaseHelper.ResourceKeys.create(modid, name + "_slab", Registries.BLOCK);
        fenceBlockId = BaseHelper.ResourceKeys.create(modid, name + "_fence", Registries.BLOCK);
        fenceGateBlockId = BaseHelper.ResourceKeys.create(modid, name + "_fence_gate", Registries.BLOCK);
        doorBlockId = BaseHelper.ResourceKeys.create(modid, name + "_door", Registries.BLOCK);
        trapdoorBlockId = BaseHelper.ResourceKeys.create(modid, name + "_trapdoor", Registries.BLOCK);
        pressurePlateBlockId = BaseHelper.ResourceKeys.create(modid, name + "_pressure_plate", Registries.BLOCK);
        buttonBlockId = BaseHelper.ResourceKeys.create(modid, name + "_button", Registries.BLOCK);
        signBlockId = BaseHelper.ResourceKeys.create(modid, name + "_sign", Registries.BLOCK);
        wallSignBlockId = BaseHelper.ResourceKeys.create(modid, name + "_wall_sign", Registries.BLOCK);
        hangingSignBlockId = BaseHelper.ResourceKeys.create(modid, name + "_hanging_sign", Registries.BLOCK);
        wallHangingSignBlockId = BaseHelper.ResourceKeys.create(modid, name + "_wall_hanging_sign", Registries.BLOCK);
        //endregion

        //region BlockItem IDs
        woodBlockItemId = new BlockItemId(woodBlockId, woodItemId);
        logBlockItemId = new BlockItemId(logBlockId, logItemId);
        plankBlockItemId = new BlockItemId(plankBlockId, plankItemId);
        leaveBlockItemId = new BlockItemId(leaveBlockId, leaveItemId);
        saplingBlockItemId = new BlockItemId(saplingBlockId, saplingItemId);
        strippedWoodBlockItemId = new BlockItemId(strippedWoodBlockId, strippedWoodItemId);
        strippedLogBlockItemId = new BlockItemId(strippedLogBlockId, strippedLogItemId);
        stairsBlockItemId = new BlockItemId(stairsBlockId, stairsItemId);
        slabBlockItemId = new BlockItemId(slabBlockId, slabItemId);
        fenceBlockItemId = new BlockItemId(fenceBlockId, fenceItemId);
        fenceGateBlockItemId = new BlockItemId(fenceGateBlockId, fenceGateItemId);
        doorBlockItemId = new BlockItemId(doorBlockId, doorItemId);
        trapdoorBlockItemId = new BlockItemId(trapdoorBlockId, trapdoorItemId);
        pressurePlateBlockItemId = new BlockItemId(pressurePlateBlockId, pressurePlateItemId);
        buttonBlockItemId = new BlockItemId(buttonBlockId, buttonItemId);
        //endregion

        //region Register
        this.planks = blockRegister.registerCopy(plankBlockItemId, Blocks.OAK_PLANKS, planks == null ? Block::new : planks);
        blockItemRegister.register(this.planks, plankBlockItemId);

        this.log = blockRegister.registerCopy(logBlockItemId, Blocks.OAK_LOG, log == null ? RotatedPillarBlock::new : log);
        blockItemRegister.register(this.log, logBlockItemId);

        this.strippedLog = blockRegister.registerCopy(strippedLogBlockItemId, Blocks.STRIPPED_OAK_LOG, strippedLog == null ? RotatedPillarBlock::new : strippedLog);
        blockItemRegister.register(this.strippedLog, strippedLogBlockItemId);

        this.strippedWood = blockRegister.registerCopy(strippedWoodBlockItemId, Blocks.STRIPPED_OAK_WOOD, strippedWood == null ? RotatedPillarBlock::new : strippedWood);
        blockItemRegister.register(this.strippedWood, strippedWoodBlockItemId);

        this.wood = blockRegister.registerCopy(woodBlockItemId, Blocks.OAK_WOOD, wood == null ? RotatedPillarBlock::new : wood);
        blockItemRegister.register(this.wood, woodBlockItemId);

        this.leaves = blockRegister.registerCopy(leaveBlockItemId, Blocks.OAK_LEAVES,
                                                leaves == null ? settings -> new TintedParticleLeavesBlock(0.01F, settings) : leaves);
        blockItemRegister.register(this.leaves, leaveBlockItemId);

        this.sapling = blockRegister.registerCopy(saplingBlockItemId, Blocks.OAK_SAPLING,
                                                sapling == null ? settings -> new SaplingBlock(saplingGenerator, settings) : sapling);
        blockItemRegister.register(this.sapling, saplingBlockItemId);

        this.stairs = blockRegister.registerCopy(stairsBlockItemId, Blocks.OAK_STAIRS,
                                                stairs == null ? settings -> new StairBlock(this.planks.defaultBlockState(), settings) : stairs);
        blockItemRegister.register(this.stairs, stairsBlockItemId);

        this.slab = blockRegister.registerCopy(slabBlockItemId, Blocks.OAK_SLAB, slab == null ? SlabBlock::new : slab);
        blockItemRegister.register(this.slab, slabBlockItemId);

        this.fence = blockRegister.registerCopy(fenceBlockItemId, Blocks.OAK_FENCE, fence == null ? FenceBlock::new : fence);
        blockItemRegister.register(this.fence, fenceBlockItemId);

        this.fenceGate = blockRegister.registerCopy(fenceGateBlockItemId, Blocks.OAK_FENCE_GATE,
                                                    fenceGate == null ? settings -> new FenceGateBlock(woodType.get(), settings) : fenceGate);
        blockItemRegister.register(this.fenceGate, fenceGateBlockItemId);

        this.door = blockRegister.registerCopy(doorBlockItemId, Blocks.OAK_DOOR,
                                            door == null ? settings -> new DoorBlock(blockSetType, settings) : door);
        blockItemRegister.register(this.door, doorBlockItemId);

        this.trapdoor = blockRegister.registerCopy(trapdoorBlockItemId, Blocks.OAK_TRAPDOOR,
                                                trapdoor == null ? settings -> new TrapDoorBlock(blockSetType, settings) : trapdoor);
        blockItemRegister.register(this.trapdoor, trapdoorBlockItemId);

        this.pressurePlate = blockRegister.registerCopy(pressurePlateBlockItemId, Blocks.OAK_PRESSURE_PLATE,
                                                        pressurePlate == null ? settings -> new PressurePlateBlock(blockSetType, settings) : pressurePlate);
        blockItemRegister.register(this.pressurePlate);

        this.button = blockRegister.registerCopy(buttonBlockItemId, Blocks.OAK_BUTTON,
                                                button == null ? settings -> new ButtonBlock(blockSetType, 30, settings) : button);
        blockItemRegister.register(this.button, buttonBlockItemId);
        //endregion

        //region Burnables
        FlammableBlockRegistry flammableBlockRegistry = FlammableBlockRegistry.getDefaultInstance();
        flammableBlockRegistry.add(this.planks, 5, 20);
        flammableBlockRegistry.add(this.log, 5, 5);
        flammableBlockRegistry.add(this.strippedLog, 5, 5);
        flammableBlockRegistry.add(this.strippedWood, 5, 5);
        flammableBlockRegistry.add(this.wood, 5, 5);
        flammableBlockRegistry.add(this.leaves, 30, 60);
        flammableBlockRegistry.add(this.sapling, 60, 20);
        flammableBlockRegistry.add(this.stairs, 5, 20);
        flammableBlockRegistry.add(this.slab, 5, 20);
        flammableBlockRegistry.add(this.fence, 5, 20);
        flammableBlockRegistry.add(this.fenceGate, 5, 20);
        flammableBlockRegistry.add(this.door, 5, 20);
        flammableBlockRegistry.add(this.trapdoor, 5, 20);
        flammableBlockRegistry.add(this.pressurePlate, 5, 20);
        flammableBlockRegistry.add(this.button, 5, 20);
        //endregion
    }
}