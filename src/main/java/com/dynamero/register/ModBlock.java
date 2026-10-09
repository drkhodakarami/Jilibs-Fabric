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

import java.util.function.Function;

import net.minecraft.core.registries.Registries;
import net.minecraft.references.BlockItemId;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import com.dynamero.register.factory.IBlockItemFactory;
import com.dynamero.shared.utils.BaseHelper;

public record ModBlock<R extends Block, I extends BlockItem>(R block, I blockItem, BlockItemId id)
{
    public static class Builder<R extends Block, I extends BlockItem>
    {
        R block;
        I blockItem;
        BlockItemId blockItemId;

        String name, modid;
        ItemRegisterar itemRegisterar;
        BlockRegisterar blockRegisterar;
        BlockItemRegisterar blockItemRegisterar;

        public Builder(String modid, String name)
        {
            this.name = name;
            this.modid = modid;

            itemRegisterar = new ItemRegisterar(modid);
            blockRegisterar = new BlockRegisterar(modid);
            blockItemRegisterar = new BlockItemRegisterar(modid);
        }

        public Builder<Block, BlockItem> add()
        {
            Builder<Block, BlockItem> next = new Builder<>(modid, name);
            next.blockItemId = new BlockItemId(BaseHelper.ResourceKeys.create(modid, name, Registries.BLOCK),
                                               BaseHelper.ResourceKeys.create(modid, name, Registries.ITEM));
            next.block = blockRegisterar.register(next.blockItemId);
            next.blockItem = blockItemRegisterar.register(block);
            return next;
        }

        public Builder<Block, BlockItem> add(BlockItemId id)
        {
            Builder<Block, BlockItem> next = new Builder<>(modid, name);
            next.blockItemId = id;
            next.block = blockRegisterar.register(id);
            next.blockItem = blockItemRegisterar.register(block);
            return next;
        }

        public <C extends Block> Builder<Block, BlockItem> copy(BlockItemId id, C copyBlock)
        {
            Builder<Block, BlockItem> next = new Builder<>(modid, name);
            next.blockItemId = id;
            next.block = blockRegisterar.registerCopy(id, copyBlock);
            next.blockItem = blockItemRegisterar.register(block);
            return next;
        }

        public Builder<R, BlockItem> add(Function<BlockBehaviour.Properties, R> blockFactory)
        {
            Builder<R, BlockItem> next = new Builder<>(modid, name);
            next.blockItemId = new BlockItemId(BaseHelper.ResourceKeys.create(modid, name, Registries.BLOCK),
                                               BaseHelper.ResourceKeys.create(modid, name, Registries.ITEM));
            next.block = blockRegisterar.register(next.blockItemId, blockFactory);
            next.blockItem = blockItemRegisterar.register(block, BlockItem::new);
            return next;
        }

        public Builder<R, BlockItem> add(BlockItemId id, Function<BlockBehaviour.Properties, R> blockFactory)
        {
            Builder<R, BlockItem> next = new Builder<>(modid, name);
            next.blockItemId = id;
            next.block = blockRegisterar.register(id, blockFactory);
            next.blockItem = blockItemRegisterar.register(block, BlockItem::new);
            return next;
        }

        public <C extends Block> Builder<R, BlockItem> copy(C copyBlock, Function<BlockBehaviour.Properties, R> blockFactory)
        {
            Builder<R, BlockItem> next = new Builder<>(modid, name);
            next.blockItemId = new BlockItemId(BaseHelper.ResourceKeys.create(modid, name, Registries.BLOCK),
                                               BaseHelper.ResourceKeys.create(modid, name, Registries.ITEM));
            next.block = blockRegisterar.registerCopy(next.blockItemId, copyBlock, blockFactory);
            next.blockItem = blockItemRegisterar.register(block, BlockItem::new);
            return next;
        }

        public <C extends Block> Builder<R, BlockItem> copy(BlockItemId id, C copyBlock, Function<BlockBehaviour.Properties, R> blockFactory)
        {
            Builder<R, BlockItem> next = new Builder<>(modid, name);
            next.blockItemId = id;
            next.block = blockRegisterar.registerCopy(id, copyBlock, blockFactory);
            next.blockItem = blockItemRegisterar.register(block, BlockItem::new);
            return next;
        }

