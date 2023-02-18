package net.impleri.blockskills.mixins;

import net.impleri.blockskills.BlockHelper;
import net.impleri.blockskills.BlockSkills;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class MixinPlayer {
    @Shadow
    public abstract boolean hasCorrectToolForDrops(BlockState arg);

    @Inject(method = "hasCorrectToolForDrops", at = @At("HEAD"), cancellable = true)
    public void onHasCorrectToolForDrops(BlockState blockState, CallbackInfoReturnable<Boolean> cir) {
        var replacement = BlockHelper.getReplacement((Player) (Object) this, blockState);
        if (BlockHelper.isReplacedBlock(blockState, replacement)) {
            BlockSkills.LOGGER.debug("Replacing {} with {} for harvest check.", BlockHelper.getBlockName(blockState), BlockHelper.getBlockName(replacement));
            cir.setReturnValue(this.hasCorrectToolForDrops(replacement));
        }
    }
}
