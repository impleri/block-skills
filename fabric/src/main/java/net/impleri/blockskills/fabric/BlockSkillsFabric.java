package net.impleri.blockskills.fabric;

import net.fabricmc.api.ModInitializer;
import net.impleri.blockskills.BlockSkills;

public class BlockSkillsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        BlockSkills.init();
    }
}