        public Builder<R, BlockItem> add(BlockBehaviour.Properties settings, Function<BlockBehaviour.Properties, R> blockFactory)
        {
            Builder<R, BlockItem> next = new Builder<>(modid, name);
            next.blockItemId = new BlockItemId(BaseHelper.ResourceKeys.create(modid, name, Registries.BLOCK),
                                               BaseHelper.ResourceKeys.create(modid, name, Registries.ITEM));
            next.block = blockRegisterar.register(next.blockItemId, settings, blockFactory);
            next.blockItem = blockItemRegisterar.register(block, BlockItem::new);
            return next;
        }

        public Builder<R, BlockItem> add(BlockItemId id, BlockBehaviour.Properties settings, Function<BlockBehaviour.Properties, R> blockFactory)
        {
            Builder<R, BlockItem> next = new Builder<>(modid, name);
            next.blockItemId = id;
            next.block = blockRegisterar.register(id, settings, blockFactory);
            next.blockItem = blockItemRegisterar.register(block, BlockItem::new);
            return next;
        }

        public Builder<Block, I> add(I blockItem)
        {
            Builder<Block, I> next = new Builder<>(modid, name);
            next.blockItemId = new BlockItemId(BaseHelper.ResourceKeys.create(modid, name, Registries.BLOCK),
                                               BaseHelper.ResourceKeys.create(modid, name, Registries.ITEM));
            next.block = blockRegisterar.register(next.blockItemId);
            next.blockItem = blockItem;
            return next;
        }

        public Builder<Block, I> add(BlockItemId id, I blockItem)
        {
            Builder<Block, I> next = new Builder<>(modid, name);
            next.blockItemId = id;
            next.block = blockRegisterar.register(id);
            next.blockItem = blockItem;
            return next;
        }

        public Builder<R, I> add(I blockItem, Function<BlockBehaviour.Properties, R> blockFactory)
        {
            Builder<R, I> next = new Builder<>(modid, name);
            next.blockItemId = new BlockItemId(BaseHelper.ResourceKeys.create(modid, name, Registries.BLOCK),
                                               BaseHelper.ResourceKeys.create(modid, name, Registries.ITEM));
            next.block = blockRegisterar.register(next.blockItemId, blockFactory);
            next.blockItem = blockItem;
            return next;
        }

        public Builder<R, I> add(BlockItemId id, I blockItem, Function<BlockBehaviour.Properties, R> blockFactory)
        {
            Builder<R, I> next = new Builder<>(modid, name);
            next.blockItemId = id;
            next.block = blockRegisterar.register(id, blockFactory);
            next.blockItem = blockItem;
            return next;
        }

        public <C extends Block> Builder<R, I> copy(C copyBlock, I blockItem, Function<BlockBehaviour.Properties, R> blockFactory)
        {
            Builder<R, I> next = new Builder<>(modid, name);
            next.blockItemId = new BlockItemId(BaseHelper.ResourceKeys.create(modid, name, Registries.BLOCK),
                                               BaseHelper.ResourceKeys.create(modid, name, Registries.ITEM));
            next.block = blockRegisterar.registerCopy(copyBlock, next.blockItemId, blockFactory);
            next.blockItem = blockItem;
            return next;
        }

        public <C extends Block> Builder<R, I> copy(BlockItemId id, C copyBlock, I blockItem, Function<BlockBehaviour.Properties, R> blockFactory)
        {
            Builder<R, I> next = new Builder<>(modid, name);
            next.blockItemId = id;
            next.block = blockRegisterar.registerCopy(copyBlock, id, blockFactory);
            next.blockItem = blockItem;
            return next;
        }

        public Builder<R, I> add(I blockItem, BlockBehaviour.Properties settings, Function<BlockBehaviour.Properties, R> blockFactory)
        {
            Builder<R, I> next = new Builder<>(modid, name);
            next.blockItemId = new BlockItemId(BaseHelper.ResourceKeys.create(modid, name, Registries.BLOCK),
                                               BaseHelper.ResourceKeys.create(modid, name, Registries.ITEM));
            next.block = blockRegisterar.register(next.blockItemId, settings, blockFactory);
            next.blockItem = blockItem;
            return next;
        }

