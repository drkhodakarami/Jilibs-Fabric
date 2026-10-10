package com.dynamero.testworld;

import com.dynamero.shared.annotations.*;
import net.minecraft.server.level.ServerLevel;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.function.Consumer;

@Developer("TurtyWurty")
@CreatedAt("2026-10-10")
@ModifiedAt("2026-10-10")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public final class TestWorldScheduler {
    private static final Map<ServerLevel, PriorityQueue<ScheduledAction>> ACTIONS = new IdentityHashMap<>();

    private static long nextSequence;

    public static void schedule(TestWorldContext context, long delay, Consumer<TestWorldContext> action) {
        long executionTick = context.overworld().getGameTime() + delay;
        ACTIONS.computeIfAbsent(context.overworld(), _ -> new PriorityQueue<>())
                .add(new ScheduledAction(executionTick, nextSequence++, context, action));
    }

    public static void tick(ServerLevel level) {
        PriorityQueue<ScheduledAction> queue = ACTIONS.get(level);
        if (queue == null)
            return;

        long currentTick = level.getGameTime();
        while (!queue.isEmpty() && queue.peek().executionTick() <= currentTick) {
            ScheduledAction action = queue.remove();
            action.action().accept(action.context());
        }

        if (queue.isEmpty()) {
            ACTIONS.remove(level);
        }
    }

    private record ScheduledAction(
            long executionTick,
            long sequence,
            TestWorldContext context,
            Consumer<TestWorldContext> action
    ) implements Comparable<ScheduledAction> {
        @Override
        public int compareTo(ScheduledAction other) {
            int tickComparison = Long.compare(executionTick, other.executionTick());
            if (tickComparison != 0)
                return tickComparison;

            return Long.compare(sequence, other.sequence());
        }
    }
}