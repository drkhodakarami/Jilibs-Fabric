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
import net.minecraft.world.level.block.state.properties.WoodType;

import com.dynamero.registerars.BlockItemRegisterar;
import com.dynamero.registerars.BlockRegisterar;
import com.dynamero.registerars.ItemRegisterar;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.utils.BaseHelper;

/**
 * Container and registry holder for sign and hanging sign blocks within a {@link TreeFamilyBuilder}.
 *
 * @param items Container holding sign items registered for these sign blocks.
 * @param sign Standing sign block instance.
 * @param wallSign Wall sign block instance.
 * @param hangingSign Ceiling hanging sign block instance.
 * @param wallHangingSign Wall hanging sign block instance.
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
public record WoodsetSignBlocks(ModBlock<? extends SignBlock, ?> sign, ModBlock<? extends WallSignBlock, ?> wallSign,
                                ModBlock<? extends CeilingHangingSignBlock, ?> hangingSign, ModBlock<? extends WallHangingSignBlock, ?> wallHangingSign,
                                WoodsetSignItems items)
{
    /**
     * Constructs and registers all sign blocks and associated items for a wood set.
     *
     * @param modid             the mod identifier
     * @param name              the base wood name
     * @param blockRegister     the block registerer helper
     * @param blockItemRegister the block-item registerer helper
     * @param itemRegister      the item registerer helper
     * @param woodType          supplier providing the wood type
     */
    public static WoodsetSignBlocks build(String modid, String name,
                             BlockRegisterar blockRegister, BlockItemRegisterar blockItemRegister, ItemRegisterar itemRegister,
                             WoodType woodType)
    {
        var signItemId = BaseHelper.ResourceKeys.create(modid, name + "_sign", Registries.ITEM);
        var wallSignItemId = BaseHelper.ResourceKeys.create(modid, name + "_wall_sign", Registries.ITEM);
        var hangingSignItemId = BaseHelper.ResourceKeys.create(modid, name + "_hanging_sign", Registries.ITEM);
        var wallHangingSignItemId = BaseHelper.ResourceKeys.create(modid, name + "_wall_hanging_sign", Registries.ITEM);

        var signBlockId = BaseHelper.ResourceKeys.create(modid, name + "_sign", Registries.BLOCK);
        var wallSignBlockId = BaseHelper.ResourceKeys.create(modid, name + "_wall_sign", Registries.BLOCK);
        var hangingSignBlockId = BaseHelper.ResourceKeys.create(modid, name + "_hanging_sign", Registries.BLOCK);
        var wallHangingSignBlockId = BaseHelper.ResourceKeys.create(modid, name + "_wall_hanging_sign", Registries.BLOCK);

        var signBlockItemId = new BlockItemId(signBlockId, signItemId);
        var wallSignBlockItemId = new BlockItemId(wallSignBlockId, wallSignItemId);
        var hangingSignBlockItemId = new BlockItemId(hangingSignBlockId, hangingSignItemId);
        var wallHangingSignBlockItemId = new BlockItemId(wallHangingSignBlockId, wallHangingSignItemId);

        //region Register
        var sign = ModBlock.register(blockRegister, blockItemRegister,
                                      Blocks.OAK_SIGN, signBlockItemId,
                                      settings -> new StandingSignBlock(woodType, settings));

        var wallSign = ModBlock.register(blockRegister, blockItemRegister,
                                          Blocks.OAK_WALL_SIGN, wallSignBlockItemId,
                                          settings -> new WallSignBlock(woodType, settings));

        var hangingSign = ModBlock.register(blockRegister, blockItemRegister,
                                             Blocks.OAK_HANGING_SIGN, hangingSignBlockItemId,
                                             settings -> new CeilingHangingSignBlock(woodType, settings));

        var wallHangingSign = ModBlock.register(blockRegister, blockItemRegister,
                                                 Blocks.OAK_WALL_HANGING_SIGN, wallHangingSignBlockItemId,
                                                 settings -> new WallHangingSignBlock(woodType, settings));

        var items = WoodsetSignItems.build(modid, name, itemRegister,
                                        sign.block(), wallSign.block(),
                                          hangingSign.block(), wallHangingSign.block());
        //endregion

        var blocks = new WoodsetSignBlocks(sign, wallSign, hangingSign, wallHangingSign, items);

        //region Burnables
        FlammableBlockRegistry flammableBlockRegistry = FlammableBlockRegistry.getDefaultInstance();
        flammableBlockRegistry.add(blocks.sign.block(), 5, 20);
        flammableBlockRegistry.add(blocks.wallSign.block(), 5, 20);
        flammableBlockRegistry.add(blocks.hangingSign.block(), 5, 20);
        flammableBlockRegistry.add(blocks.wallHangingSign.block(), 5, 20);
        //endregion

        return blocks;
    }
}