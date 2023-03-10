package net.impleri.blockskills.integrations.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import net.impleri.blockskills.integrations.kubejs.events.EventsBinding;
import net.impleri.blockskills.integrations.kubejs.events.RestrictionsRegistrationEventJS;

public class BlockSkillsPlugin extends KubeJSPlugin {
    @Override
    public void registerEvents() {
        EventsBinding.GROUP.register();
    }

    public static void onStartup() {
        EventsBinding.RESTRICTIONS.post(new RestrictionsRegistrationEventJS());
    }
}
