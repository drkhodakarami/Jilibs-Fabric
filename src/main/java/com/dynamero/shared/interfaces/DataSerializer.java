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

package com.dynamero.shared.interfaces;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import com.dynamero.shared.annotations.*;

/**
 * Represents an interface for objects that can be serialized and deserialized to/from NBT data.
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
public interface DataSerializer
{
    /**
     * Writes the object's data to an NBT view.
     *
     * @param view the write view where the data should be written
     */
    void saveAdditional(ValueOutput view);

    /**
     * Reads the object's data from an NBT view.
     *
     * @param view the read view from which the data should be read
     */
    void loadAdditional(ValueInput view);
}