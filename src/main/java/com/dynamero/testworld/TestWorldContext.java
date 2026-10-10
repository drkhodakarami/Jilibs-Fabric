package com.dynamero.testworld;

import com.dynamero.shared.annotations.*;
import com.dynamero.shared.utils.UsefulFakePlayer;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Developer("TurtyWurty")
@CreatedAt("2026-10-10")
@ModifiedAt("2026-10-10")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public record TestWorldContext(MinecraftServer server, ServerLevel overworld, BlockPos origin) {
    private static final int MAX_PATHFINDING_NODES = 250_000;

    public BlockPos pos(int x, int y, int z) {
        return origin.offset(x, y, z);
    }

    public TestWorldContext at(int x, int y, int z) {
        return new TestWorldContext(server, overworld, pos(x, y, z));
    }

    public void setBlock(int x, int y, int z, Supplier<? extends Block> blockSupplier) {
        setBlock(x, y, z, blockSupplier.get());
    }

    public void setBlock(int x, int y, int z, Block block) {
        setBlock(x, y, z, block.defaultBlockState());
    }

    public void setBlock(int x, int y, int z, BlockState state) {
        overworld.setBlockAndUpdate(pos(x, y, z), state);
    }

    public void removeBlock(int x, int y, int z) {
        overworld.removeBlock(pos(x, y, z), false);
    }

    public void fill(
            int minX, int minY, int minZ,
            int maxX, int maxY, int maxZ,
            Block block
    ) {
        fill(minX, minY, minZ, maxX, maxY, maxZ, block.defaultBlockState());
    }

    public void fill(
            int minX, int minY, int minZ,
            int maxX, int maxY, int maxZ,
            BlockState state
    ) {
        BlockPos.betweenClosedStream(
                pos(minX, minY, minZ),
                pos(maxX, maxY, maxZ)
        ).forEach(pos -> overworld.setBlockAndUpdate(pos, state));
    }

    public void fillAir(
            int minX, int minY, int minZ,
            int maxX, int maxY, int maxZ
    ) {
        fill(minX, minY, minZ, maxX, maxY, maxZ, Blocks.AIR);
    }

    public void fillReplace(
            int minX, int minY, int minZ,
            int maxX, int maxY, int maxZ,
            BlockState state,
            BlockState replace
    ) {
        fillReplaceWhere(
                minX, minY, minZ,
                maxX, maxY, maxZ,
                state,
                blockState -> blockState.equals(replace)
        );
    }

    public void fillReplace(
            int minX, int minY, int minZ,
            int maxX, int maxY, int maxZ,
            BlockState state,
            Block replace
    ) {
        fillReplaceWhere(
                minX, minY, minZ,
                maxX, maxY, maxZ,
                state,
                blockState -> blockState.is(replace)
        );
    }

    public void fillReplaceAir(
            int minX, int minY, int minZ,
            int maxX, int maxY, int maxZ,
            BlockState state
    ) {
        fillReplace(minX, minY, minZ, maxX, maxY, maxZ, state, Blocks.AIR.defaultBlockState());
    }

    public void fillReplaceWhere(
            int minX, int minY, int minZ,
            int maxX, int maxY, int maxZ,
            BlockState state,
            Predicate<BlockState> predicate
    ) {
        BlockPos.betweenClosedStream(
                pos(minX, minY, minZ),
                pos(maxX, maxY, maxZ)
        ).forEach(pos -> {
            if (predicate.test(overworld.getBlockState(pos))) {
                overworld.setBlockAndUpdate(pos, state);
            }
        });
    }

    public <T extends BlockEntity> T requireBlockEntity(int x, int y, int z, Class<T> type) {
        BlockPos pos = pos(x, y, z);
        BlockEntity blockEntity = overworld.getBlockEntity(pos);
        if (blockEntity == null)
            throw new IllegalStateException("Expected " + type.getSimpleName() + " at " + pos + ", but found no block entity");

        if (!type.isInstance(blockEntity))
            throw new IllegalStateException("Expected " + type.getSimpleName() + " at " + pos + ", but found " + blockEntity.getClass().getSimpleName());

        return type.cast(blockEntity);
    }

    public <T extends BlockEntity> void configureBlockEntity(int x, int y, int z, Class<T> type, Consumer<T> consumer) {
        T blockEntity = requireBlockEntity(x, y, z, type);
        consumer.accept(blockEntity);
        blockEntity.setChanged();
    }

    public void afterTicks(long ticks, Consumer<TestWorldContext> action) {
        if (ticks < 0)
            throw new IllegalArgumentException("Ticks must be non-negative");

        TestWorldScheduler.schedule(this, ticks, action);
    }

    public ServerPlayer createFakePlayer(int x, int y, int z, GameProfile profile) {
        BlockPos pos = pos(x, y, z);
        ServerPlayer player = UsefulFakePlayer.createPlayer(overworld, profile);
        player.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        return player;
    }

    public InteractionResult useItemOnBlock(int x, int y, int z, ServerPlayer player, ItemStack stack, InteractionHand hand) {
        BlockPos pos = pos(x, y, z);
        player.setItemInHand(hand, stack);

        Vec3 hitPos = Vec3.atCenterOf(pos).add(0, 0.5, 0);
        var hitResult = new BlockHitResult(hitPos, Direction.UP, pos, false);
        return player.gameMode.useItemOn(player, overworld, stack, hand, hitResult);
    }

    public InteractionResult useItemOnBlock(int x, int y, int z, ServerPlayer player, Item item, InteractionHand hand) {
        return useItemOnBlock(x, y, z, player, item.getDefaultInstance(), hand);
    }

    public InteractionResult useItemOnBlock(int x, int y, int z, ServerPlayer player, Supplier<? extends Item> itemSupplier, InteractionHand hand) {
        return useItemOnBlock(x, y, z, player, itemSupplier.get(), hand);
    }
}