package net.impleri.blockskills.mixins;

import net.impleri.blockskills.api.InterceptedClientboundPacket;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class MixinServerPlayer {
    @Inject(method = "trackChunk", at = @At("HEAD"))
    public void onSend(ChunkPos chunkPos, Packet<?> packet, CallbackInfo ci) {
        if (packet instanceof InterceptedClientboundPacket) {
            ((InterceptedClientboundPacket) packet).interceptRestrictions((ServerPlayer) (Object) this);
        }
    }
}
