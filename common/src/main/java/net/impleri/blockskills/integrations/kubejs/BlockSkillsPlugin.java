package net.impleri.blockskills.integrations.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import net.impleri.blockskills.integrations.kubejs.events.EventsBinding;
import net.impleri.blockskills.integrations.kubejs.events.RestrictionsRegistrationEventJS;
import net.minecraft.server.MinecraftServer;

public class BlockSkillsPlugin extends KubeJSPlugin {
    @Override
    public void registerEvents() {
        EventsBinding.GROUP.register();
    }

    public static void onStartup(MinecraftServer minecraftServer) {
        EventsBinding.RESTRICTIONS.post(new RestrictionsRegistrationEventJS(minecraftServer));
    }
}
