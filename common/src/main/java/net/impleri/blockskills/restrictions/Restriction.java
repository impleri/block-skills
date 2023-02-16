package net.impleri.blockskills.restrictions;

import net.impleri.playerskills.restrictions.AbstractRestriction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class Restriction extends AbstractRestriction<BlockState> {
    public final boolean breakable;
    public final boolean droppable;
    public final boolean usable;

    public Restriction(
            BlockState target,
            @Nullable Predicate<Player> condition,
            @Nullable Boolean breakable,
            @Nullable Boolean droppable,
            @Nullable Boolean usable,
            @NotNull BlockState replacement
    ) {
        super(target, condition, replacement);
        this.breakable = Boolean.TRUE.equals(breakable);
        this.droppable = Boolean.TRUE.equals(droppable);
        this.usable = Boolean.TRUE.equals(usable);
    }

    public Restriction(
            BlockState block,
            @Nullable Predicate<Player> condition,
            @Nullable Boolean breakable,
            @Nullable Boolean droppable,
            @NotNull BlockState replacement
    ) {
        this(block, condition, breakable, droppable, null, replacement);
    }

    public Restriction(
            BlockState block,
            @Nullable Predicate<Player> condition,
            @Nullable Boolean breakable,
            @NotNull BlockState replacement
    ) {
        this(block, condition, breakable, null, replacement);
    }

    public Restriction(
            BlockState block,
            @Nullable Predicate<Player> condition,
            @NotNull BlockState replacement
    ) {
        this(block, condition, null, replacement);
    }

    public Restriction(
            BlockState block,
            @NotNull BlockState replacement
    ) {
        this(block, null, replacement);
    }
}
