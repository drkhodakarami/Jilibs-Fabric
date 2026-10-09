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

package com.dynamero;

import com.mojang.serialization.Codec;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;

import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceKey;

import com.dynamero.dispersion.base.records.Dispersion;
import com.dynamero.dispersion.base.records.DispersionStackList;
import com.dynamero.dispersion.base.records.DispersionStackPayload;
import com.dynamero.gas.base.GasComponent;
import com.dynamero.gas.base.records.Gas;
import com.dynamero.gas.base.records.GasStackList;
import com.dynamero.heat.base.HeatComponent;
import com.dynamero.heat.base.interfaces.HeatItem;
import com.dynamero.heat.base.interfaces.HeatStorage;
import com.dynamero.heat.base.records.HeatComponentList;
import com.dynamero.pressure.base.PressureComponent;
import com.dynamero.pressure.base.interfaces.PressureItem;
import com.dynamero.pressure.base.interfaces.PressureStorage;
import com.dynamero.pressure.base.records.PressureComponentList;
import com.dynamero.shared.network.FluidComponent;
import com.dynamero.shared.records.*;
import com.dynamero.shared.records.lists.*;
import com.dynamero.shared.utils.BaseHelper;

public class Jilibs implements ModInitializer
{
    public static final String MODID = "jilibs_fabric";

    public static final ResourceKey<Registry<Gas>> GASES_REGISTRY_KEY;
    public static final ResourceKey<Registry<Dispersion>> DISPERSION_REGISTRY_KEY;

    public static final DefaultedRegistry<Gas> GASES;
    public static final DefaultedRegistry<Dispersion> DISPERSIONS;

    static
    {
        GASES_REGISTRY_KEY = ResourceKey.createRegistryKey(BaseHelper.id(MODID, "gases"));
        DISPERSION_REGISTRY_KEY = ResourceKey.createRegistryKey(BaseHelper.id(MODID, "dispersions"));

        GASES = FabricRegistryBuilder.createDefaulted(GASES_REGISTRY_KEY, BaseHelper.id(MODID, "empty"))
                                     .attribute(RegistryAttribute.MODDED)
                                     .attribute(RegistryAttribute.SYNCED)
                                     .buildAndRegister();

        DISPERSIONS = FabricRegistryBuilder.createDefaulted(DISPERSION_REGISTRY_KEY, BaseHelper.id(MODID, "empty"))
                                           .attribute(RegistryAttribute.MODDED)
                                           .attribute(RegistryAttribute.SYNCED)
                                           .buildAndRegister();
    }

    public static class Components
    {
        public static final DataComponentType<BlockPosPayload> BLOCK_POS;
        public static final DataComponentType<BooleanPayload> BOOLEAN;
        public static final DataComponentType<ConfiguredIngredient> CONFIGURED_INGREDIENT;
        public static final DataComponentType<CoordinateDataPayload> COORDINATE;
        public static final DataComponentType<DoublePayload> DOUBLE;
        public static final DataComponentType<FloatPayload> FLOAT;
        public static final DataComponentType<HandPayload> HAND;
        public static final DataComponentType<IntegerPayload> INTEGER;
        public static final DataComponentType<ItemStackPayload> ITEM_STACK;
        public static final DataComponentType<LongPayload> LONG;
        public static final DataComponentType<OutputItemStackPayload> OUTPUT_ITEM_STACK;
        public static final DataComponentType<StackDataPayload> DATA_STACK;
        public static final DataComponentType<BooleanPayload> ACTIVE;
        public static final DataComponentType<BooleanPayload> UNSTABLE;
        public static final DataComponentType<BooleanPayload> POWERED;
        public static final DataComponentType<BooleanPayload> ENABLED;
        public static final DataComponentType<Long> ENERGY;
        public static final DataComponentType<FluidComponent> FLUID;
        public static final DataComponentType<GasComponent> GAS;
        public static final DataComponentType<HeatComponent> HEAT;
        public static final DataComponentType<PressureComponent> PRESSURE;
        public static final DataComponentType<DispersionStackPayload> DISPERSION;

