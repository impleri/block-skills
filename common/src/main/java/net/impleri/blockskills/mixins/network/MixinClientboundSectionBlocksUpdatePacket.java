package net.impleri.blockskills.mixins.network;

import net.impleri.blockskills.BlockHelper;
import net.impleri.blockskills.api.InterceptedClientboundPacket;
import net.minecraft.network.protocol.game.ClientboundSectionBlocksUpdatePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClientboundSectionBlocksUpdatePacket.class)
public class MixinClientboundSectionBlocksUpdatePacket implements InterceptedClientboundPacket {
    @Shadow
    @Final
    private BlockState[] states;

    @Override
    public void interceptRestrictions(ServerPlayer player) {
        for (int i = 0; i < states.length; i++) {
            // TODO: get player
            var newState = BlockHelper.getReplacement(player, states[i]);

            if (BlockHelper.isReplacedBlock(states[i], newState)) {
                states[i] = newState;
            }
        }
    }
}
