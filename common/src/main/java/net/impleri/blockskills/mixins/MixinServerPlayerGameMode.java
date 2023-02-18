package net.impleri.blockskills.mixins;

import net.impleri.blockskills.BlockHelper;
import net.impleri.blockskills.BlockSkills;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ServerPlayerGameMode.class)
public class MixinServerPlayerGameMode {
    @Shadow
    @Final
    protected ServerPlayer player;

    private BlockState replaceBlock(BlockState original, String logAction) {
        var replacement = BlockHelper.getReplacement(player, original);
        BlockSkills.LOGGER.info("Network: {} block {} (replaced by {}).", logAction, BlockHelper.getBlockName(original), BlockHelper.getBlockName(replacement));

        return replacement;
    }

    @ModifyVariable(method = "handleBlockBreakAction", at = @At(value = "LOAD"))
    public BlockState onBlockBreak(BlockState original) {
        return replaceBlock(original, "breaking");
    }

    @ModifyVariable(method = "destroyBlock", at = @At("STORE"))
    public BlockState onDestroyBlock(BlockState original) {
        return replaceBlock(original, "destroying");
    }

    @ModifyVariable(method = "useItemOn", at = @At("STORE"))
    public BlockState onUseItemOn(BlockState original) {
        return replaceBlock(original, "right clicking");
    }
}
