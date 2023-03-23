package net.impleri.blockskills.integrations.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.ScriptType;
import net.impleri.blockskills.integrations.kubejs.events.EventsBinding;
import net.impleri.blockskills.integrations.kubejs.events.RestrictionsRegistrationEventJS;
import net.minecraft.server.MinecraftServer;

public class BlockSkillsPlugin extends KubeJSPlugin {
    public static void onStartup(MinecraftServer server) {
        new RestrictionsRegistrationEventJS(server).post(ScriptType.SERVER, EventsBinding.RESTRICTIONS);
    }
}
