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

import com.dynamero.shared.exceptions.Exceptions;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;

import com.dynamero.shared.annotations.*;

/**
 * Provides utility methods for managing armor-related data.
 */
@SuppressWarnings("unused")
@Developer("The Mentor")
@CreatedAt("2025-04-18")
@Website("https://www.dynamero.com")
@Repository("https://github.com/drkhodakarami/___PROJECTS___")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")

public class ArmorModelHelper
{
    /**
     * The default constructor for the class
     */
    public ArmorModelHelper()
    {
        Exceptions.throwArgumentException();
    }

    /**
     * Maps equipment slots to their corresponding armor model names.
     */
    public static Map<String, Identifier> SlotToArmorNames = new HashMap<>();

    static
    {
        SlotToArmorNames.put(EquipmentSlot.HEAD.getName(), ItemModelGenerators.TRIM_PREFIX_HELMET);
        SlotToArmorNames.put(EquipmentSlot.CHEST.getName(), ItemModelGenerators.TRIM_PREFIX_CHESTPLATE);
        SlotToArmorNames.put(EquipmentSlot.LEGS.getName(), ItemModelGenerators.TRIM_PREFIX_LEGGINGS);
        SlotToArmorNames.put(EquipmentSlot.FEET.getName(), ItemModelGenerators.TRIM_PREFIX_BOOTS);
        SlotToArmorNames.put(EquipmentSlot.BODY.getName(), ItemModelGenerators.prefixForSlotTrim(EquipmentSlot.BODY.getName()));
    }
}