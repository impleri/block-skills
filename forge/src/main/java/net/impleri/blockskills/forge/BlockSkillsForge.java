package net.impleri.blockskills.forge;

import dev.architectury.platform.forge.EventBuses;
import net.impleri.blockskills.BlockSkills;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(BlockSkills.MOD_ID)
public class BlockSkillsForge {
    public BlockSkillsForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(BlockSkills.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        BlockSkills.init();
    }
}
