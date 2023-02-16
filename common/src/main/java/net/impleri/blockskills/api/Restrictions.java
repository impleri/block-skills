package net.impleri.blockskills.api;

import net.impleri.blockskills.BlockHelper;
import net.impleri.blockskills.restrictions.Registry;
import net.impleri.blockskills.restrictions.Restriction;
import net.impleri.playerskills.api.RestrictionsApi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

import java.lang.reflect.Field;
import java.util.function.Predicate;

public class Restrictions extends RestrictionsApi<BlockState, Restriction> {
    private static final Field[] allRestrictionFields = Restriction.class.getDeclaredFields();
    public static Restrictions INSTANCE = new Restrictions(Registry.INSTANCE, allRestrictionFields);

    public static long getReplacementsCountFor(Player player) {
        return INSTANCE.countReplacementsFor(player).size();
    }

    public Restrictions(net.impleri.playerskills.restrictions.Registry<Restriction> registry, Field[] fields) {
        super(registry, fields);
    }

    private Predicate<BlockState> createPredicateFor(BlockState block) {
        var actualBlock = block.getBlock();

        return (BlockState target) -> target.is(actualBlock);
    }

    public boolean canPlayer(Player player, BlockState block, String fieldName) {
        Predicate<BlockState> predicate = createPredicateFor(block);

        return canPlayer(player, predicate, fieldName, BlockHelper.getBlockName(block));
    }

    public boolean isBreakable(Player player, BlockState block) {
        return canPlayer(player, block, "breakable");
    }

    public boolean isDroppable(Player player, BlockState block) {
        return canPlayer(player, block, "droppable");
    }

    public boolean isUsable(Player player, BlockState block) {
        return canPlayer(player, block, "usable");
    }

    public BlockState getReplacement(Player player, BlockState block) {
        Predicate<BlockState> predicate = createPredicateFor(block);
        return getReplacementsFor(player, predicate).stream()
                .map(restriction -> restriction.replacement)
                .findFirst()
                .orElse(block);
    }
}
