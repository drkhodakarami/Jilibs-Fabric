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

package com.dynamero.shared.exceptions;

import com.dynamero.shared.annotations.*;

/**
 * Contains custom exceptions for the Jibase project.
 */
@SuppressWarnings("unused")
@Developer("TheMentor")
@CreatedAt("2026-08-10")
@ModifiedAt("2026-08-10")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public class Exceptions
{
    /**
     * Private constructor to prevent instantiation.
     */
    public Exceptions()
    {
        throwCtorAssertion();
    }

    /**
     * Throws an assertion error if the constructor is invoked directly.
     *
     * @throws InitCtorException always, as this method is a singleton and should not be instantiated
     */
    public static void throwCtorAssertion()
    {
        throw new InitCtorException();
    }

    public static void throwRuntimeException()
    {
        throw new RuntimeExeException();
    }

    public static void throwArgumentException()
    {
        throw new ArgumentException();
    }
}