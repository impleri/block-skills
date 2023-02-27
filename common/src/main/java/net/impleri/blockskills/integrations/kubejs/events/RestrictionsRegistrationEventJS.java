package net.impleri.blockskills.integrations.kubejs.events;

import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.impleri.blockskills.BlockHelper;
import net.impleri.blockskills.restrictions.Registry;
import net.impleri.playerskills.utils.SkillResourceLocation;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class RestrictionsRegistrationEventJS extends EventJS {
    public void restrict(String name, @NotNull Consumer<RestrictionJS.Builder> consumer) {
        if (name.trim().endsWith(":*")) {
            var namespace = name.substring(0, name.indexOf(":"));

            restrictNamespace(namespace, consumer);
            return;
        }

        var blockName = SkillResourceLocation.of(name);
        restrictBlock(blockName, consumer);
    }

    @HideFromJS
    public void restrictBlock(ResourceLocation name, @NotNull Consumer<RestrictionJS.Builder> consumer) {
        var builder = new RestrictionJS.Builder(name);

        consumer.accept(builder);

        var block = BlockHelper.getBlock(name);
        if (BlockHelper.isEmptyBlock(block)) {
            ConsoleJS.SERVER.warn("Could not find any block named " + name);
            return;
        }

        var restriction = builder.createObject(block.defaultBlockState());
        ConsoleJS.SERVER.info("Created block restriction for " + name);
        Registry.INSTANCE.add(name, restriction);
    }

    @HideFromJS
    private void restrictNamespace(String namespace, @NotNull Consumer<RestrictionJS.Builder> consumer) {
        net.minecraft.core.Registry.BLOCK.keySet()
                .stream()
                .filter(blockName -> blockName.getNamespace().equals(namespace))
                .forEach(blockName -> restrictBlock(blockName, consumer));
    }
}
