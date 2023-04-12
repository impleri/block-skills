package net.impleri.blockskills.mixins;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.impleri.blockskills.BlockHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class MixinBlockStateBaseHarvestable {

    @Shadow
    protected abstract BlockState asState();

    @Inject(method = "requiresCorrectToolForDrops", at = @At("HEAD"), cancellable = true)
    private void blockSkills$requireCorrectTool(CallbackInfoReturnable<Boolean> cir) {
        if (Platform.getEnvironment() != Env.CLIENT) {
            return;
        }

        var player = Minecraft.getInstance().player;

        if (player == null) {
            return;
        }

        if (!BlockHelper.isHarvestable(player, asState(), player.blockPosition())) {
            cir.setReturnValue(true);
        }
    }
}
