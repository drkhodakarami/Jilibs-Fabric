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

package com.dynamero.energy.base.records;

import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import com.dynamero.shared.annotations.*;
import com.dynamero.shared.constants.BEKeys;
import com.dynamero.shared.interfaces.DataSerializer;

/**
 * Data serializer for serializing and deserializing energy storage amounts.
 *
 * @param storage the energy storage to serialize or deserialize
 */
@Developer("TheMentor")
@CreatedAt("2026-01-12")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
public record EnergyStorageSerializer(EnergyStorage storage) implements DataSerializer
{

    /**
     * Saves energy storage amount into the given value output.
     *
     * @param view the value output to write to
     * @throws IllegalArgumentException if the storage type is not supported
     */
    @Override
    public void saveAdditional(ValueOutput view)
    {
        if(storage instanceof SimpleEnergyStorage simpleEnergyStorage)
            view.putLong("be" + BEKeys.ENERGY_AMOUNT, simpleEnergyStorage.getAmount());
        else
            throw new IllegalArgumentException("Energy Storage type is not supported by default: " + storage.getClass().getName());
    }

    /**
     * Loads energy storage amount from the given value input into the storage.
     *
     * @param view the value input to read from
     * @throws IllegalArgumentException if the storage type is not supported
     */
    @Override
    public void loadAdditional(ValueInput view)
    {
        long amount = view.getLongOr("be" + BEKeys.ENERGY_AMOUNT, 0L);
        if(storage instanceof SimpleEnergyStorage simpleEnergyStorage)
            simpleEnergyStorage.amount = amount;
        else
            throw new IllegalArgumentException("Energy Storage type is not supported by default: " + storage.getClass().getName());
    }
}