        public static final DataComponentType<BlockPosList> BLOCK_POS_LIST;
        public static final DataComponentType<BooleanList> BOOLEAN_LIST;
        public static final DataComponentType<ConfiguredIngredientList> CONFIGURED_INGREDIENT_LIST;
        public static final DataComponentType<CoordinateDataList> COORDINATE_LIST;
        public static final DataComponentType<DoubleList> DOUBLE_LIST;
        public static final DataComponentType<FloatList> FLOAT_LIST;
        public static final DataComponentType<HandList> HAND_LIST;
        public static final DataComponentType<IntegerList> INTEGER_LIST;
        public static final DataComponentType<ItemStackList> ITEM_STACK_LIST;
        public static final DataComponentType<LongList> LONG_LIST;
        public static final DataComponentType<OutputItemStackList> OUTPUT_ITEM_STACK_LIST;
        public static final DataComponentType<StackDataList> DATA_STACK_LIST;
        public static final DataComponentType<BooleanList> ACTIVE_LIST;
        public static final DataComponentType<BooleanList> UNSTABLE_LIST;
        public static final DataComponentType<BooleanList> POWERED_LIST;
        public static final DataComponentType<BooleanList> ENABLED_LIST;
        public static final DataComponentType<LongList> ENERGY_LIST;
        public static final DataComponentType<FluidStackList> FLUID_LIST;
        public static final DataComponentType<GasStackList> GAS_LIST;
        public static final DataComponentType<HeatComponentList> HEAT_LIST;
        public static final DataComponentType<PressureComponentList> PRESSURE_LIST;
        public static final DataComponentType<DispersionStackList> DISPERSION_LIST;

