package net.impleri.blockskills;

import net.impleri.blockskills.api.Restrictions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BlockHelper {
    private static Player currentPlayer;

    public static void setPlayer(@Nullable Player player) {
        currentPlayer = player;
    }

    public static BlockState getBlockState(BlockPos blockPos, Level level) {
        return level.getBlockState(blockPos);
    }

    public static Block getBlock(BlockPos blockPos, Level level) {
        return getBlockState(blockPos, level).getBlock();
    }

    public static Block getBlock(BlockState blockState) {
        return blockState.getBlock();
    }

    public static Block getBlock(ResourceLocation block) {
        return Registry.BLOCK.get(block);
    }

    public static boolean isEmptyBlock(Block block) {
        var defaultBlock = Registry.BLOCK.get((ResourceLocation) null);

        return block.equals(defaultBlock);
    }

    public static boolean isBlock(BlockState blockState) {
        return blockState != null;
    }

    public static boolean isSameBlock(BlockState a, BlockState b) {
        return !isBlock(a) || !isBlock(b) || a.is(b.getBlock());
    }

    public static ResourceLocation getBlockName(Block block) {
        return Registry.BLOCK.getKey(block);
    }

    public static ResourceLocation getBlockName(BlockState blockState) {
        return getBlockName(getBlock(blockState));
    }

    public static ResourceLocation getBlockName(BlockPos blockPos, Level level) {
        return getBlockName(getBlock(blockPos, level));
    }

    public static boolean isHarvestable(Player player, BlockState state, boolean original) {
        var replacement = getReplacement(player, state);

        if (isBlock(replacement)) {
            return player.hasCorrectToolForDrops(replacement);
        }

        if (!Restrictions.INSTANCE.isDroppable(player, state)) {
            return false;
        }

        return original;
    }

    public static Float getBreakSpeed(Player player, BlockState state, float original, @Nullable BlockPos pos) {
        if (pos == null) {
            return original;
        }

        var newSpeed = getBreakSpeed(player, state, player.getLevel(), pos);

        return newSpeed == null ? original : newSpeed;
    }

    public static Float getBreakSpeed(Player player, BlockState state, BlockGetter blockGetter, BlockPos pos) {
        var replacement = getReplacement(player, state);

        if (isBlock(replacement)) {
            return replacement.getDestroyProgress(player, blockGetter, pos);
        }

        if (!Restrictions.INSTANCE.isBreakable(player, state)) {
            return -1.0F;
        }

        return null;
    }

    public static List<ItemStack> getDrops(Player player, BlockState state, ServerLevel serverLevel, BlockPos blockPos, @Nullable BlockEntity blockEntity, ItemStack tool) {
        var replacement = getReplacement(player, state);

        if (isBlock(replacement) && !isSameBlock(state, replacement)) {
            var drops = Block.getDrops(replacement, serverLevel, blockPos, blockEntity, player, tool);
            BlockSkills.LOGGER.debug("Drops for {} are: {}", getBlockName(state), drops);

            return drops;
        }

        if (!Restrictions.INSTANCE.isDroppable(player, state)) {
            BlockSkills.LOGGER.debug("Block {} is not droppable", getBlockName(state));
            return new ArrayList<>();
        }

        return null;
    }

    @Nullable
    public static BlockState getReplacement(Player player, BlockState blockState) {
        var replacement = Restrictions.INSTANCE.getReplacement(player, blockState);

        if (isBlock(replacement) && !isSameBlock(blockState, replacement)) {
            BlockSkills.LOGGER.debug("Replacement for {} is {}", getBlockName(blockState), getBlockName(replacement));
        }

        return replacement;
    }

    public static int getReplacementId(BlockState blockState) {
        BlockState replacement = blockState;

        if (currentPlayer != null) {
            var maybeReplacement = getReplacement(currentPlayer, blockState);

            if (isBlock(maybeReplacement)) {
                replacement = maybeReplacement;
            }
        }

        return Block.BLOCK_STATE_REGISTRY.getId(replacement);
    }
}
