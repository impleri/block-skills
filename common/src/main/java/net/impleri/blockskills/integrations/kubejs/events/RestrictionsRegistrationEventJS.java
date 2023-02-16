package net.impleri.blockskills.integrations.kubejs.events;

import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.impleri.blockskills.BlockHelper;
import net.impleri.blockskills.BlockSkills;
import net.impleri.blockskills.restrictions.Registry;
import net.impleri.playerskills.utils.SkillResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class RestrictionsRegistrationEventJS extends EventJS {
    public void restrict(String blockName, @NotNull Consumer<RestrictionJS.Builder> consumer) {
        var name = SkillResourceLocation.of(blockName);
        var builder = new RestrictionJS.Builder(name);

        consumer.accept(builder);

        var block = BlockHelper.getBlock(name);
        if (BlockHelper.isEmptyBlock(block)) {
            BlockSkills.LOGGER.warn("Could not find any block named %s", name);
            return;
        }

        var restriction = builder.createObject(block.defaultBlockState());
        ConsoleJS.STARTUP.info("Created block restriction for " + blockName);
        Registry.INSTANCE.add(name, restriction);
    }
}
