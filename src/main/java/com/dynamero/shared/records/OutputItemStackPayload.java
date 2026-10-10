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

package com.dynamero.shared.records;

import static com.dynamero.Jilibs.MODID;

import java.util.List;
import java.util.stream.IntStream;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.NotNull;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.display.SlotDisplay;

import com.dynamero.shared.annotations.*;
import com.dynamero.shared.network.ExtraStreamCodecs;

/**
 * Represents a custom payload containing output information for an item stack, including the item, count provider, and chance.
 * @param chance The chance of success for the payload
 * @param count The output item count for the payload
 * @param output The output item for the item stack payload
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
public record OutputItemStackPayload(Item output, IntProvider count, FloatProvider chance) implements CustomPacketPayload
{
    /**
     * The unique identifier for this custom payload.
     */
    public static final Type<@NotNull BlockPosPayload> ID = new Type<>(Identifier.fromNamespaceAndPath(MODID, "output_item_stack_payload"));

    /**
     * The default chance value (1.0f).
     */
    public static final ConstantFloat DEFAULT_CHANCE = ConstantFloat.of(1.0f);

    /**
     * An empty instance of OutputItemStackPayload.
     */
    public static final OutputItemStackPayload EMPTY = new OutputItemStackPayload(ItemStack.EMPTY);

    /**
     * The codec used to serialize and deserialize the OutputItemStackPayload.
     */
    public static final MapCodec<OutputItemStackPayload> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("output").forGetter(OutputItemStackPayload::output),
            IntProviders.CODEC.fieldOf("count").forGetter(OutputItemStackPayload::count),
            FloatProviders.CODEC.fieldOf("chance").forGetter(OutputItemStackPayload::chance)
    ).apply(inst, OutputItemStackPayload::new));

    /**
     * The packet codec used to send and receive the OutputItemStackPayload.
     */
    public static final StreamCodec<RegistryFriendlyByteBuf, OutputItemStackPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.registry(Registries.ITEM), OutputItemStackPayload::output,
                    ExtraStreamCodecs.INT_PROVIDER_STREAM_CODEC, OutputItemStackPayload::count,
                    ExtraStreamCodecs.FLOAT_PROVIDER_STREAM_CODEC, OutputItemStackPayload::chance,
                    OutputItemStackPayload::new
            );

    public static final Codec<List<OutputItemStackPayload>> LIST_CODEC = CODEC.codec().listOf();

    /**
     * Constructs a new {@link OutputItemStackPayload} and performs necessary validations.
     *
     * @throws IllegalArgumentException if any of the parameters are null
     */
    public OutputItemStackPayload
    {
        if(output == null)
            throw new IllegalArgumentException("Item can't be null");
        if(count == null)
            throw new IllegalArgumentException("Count can't be null");
        if(chance == null)
            throw new IllegalArgumentException("Chance can't be null");
    }

    /**
     * Constructs an {@link OutputItemStackPayload} with the specified item, count, and chance.
     *
     * @param output The item to be included in the payload.
     * @param count  The count of items.
     * @param chance The chance of the item being generated.
     */
    public OutputItemStackPayload(Item output, int count, float chance)
    {
        this(output, ConstantInt.of(count), ConstantFloat.of(chance));
    }

    /**
     * Constructs an {@link OutputItemStackPayload} from a given {@link ItemStack}.
     *
     * @param stack The {@link ItemStack} to be converted into the payload.
     * @throws IllegalArgumentException if the stack is null or its item is null
     */
    public OutputItemStackPayload(ItemStack stack)
    {
        this(stack.getItem(), ConstantInt.of(stack.getCount()), DEFAULT_CHANCE);
    }

    /**
     * Constructs an {@link OutputItemStackPayload} with the given item and count.
     *
     * @param output The item to be included in the payload, cannot be null.
     * @param count  The count provider for the items, cannot be null or provide zero or negative values.
     * @param chance The chance of the item appearing, cannot be null or less than 0.
     */
    public OutputItemStackPayload(Item output, IntProvider count, float chance)
    {
        this(output, count, ConstantFloat.of(chance));
    }

    /**
     * Constructs an {@link OutputItemStackPayload} with the given item and count.
     *
     * @param output The item to be included in the payload, cannot be null.
     * @param count  The count of items, cannot be zero or negative.
     * @param chance The chance provider for the item appearing, cannot be null.
     */
    public OutputItemStackPayload(Item output, int count, FloatProvider chance)
    {
        this(output, ConstantInt.of(count), chance);
    }

    /**
     * Constructs an {@link OutputItemStackPayload} with the given item and count.
     *
     * @param output The item to be included in the payload, cannot be null.
     * @param count  The count of items, cannot be zero or negative.
     */
    public OutputItemStackPayload(Item output, int count)
    {
        this(output, ConstantInt.of(count), DEFAULT_CHANCE);
    }

    /**
     * Constructs an {@link OutputItemStackPayload} with the given item and count provider.
     *
     * @param output The item to be included in the payload, cannot be null.
     * @param count  The count provider for the items, cannot be null or provide zero or negative values.
     */
    public OutputItemStackPayload(Item output, IntProvider count)
    {
        this(output, count, DEFAULT_CHANCE);
    }

    /**
     * Constructs an {@link OutputItemStackPayload} with the given item and chance.
     *
     * @param output The item to be included in the payload, cannot be null.
     * @param chance The chance of the item appearing, cannot be null or less than 0.
     */
    public OutputItemStackPayload(Item output, float chance)
    {
        this(output, ConstantInt.of(1), ConstantFloat.of(chance));
    }

    /**
     * Constructs an {@link OutputItemStackPayload} with the given item and chance provider.
     *
     * @param output The item to be included in the payload, cannot be null.
     * @param chance The chance provider for the item appearing, cannot be null.
     */
    public OutputItemStackPayload(Item output, FloatProvider chance)
    {
        this(output, ConstantInt.of(1), chance);
    }

    /**
     * Constructs an {@link OutputItemStackPayload} with the given item.
     *
     * @param output The item to be included in the payload, cannot be null.
     */
    public OutputItemStackPayload(Item output)
    {
        this(output, ConstantInt.of(1), DEFAULT_CHANCE);
    }

    /**
     * Constructs an {@link OutputItemStackPayload} from a given {@link ItemStack} and chance.
     *
     * @param stack  The {@link ItemStack} to be converted into the payload, cannot be null or empty.
     * @param chance The chance of the item appearing, cannot be null or less than 0.
     */
    public OutputItemStackPayload(ItemStack stack, float chance)
    {
        this(stack.getItem(), ConstantInt.of(stack.getCount()), ConstantFloat.of(chance));
    }

    /**
     * Constructs an {@link OutputItemStackPayload} from a given {@link ItemStack} and chance provider.
     *
     * @param stack  The {@link ItemStack} to be converted into the payload, cannot be null or empty.
     * @param chance The chance provider for the item appearing, cannot be null.
     */
    public OutputItemStackPayload(ItemStack stack, FloatProvider chance)
    {
        this(stack.getItem(), ConstantInt.of(stack.getCount()), chance);
    }

    /**
     * Represents a payload for an output item stack with configurable properties.
     */
    public ItemStack createStack(RandomSource random)
    {
        return this.chance.sample(random) < random.nextFloat()
            ? ItemStack.EMPTY
            : new ItemStack(this.output, this.count.sample(random));
    }

    /**
     * Converts this payload to a display representation of multiple item stacks.
     *
     * @return A {@link SlotDisplay} containing multiple item stacks.
     */
    public SlotDisplay toDisplay()
    {
        if (this.output == Items.AIR || this.count.maxInclusive() <= 0) {
            return SlotDisplay.Empty.INSTANCE;
        }

        var displays = IntStream.rangeClosed(this.count.minInclusive(), this.count.maxInclusive())
                                .mapToObj(count -> new ItemStack(this.output, count))
                                .filter(stack -> !stack.isEmpty())
                                .map(ItemStackTemplate::fromNonEmptyStack)
                                .map(SlotDisplay.ItemStackSlotDisplay::new)
                                .map(SlotDisplay.class::cast)
                                .toList();

        return displays.isEmpty() ? SlotDisplay.Empty.INSTANCE : new SlotDisplay.Composite(displays);
    }

    /**
     * Retrieves the unique identifier for this custom payload.
     *
     * @return the unique identifier
     */
    @Override
    public @NotNull Type<? extends @NotNull CustomPacketPayload> type()
    {
        return ID;
    }
}