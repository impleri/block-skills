package net.impleri.blockskills.mixins;

import net.impleri.blockskills.BlockHelper;
import net.impleri.blockskills.BlockSkills;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class MixinBlockStateBase {
    @Shadow
    protected abstract BlockState asState();

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void blockSkills$onUse(Level level, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
        var actualBlock = BlockHelper.getReplacement(player, asState(), blockHitResult.getBlockPos());

        if (BlockHelper.isReplacedBlock(asState(), actualBlock)) {
            BlockSkills.LOGGER.debug("Replacing {} with {} for right click.", BlockHelper.getBlockName(asState()), BlockHelper.getBlockName(actualBlock));
            cir.setReturnValue(actualBlock.use(level, player, interactionHand, blockHitResult));
        }
    }

    @Inject(method = "getDestroySpeed", at = @At("HEAD"), cancellable = true)
    private void blockSkills$onGetDestroySpeed(BlockGetter blockGetter, BlockPos blockPos, CallbackInfoReturnable<Float> cir) {
        if (!BlockHelper.isBreakable(blockGetter, blockPos)) {
            cir.setReturnValue(-1.0F);
        }
    }
}
