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

package com.dynamero.config;

import com.dynamero.shared.annotations.*;

/**
 * Provides a contract for handling configuration data.
 */
@Developer("Magistermaks")
@CreatedAt("2026-08-10")
@ModifiedAt("2026-08-10")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/magistermaks/fabric-simplelibs/blob/master/simple-config/SimpleConfig.java")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public interface IConfigProvider
{
    /**
     * Retrieves an empty string as the configuration content.
     *
     * @param ignoredNamespace The namespace parameter is not used in this implementation.
     * @return An empty string representing the configuration content.
     */
    static String empty(String ignoredNamespace)
    {
        return "";
    }

    /**
     * Retrieves the configuration content for a given namespace.
     *
     * @param namespace The namespace for which to retrieve the configuration content.
     * @return The configuration content as a string.
     */
    String get(String namespace);
}