        static
        {

            BLOCK_POS = DataComponentType.<BlockPosPayload>builder()
                                         .persistent(BlockPosPayload.CODEC)
                                         .networkSynchronized(BlockPosPayload.STREAM_CODEC)
                                         .build();

            BOOLEAN = DataComponentType.<BooleanPayload>builder()
                                       .persistent(BooleanPayload.CODEC)
                                       .networkSynchronized(BooleanPayload.STREAM_CODEC)
                                       .build();

            CONFIGURED_INGREDIENT = DataComponentType.<ConfiguredIngredient>builder()
                                                     .persistent(ConfiguredIngredient.CODEC)
                                                     .networkSynchronized(ConfiguredIngredient.STREAM_CODEC)
                                                     .build();

            COORDINATE = DataComponentType.<CoordinateDataPayload>builder()
                                          .persistent(CoordinateDataPayload.CODEC)
                                          .networkSynchronized(CoordinateDataPayload.STREAM_CODEC)
                                          .build();

            DOUBLE = DataComponentType.<DoublePayload>builder()
                                      .persistent(DoublePayload.CODEC)
                                      .networkSynchronized(DoublePayload.STREAM_CODEC)
                                      .build();

            FLOAT = DataComponentType.<FloatPayload>builder()
                                     .persistent(FloatPayload.CODEC)
                                     .networkSynchronized(FloatPayload.STREAM_CODEC)
                                     .build();

            HAND = DataComponentType.<HandPayload>builder()
                                    .persistent(HandPayload.CODEC)
                                    .networkSynchronized(HandPayload.STREAM_CODEC)
                                    .build();

            INTEGER = DataComponentType.<IntegerPayload>builder()
                                       .persistent(IntegerPayload.CODEC)
                                       .networkSynchronized(IntegerPayload.STREAM_CODEC)
                                       .build();

            ITEM_STACK = DataComponentType.<ItemStackPayload>builder()
                                          .persistent(ItemStackPayload.CODEC)
                                          .networkSynchronized(ItemStackPayload.STREAM_CODEC)
                                          .build();

            LONG = DataComponentType.<LongPayload>builder()
                                    .persistent(LongPayload.CODEC)
                                    .networkSynchronized(LongPayload.STREAM_CODEC)
                                    .build();

            OUTPUT_ITEM_STACK = DataComponentType.<OutputItemStackPayload>builder()
                                                 .persistent(OutputItemStackPayload.CODEC.codec())
                                                 .networkSynchronized(OutputItemStackPayload.STREAM_CODEC)
                                                 .build();

            DATA_STACK = DataComponentType.<StackDataPayload>builder()
                                          .persistent(StackDataPayload.CODEC)
                                          .networkSynchronized(StackDataPayload.STREAM_CODEC)
                                          .build();

            ACTIVE = DataComponentType.<BooleanPayload>builder()
                                      .persistent(BooleanPayload.CODEC)
                                      .networkSynchronized(BooleanPayload.STREAM_CODEC)
                                      .build();

            UNSTABLE = DataComponentType.<BooleanPayload>builder()
                                        .persistent(BooleanPayload.CODEC)
                                        .networkSynchronized(BooleanPayload.STREAM_CODEC)
                                        .build();

            POWERED = DataComponentType.<BooleanPayload>builder()
                                       .persistent(BooleanPayload.CODEC)
                                       .networkSynchronized(BooleanPayload.STREAM_CODEC)
                                       .build();

            ENABLED = DataComponentType.<BooleanPayload>builder()
                                       .persistent(BooleanPayload.CODEC)
                                       .networkSynchronized(BooleanPayload.STREAM_CODEC)
                                       .build();

            ENERGY = DataComponentType.<Long>builder()
                                      .persistent(Codec.LONG)
                                      .networkSynchronized(ByteBufCodecs.LONG)
                                      .build();

            FLUID = DataComponentType.<FluidComponent>builder()
                                     .persistent(FluidComponent.CODEC)
                                     .networkSynchronized(FluidComponent.STREAM_CODEC)
                                     .build();

            GAS = DataComponentType.<GasComponent>builder()
                                   .persistent(GasComponent.CODEC)
                                   .networkSynchronized(GasComponent.STREAM_CODEC)
                                   .build();

            HEAT = DataComponentType.<HeatComponent>builder()
                                    .persistent(HeatComponent.CODEC)
                                    .networkSynchronized(HeatComponent.STREAM_CODEC)
                                    .build();

            PRESSURE = DataComponentType.<PressureComponent>builder()
                                        .persistent(PressureComponent.CODEC)
                                        .networkSynchronized(PressureComponent.STREAM_CODEC)
                                        .build();

            DISPERSION = DataComponentType.<DispersionStackPayload>builder()
                                          .persistent(DispersionStackPayload.CODEC)
                                          .networkSynchronized(DispersionStackPayload.STREAM_CODEC)
                                          .build();




            BLOCK_POS_LIST = DataComponentType.<BlockPosList>builder()
                                              .persistent(BlockPosList.CODEC)
                                              .networkSynchronized(BlockPosList.STREAM_CODEC)
                                              .build();

            BOOLEAN_LIST = DataComponentType.<BooleanList>builder()
                                            .persistent(BooleanList.CODEC)
                                            .networkSynchronized(BooleanList.STREAM_CODEC)
                                            .build();

            CONFIGURED_INGREDIENT_LIST = DataComponentType.<ConfiguredIngredientList>builder()
                                                          .persistent(ConfiguredIngredientList.CODEC)
                                                          .networkSynchronized(ConfiguredIngredientList.STREAM_CODEC)
                                                          .build();

            COORDINATE_LIST = DataComponentType.<CoordinateDataList>builder()
                                               .persistent(CoordinateDataList.CODEC)
                                               .networkSynchronized(CoordinateDataList.STREAM_CODEC)
                                               .build();

            DOUBLE_LIST = DataComponentType.<DoubleList>builder()
                                           .persistent(DoubleList.CODEC)
                                           .networkSynchronized(DoubleList.STREAM_CODEC)
                                           .build();

            FLOAT_LIST = DataComponentType.<FloatList>builder()
                                          .persistent(FloatList.CODEC)
                                          .networkSynchronized(FloatList.STREAM_CODEC)
                                          .build();

            HAND_LIST = DataComponentType.<HandList>builder()
                                         .persistent(HandList.CODEC)
                                         .networkSynchronized(HandList.STREAM_CODEC)
                                         .build();

            INTEGER_LIST = DataComponentType.<IntegerList>builder()
                                            .persistent(IntegerList.CODEC)
                                            .networkSynchronized(IntegerList.STREAM_CODEC)
                                            .build();

            ITEM_STACK_LIST = DataComponentType.<ItemStackList>builder()
                                               .persistent(ItemStackList.CODEC)
                                               .networkSynchronized(ItemStackList.STREAM_CODEC)
                                               .build();

            LONG_LIST = DataComponentType.<LongList>builder()
                                         .persistent(LongList.CODEC)
                                         .networkSynchronized(LongList.STREAM_CODEC)
                                         .build();

            OUTPUT_ITEM_STACK_LIST = DataComponentType.<OutputItemStackList>builder()
                                                      .persistent(OutputItemStackList.CODEC)
                                                      .networkSynchronized(OutputItemStackList.STREAM_CODEC)
                                                      .build();

            DATA_STACK_LIST = DataComponentType.<StackDataList>builder()
                                               .persistent(StackDataList.CODEC)
                                               .networkSynchronized(StackDataList.STREAM_CODEC)
                                               .build();

            ACTIVE_LIST = DataComponentType.<BooleanList>builder()
                                           .persistent(BooleanList.CODEC)
                                           .networkSynchronized(BooleanList.STREAM_CODEC)
                                           .build();

            UNSTABLE_LIST = DataComponentType.<BooleanList>builder()
                                             .persistent(BooleanList.CODEC)
                                             .networkSynchronized(BooleanList.STREAM_CODEC)
                                             .build();

            POWERED_LIST = DataComponentType.<BooleanList>builder()
                                            .persistent(BooleanList.CODEC)
                                            .networkSynchronized(BooleanList.STREAM_CODEC)
                                            .build();

            ENABLED_LIST = DataComponentType.<BooleanList>builder()
                                            .persistent(BooleanList.CODEC)
                                            .networkSynchronized(BooleanList.STREAM_CODEC)
                                            .build();

            ENERGY_LIST = DataComponentType.<LongList>builder()
                                           .persistent(LongList.CODEC)
                                           .networkSynchronized(LongList.STREAM_CODEC)
                                           .build();

            FLUID_LIST = DataComponentType.<FluidStackList>builder()
                                          .persistent(FluidStackList.CODEC)
                                          .networkSynchronized(FluidStackList.STREAM_CODEC)
                                          .build();

            GAS_LIST = DataComponentType.<GasStackList>builder()
                                        .persistent(GasStackList.CODEC)
                                        .networkSynchronized(GasStackList.STREAM_CODEC)
                                        .build();

            HEAT_LIST = DataComponentType.<HeatComponentList>builder()
                                         .persistent(HeatComponentList.CODEC)
                                         .networkSynchronized(HeatComponentList.STREAM_CODEC)
                                         .build();

            PRESSURE_LIST = DataComponentType.<PressureComponentList>builder()
                                         .persistent(PressureComponentList.CODEC)
                                         .networkSynchronized(PressureComponentList.STREAM_CODEC)
                                         .build();

            DISPERSION_LIST = DataComponentType.<DispersionStackList>builder()
                                               .persistent(DispersionStackList.CODEC)
                                               .networkSynchronized(DispersionStackList.STREAM_CODEC)
                                               .build();
        }

