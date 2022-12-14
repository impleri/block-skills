package net.impleri.blockskills;

import net.impleri.playerskills.utils.PlayerSkillsLogger;

public class BlockSkills {
    public static final String MOD_ID = "blockskills";
    public static final PlayerSkillsLogger LOGGER = PlayerSkillsLogger.create(MOD_ID, "BLOCKS");

    public static void init() {
        LOGGER.info("Loaded Block Skills");
    }
}
