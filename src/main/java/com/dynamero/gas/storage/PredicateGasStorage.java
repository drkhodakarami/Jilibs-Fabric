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

package com.dynamero.gas.storage;

import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import com.dynamero.gas.GasConstants;
import com.dynamero.gas.base.GasComponent;
import com.dynamero.gas.base.interfaces.GasVariant;
import com.dynamero.gas.base.storage.GasStorage;
import com.dynamero.shared.annotations.*;
import org.jspecify.annotations.NonNull;

/**
 * Conditional synchronized gas storage delegating insertion and extraction permission checks to predicates,
 * and offering static predicate helper builders for evaluating item stack insertability and extractability.
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
public class PredicateGasStorage extends SyncedGasStorage
{
    /**
     * Predicate determining whether a given gas variant may be inserted.
     */
    private final Predicate<GasVariant> canInsert;

    /**
     * Predicate determining whether a given gas variant may be extracted.
     */
    private final Predicate<GasVariant> canExtract;

    /**
     * Constructs a PredicateGasStorage with custom insertion and extraction predicates.
     *
     * @param blockEntity the owning block entity
     * @param capacity    the maximum capacity in droplets
     * @param canInsert   the insertion predicate
     * @param canExtract  the extraction predicate
     */
    public PredicateGasStorage(BlockEntity blockEntity, long capacity, Predicate<GasVariant> canInsert, Predicate<GasVariant> canExtract)
    {
        super(blockEntity, capacity);
        this.canInsert = canInsert;
        this.canExtract = canExtract;
    }

    /**
     * Evaluates whether the given gas variant can be inserted into this storage.
     *
     * @param variant the gas variant attempting insertion
     * @return {@code true} if allowed by the predicate, {@code false} otherwise
     */
    @Override
    public boolean canInsert(@NonNull GasVariant variant)
    {
        return this.canInsert.test(variant);
    }

    /**
     * Evaluates whether the given gas variant can be extracted from this storage.
     *
     * @param variant the gas variant attempting extraction
     * @return {@code true} if allowed by the predicate, {@code false} otherwise
     */
    @Override
    public boolean canExtract(@NonNull GasVariant variant)
    {
        return this.canExtract.test(variant);
    }

    //region Create Insertable
    /**
     * Creates an item stack bi-predicate testing whether gas can be extracted from the stack to insert into storage, using gas component and amount suppliers.
     *
     * @param stackSupplier supplier of the gas component
     * @param amount        supplier of the droplet amount
     * @return the bi-predicate accepting item stack and slot index
     */
    public static BiPredicate<ItemStack, Integer> createStackInsertable(Supplier<GasComponent> stackSupplier, Supplier<Long> amount)
    {
        return (stack, index) -> {
            Storage<GasVariant> storage = ContainerItemContext.withConstant(stack).find(GasStorage.ITEM);
            if (storage == null || !storage.supportsInsertion())
                return false;

            try (Transaction transaction = Transaction.openOuter()) {
                return storage.extract(stackSupplier.get().getGas(), amount.get(), transaction) > 0;
            }
        };
    }

    /**
     * Creates an item stack bi-predicate testing whether gas can be extracted from the stack, using a gas component supplier and fixed amount.
     *
     * @param stackSupplier supplier of the gas component
     * @param amount        the droplet amount
     * @return the bi-predicate accepting item stack and slot index
     */
    public static BiPredicate<ItemStack, Integer> createStackInsertable(Supplier<GasComponent> stackSupplier, long amount)
    {
        return createStackInsertable(stackSupplier, () -> amount);
    }

    /**
     * Creates an item stack bi-predicate testing whether one unit of gas can be extracted from the stack, using a gas component supplier.
     *
     * @param stackSupplier supplier of the gas component
     * @return the bi-predicate accepting item stack and slot index
     */
    public static BiPredicate<ItemStack, Integer> createStackInsertable(Supplier<GasComponent> stackSupplier)
    {
        return createStackInsertable(stackSupplier, () -> GasConstants.UNIT);
    }

    /**
     * Creates an item stack bi-predicate testing whether gas can be extracted from the stack, using a fixed gas component and amount supplier.
     *
     * @param stack  the gas component
     * @param amount supplier of the droplet amount
     * @return the bi-predicate accepting item stack and slot index
     */
    public static BiPredicate<ItemStack, Integer> createStackInsertable(GasComponent stack, Supplier<Long> amount)
    {
        return createStackInsertable(() -> stack, amount);
    }

    /**
     * Creates an item stack bi-predicate testing whether gas can be extracted from the stack, using a fixed gas component and fixed amount.
     *
     * @param stack  the gas component
     * @param amount the droplet amount
     * @return the bi-predicate accepting item stack and slot index
     */
    public static BiPredicate<ItemStack, Integer> createStackInsertable(GasComponent stack, long amount)
    {
        return createStackInsertable(() -> stack, () -> amount);
    }

    /**
     * Creates an item stack bi-predicate testing whether one unit of gas can be extracted from the stack, using a fixed gas component.
     *
     * @param stack the gas component
     * @return the bi-predicate accepting item stack and slot index
     */
    public static BiPredicate<ItemStack, Integer> createStackInsertable(GasComponent stack)
    {
        return createStackInsertable(() -> stack, () -> GasConstants.UNIT);
    }

    /**
     * Creates an item stack bi-predicate testing whether gas can be inserted into the stack, using variant and amount suppliers.
     *
     * @param variantSupplier supplier of the gas variant
     * @param amount          supplier of the droplet amount
     * @return the bi-predicate accepting item stack and slot index
     */
    public static BiPredicate<ItemStack, Integer> createInsertable(Supplier<GasVariant> variantSupplier, Supplier<Long> amount)
    {
        return ((stack, integer) ->
        {
            Storage<GasVariant> storage = ContainerItemContext.withConstant(stack).find(GasStorage.ITEM);
            if(storage == null || !storage.supportsInsertion())
                return false;

            try(Transaction transaction = Transaction.openOuter())
            {
                return storage.insert(variantSupplier.get(), amount.get(),  transaction) > 0;
            }
        });
    }

    /**
     * Creates an item stack bi-predicate testing whether gas can be inserted into the stack, using a fixed variant and amount supplier.
     *
     * @param variant the gas variant
     * @param amount  supplier of the droplet amount
     * @return the bi-predicate accepting item stack and slot index
     */
    public static BiPredicate<ItemStack, Integer> createInsertable(GasVariant variant, Supplier<Long> amount)
    {
        return createInsertable(() -> variant, amount);
    }

    /**
     * Creates an item stack bi-predicate testing whether gas can be inserted into the stack, using a variant supplier and fixed amount.
     *
     * @param variantSupplier supplier of the gas variant
     * @param amount          the droplet amount
     * @return the bi-predicate accepting item stack and slot index
     */
    public static BiPredicate<ItemStack, Integer> createInsertable(Supplier<GasVariant> variantSupplier, long amount)
    {
        return createInsertable(variantSupplier, () -> amount);
    }

    /**
     * Creates an item stack bi-predicate testing whether gas can be inserted into the stack, using a fixed variant and fixed amount.
     *
     * @param variant the gas variant
     * @param amount  the droplet amount
     * @return the bi-predicate accepting item stack and slot index
     */
    public static BiPredicate<ItemStack, Integer> createInsertable(GasVariant variant, long amount)
    {
        return createInsertable(() -> variant, () -> amount);
    }

    /**
     * Creates an item stack bi-predicate testing whether one unit of gas can be inserted into the stack, using a variant supplier.
     *
     * @param variantSupplier supplier of the gas variant
     * @return the bi-predicate accepting item stack and slot index
     */
    public static BiPredicate<ItemStack, Integer> createInsertable(Supplier<GasVariant> variantSupplier)
    {
        return createInsertable(variantSupplier, () -> GasConstants.UNIT);
    }

    /**
     * Creates an item stack bi-predicate testing whether one unit of gas can be inserted into the stack, using a fixed variant.
     *
     * @param variant the gas variant
     * @return the bi-predicate accepting item stack and slot index
     */
    public static BiPredicate<ItemStack, Integer> createInsertable(GasVariant variant)
    {
        return createInsertable(() -> variant, () -> GasConstants.UNIT);
    }
    //endregion

    //region Create Extractable
    /**
     * Creates an item stack bi-predicate testing whether gas can be extracted from the stack, using component and amount suppliers.
     *
     * @param stackSupplier supplier of the gas component
     * @param amount        supplier of the droplet amount
     * @return the bi-predicate accepting item stack and slot index
     */
    public static BiPredicate<ItemStack, Integer> createStackExtractable(Supplier<GasComponent> stackSupplier, Supplier<Long> amount)
    {
        return (stack, index) -> {
            Storage<GasVariant> storage = ContainerItemContext.withConstant(stack).find(GasStorage.ITEM);
            if (storage == null || !storage.supportsExtraction())
                return false;

            try (Transaction transaction = Transaction.openOuter()) {
                return storage.extract(stackSupplier.get().getGas(), amount.get(), transaction) > 0;
            }
        };
    }

    /**
     * Creates an item stack bi-predicate testing whether gas can be extracted from the stack, using a gas component supplier and fixed amount.
     *
     * @param stackSupplier supplier of the gas component
     * @param amount        the droplet amount
     * @return the bi-predicate accepting item stack and slot index
     */
    public static BiPredicate<ItemStack, Integer> createStackExtractable(Supplier<GasComponent> stackSupplier, long amount)
    {
        return createStackExtractable(stackSupplier, () -> amount);
    }

    /**
     * Creates an item stack bi-predicate testing whether one unit of gas can be extracted from the stack, using a gas component supplier.
     *
     * @param stackSupplier supplier of the gas component
     * @return the bi-predicate accepting item stack and slot index
     */
    public static BiPredicate<ItemStack, Integer> createStackExtractable(Supplier<GasComponent> stackSupplier)
    {
        return createStackExtractable(stackSupplier, () -> GasConstants.UNIT);
    }

    /**
     * Creates an item stack bi-predicate testing whether gas can be extracted from the stack, using a fixed gas component and amount supplier.
     *
     * @param stack  the gas component
     * @param amount supplier of the droplet amount
     * @return the bi-predicate accepting item stack and slot index
     */
    public static BiPredicate<ItemStack, Integer> createStackExtractable(GasComponent stack, Supplier<Long> amount)
    {
        return createStackExtractable(() -> stack, amount);
    }

    /**
     * Creates an item stack bi-predicate testing whether gas can be extracted from the stack, using a fixed gas component and fixed amount.
     *
     * @param stack  the gas component
     * @param amount the droplet amount
     * @return the bi-predicate accepting item stack and slot index
     */
    public static BiPredicate<ItemStack, Integer> createStackExtractable(GasComponent stack, long amount)
    {
        return createStackExtractable(() -> stack, () -> amount);
    }

    /**
     * Creates an item stack bi-predicate testing whether one unit of gas can be extracted from the stack, using a fixed gas component.
     *
     * @param stack the gas component
     * @return the bi-predicate accepting item stack and slot index
     */
    public static BiPredicate<ItemStack, Integer> createStackExtractable(GasComponent stack)
    {
        return createStackExtractable(() -> stack, () -> GasConstants.UNIT);
    }

    /**
     * Creates an item stack bi-predicate testing whether gas can be extracted from the stack, using variant and amount suppliers.
     *
     * @param variantSupplier supplier of the gas variant
     * @param amount          supplier of the droplet amount
     * @return the bi-predicate accepting item stack and slot index
     */
    public static BiPredicate<ItemStack, Integer> createExtractable(Supplier<GasVariant> variantSupplier, Supplier<Long> amount)
    {
        return ((stack, integer) ->
        {
            Storage<GasVariant> storage = ContainerItemContext.withConstant(stack).find(GasStorage.ITEM);
            if(storage == null || !storage.supportsExtraction())
                return false;

            try(Transaction transaction = Transaction.openOuter())
            {
                return storage.extract(variantSupplier.get(), amount.get(),  transaction) > 0;
            }
        });
    }

    /**
     * Creates an item stack bi-predicate testing whether gas can be extracted from the stack, using a fixed variant and amount supplier.
     *
     * @param variant the gas variant
     * @param amount  supplier of the droplet amount
     * @return the bi-predicate accepting item stack and slot index
     */
    public static BiPredicate<ItemStack, Integer> createExtractable(GasVariant variant, Supplier<Long> amount)
    {
        return createExtractable(() -> variant, amount);
    }

    /**
     * Creates an item stack bi-predicate testing whether gas can be extracted from the stack, using a variant supplier and fixed amount.
     *
     * @param variantSupplier supplier of the gas variant
     * @param amount          the droplet amount
     * @return the bi-predicate accepting item stack and slot index
     */
    public static BiPredicate<ItemStack, Integer> createExtractable(Supplier<GasVariant> variantSupplier, long amount)
    {
        return createExtractable(variantSupplier, () -> amount);
    }

    /**
     * Creates an item stack bi-predicate testing whether gas can be extracted from the stack, using a fixed variant and fixed amount.
     *
     * @param variant the gas variant
     * @param amount  the droplet amount
     * @return the bi-predicate accepting item stack and slot index
     */
    public static BiPredicate<ItemStack, Integer> createExtractable(GasVariant variant, long amount)
    {
        return createExtractable(() -> variant, () -> amount);
    }

    /**
     * Creates an item stack bi-predicate testing whether one unit of gas can be extracted from the stack, using a variant supplier.
     *
     * @param variantSupplier supplier of the gas variant
     * @return the bi-predicate accepting item stack and slot index
     */
    public static BiPredicate<ItemStack, Integer> createExtractable(Supplier<GasVariant> variantSupplier)
    {
        return createExtractable(variantSupplier, () -> GasConstants.UNIT);
    }

    /**
     * Creates an item stack bi-predicate testing whether one unit of gas can be extracted from the stack, using a fixed variant.
     *
     * @param variant the gas variant
     * @return the bi-predicate accepting item stack and slot index
     */
    public static BiPredicate<ItemStack, Integer> createExtractable(GasVariant variant)
    {
        return createExtractable(() -> variant, () -> GasConstants.UNIT);
    }
    //endregion
}