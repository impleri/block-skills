package net.impleri.blockskills.integrations.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.ScriptType;
import net.impleri.blockskills.integrations.kubejs.events.EventsBinding;
import net.impleri.blockskills.integrations.kubejs.events.RestrictionsRegistrationEventJS;

public class BlockSkillsPlugin extends KubeJSPlugin {
    public static void onStartup() {
        new RestrictionsRegistrationEventJS().post(ScriptType.SERVER, EventsBinding.RESTRICTIONS);
    }
}
