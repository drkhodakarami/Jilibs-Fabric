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

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import com.dynamero.registerars.interfaces.IEntityRegisterar;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.utils.BaseHelper;

/**
 * Registers custom block entities and entity types for Minecraft.
 */
@Developer("TheMentor")
@CreatedAt("2026-10-08")
@ModifiedAt("2026-10-08")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public class EntityRegisterar implements IEntityRegisterar
{
    /**
     * The mod ID used for registering block entities and entity types.
     */
    private final String modId;

    /**
     * Constructs a new instance of EntityRegisterer with the specified mod ID.
     *
     * @param modId the mod ID
     */
    public EntityRegisterar(String modId)
    {
        this.modId = modId;
    }

    @Override
    public <R extends BlockEntity> BlockEntityType<R> register(String name, Block block, FabricBlockEntityTypeBuilder.Factory<R> factory)
    {
        ResourceKey<BlockEntityType<?>> key = BaseHelper.ResourceKeys.create(this.modId, name, Registries.BLOCK_ENTITY_TYPE);
        BlockEntityType<R> beType = FabricBlockEntityTypeBuilder.create(factory, block).build();
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, key, beType);
    }

    @Override
    public <R extends Entity> EntityType<R> register(String name, MobCategory spawnGroup, EntityType.EntityFactory<R> factory)
    {
        ResourceKey<EntityType<?>> key = BaseHelper.ResourceKeys.create(this.modId, name, Registries.ENTITY_TYPE);
        EntityType<R> entityType = EntityType.Builder.of(factory, spawnGroup).build(key);
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, entityType);
    }

    @Override
    public <R extends Entity> EntityType<R> register(String name, EntityType.Builder<R> type)
    {
        Identifier id = BaseHelper.id(this.modId, name);
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, id, type.build(ResourceKey.create(Registries.ENTITY_TYPE, id)));
    }
}