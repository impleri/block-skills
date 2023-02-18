package net.impleri.blockskills.api;

import net.impleri.blockskills.BlockHelper;
import net.impleri.blockskills.BlockSkills;
import net.impleri.blockskills.restrictions.Registry;
import net.impleri.blockskills.restrictions.Restriction;
import net.impleri.playerskills.api.RestrictionsApi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class Restrictions extends RestrictionsApi<BlockState, Restriction> {
    private static final Field[] allRestrictionFields = Restriction.class.getDeclaredFields();
    public static Restrictions INSTANCE = new Restrictions(Registry.INSTANCE, allRestrictionFields);

    public Restrictions(net.impleri.playerskills.restrictions.Registry<Restriction> registry, Field[] fields) {
        super(registry, fields);
    }

    private Predicate<BlockState> createPredicateFor(BlockState block) {
        var actualBlock = block.getBlock();

        return (BlockState target) -> target.is(actualBlock);
    }

    protected boolean canPlayer(Player player, BlockState block, String fieldName) {
        Predicate<BlockState> predicate = createPredicateFor(block);

        return canPlayer(player, predicate, fieldName, BlockHelper.getBlockName(block));
    }

    private final Map<Player, Map<BlockState, BlockState>> replacementCache = new HashMap<>();

    public void clearPlayerCache(Player player) {
        replacementCache.put(player, new HashMap<>());
    }

    @NotNull
    public BlockState getReplacementInternal(Player player, BlockState block) {
        BlockState replacement = block;
        boolean hasReplacement = true;

        // Recurse through replacements until we don't have one so that we can allow for cascading replacements
        while (hasReplacement) {
            var nextReplacement = getReplacementsFor(player, createPredicateFor(replacement))
                    .stream()
                    .map(restriction -> restriction.replacement)
                    .findFirst();

            if (nextReplacement.isEmpty()) {
                hasReplacement = false;
            } else {
                replacement = nextReplacement.get();
            }
        }

        return replacement;
    }

    @NotNull
    public BlockState getReplacement(Player player, BlockState block) {
        Map<BlockState, BlockState> playerCache = replacementCache.getOrDefault(player, new HashMap<>());

        if (playerCache.containsKey(block)) {
            return playerCache.get(block);
        }

        var replacement = getReplacementInternal(player, block);

        playerCache.put(block, replacement);
        replacementCache.put(player, playerCache);

        return replacement;
    }

    public long getReplacementsCountFor(Player player) {
        return countReplacementsFor(player).size();
    }


    public boolean isBreakable(Player player, BlockState block) {
        var actualBlock = getReplacement(player, block);
        BlockSkills.LOGGER.info("Checking if {} ({}) is breakable.", BlockHelper.getBlockName(block), BlockHelper.getBlockName(actualBlock));
        return canPlayer(player, actualBlock, "breakable");
    }

    public boolean isHarvestable(Player player, BlockState block) {
        var actualBlock = getReplacement(player, block);
        BlockSkills.LOGGER.info("Checking if {} ({}) is harvestable.", BlockHelper.getBlockName(block), BlockHelper.getBlockName(actualBlock));
        return canPlayer(player, actualBlock, "harvestable");
    }

    public boolean isUsable(Player player, BlockState block) {
        var actualBlock = getReplacement(player, block);
        BlockSkills.LOGGER.info("Checking if {} ({}) is usable.", BlockHelper.getBlockName(block), BlockHelper.getBlockName(actualBlock));
        return canPlayer(player, actualBlock, "usable");
    }

}
