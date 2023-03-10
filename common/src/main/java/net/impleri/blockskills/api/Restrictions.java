package net.impleri.blockskills.api;

import net.impleri.blockskills.BlockHelper;
import net.impleri.blockskills.BlockSkills;
import net.impleri.blockskills.restrictions.Restriction;
import net.impleri.playerskills.api.RestrictionsApi;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

import java.lang.reflect.Field;
import java.util.function.Predicate;

public class Restrictions extends RestrictionsApi<BlockState, Restriction> {
    private static final Field[] allRestrictionFields = Restriction.class.getDeclaredFields();
    public static Restrictions INSTANCE = new Restrictions(BlockSkills.RESTRICTIONS, allRestrictionFields);

    public Restrictions(net.impleri.playerskills.restrictions.Registry<Restriction> registry, Field[] fields) {
        super(registry, fields);
    }

    @Override
    protected ResourceLocation getTargetName(BlockState target) {
        return BlockHelper.getBlockName(target);
    }

    @Override
    protected Predicate<BlockState> createPredicateFor(BlockState block) {
        var actualBlock = block.getBlock();

        return (BlockState target) -> target.is(actualBlock);
    }

    public boolean isBreakable(Player player, BlockState block) {
        return canPlayer(player, block, "breakable");
    }

    public boolean isHarvestable(Player player, BlockState block) {
        return canPlayer(player, block, "harvestable");
    }

    public boolean isUsable(Player player, BlockState block) {
        return canPlayer(player, block, "usable");
    }

}
