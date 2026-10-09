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
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.WoodType;

import com.dynamero.register.BlockItemRegisterar;
import com.dynamero.register.BlockRegisterar;
import com.dynamero.register.ItemRegisterar;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.utils.BaseHelper;

/**
 * Container and registry holder for sign and hanging sign blocks within a {@link WoodSet}.
 */
@SuppressWarnings("unused")
@Developer("TurtyWurty")
@ModifiedBy("The Mentor")
@CreatedAt("2025-04-15")
@Repository("https://github.com/DaRealTurtyWurty/Industria")
@Discord("https://discord.turtywurty.dev/")
@Youtube("https://www.youtube.com/@TurtyWurty")
public class WoodsetSignBlocks
{
    /**
     * Container holding sign items registered for these sign blocks.
     */
    public final WoodsetSignItems items;

    /**
     * Resource key for the standing sign item.
     */
    public final ResourceKey<Item> signItemId;

    /**
     * Resource key for the wall sign item.
     */
    public final ResourceKey<Item> wallSignItemId;

    /**
     * Resource key for the hanging sign item.
     */
    public final ResourceKey<Item> hangingSignItemId;

    /**
     * Resource key for the wall hanging sign item.
     */
    public final ResourceKey<Item> wallHangingSignItemId;

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
     * Block-item identifier pair for the standing sign block and item.
     */
    public final BlockItemId signBlockItemId;

    /**
     * Block-item identifier pair for the wall sign block and item.
     */
    public final BlockItemId wallSignBlockItemId;

    /**
     * Block-item identifier pair for the hanging sign block and item.
     */
    public final BlockItemId hangingSignBlockItemId;

    /**
     * Block-item identifier pair for the wall hanging sign block and item.
     */
    public final BlockItemId wallHangingSignBlockItemId;

    /**
     * Standing sign block instance.
     */
    public final StandingSignBlock sign;

    /**
     * Wall sign block instance.
     */
    public final WallSignBlock wallSign;

    /**
     * Ceiling hanging sign block instance.
     */
    public final CeilingHangingSignBlock hangingSign;

    /**
     * Wall hanging sign block instance.
     */
    public final WallHangingSignBlock wallHangingSign;

    /**
     * Constructs and registers all sign blocks and associated items for a wood set.
     *
     * @param modid             the mod identifier
     * @param name              the base wood name
     * @param blockRegister     the block registerer helper
     * @param blockItemRegister the block-item registerer helper
     * @param itemRegister      the item registerer helper
     * @param woodType          supplier providing the wood type
     * @param sign              factory creating standing sign blocks
     * @param wallSign          factory creating wall sign blocks
     * @param hangingSign       factory creating ceiling hanging sign blocks
     * @param wallHangingSign   factory creating wall hanging sign blocks
     * @param signItem          factory creating sign items
     * @param hangingSignItem   factory creating hanging sign items
     */
    public WoodsetSignBlocks(String modid, String name,
                             BlockRegisterar blockRegister, BlockItemRegisterar blockItemRegister, ItemRegisterar itemRegister,
                             Supplier<WoodType> woodType,
                             Function<BlockBehaviour.Properties, StandingSignBlock> sign,
                             Function<BlockBehaviour.Properties, WallSignBlock> wallSign,
                             Function<BlockBehaviour.Properties, CeilingHangingSignBlock> hangingSign,
                             Function<BlockBehaviour.Properties, WallHangingSignBlock> wallHangingSign,
                             Function<Item.Properties, SignItem> signItem,
                             Function<Item.Properties, HangingSignItem> hangingSignItem)
    {
        signItemId = BaseHelper.ResourceKeys.create(modid, name + "_sign", Registries.ITEM);
        wallSignItemId = BaseHelper.ResourceKeys.create(modid, name + "_wall_sign", Registries.ITEM);
        hangingSignItemId = BaseHelper.ResourceKeys.create(modid, name + "_hanging_sign", Registries.ITEM);
        wallHangingSignItemId = BaseHelper.ResourceKeys.create(modid, name + "_wall_hanging_sign", Registries.ITEM);

        signBlockId = BaseHelper.ResourceKeys.create(modid, name + "_sign", Registries.BLOCK);
        wallSignBlockId = BaseHelper.ResourceKeys.create(modid, name + "_wall_sign", Registries.BLOCK);
        hangingSignBlockId = BaseHelper.ResourceKeys.create(modid, name + "_hanging_sign", Registries.BLOCK);
        wallHangingSignBlockId = BaseHelper.ResourceKeys.create(modid, name + "_wall_hanging_sign", Registries.BLOCK);

        signBlockItemId = new BlockItemId(signBlockId, signItemId);
        wallSignBlockItemId = new BlockItemId(wallSignBlockId, wallSignItemId);
        hangingSignBlockItemId = new BlockItemId(hangingSignBlockId, hangingSignItemId);
        wallHangingSignBlockItemId = new BlockItemId(wallHangingSignBlockId, wallHangingSignItemId);

        //region Register
        this.sign = blockRegister.registerCopy(signBlockItemId, Blocks.OAK_SIGN,
                                            sign == null ? settings -> new StandingSignBlock(woodType.get(), settings) : sign);
        blockItemRegister.register(this.sign, signBlockItemId);

        this.wallSign = blockRegister.registerCopy(wallSignBlockItemId, Blocks.OAK_WALL_SIGN,
                                                wallSign == null ? settings -> new WallSignBlock(woodType.get(), settings) : wallSign);
        blockItemRegister.register(this.wallSign, wallSignBlockItemId);

        this.hangingSign = blockRegister.registerCopy(hangingSignBlockItemId, Blocks.OAK_HANGING_SIGN,
                                                    hangingSign == null ? settings -> new CeilingHangingSignBlock(woodType.get(), settings) : hangingSign);
        blockItemRegister.register(this.hangingSign, hangingSignBlockItemId);

        this.wallHangingSign = blockRegister.registerCopy(wallHangingSignBlockItemId, Blocks.OAK_WALL_HANGING_SIGN,
                                                        wallHangingSign == null ? settings -> new WallHangingSignBlock(woodType.get(), settings) : wallHangingSign);
        blockItemRegister.register(this.wallHangingSign, wallHangingSignBlockItemId);

        this.items = new WoodsetSignItems(modid, name, itemRegister,
                                        this.sign, this.wallSign, this.hangingSign, this.wallHangingSign,
                                        signItem, hangingSignItem);
        //endregion

        //region Burnables
        FlammableBlockRegistry flammableBlockRegistry = FlammableBlockRegistry.getDefaultInstance();
        flammableBlockRegistry.add(this.sign, 5, 20);
        flammableBlockRegistry.add(this.wallSign, 5, 20);
        flammableBlockRegistry.add(this.hangingSign, 5, 20);
        flammableBlockRegistry.add(this.wallHangingSign, 5, 20);
        //endregion
    }
}