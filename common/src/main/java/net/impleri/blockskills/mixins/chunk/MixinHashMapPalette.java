package net.impleri.blockskills.mixins.chunk;

import net.impleri.blockskills.BlockHelper;
import net.minecraft.core.IdMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.CrudeIncrementalIntIdentityHashBiMap;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.HashMapPalette;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HashMapPalette.class)
abstract public class MixinHashMapPalette<T> {
    @Shadow
    @Final
    private IdMap<T> registry;
    @Shadow
    @Final
    private CrudeIncrementalIntIdentityHashBiMap<T> values;

    @Shadow
    abstract public int getSize();

    @Inject(method = "write", at = @At("HEAD"), cancellable = true)
    public void onWrite(FriendlyByteBuf arg, CallbackInfo ci) {
        if (this.getSize() > 0 && this.values.byId(0) instanceof BlockState) {
            int i = this.getSize();
            arg.writeVarInt(i);

            for (int j = 0; j < i; ++j) {
                arg.writeVarInt(BlockHelper.getReplacementId((BlockState) this.values.byId(j), null));
            }

            ci.cancel();
        }
    }
}
