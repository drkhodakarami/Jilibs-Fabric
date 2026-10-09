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

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableSet;
import net.fabricmc.fabric.api.object.builder.v1.block.type.WoodTypeBuilder;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import org.jetbrains.annotations.NotNull;

import net.minecraft.core.dispenser.BoatDispenseItemBehavior;
import net.minecraft.data.BlockFamily;
import net.minecraft.tags.BlockItemTagId;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.entity.vehicle.boat.ChestBoat;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityTypes;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
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
 * WoodSet woodset = new WoodSet
 * 				.Builder("test_mod_id", "rubber", ModSaplings.RUBBER)
 *             .build();
 *
 * //getting access to parts:
 * woodset.blocks.planks ...
 * woodset.signs.wallSign ...
 * woodset.signs.items.hangingSignItem ...
 * woodset.boats.chestBoatEntityType ...
 * woodset.treeGrower ... //TreeGrower
 * woodset.woodType ...
 * woodset.blockSetType ...
 * woodset.logsBlockItemTag ...
 * }
 * </pre>
 */
@SuppressWarnings("unused")
@Developer("TurtyWurty")
@CreatedAt("2026-08-10")
@ModifiedAt("2026-08-10")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public class WoodSet
{
    /** The unique name of the wood set. */
    private final String name;
    /** The tree grower associated with the wood set's sapling. */
    private final TreeGrower treeGrower;

    /** The custom {@link WoodType} registered for this wood set. */
    public final WoodType woodType;
    /** The {@link BlockSetType} associated with these blocks. */
    public final BlockSetType blockSetType;

    /** The tag ID for identifying logs in this wood set. */
    public final BlockItemTagId logsBlockItemTag;

    /** Container for all blocks within this wood set. */
    public final WoodsetBlocks blocks;
    /** Container for all sign-related blocks within this wood set. */
    public final WoodsetSignBlocks signs;
    /** Container for all boat-related entities and items within this wood set. */
    public final WoodsetBoats boats;

    /**
     * Constructs the wood set and registers all its components.
     *
     * @param modid The mod ID for registration.
     * @param name The base name for the wood type, used in block naming.
     * @param treeGrower The {@link TreeGrower} for the tree type.
     * @param woodType The base {@link WoodType} to use.
     * @param planks The factory for planks.
     * @param log The factory for logs.
     * @param strippedLog The factory for stripped logs.
     * @param strippedWood The factory for stripped wood.
     * @param wood The factory for wood.
     * @param leaves The factory for leaves.
     * @param sapling The factory for saplings.
     * @param stairs The factory for stairs.
     * @param slab The factory for slabs.
     * @param fence The factory for fences.
     * @param fenceGate The factory for fence gates.
     * @param door The factory for doors.
     * @param trapdoor The factory for trapdoors.
     * @param pressurePlate The factory for pressure plates.
     * @param button The factory for buttons.
     * @param sign The factory for standing signs.
     * @param wallSign The factory for wall signs.
     * @param hangingSign The factory for hanging signs.
     * @param wallHangingSign The factory for wall hanging signs.
     * @param signItem The factory for sign items.
     * @param hangingSignItem The factory for hanging sign items.
     * @param boatEntityType The factory for boat entity builder.
     * @param chestBoatEntityType The factory for chest boat entity builder.
     * @param boatItem The factory for boat item.
     * @param chestBoatItem The factory for chest boat item.
     */
    public WoodSet(String modid, String name,
                TreeGrower treeGrower,
                Supplier<WoodType> woodType,
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
                Function<BlockBehaviour.Properties, ButtonBlock> button,
                Function<BlockBehaviour.Properties, StandingSignBlock> sign,
                Function<BlockBehaviour.Properties, WallSignBlock> wallSign,
                Function<BlockBehaviour.Properties, CeilingHangingSignBlock> hangingSign,
                Function<BlockBehaviour.Properties, WallHangingSignBlock> wallHangingSign,
                Function<Item.Properties, SignItem> signItem,
                Function<Item.Properties, HangingSignItem> hangingSignItem,
                Function<Supplier<Item>, EntityType.Builder<@NotNull Boat>> boatEntityType,
                Function<Supplier<Item>, EntityType.Builder<@NotNull ChestBoat>> chestBoatEntityType,
                Function<Item.Properties, Item> boatItem,
                Function<Item.Properties, Item> chestBoatItem)
    {
        BlockRegisterar blockRegister = new BlockRegisterar(modid);
        ItemRegisterar itemRegister = new ItemRegisterar(modid);
        BlockItemRegisterar blockItemRegister = new BlockItemRegisterar(modid);
        EntityRegisterar entityRegister = new EntityRegisterar(modid);

        this.name = name;
        this.treeGrower = treeGrower;

        this.blockSetType = new BlockSetType(BaseHelper.id(modid, this.name).toString());

        this.woodType = WoodTypeBuilder.copyOf(woodType.get()).register(BaseHelper.id(modid, this.name), this.blockSetType);

        blocks = new WoodsetBlocks(modid, name, blockRegister, blockItemRegister, treeGrower, woodType, blockSetType, planks, log,
                                strippedLog, strippedWood, wood, leaves, sapling, stairs, slab, fence, fenceGate, door, trapdoor, pressurePlate,
                                button);

        signs = new WoodsetSignBlocks(modid, name, blockRegister, blockItemRegister, itemRegister, woodType,
                                    sign, wallSign, hangingSign, wallHangingSign, signItem, hangingSignItem);

        boats = new WoodsetBoats(modid, name, itemRegister, entityRegister, blocks.planks, boatEntityType, chestBoatEntityType, boatItem, chestBoatItem);

        StrippableBlockRegistry.register(blocks.log, blocks.strippedLog);
        StrippableBlockRegistry.register(blocks.wood, blocks.strippedWood);

        this.logsBlockItemTag = BaseHelper.blockItemTagId(modid, this.name + "_logs");

        DispenserBlock.registerBehavior(boats.boatItem, new BoatDispenseItemBehavior(boats.boatEntityType));
        DispenserBlock.registerBehavior(boats.chestBoatItem, new BoatDispenseItemBehavior(boats.chestBoatEntityType));

        addBlocksToBlockEntityType(BlockEntityTypes.SIGN, signs.sign, signs.wallSign);
        addBlocksToBlockEntityType(BlockEntityTypes.HANGING_SIGN, signs.hangingSign, signs.wallHangingSign);

        WoodSets.get().add(this);
    }

    /**
     * Creates a {@link BlockFamily} for recipes and data generation based on this wood set.
     *
     * @return A newly built {@link BlockFamily}.
     */
    public BlockFamily createBlockFamily()
    {
        return new BlockFamily.Builder(blocks.planks)
                .button(blocks.button)
                .fence(blocks.fence)
                .fenceGate(blocks.fenceGate)
                .pressurePlate(blocks.pressurePlate)
                .slab(blocks.slab)
                .stairs(blocks.stairs)
                .door(blocks.door)
                .trapdoor(blocks.trapdoor)
                .sign(signs.sign, signs.wallSign)
                .hangingSign(signs.hangingSign, signs.wallHangingSign)
                .recipeGroupPrefix("wooden")
                .recipeUnlockedBy("has_planks")
                .getFamily();
    }

    /** @return The name of this wood set. */
    public String getName()
    {
        return this.name;
    }

    /** @return The {@link TreeGrower} for this wood set's tree type. */
    public TreeGrower getSaplingGenerator()
    {
        return this.treeGrower;
    }

    /**
     * Returns a string representation of this wood set.
     *
     * @return string representation containing the name and tree grower
     */
    @Override
    public String toString()
    {
        return "JiWoodSet[name=" + name + ", treeGrower=" + this.treeGrower + "]";
    }

    /**
     * Checks equality between this wood set and another object.
     *
     * @param obj the object to compare against
     * @return {@code true} if equal, {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        var that = (WoodSet) obj;
        return name.equals(that.name) && treeGrower.equals(that.treeGrower);
    }

    /**
     * Computes the hash code for this wood set based on name and tree grower.
     *
     * @return the computed hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(name, treeGrower);
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

    /**
     * Builder class for {@link WoodSet}.
     *
     * <p>Provides a fluent API to configure and register a new wood set.</p>
     */
    public static class Builder
    {
        /**
         * The mod identifier for registration.
         */
        private final String modid;

        /**
         * The base name of the wood set.
         */
        private final String name;

        /**
         * The tree grower for sapling generation.
         */
        private final TreeGrower treeGrower;

        /**
         * Supplier providing the base wood type.
         */
        private Supplier<WoodType> woodType = () -> WoodType.OAK;

        /**
         * Factory function for creating planks.
         */
        private Function<BlockBehaviour.Properties, Block> planks;

        /**
         * Factory function for creating leaves.
         */
        private Function<BlockBehaviour.Properties, Block> leaves;

        /**
         * Factory function for creating log blocks.
         */
        private Function<BlockBehaviour.Properties, RotatedPillarBlock> log;

        /**
         * Factory function for creating stripped log blocks.
         */
        private Function<BlockBehaviour.Properties, RotatedPillarBlock> strippedLog;

        /**
         * Factory function for creating wood blocks.
         */
        private Function<BlockBehaviour.Properties, RotatedPillarBlock> wood;

        /**
         * Factory function for creating stripped wood blocks.
         */
        private Function<BlockBehaviour.Properties, RotatedPillarBlock> strippedWood;

        /**
         * Factory function for creating sapling blocks.
         */
        private Function<BlockBehaviour.Properties, SaplingBlock> sapling;

        /**
         * Factory function for creating stairs blocks.
         */
        private Function<BlockBehaviour.Properties, StairBlock> stairs;

        /**
         * Factory function for creating slab blocks.
         */
        private Function<BlockBehaviour.Properties, SlabBlock> slab;

        /**
         * Factory function for creating fence blocks.
         */
        private Function<BlockBehaviour.Properties, FenceBlock> fence;

        /**
         * Factory function for creating fence gate blocks.
         */
        private Function<BlockBehaviour.Properties, FenceGateBlock> fenceGate;

        /**
         * Factory function for creating door blocks.
         */
        private Function<BlockBehaviour.Properties, DoorBlock> door;

        /**
         * Factory function for creating trapdoor blocks.
         */
        private Function<BlockBehaviour.Properties, TrapDoorBlock> trapdoor;

        /**
         * Factory function for creating pressure plate blocks.
         */
        private Function<BlockBehaviour.Properties, PressurePlateBlock> pressurePlate;

        /**
         * Factory function for creating button blocks.
         */
        private Function<BlockBehaviour.Properties, ButtonBlock> button;

        /**
         * Factory function for creating standing sign blocks.
         */
        private Function<BlockBehaviour.Properties, StandingSignBlock> sign;

        /**
         * Factory function for creating wall sign blocks.
         */
        private Function<BlockBehaviour.Properties, WallSignBlock> wallSign;

        /**
         * Factory function for creating ceiling hanging sign blocks.
         */
        private Function<BlockBehaviour.Properties, CeilingHangingSignBlock> hangingSign;

        /**
         * Factory function for creating wall hanging sign blocks.
         */
        private Function<BlockBehaviour.Properties, WallHangingSignBlock> wallHangingSign;

        /**
         * Factory function for creating sign items.
         */
        private Function<Item.Properties, SignItem> signItem;

        /**
         * Factory function for creating hanging sign items.
         */
        private Function<Item.Properties, HangingSignItem> hangingSignItem;

        /**
         * Factory function for building boat entity types.
         */
        private Function<Supplier<Item>, EntityType.Builder<@NotNull Boat>> boatType;

        /**
         * Factory function for building chest boat entity types.
         */
        private Function<Supplier<Item>, EntityType.Builder<@NotNull ChestBoat>> chestBoatType;

        /**
         * Factory function for creating boat items.
         */
        private Function<Item.Properties, Item> boatItem;

        /**
         * Factory function for creating chest boat items.
         */
        private Function<Item.Properties, Item> chestBoatItem;

        /**
         * Creates a new Builder instance.
         *
         * @param modid The mod ID for registration.
         * @param name The name of the wood set.
         * @param treeGrower The sapling grower.
         */
        public Builder(String modid, String name, TreeGrower treeGrower)
        {
            this.modid = modid;
            this.name = name;
            this.treeGrower = treeGrower;
        }

        //region Main SET

        /**
         * Example call :
         * <pre><code>
         * public class SomeWood extends BaseWoodBlock
         * {...}
         *
         * WoodSet woodset = new WoodSet.Builder(moid, name, treeGrower)
         *     .set(SomeWood::new, Block::new)
         *     .build();
         * </code></pre>
         * @param woodFactory The factory for logs and wood types.
         * @param leaves The factory for leaves.
         * @return This builder instance for chaining.
         */
        public Builder set(BiFunction<BlockBehaviour.Properties, Boolean, RotatedPillarBlock> woodFactory,
                        Function<BlockBehaviour.Properties, Block> leaves)
        {
            this.log = settings -> woodFactory.apply(settings, false);
            this.strippedLog = settings -> woodFactory.apply(settings, true);
            this.wood = settings -> woodFactory.apply(settings, false);
            this.strippedWood = settings -> woodFactory.apply(settings, true);
            this.leaves = leaves;
            return this;
        }

        /**
         * Example call :
         * <pre><code>
         * public class SomeWood extends BaseWoodBlock
         * {...}
         *
         * WoodSet woodset = new WoodSet.Builder(moid, name, treeGrower)
         *     .set(SomeWood::new, SomeLeave::new, () -> WoodType.OAK)
         *     .build();
         * </code></pre>
         * @param woodFactory The factory for logs and wood types.
         * @param leaves The factory for leaves.
         * @param woodType The {@link WoodType} supplier.
         * @return This builder instance for chaining.
         */
        public Builder set(BiFunction<BlockBehaviour.Properties, Boolean, RotatedPillarBlock> woodFactory,
                        Function<BlockBehaviour.Properties, Block> leaves,
                        Supplier<WoodType> woodType)
        {
            this.woodType = woodType;
            return set(woodFactory, leaves);
        }

        /**
         * Example call :
         * <pre><code>
         * public class SomeWood extends BaseWoodBlock
         * {...}
         *
         * WoodSet woodset = new WoodSet.Builder(moid, name, treeGrower)
         *     .set(SomeWood::new, Block::new, Block::new)
         *     .build();
         * </code></pre>
         * @param woodFactory The factory for logs and wood types.
         * @param leaves The factory for leaves.
         * @param planks The factory for planks.
         * @return This builder instance for chaining.
         */
        public Builder set(BiFunction<BlockBehaviour.Properties, Boolean, RotatedPillarBlock> woodFactory,
                        Function<BlockBehaviour.Properties, Block> leaves,
                        Function<BlockBehaviour.Properties, Block> planks)
        {
            this.planks = planks;
            return set(woodFactory, leaves);
        }

        /**
         * Example call :
         * <pre><code>
         * public class SomeWood extends BaseWoodBlock
         * {...}
         *
         * WoodSet woodset = new WoodSet.Builder(moid, name, treeGrower)
         *     .set(SomeWood::new, Block::new, Block::new, () -> WoodType.OAK)
         *     .build();
         * </code></pre>
         * @param woodFactory The factory for logs and wood types.
         * @param leaves The factory for leaves.
         * @param planks The factory for planks.
         * @param woodType The {@link WoodType} supplier.
         * @return This builder instance for chaining.
         */
        public Builder set(BiFunction<BlockBehaviour.Properties, Boolean, RotatedPillarBlock> woodFactory,
                        Function<BlockBehaviour.Properties, Block> leaves,
                        Function<BlockBehaviour.Properties, Block> planks,
                        Supplier<WoodType> woodType)
        {
            this.woodType = woodType;
            this.planks = planks;
            return set(woodFactory, leaves);
        }

        /**
         * Example call :
         * <pre><code>
         * public class SomeWood extends BaseWoodBlock
         * {...}
         *
         * WoodSet woodset = new WoodSet.Builder(moid, name, treeGrower)
         *     .set(SomeWood::new, SomeLeave::new, Block::new, Block::new, () -> WoodType.OAK)
         *     .build();
         * </code></pre>
         * @param woodFactory The factory for logs and wood types.
         * @param leaves The factory for leaves.
         * @param planks The factory for planks.
         * @param sapling The factory for saplings.
         * @param woodType The {@link WoodType} supplier.
         * @return This builder instance for chaining.
         */
        public Builder set(BiFunction<BlockBehaviour.Properties, Boolean, RotatedPillarBlock> woodFactory,
                        Function<BlockBehaviour.Properties, Block> leaves,
                        Function<BlockBehaviour.Properties, Block> planks,
                        Function<BlockBehaviour.Properties, SaplingBlock> sapling,
                        Supplier<WoodType> woodType)
        {
            this.sapling = sapling;
            this.woodType = woodType;
            this.planks = planks;
            return set(woodFactory, leaves);
        }

        //endregion

        //region special Setters

        /**
         * Example call :
         * <pre><code>
         * WoodSet woodset = new WoodSet.Builder(moid, name, treeGrower)
         *     .woodType(() -> WoodType.OAK)
         *     .build();
         * </code></pre>
         * @param woodType The {@link WoodType} supplier.
         * @return This builder instance for chaining.
         */
        public Builder woodType(Supplier<WoodType> woodType)
        {
            this.woodType = woodType;

            return this;
        }

        /**
         * Example call :
         * <pre><code>
         * WoodSet woodset = new WoodSet.Builder(moid, name, treeGrower)
         *     .plank(Block::new)
         *     .build();
         * </code></pre>
         * @param planks The factory for planks.
         * @return This builder instance for chaining.
         */
        public Builder planks(Function<BlockBehaviour.Properties, Block> planks)
        {
            this.planks = planks;
            return this;
        }

        /**
         * Example call :
         * <pre><code>
         * public class SomeLog extends BaseWoodBlock
         * {...}
         *
         * WoodSet woodset = new WoodSet.Builder(moid, name, treeGrower)
         *     .log(SomeLog::new)
         *     .build();
         * </code></pre>
         * @param factory The factory for logs.
         * @return This builder instance for chaining.
         */
        public Builder log(BiFunction<BlockBehaviour.Properties, Boolean, RotatedPillarBlock> factory)
        {
            this.log = settings -> factory.apply(settings, false);
            this.strippedLog = settings -> factory.apply(settings, true);
            return this;
        }

        /**
         * Example call :
         * <pre><code>
         * WoodSet woodset = new WoodSet.Builder(moid, name, treeGrower)
         *     .log(RotatedPillarBlock::new, RotatedPillarBlock::new)
         *     .build();
         * </code></pre>
         * @param log The factory for logs.
         * @param strippedLog The factory for stripped logs.
         * @return This builder instance for chaining.
         */
        public Builder log(Function<BlockBehaviour.Properties, RotatedPillarBlock> log, Function<BlockBehaviour.Properties, RotatedPillarBlock> strippedLog)
        {
            this.log = log;
            this.strippedLog = strippedLog;
            return this;
        }

        /**
         * Example call :
         * <pre><code>
         * WoodSet woodset = new WoodSet.Builder(moid, name, treeGrower)
         *     .log(RotatedPillarBlock::new)
         *     .build();
         * </code></pre>
         * @param log The factory for logs.
         * @return This builder instance for chaining.
         */
        public Builder log(Function<BlockBehaviour.Properties, RotatedPillarBlock> log)
        {
            this.log = log;
            return this;
        }

        /**
         * Example call :
         * <pre><code>
         * WoodSet woodset = new WoodSet.Builder(moid, name, treeGrower)
         *     .strippedLog(RotatedPillarBlock::new)
         *     .build();
         * </code></pre>
         * @param strippedLog The factory for stripped logs.
         * @return This builder instance for chaining.
         */
        public Builder strippedLog(Function<BlockBehaviour.Properties, RotatedPillarBlock> strippedLog)
        {
            this.strippedLog = strippedLog;
            return this;
        }

        /**
         * Example call :
         * <pre><code>
         * WoodSet woodset = new WoodSet.Builder(moid, name, treeGrower)
         *     .wood(RotatedPillarBlock::new)
         *     .build();
         * </code></pre>
         * @param factory The factory for wood.
         * @return This builder instance for chaining.
         */
        public Builder wood(BiFunction<BlockBehaviour.Properties, Boolean, RotatedPillarBlock> factory)
        {
            this.wood = settings -> factory.apply(settings, false);
            this.strippedWood = settings -> factory.apply(settings, true);
            return this;
        }

        /**
         * Example call :
         * <pre><code>
         * WoodSet woodset = new WoodSet.Builder(moid, name, treeGrower)
         *     .wood(RotatedPillarBlock::new, RotatedPillarBlock::new)
         *     .build();
         * </code></pre>
         * @param wood The factory for wood.
         * @param strippedWood The factory for stripped wood.
         * @return This builder instance for chaining.
         */
        public Builder wood(Function<BlockBehaviour.Properties, RotatedPillarBlock> wood, Function<BlockBehaviour.Properties, RotatedPillarBlock> strippedWood)
        {
            this.wood = wood;
            this.strippedWood = strippedWood;
            return this;
        }

        /**
         * Example call :
         * <pre><code>
         * WoodSet woodset = new WoodSet.Builder(moid, name, treeGrower)
         *     .strippedWood(RotatedPillarBlock::new)
         *     .build();
         * </code></pre>
         * @param strippedWood The factory for stripped wood.
         * @return This builder instance for chaining.
         */
        public Builder strippedWood(Function<BlockBehaviour.Properties, RotatedPillarBlock> strippedWood)
        {
            this.strippedWood = strippedWood;
            return this;
        }

        /**
         * Example call :
         * <pre><code>
         * WoodSet woodset = new WoodSet.Builder(moid, name, treeGrower)
         *     .wood(RotatedPillarBlock::new)
         *     .build();
         * </code></pre>
         * @param wood The factory for wood.
         * @return This builder instance for chaining.
         */
        public Builder wood(Function<BlockBehaviour.Properties, RotatedPillarBlock> wood)
        {
            this.wood = wood;
            return this;
        }

        /**
         * Example call :
         * <pre><code>
         * WoodSet woodset = new WoodSet.Builder(moid, name, treeGrower)
         *     .leaves(Block::new)
         *     .build();
         * </code></pre>
         * @param leaves The factory for leaves.
         * @return This builder instance for chaining.
         */
        public Builder leaves(Function<BlockBehaviour.Properties, Block> leaves)
        {
            this.leaves = leaves;
            return this;
        }

        /**
         * Example call :
         * <pre><code>
         * WoodSet woodset = new WoodSet.Builder(moid, name, treeGrower)
         *     .sapling(SaplingBlock::new)
         *     .build();
         * </code></pre>
         * @param sapling The factory for saplings.
         * @return This builder instance for chaining.
         */
        public Builder sapling(Function<BlockBehaviour.Properties, SaplingBlock> sapling)
        {
            this.sapling = sapling;
            return this;
        }

        /**
         * Example call :
         * <pre><code>
         * WoodSet woodset = new WoodSet.Builder(moid, name, treeGrower)
         *     .stairs(StairBlock::new)
         *     .build();
         * </code></pre>
         * @param stairs The factory for stairs.
         * @return This builder instance for chaining.
         */
        public Builder stairs(Function<BlockBehaviour.Properties, StairBlock> stairs)
        {
            this.stairs = stairs;
            return this;
        }

        /**
         * Example call :
         * <pre><code>
         * WoodSet woodset = new WoodSet.Builder(moid, name, treeGrower)
         *     .slab(SlabBlock::new)
         *     .build();
         * </code></pre>
         * @param slab The factory for slabs.
         * @return This builder instance for chaining.
         */
        public Builder slab(Function<BlockBehaviour.Properties, SlabBlock> slab)
        {
            this.slab = slab;
            return this;
        }

        /**
         * Example call :
         * <pre><code>
         * WoodSet woodset = new WoodSet.Builder(moid, name, treeGrower)
         *     .fence(FenceBlock::new)
         *     .build();
         * </code></pre>
         * @param fence The factory for fences.
         * @return This builder instance for chaining.
         */
        public Builder fence(Function<BlockBehaviour.Properties, FenceBlock> fence)
        {
            this.fence = fence;
            return this;
        }

        /**
         * Example call :
         * <pre><code>
         * WoodSet woodset = new WoodSet.Builder(moid, name, treeGrower)
         *     .fenceGate(FenceGateBlock::new)
         *     .build();
         * </code></pre>
         * @param fenceGate The factory for fence gates.
         * @return This builder instance for chaining.
         */
        public Builder fenceGate(Function<BlockBehaviour.Properties, FenceGateBlock> fenceGate)
        {
            this.fenceGate = fenceGate;
            return this;
        }

        /**
         * Example call :
         * <pre><code>
         * WoodSet woodset = new WoodSet.Builder(moid, name, treeGrower)
         *     .door(DoorBlock::new)
         *     .build();
         * </code></pre>
         * @param door The factory for doors.
         * @return This builder instance for chaining.
         */
        public Builder door(Function<BlockBehaviour.Properties, DoorBlock> door)
        {
            this.door = door;
            return this;
        }

        /**
         * Example call :
         * <pre><code>
         * WoodSet woodset = new WoodSet.Builder(moid, name, treeGrower)
         *     .trapdoor(TrapdoorBlock::new)
         *     .build();
         * </code></pre>
         * @param trapdoor The factory for trapdoors.
         * @return This builder instance for chaining.
         */
        public Builder trapdoor(Function<BlockBehaviour.Properties, TrapDoorBlock> trapdoor)
        {
            this.trapdoor = trapdoor;
            return this;
        }

        /**
         * Example call :
         * <pre><code>
         * WoodSet woodset = new WoodSet.Builder(moid, name, treeGrower)
         *     .pressurePlate(PressurePlateBlock::new)
         *     .build();
         * </code></pre>
         * @param pressurePlate The factory for pressure plates.
         * @return This builder instance for chaining.
         */
        public Builder pressurePlate(Function<BlockBehaviour.Properties, PressurePlateBlock> pressurePlate)
        {
            this.pressurePlate = pressurePlate;
            return this;
        }

        /**
         * Example call :
         * <pre><code>
         * WoodSet woodset = new WoodSet.Builder(moid, name, treeGrower)
         *     .button(ButtonBlock::new)
         *     .build();
         * </code></pre>
         * @param button The factory for buttons.
         * @return This builder instance for chaining.
         */
        public Builder button(Function<BlockBehaviour.Properties, ButtonBlock> button)
        {
            this.button = button;
            return this;
        }

        /**
         * Example call :
         * <pre><code>
         * WoodSet woodset = new WoodSet.Builder(moid, name, treeGrower)
         *     .sign(SignBlock::new, WallSignBlock::new, CeilingHangingSignBlock::new, WallHangingSignBlock::new)
         *     .build();
         * </code></pre>
         * @param sign The factory for standing signs.
         * @param wallSign The factory for wall signs.
         * @param hangingSign The factory for hanging signs.
         * @param wallHangingSign The factory for wall hanging signs.
         * @return This builder instance for chaining.
         */
        public Builder sign(Function<BlockBehaviour.Properties, StandingSignBlock> sign,
                            Function<BlockBehaviour.Properties, WallSignBlock> wallSign,
                            Function<BlockBehaviour.Properties, CeilingHangingSignBlock> hangingSign,
                            Function<BlockBehaviour.Properties, WallHangingSignBlock> wallHangingSign)
        {
            this.sign = sign;
            this.wallSign = wallSign;
            this.hangingSign = hangingSign;
            this.wallHangingSign = wallHangingSign;
            return this;
        }

        /**
         * Example call :
         * <pre><code>
         * WoodSet woodset = new WoodSet.Builder(moid, name, treeGrower)
         *     .sign(SignBlock::new, WallSignBlock::new)
         *     .build();
         * </code></pre>
         * @param sign The factory for standing signs.
         * @param wallSign The factory for wall signs.
         * @return This builder instance for chaining.
         */
        public Builder sign(Function<BlockBehaviour.Properties, StandingSignBlock> sign,
                            Function<BlockBehaviour.Properties, WallSignBlock> wallSign)
        {
            this.sign = sign;
            this.wallSign = wallSign;
            return this;
        }

        /**
         * Example call :
         * <pre><code>
         * WoodSet woodset = new WoodSet.Builder(moid, name, treeGrower)
         *     .sign(SignBlock::new)
         *     .build();
         * </code></pre>
         * @param sign The factory for standing signs.
         * @return This builder instance for chaining.
         */
        public Builder sign(Function<BlockBehaviour.Properties, StandingSignBlock> sign)
        {
            this.sign = sign;
            return this;
        }

        /**
         * Example call :
         * <pre><code>
         * WoodSet woodset = new WoodSet.Builder(moid, name, treeGrower)
         *     .wallSign(WallSignBlock::new)
         *     .build();
         * </code></pre>
         * @param wallSign The factory for wall signs.
         * @return This builder instance for chaining.
         */
        public Builder wallSign(Function<BlockBehaviour.Properties, WallSignBlock> wallSign)
        {
            this.wallSign = wallSign;
            return this;
        }

        /**
         * Example call :
         * <pre><code>
         * WoodSet woodset = new WoodSet.Builder(moid, name, treeGrower)
         *     .hangingSign(CeilingHangingSignBlock::new)
         *     .build();
         * </code></pre>
         * @param hangingSign The factory for hanging signs.
         * @return This builder instance for chaining.
         */
        public Builder hangingSign(Function<BlockBehaviour.Properties, CeilingHangingSignBlock> hangingSign)
        {
            this.hangingSign = hangingSign;
            return this;
        }

        /**
         * Example call :
         * <pre><code>
         * WoodSet woodset = new WoodSet.Builder(moid, name, treeGrower)
         *     .wallHangingSign(WallHangingSignBlock::new)
         *     .build();
         * </code></pre>
         * @param wallHangingSign The factory for wall hanging signs.
         * @return This builder instance for chaining.
         */
        public Builder wallHangingSign(Function<BlockBehaviour.Properties, WallHangingSignBlock> wallHangingSign)
        {
            this.wallHangingSign = wallHangingSign;
            return this;
        }

        /**
         * Example call :
         * <pre><code>
         * WoodSet woodset = new WoodSet.Builder(moid, name, treeGrower)
         *     .hangingSign(CeilingHangingSignBlock::new, WallHangingSignBlock::new)
         *     .build();
         * </code></pre>
         * @param hangingSign The factory for hanging signs.
         * @param wallHangingSign The factory for wall hanging signs.
         * @return This builder instance for chaining.
         */
        public Builder hangingSign(Function<BlockBehaviour.Properties, CeilingHangingSignBlock> hangingSign,
                                Function<BlockBehaviour.Properties, WallHangingSignBlock> wallHangingSign)
        {
            this.hangingSign = hangingSign;
            this.wallHangingSign = wallHangingSign;
            return this;
        }

        /**
         * Example call :
         * <pre><code>
         * WoodSet woodset = new WoodSet.Builder(moid, name, treeGrower)
         *     .signItem(SignItem::new)
         *     .build();
         * </code></pre>
         * @param signItem The factory for sign items.
         * @return This builder instance for chaining.
         */
        public Builder signItem(Function<Item.Properties, SignItem> signItem)
        {
            this.signItem = signItem;
            return this;
        }

        /**
         * Example call :
         * <pre><code>
         * WoodSet woodset = new WoodSet.Builder(moid, name, treeGrower)
         *     .hangingSignItem(HangingSignItem::new)
         *     .build();
         * </code></pre>
         * @param hangingSignItem The factory for hanging sign items.
         * @return This builder instance for chaining.
         */
        public Builder hangingSignItem(Function<Item.Properties, HangingSignItem> hangingSignItem)
        {
            this.hangingSignItem = hangingSignItem;
            return this;
        }

        /**
         * Example call :
         * <pre><code>
         * WoodSet woodset = new WoodSet.Builder(moid, name, treeGrower)
         *     .boatType(Boat::new, ChestBoat::new)
         *     .build();
         * </code></pre>
         * @param boatType The factory for boat entities.
         * @param chestBoatType The factory for chest boat entities.
         * @return This builder instance for chaining.
         */
        public Builder boatType(Function<Supplier<Item>, EntityType.Builder<@NotNull Boat>> boatType,
                                Function<Supplier<Item>, EntityType.Builder<@NotNull ChestBoat>> chestBoatType)
        {
            this.boatType = boatType;
            this.chestBoatType = chestBoatType;
            return this;
        }

        /**
         * Example call :
         * <pre><code>
         * WoodSet woodset = new WoodSet.Builder(moid, name, treeGrower)
         *     .boatType(Boat::new)
         *     .build();
         * </code></pre>
         * @param boatType The factory for boat entities.
         * @return This builder instance for chaining.
         */
        public Builder boatType(Function<Supplier<Item>, EntityType.Builder<@NotNull Boat>> boatType)
        {
            this.boatType = boatType;
            return this;
        }

        /**
         * Example call :
         * <pre><code>
         * WoodSet woodset = new WoodSet.Builder(moid, name, treeGrower)
         *     .chestBoatType(ChestBoat::new)
         *     .build();
         * </code></pre>
         * @param chestBoatType The factory for chest boat entities.
         * @return This builder instance for chaining.
         */
        public Builder chestBoatType(Function<Supplier<Item>, EntityType.Builder<@NotNull ChestBoat>> chestBoatType)
        {
            this.chestBoatType = chestBoatType;
            return this;
        }

        /**
         * Example call :
         * <pre><code>
         * WoodSet woodset = new WoodSet.Builder(moid, name, treeGrower)
         *     .boatItem(BoatItem::new, ChestBoatItem::new)
         *     .build();
         * </code></pre>
         * @param boatItem The factory for boat items.
         * @param chestBoatItem The factory for chest boat items.
         * @return This builder instance for chaining.
         */
        public Builder boatItem(Function<Item.Properties, Item> boatItem,
                                Function<Item.Properties, Item> chestBoatItem)
        {
            this.boatItem = boatItem;
            this.chestBoatItem = chestBoatItem;
            return this;
        }

        /**
         * Example call :
         * <pre><code>
         * WoodSet woodset = new WoodSet.Builder(moid, name, treeGrower)
         *     .boatItem(BoatItem::new)
         *     .build();
         * </code></pre>
         * @param boatItem The factory for boat items.
         * @return This builder instance for chaining.
         */
        public Builder boatItem(Function<Item.Properties, Item> boatItem)
        {
            this.boatItem = boatItem;
            return this;
        }

        /**
         * Finalizes the construction of the {@link WoodSet}.
         *
         * @return A newly built and registered {@link WoodSet}.
         */
        public WoodSet build()
        {
            return new WoodSet(modid, name, treeGrower, woodType, planks, log, strippedLog, strippedWood, wood, leaves, sapling,
                            stairs, slab, fence, fenceGate, door, trapdoor, pressurePlate, button, sign, wallSign, hangingSign,
                            wallHangingSign, signItem, hangingSignItem, boatType, chestBoatType, boatItem, chestBoatItem);
        }
    }
}