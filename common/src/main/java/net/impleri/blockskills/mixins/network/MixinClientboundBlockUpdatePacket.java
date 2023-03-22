package net.impleri.blockskills.mixins.network;

import net.impleri.blockskills.BlockHelper;
import net.impleri.blockskills.api.InterceptedClientboundPacket;
import net.minecraft.core.BlockPos;
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

    @Shadow
    @Final
    private BlockPos pos;

    @Override
    public void interceptRestrictions(ServerPlayer player) {
        var newState = BlockHelper.getReplacement(player, blockState, pos);

        if (BlockHelper.isReplacedBlock(blockState, newState)) {
            blockState = newState;
        }
    }
}
