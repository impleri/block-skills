package net.impleri.blockskills.fabric.mixins;

import net.impleri.blockskills.BlockHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class MixinPlayer {
    @Inject(method = "hasCorrectToolForDrops", at = @At("RETURN"), cancellable = true)
    private void isHarvestable(BlockState blockState, CallbackInfoReturnable<Boolean> cir) {
        boolean currentValue = cir.getReturnValue();
        var newValue = BlockHelper.isHarvestable((Player) (Object) this, blockState, currentValue);

        if (newValue != currentValue) {
            cir.setReturnValue(newValue);
        }
    }
}
