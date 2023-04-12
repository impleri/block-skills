package net.impleri.blockskills.mixins;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.impleri.blockskills.BlockHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class MixinItemStack {
    // Only used client-side for WAILA integration
    @Inject(method = "isCorrectToolForDrops", at = @At("HEAD"), cancellable = true)
    private void blockSkills$isCorrectTool(BlockState blockState, CallbackInfoReturnable<Boolean> cir) {
        if (Platform.getEnvironment() != Env.CLIENT) {
            return;
        }

        var player = Minecraft.getInstance().player;

        if (player == null) {
            return;
        }

        if (!BlockHelper.isHarvestable(player, blockState, player.blockPosition())) {
            cir.setReturnValue(false);
        }
    }
}
