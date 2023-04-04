package net.impleri.blockskills.restrictions;

import net.impleri.blockskills.BlockHelper;
import net.impleri.blockskills.BlockSkills;
import net.impleri.playerskills.restrictions.Registry;
import net.impleri.playerskills.restrictions.RestrictionsApi;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

import java.lang.reflect.Field;
import java.util.function.Predicate;

public class Restrictions extends RestrictionsApi<BlockState, Restriction> {
    private static final Field[] allRestrictionFields = Restriction.class.getDeclaredFields();
    public static Restrictions INSTANCE = new Restrictions(BlockSkills.RESTRICTIONS, allRestrictionFields);

    public Restrictions(Registry<Restriction> registry, Field[] fields) {
        super(registry, fields, BlockSkills.LOGGER);
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

    private boolean canHelper(Player player, BlockState block, BlockPos pos, String resource) {
        var level = player.getLevel();
        return canPlayer(player, block, level.dimension().location(), level.getBiome(pos).unwrapKey().orElseThrow().location(), resource);
    }

    public boolean isBreakable(Player player, BlockState block, BlockPos pos) {
        return canHelper(player, block, pos, "breakable");
    }

    public boolean isHarvestable(Player player, BlockState block, BlockPos pos) {
        return canHelper(player, block, pos, "harvestable");
    }

    public boolean isUsable(Player player, BlockState block, BlockPos pos) {
        return canHelper(player, block, pos, "usable");
    }

}