        public static void init()
        {
            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                              BaseHelper.id(MODID, "block_pos_component"),
                              BLOCK_POS);
            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                              BaseHelper.id(MODID, "boolean_component"),
                              BOOLEAN);
            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                              BaseHelper.id(MODID, "configured_ingredient_component"),
                              CONFIGURED_INGREDIENT);
            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                              BaseHelper.id(MODID, "coordinate_component"),
                              COORDINATE);
            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                              BaseHelper.id(MODID, "double_component"),
                              DOUBLE);
            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                              BaseHelper.id(MODID, "float_component"),
                              FLOAT);
            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                              BaseHelper.id(MODID, "hand_component"),
                              HAND);
            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                              BaseHelper.id(MODID, "integer_component"),
                              INTEGER);
            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                              BaseHelper.id(MODID, "item_stack_component"),
                              ITEM_STACK);
            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                              BaseHelper.id(MODID, "long_component"),
                              LONG);
            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                              BaseHelper.id(MODID, "output_item_stack_component"),
                              OUTPUT_ITEM_STACK);
            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                              BaseHelper.id(MODID, "data_stack_component"),
                              DATA_STACK);
            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                              BaseHelper.id(MODID, "active_component"),
                              ACTIVE);
            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                              BaseHelper.id(MODID, "unstable_component"),
                              UNSTABLE);
            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                              BaseHelper.id(MODID, "powered_component"),
                              POWERED);
            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                              BaseHelper.id(MODID, "enabled_component"),
                              ENABLED);
            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                              BaseHelper.id(MODID, "energy_component"),
                              ENERGY);
            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                              BaseHelper.id(MODID, "fluid_component"),
                              FLUID);
            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                              BaseHelper.id(MODID, "gas_component"),
                              GAS);
            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                              BaseHelper.id(MODID, "heat_component"),
                              HEAT);
            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                              BaseHelper.id(MODID, "pressure_component"),
                              PRESSURE);
            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                              BaseHelper.id(MODID, "dispersion_component"),
                              DISPERSION);

            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                              BaseHelper.id(MODID, "block_pos_component_list"),
                              BLOCK_POS_LIST);
            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                              BaseHelper.id(MODID, "boolean_component_list"),
                              BOOLEAN_LIST);
            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                              BaseHelper.id(MODID, "configured_ingredient_component_list"),
                              CONFIGURED_INGREDIENT_LIST);
            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                              BaseHelper.id(MODID, "coordinate_component_list"),
                              COORDINATE_LIST);
            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                              BaseHelper.id(MODID, "double_component_list"),
                              DOUBLE_LIST);
            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                              BaseHelper.id(MODID, "float_component_list"),
                              FLOAT_LIST);
            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                              BaseHelper.id(MODID, "hand_component_list"),
                              HAND_LIST);
            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                              BaseHelper.id(MODID, "integer_component_list"),
                              INTEGER_LIST);
            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                              BaseHelper.id(MODID, "item_stack_component_list"),
                              ITEM_STACK_LIST);
            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                              BaseHelper.id(MODID, "long_component_list"),
                              LONG_LIST);
            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                              BaseHelper.id(MODID, "output_item_stack_component_list"),
                              OUTPUT_ITEM_STACK_LIST);
            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                              BaseHelper.id(MODID, "data_stack_component_list"),
                              DATA_STACK_LIST);
            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                              BaseHelper.id(MODID, "active_component_list"),
                              ACTIVE_LIST);
            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                              BaseHelper.id(MODID, "unstable_component_list"),
                              UNSTABLE_LIST);
            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                              BaseHelper.id(MODID, "powered_component_list"),
                              POWERED_LIST);
            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                              BaseHelper.id(MODID, "enabled_component_list"),
                              ENABLED_LIST);
            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                              BaseHelper.id(MODID, "energy_component_list"),
                              ENERGY_LIST);
            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                              BaseHelper.id(MODID, "fluid_component_list"),
                              FLUID_LIST);
            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                              BaseHelper.id(MODID, "gas_component_list"),
                              GAS_LIST);
            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                              BaseHelper.id(MODID, "heat_component_list"),
                              HEAT_LIST);
            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                              BaseHelper.id(MODID, "pressure_component_list"),
                              PRESSURE_LIST);
            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                              BaseHelper.id(MODID, "dispersion_component_list"),
                              DISPERSION_LIST);
        }
    }

    @Override
    public void onInitialize()
    {
        HeatStorage.ITEM.registerFallback((stack, context) -> {
            if(stack.getItem() instanceof HeatItem item)
                return HeatItem.createStorage(context, item.getHeatCapacity(stack), item.getHeatMaxInput(stack), item.getHeatMaxOutput(stack));
            return null;
        });

        PressureStorage.ITEM.registerFallback((stack, context) -> {
            if(stack.getItem() instanceof PressureItem item)
                return PressureItem.createStorage(context, item.getPressureCapacity(stack), item.getPressureMaxInput(stack), item.getPressureMaxOutput(stack));
            return null;
        });

        Components.init();
    }
}