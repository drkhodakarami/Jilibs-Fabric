package com.dynamero.events;

import com.dynamero.shared.annotations.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

@Developer("TurtyWurty")
@CreatedAt("2026-10-10")
@ModifiedAt("2026-10-10")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")

@FunctionalInterface
public interface LivingDamageCallback
{
    /**
     * Runs after damage has been applied to a living entity on the logical server.
     *
     * @param damageTaken the final amount of damage applied after reductions
     */
    void afterDamage(LivingEntity entity, DamageSource source, float damageTaken);
}