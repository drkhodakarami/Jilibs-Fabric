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

package com.dynamero.inventory.base;

import java.util.function.Supplier;

import org.spongepowered.include.com.google.common.base.Objects;

import com.dynamero.shared.annotations.*;

/**
 * Key object encapsulating insertion and extraction permission suppliers for caching and comparison.
 */
@Developer("TurtyWurty")
@ModifiedBy("The Mentor")
@CreatedAt("2025-04-18")
@Repository("https://github.com/DaRealTurtyWurty/Industria")
@Discord("https://discord.turtywurty.dev/")
@Youtube("https://www.youtube.com/@TurtyWurty")
public class PredicateKey
{
    /**
     * Supplier determining whether insertion is permitted.
     */
    private final Supplier<Boolean> canInsert;

    /**
     * Supplier determining whether extraction is permitted.
     */
    private final Supplier<Boolean> canExtract;

    /**
     * Constructs a PredicateKey with the specified permission suppliers.
     *
     * @param canInsert  insertion supplier
     * @param canExtract extraction supplier
     */
    public PredicateKey(Supplier<Boolean> canInsert, Supplier<Boolean> canExtract)
    {
        this.canInsert = canInsert;
        this.canExtract = canExtract;
    }

    /**
     * Checks equality between this PredicateKey and another object.
     *
     * @param obj the object to compare against
     * @return {@code true} if equal, {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;
        if(obj == null || getClass() != obj.getClass())
            return false;
        PredicateKey that = (PredicateKey) obj;
        return Objects.equal(canInsert, that.canInsert) &&
            Objects.equal(canExtract, that.canExtract);
    }

    /**
     * Computes the hash code for this PredicateKey.
     *
     * @return hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hashCode(canInsert, canExtract);
    }
}