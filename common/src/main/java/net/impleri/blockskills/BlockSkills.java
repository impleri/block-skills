package net.impleri.blockskills;

import net.impleri.blockskills.restrictions.Restriction;
import net.impleri.playerskills.restrictions.Registry;
import net.impleri.playerskills.utils.PlayerSkillsLogger;

public class BlockSkills {
    public static final String MOD_ID = "blockskills";

    public static final PlayerSkillsLogger LOGGER = PlayerSkillsLogger.create(MOD_ID, "BLOCKS");

    public static Registry<Restriction> RESTRICTIONS = new Registry<>(MOD_ID);

    private static final BlockEvents INSTANCE = new BlockEvents();

    public static void init() {
        LOGGER.info("Loaded Block Skills");
        INSTANCE.registerEventHandlers();
        INSTANCE.registerCommands();
    }

    public static void enableDebug() {
        LOGGER.enableDebug();
    }

    public static boolean toggleDebug() {
        return LOGGER.toggleDebug();
    }
}
