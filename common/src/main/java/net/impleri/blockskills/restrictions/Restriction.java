package net.impleri.blockskills.restrictions;

import net.impleri.playerskills.restrictions.AbstractRestriction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class Restriction extends AbstractRestriction<Block> {
    public final boolean breakable;
    public final boolean harvestable;
    public final boolean usable;

    public Restriction(
            Block target,
            @Nullable Predicate<Player> condition,
            @Nullable Boolean breakable,
            @Nullable Boolean harvestable,
            @Nullable Boolean usable,
            @Nullable List<ResourceLocation> includeDimensions,
            @Nullable List<ResourceLocation> excludeDimensions,
            @Nullable List<ResourceLocation> includeBiomes,
            @Nullable List<ResourceLocation> excludeBiomes,
            @NotNull Block replacement
    ) {
        super(target, condition, includeDimensions, excludeDimensions, includeBiomes, excludeBiomes, replacement);

        this.breakable = Boolean.TRUE.equals(breakable);
        this.harvestable = Boolean.TRUE.equals(harvestable);
        this.usable = Boolean.TRUE.equals(usable);
    }
}
