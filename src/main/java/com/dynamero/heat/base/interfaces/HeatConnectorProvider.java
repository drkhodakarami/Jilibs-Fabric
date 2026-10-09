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

package com.dynamero.heat.base.interfaces;

import com.dynamero.heat.base.HeatConnector;
import com.dynamero.shared.annotations.*;

/**
 * Interface for objects that provide a {@link HeatConnector}.
 *
 * @param <T> the heat storage type
 */
@SuppressWarnings("unused")
@Developer("TheMentor")
@CreatedAt("2026-01-12")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
public interface HeatConnectorProvider<T extends HeatStorage>
{
    /**
     * Retrieves the heat connector.
     *
     * @return the heat connector
     */
    HeatConnector<T> getHeatConnector();
}