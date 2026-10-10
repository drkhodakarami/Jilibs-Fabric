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

package com.dynamero.dispersion.base.interfaces;

import com.dynamero.dispersion.base.DispersionConnector;
import com.dynamero.dispersion.base.storage.SingleDispersionStorage;
import com.dynamero.shared.annotations.*;

/**
 * Interface for objects that provide a {@link DispersionConnector}.
 *
 * @param <T> the single dispersion storage type
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
public interface DispersionConnectorProvider<T extends SingleDispersionStorage>
{
    /**
     * Retrieves the dispersion connector.
     *
     * @return the dispersion connector
     */
    DispersionConnector<T> getDispersionConnector();
}