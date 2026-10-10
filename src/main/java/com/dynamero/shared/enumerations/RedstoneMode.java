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

package com.dynamero.shared.enumerations;

import com.dynamero.shared.annotations.*;
import com.dynamero.shared.interfaces.TraversableEnum;

/**
 * Enum representing different redstone modes.
 */
@SuppressWarnings("unused")
@Developer("Direwolf20")
@CreatedAt("2026-10-08")
@ModifiedAt("2026-10-08")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public enum RedstoneMode implements TraversableEnum<RedstoneMode>
{
    /**
     * Represents a mode where redstone is ignored.
     */
    IGNORED,

    /**
     * Represents a mode where the redstone signal level is considered low.
     */
    LOW,

    /**
     * Represents a mode where the redstone signal level is considered high.
     */
    HIGH,

    /**
     * Represents a mode where the redstone signal creates a pulse.
     */
    PULSE
}