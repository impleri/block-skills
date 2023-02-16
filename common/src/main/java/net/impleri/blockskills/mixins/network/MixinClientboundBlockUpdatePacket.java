package net.impleri.blockskills.mixins.network;

import net.impleri.blockskills.BlockHelper;
import net.impleri.blockskills.api.InterceptedClientboundPacket;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClientboundBlockUpdatePacket.class)
public class MixinClientboundBlockUpdatePacket implements InterceptedClientboundPacket {
    @Shadow
    @Final
    @Mutable
    private BlockState blockState;

    @Override
    public void interceptRestrictions(ServerPlayer player) {
        var newState = BlockHelper.getReplacement(player, blockState);

        if (newState != null && !newState.equals(blockState)) {
            blockState = newState;
        }
    }
}
