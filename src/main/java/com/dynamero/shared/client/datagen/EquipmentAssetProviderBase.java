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

package com.dynamero.shared.client.datagen;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import org.jspecify.annotations.NonNull;

import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.EquipmentAsset;

@SuppressWarnings("unused")
public abstract class EquipmentAssetProviderBase implements DataProvider
{
    private final PackOutput.PathProvider pathProvider;
    private final String modid;

    public EquipmentAssetProviderBase(String modid, PackOutput packOutput, CompletableFuture<HolderLookup.Provider> completableFuture)
    {
        this.pathProvider = packOutput.createPathProvider(PackOutput.Target.RESOURCE_PACK, "equipment");
        this.modid = modid;
    }

    /**
     * Example registring of a humanoid armor:
     * <pre>{@code
     *  //"something" should match the name of the image file
     * consumer.accept(ModArmorMaterials.SOME_MATERIAL,
     *     EquipmentClientInfo.builder()
     *         .addHumanoidLayers(BaseHelper.id(MODID, "something"))
     *         .addLayers(EquipmentClientInfo.LayerType.HORSE_BODY,
     *             new EquipmentClientInfo.Layer(BaseHelper.id(MODID, "something")))
     *         .build());
     * }</pre>
     * @param consumer the consumer that is provided internally by class
     */
    protected abstract void bootstap(BiConsumer<ResourceKey<EquipmentAsset>, EquipmentClientInfo> consumer);

    @Override
    public @NonNull CompletableFuture<?> run(@NonNull CachedOutput cache)
    {
        Map<ResourceKey<EquipmentAsset>, EquipmentClientInfo> equipmentAssets = new HashMap<>();
        bootstap((id, asset) -> {
            if(equipmentAssets.putIfAbsent(id, asset) != null)
                throw new IllegalStateException("Tried to register equipment asset twice for id: " + id);
        });
        return DataProvider.saveAll(cache, EquipmentClientInfo.CODEC, this. pathProvider::json, equipmentAssets);
    }

    @Override
    public @NonNull String getName()
    {
        return this.modid + "_equipment_asset_definition";
    }
}