package net.impleri.blockskills;

import net.impleri.playerskills.utils.PlayerSkillsLogger;

public class BlockSkills {
    public static final String MOD_ID = "blockskills";
    public static final PlayerSkillsLogger LOGGER = PlayerSkillsLogger.create(MOD_ID, "BLOCKS");

    private static final BlockEvents INSTANCE = new BlockEvents();

    public static void init() {
        LOGGER.info("Loaded Block Skills");
        INSTANCE.registerEventHandlers();
    }

    public static void enableDebug() {
        LOGGER.enableDebug();
    }
}
