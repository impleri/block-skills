package net.impleri.blockskills.integrations.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import net.impleri.blockskills.integrations.kubejs.events.EventsBinding;
import net.impleri.blockskills.integrations.kubejs.events.RestrictionsRegistrationEventJS;
import net.impleri.blockskills.restrictions.Registry;

public class BlockSkillsPlugin extends KubeJSPlugin {
    @Override
    public void registerEvents() {
        EventsBinding.GROUP.register();
    }

    public static void onStartup() {
        Registry.INSTANCE.clear();
        EventsBinding.RESTRICTIONS.post(new RestrictionsRegistrationEventJS());
    }
}
