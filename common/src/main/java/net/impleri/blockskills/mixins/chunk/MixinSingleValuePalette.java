package net.impleri.blockskills.mixins.chunk;

import net.impleri.blockskills.BlockHelper;
import net.minecraft.core.IdMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.SingleValuePalette;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(SingleValuePalette.class)
public class MixinSingleValuePalette<T> {
    @Shadow
    @Final
    private IdMap<T> registry;

    @Shadow
    @Nullable
    private T value;

    @Inject(method = "write", at = @At("HEAD"), cancellable = true)
    public void onWrite(FriendlyByteBuf arg, CallbackInfo ci) {
        if (this.value != null && this.value instanceof BlockState) {
            arg.writeVarInt(BlockHelper.getReplacementId((BlockState) this.value, null));

            ci.cancel();
        }
    }
}
