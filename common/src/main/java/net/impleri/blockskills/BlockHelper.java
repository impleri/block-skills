package net.impleri.blockskills;

import net.impleri.blockskills.restrictions.Restrictions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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

    public static Block getBlock(BlockState blockState) {
        return blockState.getBlock();
    }

    public static Block getBlock(ResourceLocation block) {
        return Registry.BLOCK.get(block);
    }

    public static boolean isEmptyBlock(Block block) {
        var defaultBlock = Registry.BLOCK.get((ResourceLocation) null);

        return block == null || defaultBlock.defaultBlockState().is(block);
    }

    public static boolean isBlock(BlockState blockState) {
        return blockState != null;
    }

    public static boolean isReplacedBlock(BlockState a, BlockState b) {
        return isBlock(a) && isBlock(b) && !b.is(a.getBlock());
    }

    public static ResourceLocation getBlockName(Block block) {
        return Registry.BLOCK.getKey(block);
    }

    public static ResourceLocation getBlockName(BlockState blockState) {
        return getBlockName(getBlock(blockState));
    }

    public static BlockState getReplacement(Player player, BlockState original, BlockPos pos) {
        var level = player.getLevel();
        var dimension = level.dimension().location();
        var biome = level.getBiome(pos).unwrapKey().orElseThrow().location();
        var replacement = Restrictions.INSTANCE.getReplacementFor(player, original, dimension, biome);

        if (isReplacedBlock(original, replacement)) {
            BlockSkills.LOGGER.debug("Replacement for {} is {}", getBlockName(original), getBlockName(replacement));
        }

        return replacement;
    }

    public static int getReplacementId(BlockState original, @Nullable BlockPos pos) {
        BlockState replacement = original;

        // Inject replacement block
        if (currentPlayer != null) {
            // Slightly hacky here but we don't know the blockPos when this method is called in Palettes, so we're assuming the player's current location for biome matches
            var actualPos = pos == null ? currentPlayer.blockPosition() : pos;

            var maybeReplacement = getReplacement(currentPlayer, original, actualPos);

            if (isReplacedBlock(original, maybeReplacement)) {
                replacement = maybeReplacement;
            }
        }

        return Block.BLOCK_STATE_REGISTRY.getId(replacement);
    }

    public static long getReplacementsCountFor(Player player) {
        return Restrictions.INSTANCE.countReplacementsFor(player);
    }

    public static boolean isUsable(Player player, BlockState blockState, BlockPos pos) {
        return Restrictions.INSTANCE.isUsable(player, blockState, pos);
    }

    public static boolean isBreakable(Player player, BlockState blockState, BlockPos pos) {
        return Restrictions.INSTANCE.isBreakable(player, blockState, pos);
    }

    private static boolean checkHarvestable(Player player, BlockState blockState, BlockPos pos) {
        return Restrictions.INSTANCE.isHarvestable(player, blockState, pos) && isBreakable(player, blockState, pos);
    }

    private static final List<ItemStack> EMPTY_DROPS = new ArrayList<>();

    public static List<ItemStack> getDrops(Player player, BlockState original, ServerLevel serverLevel, BlockPos blockPos, @Nullable BlockEntity blockEntity, ItemStack tool) {
        var replacement = getReplacement(player, original, blockPos);

        // Determine drops from replacement block
        if (isReplacedBlock(original, replacement)) {
            var drops = checkHarvestable(player, replacement, blockPos) ? Block.getDrops(replacement, serverLevel, blockPos, blockEntity, player, tool) : EMPTY_DROPS;
            BlockSkills.LOGGER.debug("Drops for {} ({}) are: {}", getBlockName(original), getBlockName(replacement), drops);

            return drops;
        }

        // Maybe prevent drops
        if (!checkHarvestable(player, original, blockPos)) {
            BlockSkills.LOGGER.debug("Block {} is not droppable", getBlockName(original));
            return EMPTY_DROPS;
        }

        return null;
    }
}
