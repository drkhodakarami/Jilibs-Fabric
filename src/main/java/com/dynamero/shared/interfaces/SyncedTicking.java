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

import java.util.List;

import com.dynamero.shared.annotations.*;

/**
 * Represents an interface for entities that require synchronized ticks and can be synced.
 */
@SuppressWarnings("unused")
@Developer("TurtyWurty")
@CreatedAt("2026-10-08")
@ModifiedAt("2026-10-08")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public interface SyncedTicking extends Ticking, Syncing
{
    /**
     * Called when the entity is ticked on the server.
     */
    void onTick();

    /**
     * Called when the entity is ticked on the client.
     */
    void onTickClient();

    /**
     * Determines whether the entity should be synced with other entities.
     *
     * @return true if the entity should be synced, false otherwise
     */
    boolean shouldSync();

    /**
     * Default implementation of the server-side tick method.
     */
    @Override
    default void tick()
    {
        onTick();

        List<Syncable> syncables = getSyncables();

        if (shouldSync() && syncables != null && !syncables.isEmpty())
            syncables.forEach(Syncable::sync);

        if(this instanceof Updatable updatable)
            updatable.onTickEnd();
    }

    /**
     * Default implementation of the client-side tick method.
     */
    @Override
    default void tickClient()
    {
        onTickClient();

        if(this instanceof Updatable updatable)
            updatable.onTickClientEnd();
    }
}