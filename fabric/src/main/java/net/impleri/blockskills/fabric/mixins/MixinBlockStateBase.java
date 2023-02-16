package net.impleri.blockskills.fabric.mixins;

import net.impleri.blockskills.BlockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class MixinBlockStateBase {
    @Shadow
    protected abstract BlockState asState();

    @Inject(method = "getDestroyProgress", at = @At("HEAD"), cancellable = true)
    private void onGetDestroyProgress(Player player, BlockGetter blockGetter, BlockPos blockPos, CallbackInfoReturnable<Float> cir) {
        var newSpeed = BlockHelper.getBreakSpeed(player, asState(), blockGetter, blockPos);
        if (newSpeed != null) {
            cir.setReturnValue(newSpeed);
        }
    }
}
