package net.impleri.blockskills.integrations.kubejs.events;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public abstract class EventsBinding {
    public static final EventGroup GROUP = EventGroup.of("BlockSkillEvents");

    public static final EventHandler RESTRICTIONS = GROUP.startup("register", () -> RestrictionsRegistrationEventJS.class);
}
