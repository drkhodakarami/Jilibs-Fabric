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

package com.dynamero.dispersion.storage;

import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import org.jspecify.annotations.NonNull;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import com.dynamero.dispersion.DispersionConstants;
import com.dynamero.dispersion.base.interfaces.DispersionVariant;
import com.dynamero.dispersion.base.records.DispersionStackPayload;
import com.dynamero.dispersion.base.storage.DispersionStorage;
import com.dynamero.gas.GasConstants;
import com.dynamero.shared.annotations.*;

/**
 * Conditional synchronized dispersion storage delegating insertion and extraction permission checks to predicates,
 * and offering static predicate helper builders for evaluating item stack insertability and extractability.
 */
@SuppressWarnings("unused")
@Developer("TheMentor")
@CreatedAt("2026-10-08")
@ModifiedAt("2026-10-08")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public class PredicateDispersionStorage extends SyncedDispersionStorage
{
    /**
     * Predicate determining whether a given dispersion variant may be inserted.
     */
    private final Predicate<DispersionVariant> canInsert;

    /**
     * Predicate determining whether a given dispersion variant may be extracted.
     */
    private final Predicate<DispersionVariant> canExtract;

    /**
     * Constructs a PredicateDispersionStorage with custom insertion and extraction predicates.
     *
     * @param blockEntity the owning block entity
     * @param capacity    the maximum capacity in droplets
     * @param canInsert   the insertion predicate
     * @param canExtract  the extraction predicate
     */
    public PredicateDispersionStorage(BlockEntity blockEntity, long capacity, Predicate<DispersionVariant> canInsert, Predicate<DispersionVariant> canExtract)
    {
        super(blockEntity, capacity);
        this.canInsert = canInsert;
        this.canExtract = canExtract;
    }

    /**
     * Evaluates whether the given dispersion variant can be inserted into this storage.
     *
     * @param variant the dispersion variant attempting insertion
     * @return {@code true} if allowed by the predicate, {@code false} otherwise
     */
    @Override
    public boolean canInsert(@NonNull DispersionVariant variant)
    {
        return this.canInsert.test(variant);
    }

    /**
     * Evaluates whether the given dispersion variant can be extracted from this storage.
     *
     * @param variant the dispersion variant attempting extraction
     * @return {@code true} if allowed by the predicate, {@code false} otherwise
     */
    @Override
    public boolean canExtract(@NonNull DispersionVariant variant)
    {
        return this.canExtract.test(variant);
    }

    //region Create Insertable
    /**
     * Creates an item stack bi-predicate testing whether dispersion can be extracted from the stack to insert into storage, using payload and amount suppliers.
     *
     * @param stackSupplier supplier of the dispersion stack payload
     * @param amount        supplier of the droplet amount
     * @return the bi-predicate accepting item stack and slot index
     */
    public static BiPredicate<ItemStack, Integer> createStackInsertable(Supplier<DispersionStackPayload> stackSupplier, Supplier<Long> amount)
    {
        return (stack, index) -> {
            Storage<DispersionVariant> storage = ContainerItemContext.withConstant(stack).find(DispersionStorage.ITEM);
            if (storage == null || !storage.supportsInsertion())
                return false;

            try (Transaction transaction = Transaction.openOuter()) {
                return storage.extract(stackSupplier.get().variant(), amount.get(), transaction) > 0;
            }
        };
    }

    /**
     * Creates an item stack bi-predicate testing whether dispersion can be extracted from the stack, using a payload supplier and fixed amount.
     *
     * @param stackSupplier supplier of the dispersion stack payload
     * @param amount        the droplet amount
     * @return the bi-predicate accepting item stack and slot index
     */
    public static BiPredicate<ItemStack, Integer> createStackInsertable(Supplier<DispersionStackPayload> stackSupplier, long amount)
    {
        return createStackInsertable(stackSupplier, () -> amount);
    }

    /**
     * Creates an item stack bi-predicate testing whether one bucket of dispersion can be extracted from the stack, using a payload supplier.
     *
     * @param stackSupplier supplier of the dispersion stack payload
     * @return the bi-predicate accepting item stack and slot index
     */
    public static BiPredicate<ItemStack, Integer> createStackInsertable(Supplier<DispersionStackPayload> stackSupplier)
    {
        return createStackInsertable(stackSupplier, () -> DispersionConstants.BUCKET);
    }

    /**
     * Creates an item stack bi-predicate testing whether dispersion can be extracted from the stack, using a fixed payload and amount supplier.
     *
     * @param stack  the dispersion stack payload
     * @param amount supplier of the droplet amount
     * @return the bi-predicate accepting item stack and slot index
     */
    public static BiPredicate<ItemStack, Integer> createStackInsertable(DispersionStackPayload stack, Supplier<Long> amount)
    {
        return createStackInsertable(() -> stack, amount);
    }

    /**
     * Creates an item stack bi-predicate testing whether dispersion can be extracted from the stack, using a fixed payload and fixed amount.
     *
     * @param stack  the dispersion stack payload
     * @param amount the droplet amount
     * @return the bi-predicate accepting item stack and slot index
     */
    public static BiPredicate<ItemStack, Integer> createStackInsertable(DispersionStackPayload stack, long amount)
    {
        return createStackInsertable(() -> stack, () -> amount);
    }

    /**
     * Creates an item stack bi-predicate testing whether one unit of dispersion can be extracted from the stack, using a fixed payload.
     *
     * @param stack the dispersion stack payload
     * @return the bi-predicate accepting item stack and slot index
     */
    public static BiPredicate<ItemStack, Integer> createStackInsertable(DispersionStackPayload stack)
    {
        return createStackInsertable(() -> stack, () -> GasConstants.UNIT);
    }

    /**
     * Creates an item stack bi-predicate testing whether dispersion can be inserted into the stack, using variant and amount suppliers.
     *
     * @param variantSupplier supplier of the dispersion variant
     * @param amount          supplier of the droplet amount
     * @return the bi-predicate accepting item stack and slot index
     */
    public static BiPredicate<ItemStack, Integer> createInsertable(Supplier<DispersionVariant> variantSupplier, Supplier<Long> amount)
    {
        return ((stack, integer) ->
        {
            Storage<DispersionVariant> storage = ContainerItemContext.withConstant(stack).find(DispersionStorage.ITEM);
            if(storage == null || !storage.supportsInsertion())
                return false;

            try(Transaction transaction = Transaction.openOuter())
            {
                return storage.insert(variantSupplier.get(), amount.get(),  transaction) > 0;
            }
        });
    }

    /**
     * Creates an item stack bi-predicate testing whether dispersion can be inserted into the stack, using a fixed variant and amount supplier.
     *
     * @param variant the dispersion variant
     * @param amount  supplier of the droplet amount
     * @return the bi-predicate accepting item stack and slot index
     */
    public static BiPredicate<ItemStack, Integer> createInsertable(DispersionVariant variant, Supplier<Long> amount)
    {
        return createInsertable(() -> variant, amount);
    }

    /**
     * Creates an item stack bi-predicate testing whether dispersion can be inserted into the stack, using a variant supplier and fixed amount.
     *
     * @param variantSupplier supplier of the dispersion variant
     * @param amount          the droplet amount
     * @return the bi-predicate accepting item stack and slot index
     */
    public static BiPredicate<ItemStack, Integer> createInsertable(Supplier<DispersionVariant> variantSupplier, long amount)
    {
        return createInsertable(variantSupplier, () -> amount);
    }

    /**
     * Creates an item stack bi-predicate testing whether dispersion can be inserted into the stack, using a fixed variant and fixed amount.
     *
     * @param variant the dispersion variant
     * @param amount  the droplet amount
     * @return the bi-predicate accepting item stack and slot index
     */
    public static BiPredicate<ItemStack, Integer> createInsertable(DispersionVariant variant, long amount)
    {
        return createInsertable(() -> variant, () -> amount);
    }

    /**
     * Creates an item stack bi-predicate testing whether one bucket of dispersion can be inserted into the stack, using a variant supplier.
     *
     * @param variantSupplier supplier of the dispersion variant
     * @return the bi-predicate accepting item stack and slot index
     */
    public static BiPredicate<ItemStack, Integer> createInsertable(Supplier<DispersionVariant> variantSupplier)
    {
        return createInsertable(variantSupplier, () -> DispersionConstants.BUCKET);
    }

    /**
     * Creates an item stack bi-predicate testing whether one bucket of dispersion can be inserted into the stack, using a fixed variant.
     *
     * @param variant the dispersion variant
     * @return the bi-predicate accepting item stack and slot index
     */
    public static BiPredicate<ItemStack, Integer> createInsertable(DispersionVariant variant)
    {
        return createInsertable(() -> variant, () -> DispersionConstants.BUCKET);
    }
    //endregion

    //region Create Extractable
    /**
     * Creates an item stack bi-predicate testing whether dispersion can be extracted from the stack, using payload and amount suppliers.
     *
     * @param stackSupplier supplier of the dispersion stack payload
     * @param amount        supplier of the droplet amount
     * @return the bi-predicate accepting item stack and slot index
     */
    public static BiPredicate<ItemStack, Integer> createStackExtractable(Supplier<DispersionStackPayload> stackSupplier, Supplier<Long> amount)
    {
        return (stack, index) -> {
            Storage<DispersionVariant> storage = ContainerItemContext.withConstant(stack).find(DispersionStorage.ITEM);
            if (storage == null || !storage.supportsExtraction())
                return false;

            try (Transaction transaction = Transaction.openOuter()) {
                return storage.extract(stackSupplier.get().variant(), amount.get(), transaction) > 0;
            }
        };
    }

    /**
     * Creates an item stack bi-predicate testing whether dispersion can be extracted from the stack, using a payload supplier and fixed amount.
     *
     * @param stackSupplier supplier of the dispersion stack payload
     * @param amount        the droplet amount
     * @return the bi-predicate accepting item stack and slot index
     */
    public static BiPredicate<ItemStack, Integer> createStackExtractable(Supplier<DispersionStackPayload> stackSupplier, long amount)
    {
        return createStackExtractable(stackSupplier, () -> amount);
    }

    /**
     * Creates an item stack bi-predicate testing whether one bucket of dispersion can be extracted from the stack, using a payload supplier.
     *
     * @param stackSupplier supplier of the dispersion stack payload
     * @return the bi-predicate accepting item stack and slot index
     */
    public static BiPredicate<ItemStack, Integer> createStackExtractable(Supplier<DispersionStackPayload> stackSupplier)
    {
        return createStackExtractable(stackSupplier, () -> DispersionConstants.BUCKET);
    }

    /**
     * Creates an item stack bi-predicate testing whether dispersion can be extracted from the stack, using a fixed payload and amount supplier.
     *
     * @param stack  the dispersion stack payload
     * @param amount supplier of the droplet amount
     * @return the bi-predicate accepting item stack and slot index
     */
    public static BiPredicate<ItemStack, Integer> createStackExtractable(DispersionStackPayload stack, Supplier<Long> amount)
    {
        return createStackExtractable(() -> stack, amount);
    }

    /**
     * Creates an item stack bi-predicate testing whether dispersion can be extracted from the stack, using a fixed payload and fixed amount.
     *
     * @param stack  the dispersion stack payload
     * @param amount the droplet amount
     * @return the bi-predicate accepting item stack and slot index
     */
    public static BiPredicate<ItemStack, Integer> createStackExtractable(DispersionStackPayload stack, long amount)
    {
        return createStackExtractable(() -> stack, () -> amount);
    }

    /**
     * Creates an item stack bi-predicate testing whether one bucket of dispersion can be extracted from the stack, using a fixed payload.
     *
     * @param stack the dispersion stack payload
     * @return the bi-predicate accepting item stack and slot index
     */
    public static BiPredicate<ItemStack, Integer> createStackExtractable(DispersionStackPayload stack)
    {
        return createStackExtractable(() -> stack, () -> DispersionConstants.BUCKET);
    }

    /**
     * Creates an item stack bi-predicate testing whether dispersion can be extracted from the stack, using variant and amount suppliers.
     *
     * @param variantSupplier supplier of the dispersion variant
     * @param amount          supplier of the droplet amount
     * @return the bi-predicate accepting item stack and slot index
     */
    public static BiPredicate<ItemStack, Integer> createExtractable(Supplier<DispersionVariant> variantSupplier, Supplier<Long> amount)
    {
        return ((stack, integer) ->
        {
            Storage<DispersionVariant> storage = ContainerItemContext.withConstant(stack).find(DispersionStorage.ITEM);
            if(storage == null || !storage.supportsExtraction())
                return false;

            try(Transaction transaction = Transaction.openOuter())
            {
                return storage.extract(variantSupplier.get(), amount.get(),  transaction) > 0;
            }
        });
    }

    /**
     * Creates an item stack bi-predicate testing whether dispersion can be extracted from the stack, using a fixed variant and amount supplier.
     *
     * @param variant the dispersion variant
     * @param amount  supplier of the droplet amount
     * @return the bi-predicate accepting item stack and slot index
     */
    public static BiPredicate<ItemStack, Integer> createExtractable(DispersionVariant variant, Supplier<Long> amount)
    {
        return createExtractable(() -> variant, amount);
    }

    /**
     * Creates an item stack bi-predicate testing whether dispersion can be extracted from the stack, using a variant supplier and fixed amount.
     *
     * @param variantSupplier supplier of the dispersion variant
     * @param amount          the droplet amount
     * @return the bi-predicate accepting item stack and slot index
     */
    public static BiPredicate<ItemStack, Integer> createExtractable(Supplier<DispersionVariant> variantSupplier, long amount)
    {
        return createExtractable(variantSupplier, () -> amount);
    }

    /**
     * Creates an item stack bi-predicate testing whether dispersion can be extracted from the stack, using a fixed variant and fixed amount.
     *
     * @param variant the dispersion variant
     * @param amount  the droplet amount
     * @return the bi-predicate accepting item stack and slot index
     */
    public static BiPredicate<ItemStack, Integer> createExtractable(DispersionVariant variant, long amount)
    {
        return createExtractable(() -> variant, () -> amount);
    }

    /**
     * Creates an item stack bi-predicate testing whether one bucket of dispersion can be extracted from the stack, using a variant supplier.
     *
     * @param variantSupplier supplier of the dispersion variant
     * @return the bi-predicate accepting item stack and slot index
     */
    public static BiPredicate<ItemStack, Integer> createExtractable(Supplier<DispersionVariant> variantSupplier)
    {
        return createExtractable(variantSupplier, () -> DispersionConstants.BUCKET);
    }

    /**
     * Creates an item stack bi-predicate testing whether one bucket of dispersion can be extracted from the stack, using a fixed variant.
     *
     * @param variant the dispersion variant
     * @return the bi-predicate accepting item stack and slot index
     */
    public static BiPredicate<ItemStack, Integer> createExtractable(DispersionVariant variant)
    {
        return createExtractable(() -> variant, () -> DispersionConstants.BUCKET);
    }
    //endregion
}