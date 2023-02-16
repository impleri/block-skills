package net.impleri.blockskills.forge;

import dev.architectury.platform.forge.EventBuses;
import net.impleri.blockskills.BlockHelper;
import net.impleri.blockskills.BlockSkills;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(BlockSkills.MOD_ID)
public class BlockSkillsForge {
    public BlockSkillsForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(BlockSkills.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        BlockSkills.init();
    }

    @SubscribeEvent()
    public void onHarvestCheck(PlayerEvent.HarvestCheck event) {
        var canHarvest = BlockHelper.isHarvestable(event.getEntity(), event.getTargetBlock(), event.canHarvest());
        if (canHarvest != event.canHarvest()) {
            event.setCanHarvest(canHarvest);
        }
    }

    @SubscribeEvent()
    public void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        var newSpeed = BlockHelper.getBreakSpeed(event.getEntity(), event.getState(), event.getOriginalSpeed(), event.getPosition().orElse(null));
        if (newSpeed != null && newSpeed != event.getOriginalSpeed()) {
            event.setNewSpeed(newSpeed);
        }
    }
}
