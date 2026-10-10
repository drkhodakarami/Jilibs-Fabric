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

import java.util.*;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableSet;
import net.fabricmc.fabric.api.object.builder.v1.block.type.WoodTypeBuilder;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;

import net.minecraft.core.dispenser.BoatDispenseItemBehavior;
import net.minecraft.data.BlockFamily;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityTypes;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

import com.dynamero.mixin.BlockEntityTypeAccessor;
import com.dynamero.registerars.BlockItemRegisterar;
import com.dynamero.registerars.BlockRegisterar;
import com.dynamero.registerars.EntityRegisterar;
import com.dynamero.registerars.ItemRegisterar;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.utils.BaseHelper;

/**
 * Registers a new wood type with all the related family blocks and items in Minecraft.
 *
 * <p>This class provides a comprehensive builder-based API to automate the registration
 * of a complete wood set, including logs, planks, leaves, saplings, signs, and boats,
 * ensuring proper integration with the game's registry and data generation systems.</p>
 *
 * <pre>
 * {@code
 * //RubberWoodBlock extends BaseWoodBlock
 * //RubberLeavesBlock extends BaseLeavesBlock
 * TreeFamily set = new TreeFamilyBuilder
 *                       .build("test_mod_id",
 *                              "rubber",
 *                              ModTreeGrowers.RUBBER,
 *                              () -> WoodType.OAK);
 *
 * //getting access to parts:
 * set.blocks().planks() ...
 * set.blocks().woodType() ...
 * set.blocks().blockSetType() ...
 * set.signs().wallSign() ...
 * set.signs().items().hangingSign() ...
 * set.boats().chestBoat().entity() ...
 * set.tags().block() ...
 * set.tags().item() ...
 * set.treeGrower() ... //TreeGrower
 * set.blockFamily() ...
 * set.name() ...
 * }
 * </pre>
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
public class TreeFamilyBuilder
{
    /**
     * Constructs the wood set and registers all its components.
     *
     * @param modid The mod ID for registration.
     * @param name The base name for the wood type, used in block naming.
     * @param treeGrower The {@link TreeGrower} for the tree type.
     * @param woodType The base {@link WoodType} to use.
     */
    public TreeFamily build(String modid, String name,
                                        TreeGrower treeGrower,
                                        Supplier<WoodType> woodType)
    {
        BlockRegisterar blockRegister = new BlockRegisterar(modid);
        ItemRegisterar itemRegister = new ItemRegisterar(modid);
        BlockItemRegisterar blockItemRegister = new BlockItemRegisterar(modid);
        EntityRegisterar entityRegister = new EntityRegisterar(modid);

        var blockSetType = new BlockSetType(BaseHelper.id(modid, name).toString());
        var type = WoodTypeBuilder.copyOf(woodType.get()).register(BaseHelper.id(modid, name), blockSetType);

        WoodsetBlocks blocks = WoodsetBlocks.build(modid, name, blockRegister, blockItemRegister, treeGrower, type, blockSetType);
        WoodsetSignBlocks signs = WoodsetSignBlocks.build(modid, name, blockRegister, blockItemRegister, itemRegister, type);
        WoodsetBoats boats = WoodsetBoats.build(modid, name, itemRegister, entityRegister, blocks.planks().block());
        var logsBlockItemTag = BaseHelper.blockItemTagId(modid, name + "_logs");

        TreeFamily tree = new TreeFamily(modid, name, treeGrower,
                                         blocks, signs, boats,
                                         createBlockFamily(blocks, signs), logsBlockItemTag);

        StrippableBlockRegistry.register(blocks.log().block(), blocks.strippedLog().block());
        StrippableBlockRegistry.register(blocks.wood().block(), blocks.strippedWood().block());

        DispenserBlock.registerBehavior(boats.boatItem().item(), new BoatDispenseItemBehavior(boats.boat().entity()));
        DispenserBlock.registerBehavior(boats.chestBoatItem().item(), new BoatDispenseItemBehavior(boats.chestBoat().entity()));

        addBlocksToBlockEntityType(BlockEntityTypes.SIGN, signs.sign().block(), signs.wallSign().block());
        addBlocksToBlockEntityType(BlockEntityTypes.HANGING_SIGN, signs.hangingSign().block(), signs.wallHangingSign().block());

        TreeFamilySets.get().add(tree);

        return tree;
    }

    /**
     * Creates a {@link BlockFamily} for recipes and data generation based on this wood set.
     *
     * @return A newly built {@link BlockFamily}.
     */
    public BlockFamily createBlockFamily(WoodsetBlocks blocks, WoodsetSignBlocks signs)
    {
        return new BlockFamily.Builder(blocks.planks().block())
                .button(blocks.button().block())
                .fence(blocks.fence().block())
                .fenceGate(blocks.fenceGate().block())
                .pressurePlate(blocks.pressurePlate().block())
                .slab(blocks.slab().block())
                .stairs(blocks.stairs().block())
                .door(blocks.door().block())
                .trapdoor(blocks.trapdoor().block())
                .sign(signs.sign().block(), signs.wallSign().block())
                .hangingSign(signs.hangingSign().block(), signs.wallHangingSign().block())
                .recipeGroupPrefix("wooden")
                .recipeUnlockedBy("has_planks")
                .getFamily();
    }
    /**
     * Adds blocks to an existing {@link BlockEntityType}.
     *
     * @param blockEntityType The target block entity type.
     * @param blocks The blocks to add.
     */
    static void addBlocksToBlockEntityType(BlockEntityType<?> blockEntityType, Block... blocks)
    {
        addBlocksToBlockEntityType(blockEntityType, Arrays.asList(blocks));
    }

    /**
     * Adds a collection of blocks to an existing {@link BlockEntityType}.
     *
     * @param blockEntityType The target block entity type.
     * @param blocks The collection of blocks to add.
     */
    static void addBlocksToBlockEntityType(BlockEntityType<?> blockEntityType, Collection<Block> blocks)
    {
        BlockEntityTypeAccessor accessor = (BlockEntityTypeAccessor) blockEntityType;
        Set<Block> originalBlocks = accessor.getBlocks();
        accessor.setBlocks(ImmutableSet.<Block>builderWithExpectedSize(originalBlocks.size() + blocks.size())
                                    .addAll(originalBlocks)
                                    .addAll(blocks)
                                    .build());
    }
}