        public Builder<R, I> add(BlockItemId id, I blockItem, BlockBehaviour.Properties settings, Function<BlockBehaviour.Properties, R> blockFactory)
        {
            Builder<R, I> next = new Builder<>(modid, name);
            next.blockItemId = id;
            next.block = blockRegisterar.register(id, settings, blockFactory);
            next.blockItem = blockItem;
            return next;
        }

        public Builder<R, I> add(Function<BlockBehaviour.Properties, R> blockFactory, IBlockItemFactory<Item.Properties, I> itemFactory)
        {
            Builder<R, I> next = new Builder<>(modid, name);
            next.blockItemId = new BlockItemId(BaseHelper.ResourceKeys.create(modid, name, Registries.BLOCK),
                                               BaseHelper.ResourceKeys.create(modid, name, Registries.ITEM));
            next.block = blockRegisterar.register(next.blockItemId, blockFactory);
            next.blockItem = blockItemRegisterar.register(block, next.blockItemId, itemFactory);
            return next;
        }

        public Builder<R, I> add(BlockItemId id, Function<BlockBehaviour.Properties, R> blockFactory, IBlockItemFactory<Item.Properties, I> itemFactory)
        {
            Builder<R, I> next = new Builder<>(modid, name);
            next.blockItemId = id;
            next.block = blockRegisterar.register(id, blockFactory);
            next.blockItem = blockItemRegisterar.register(block, id, itemFactory);
            return next;
        }

        public <C extends Block> Builder<R, I> add(C copyBlock, Function<BlockBehaviour.Properties, R> blockFactory, IBlockItemFactory<Item.Properties, I> itemFactory)
        {
            Builder<R, I> next = new Builder<>(modid, name);
            next.blockItemId = new BlockItemId(BaseHelper.ResourceKeys.create(modid, name, Registries.BLOCK),
                                               BaseHelper.ResourceKeys.create(modid, name, Registries.ITEM));
            next.block = blockRegisterar.registerCopy(copyBlock, next.blockItemId, blockFactory);
            next.blockItem = blockItemRegisterar.register(block, next.blockItemId, itemFactory);
            return next;
        }

        public <C extends Block> Builder<R, I> add(BlockItemId id, C copyBlock, Function<BlockBehaviour.Properties, R> blockFactory, IBlockItemFactory<Item.Properties, I> itemFactory)
        {
            Builder<R, I> next = new Builder<>(modid, name);
            next.blockItemId = id;
            next.block = blockRegisterar.registerCopy(copyBlock, id, blockFactory);
            next.blockItem = blockItemRegisterar.register(block, id, itemFactory);
            return next;
        }

        public Builder<R, I> add(BlockBehaviour.Properties settings, Function<BlockBehaviour.Properties, R> blockFactory, IBlockItemFactory<Item.Properties, I> itemFactory)
        {
            Builder<R, I> next = new Builder<>(modid, name);
            next.blockItemId = new BlockItemId(BaseHelper.ResourceKeys.create(modid, name, Registries.BLOCK),
                                               BaseHelper.ResourceKeys.create(modid, name, Registries.ITEM));
            next.block = blockRegisterar.register(next.blockItemId, settings, blockFactory);
            next.blockItem = blockItemRegisterar.register(block, next.blockItemId, itemFactory);
            return next;
        }

        public Builder<R, I> add(BlockItemId id, BlockBehaviour.Properties settings, Function<BlockBehaviour.Properties, R> blockFactory, IBlockItemFactory<Item.Properties, I> itemFactory)
        {
            Builder<R, I> next = new Builder<>(modid, name);
            next.blockItemId = id;
            next.block = blockRegisterar.register(id, settings, blockFactory);
            next.blockItem = blockItemRegisterar.register(block, id, itemFactory);
            return next;
        }

        public ModBlock<?, ?> build()
        {
            if(block == null)
                return add().build();

            return new ModBlock<>(block, blockItem, blockItemId);
        }
    }
}