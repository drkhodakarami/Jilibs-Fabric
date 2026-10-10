package com.dynamero.events.client;

import com.dynamero.shared.annotations.*;

/**
 * Render stages shared by Fabric and NeoForge that Industria currently consumes.
 */
@Developer("TurtyWurty")
@CreatedAt("2026-10-10")
@ModifiedAt("2026-10-10")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public enum RenderStage {
    /**
     * Submit geometry to {@link LevelRenderContext#submitNodeCollector()}.
     */
    COLLECT_SUBMITS,
    /**
     * Render after opaque entities, block entities, and particles.
     */
    AFTER_SOLID_FEATURES
}