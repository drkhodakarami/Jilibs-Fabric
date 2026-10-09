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

package com.dynamero.fluid.storage;

import java.util.function.Predicate;
import java.util.function.Supplier;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import com.dynamero.shared.annotations.*;
import com.dynamero.fluid.base.FluidComponent;
import org.jspecify.annotations.NonNull;

/**
 * Conditional synchronized fluid storage delegating insertion and extraction permission checks to predicates,
 * and offering static predicate helper builders for evaluating item stack insertability and extractability.
 */
@SuppressWarnings("unused")
@Developer("TrutyWurty")
@CreatedAt("2026-08-10")
@ModifiedAt("2026-08-10")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public class PredicateFluidStorage extends SyncedFluidStorage
{
    /**
     * Predicate determining whether a given fluid variant may be inserted.
     */
    private final Predicate<FluidVariant> canInsert;

    /**
     * Predicate determining whether a given fluid variant may be extracted.
     */
    private final Predicate<FluidVariant> canExtract;

    /**
     * Constructs a PredicateFluidStorage with custom insertion and extraction predicates.
     *
     * @param blockEntity the owning block entity
     * @param capacity    the maximum capacity in droplets
     * @param canInsert   the insertion predicate
     * @param canExtract  the extraction predicate
     */
    public PredicateFluidStorage(BlockEntity blockEntity, long capacity, Predicate<FluidVariant> canInsert, Predicate<FluidVariant> canExtract)
    {
        super(blockEntity, capacity);
        this.canInsert = canInsert;
        this.canExtract = canExtract;
    }

    /**
     * Evaluates whether the given fluid variant can be inserted into this storage.
     *
     * @param variant the fluid variant attempting insertion
     * @return {@code true} if allowed by the predicate, {@code false} otherwise
     */
    @Override
    public boolean canInsert(@NonNull FluidVariant variant)
    {
        return this.canInsert.test(variant);
    }

    /**
     * Evaluates whether the given fluid variant can be extracted from this storage.
     *
     * @param variant the fluid variant attempting extraction
     * @return {@code true} if allowed by the predicate, {@code false} otherwise
     */
    @Override
    public boolean canExtract(@NonNull FluidVariant variant)
    {
        return this.canExtract.test(variant);
    }

    //region Create Insertable
    /**
     * Creates an item stack predicate testing whether the item can have fluid extracted to insert into storage, using fluid and amount suppliers.
     *
     * @param stackSupplier supplier of the fluid component to test
     * @param amount        supplier of the droplet amount
     * @return the item stack predicate
     */
    public static Predicate<ItemStack> createStackInsertable(Supplier<FluidComponent> stackSupplier, Supplier<Long> amount)
    {
        return stack ->
        {
            Storage<FluidVariant> storage = ContainerItemContext.withConstant(stack).find(FluidStorage.ITEM);
            if (storage == null || !storage.supportsInsertion())
                return false;

            try (Transaction transaction = Transaction.openOuter()) {
                return storage.extract(stackSupplier.get().getFluid(), amount.get(), transaction) > 0;
            }
        };
    }

    /**
     * Creates an item stack predicate testing whether the item can have fluid extracted to insert into storage, using a fluid supplier and fixed amount.
     *
     * @param stackSupplier supplier of the fluid component to test
     * @param amount        the droplet amount
     * @return the item stack predicate
     */
    public static Predicate<ItemStack> createStackInsertable(Supplier<FluidComponent> stackSupplier, long amount)
    {
        return createStackInsertable(stackSupplier, () -> amount);
    }

    /**
     * Creates an item stack predicate testing whether the item can have a bucket of fluid extracted to insert into storage, using a fluid supplier.
     *
     * @param stackSupplier supplier of the fluid component to test
     * @return the item stack predicate
     */
    public static Predicate<ItemStack> createStackInsertable(Supplier<FluidComponent> stackSupplier)
    {
        return createStackInsertable(stackSupplier, () -> FluidConstants.BUCKET);
    }

    /**
     * Creates an item stack predicate testing whether the item can have fluid extracted to insert into storage, using a fluid stack and amount supplier.
     *
     * @param fluidStack the fluid component to test
     * @param amount     supplier of the droplet amount
     * @return the item stack predicate
     */
    public static Predicate<ItemStack> createStackInsertable(FluidComponent fluidStack, Supplier<Long> amount)
    {
        return createStackInsertable(() -> fluidStack, amount);
    }

    /**
     * Creates an item stack predicate testing whether the item can have fluid extracted to insert into storage, using a fluid stack and fixed amount.
     *
     * @param fluidStack the fluid component to test
     * @param amount     the droplet amount
     * @return the item stack predicate
     */
    public static Predicate<ItemStack> createStackInsertable(FluidComponent fluidStack, long amount)
    {
        return createStackInsertable(() -> fluidStack, () -> amount);
    }

    /**
     * Creates an item stack predicate testing whether the item can have a bucket of fluid extracted to insert into storage, using a fluid stack.
     *
     * @param fluidStack the fluid component to test
     * @return the item stack predicate
     */
    public static Predicate<ItemStack> createStackInsertable(FluidComponent fluidStack)
    {
        return createStackInsertable(() -> fluidStack, () -> FluidConstants.BUCKET);
    }

    /**
     * Creates an item stack predicate testing whether the item can accept fluid insertion, using variant and amount suppliers.
     *
     * @param variantSupplier supplier of the fluid variant to insert
     * @param amount          supplier of the droplet amount
     * @return the item stack predicate
     */
    public static Predicate<ItemStack> createInsertable(Supplier<FluidVariant> variantSupplier, Supplier<Long> amount)
    {
        return stack ->
        {
            Storage<FluidVariant> storage = ContainerItemContext.withConstant(stack).find(FluidStorage.ITEM);
            if(storage == null || !storage.supportsInsertion())
                return false;

            try(Transaction transaction = Transaction.openOuter())
            {
                return storage.insert(variantSupplier.get(), amount.get(),  transaction) > 0;
            }
        };
    }

    /**
     * Creates an item stack predicate testing whether the item can accept fluid insertion, using a fixed variant and amount supplier.
     *
     * @param variant the fluid variant to insert
     * @param amount  supplier of the droplet amount
     * @return the item stack predicate
     */
    public static Predicate<ItemStack> createInsertable(FluidVariant variant, Supplier<Long> amount)
    {
        return createInsertable(() -> variant, amount);
    }

    /**
     * Creates an item stack predicate testing whether the item can accept fluid insertion, using a variant supplier and fixed amount.
     *
     * @param variantSupplier supplier of the fluid variant to insert
     * @param amount          the droplet amount
     * @return the item stack predicate
     */
    public static Predicate<ItemStack> createInsertable(Supplier<FluidVariant> variantSupplier, long amount)
    {
        return createInsertable(variantSupplier, () -> amount);
    }

    /**
     * Creates an item stack predicate testing whether the item can accept fluid insertion, using a fixed variant and fixed amount.
     *
     * @param variant the fluid variant to insert
     * @param amount  the droplet amount
     * @return the item stack predicate
     */
    public static Predicate<ItemStack> createInsertable(FluidVariant variant, long amount)
    {
        return createInsertable(() -> variant, () -> amount);
    }

    /**
     * Creates an item stack predicate testing whether the item can accept one bucket of fluid insertion, using a variant supplier.
     *
     * @param variantSupplier supplier of the fluid variant to insert
     * @return the item stack predicate
     */
    public static Predicate<ItemStack> createInsertable(Supplier<FluidVariant> variantSupplier)
    {
        return createInsertable(variantSupplier, () -> FluidConstants.BUCKET);
    }

    /**
     * Creates an item stack predicate testing whether the item can accept one bucket of the specified fluid variant.
     *
     * @param variant the fluid variant to insert
     * @return the item stack predicate
     */
    public static Predicate<ItemStack> createInsertable(FluidVariant variant)
    {
        return createInsertable(() -> variant, () -> FluidConstants.BUCKET);
    }
    //endregion

    //region Create Extractable
    /**
     * Creates an item stack predicate testing whether the item can have fluid extracted from it, using fluid stack and amount suppliers.
     *
     * @param stackSupplier supplier of the fluid component to extract
     * @param amount        supplier of the droplet amount
     * @return the item stack predicate
     */
    public static Predicate<ItemStack> createStackExtractable(Supplier<FluidComponent> stackSupplier, Supplier<Long> amount)
    {
        return stack ->
        {
            Storage<FluidVariant> storage = ContainerItemContext.withConstant(stack).find(FluidStorage.ITEM);
            if (storage == null || !storage.supportsExtraction())
                return false;

            try (Transaction transaction = Transaction.openOuter()) {
                return storage.extract(stackSupplier.get().getFluid(), amount.get(), transaction) > 0;
            }
        };
    }

    /**
     * Creates an item stack predicate testing whether the item can have fluid extracted from it, using a fluid stack supplier and fixed amount.
     *
     * @param stackSupplier supplier of the fluid component to extract
     * @param amount        the droplet amount
     * @return the item stack predicate
     */
    public static Predicate<ItemStack> createStackExtractable(Supplier<FluidComponent> stackSupplier, long amount)
    {
        return createStackExtractable(stackSupplier, () -> amount);
    }

    /**
     * Creates an item stack predicate testing whether the item can have one bucket of fluid extracted, using a fluid stack supplier.
     *
     * @param stackSupplier supplier of the fluid component to extract
     * @return the item stack predicate
     */
    public static Predicate<ItemStack> createStackExtractable(Supplier<FluidComponent> stackSupplier)
    {
        return createStackExtractable(stackSupplier, () -> FluidConstants.BUCKET);
    }

    /**
     * Creates an item stack predicate testing whether the item can have fluid extracted from it, using a fixed fluid stack and amount supplier.
     *
     * @param stack  the fluid component to extract
     * @param amount supplier of the droplet amount
     * @return the item stack predicate
     */
    public static Predicate<ItemStack> createStackExtractable(FluidComponent stack, Supplier<Long> amount)
    {
        return createStackExtractable(() -> stack, amount);
    }

    /**
     * Creates an item stack predicate testing whether the item can have fluid extracted from it, using a fixed fluid stack and fixed amount.
     *
     * @param stack  the fluid component to extract
     * @param amount the droplet amount
     * @return the item stack predicate
     */
    public static Predicate<ItemStack> createStackExtractable(FluidComponent stack, long amount)
    {
        return createStackExtractable(() -> stack, () -> amount);
    }

    /**
     * Creates an item stack predicate testing whether the item can have one bucket of fluid extracted, using a fixed fluid stack.
     *
     * @param stack the fluid component to extract
     * @return the item stack predicate
     */
    public static Predicate<ItemStack> createStackExtractable(FluidComponent stack)
    {
        return createStackExtractable(() -> stack, () -> FluidConstants.BUCKET);
    }

    /**
     * Creates an item stack predicate testing whether the item can have fluid extracted, using variant and amount suppliers.
     *
     * @param variantSupplier supplier of the fluid variant to extract
     * @param amount          supplier of the droplet amount
     * @return the item stack predicate
     */
    public static Predicate<ItemStack> createExtractable(Supplier<FluidVariant> variantSupplier, Supplier<Long> amount)
    {
        return stack ->
        {
            Storage<FluidVariant> storage = ContainerItemContext.withConstant(stack).find(FluidStorage.ITEM);
            if(storage == null || !storage.supportsExtraction())
                return false;

            try(Transaction transaction = Transaction.openOuter())
            {
                return storage.extract(variantSupplier.get(), amount.get(),  transaction) > 0;
            }
        };
    }

    /**
     * Creates an item stack predicate testing whether the item can have fluid extracted, using a fixed variant and amount supplier.
     *
     * @param variant the fluid variant to extract
     * @param amount  supplier of the droplet amount
     * @return the item stack predicate
     */
    public static Predicate<ItemStack> createExtractable(FluidVariant variant, Supplier<Long> amount)
    {
        return createExtractable(() -> variant, amount);
    }

    /**
     * Creates an item stack predicate testing whether the item can have fluid extracted, using a variant supplier and fixed amount.
     *
     * @param variantSupplier supplier of the fluid variant to extract
     * @param amount          the droplet amount
     * @return the item stack predicate
     */
    public static Predicate<ItemStack> createExtractable(Supplier<FluidVariant> variantSupplier, long amount)
    {
        return createExtractable(variantSupplier, () -> amount);
    }

    /**
     * Creates an item stack predicate testing whether the item can have fluid extracted, using a fixed variant and fixed amount.
     *
     * @param variant the fluid variant to extract
     * @param amount  the droplet amount
     * @return the item stack predicate
     */
    public static Predicate<ItemStack> createExtractable(FluidVariant variant, long amount)
    {
        return createExtractable(() -> variant, () -> amount);
    }

    /**
     * Creates an item stack predicate testing whether the item can have one bucket of fluid extracted, using a variant supplier.
     *
     * @param variantSupplier supplier of the fluid variant to extract
     * @return the item stack predicate
     */
    public static Predicate<ItemStack> createExtractable(Supplier<FluidVariant> variantSupplier)
    {
        return createExtractable(variantSupplier, () -> FluidConstants.BUCKET);
    }

    /**
     * Creates an item stack predicate testing whether the item can have one bucket of the specified fluid variant extracted.
     *
     * @param variant the fluid variant to extract
     * @return the item stack predicate
     */
    public static Predicate<ItemStack> createExtractable(FluidVariant variant)
    {
        return createExtractable(() -> variant, () -> FluidConstants.BUCKET);
    }
    //endregion
}