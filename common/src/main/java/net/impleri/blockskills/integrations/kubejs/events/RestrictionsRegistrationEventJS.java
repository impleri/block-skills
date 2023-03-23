package net.impleri.blockskills.integrations.kubejs.events;

import dev.latvian.mods.kubejs.server.ServerEventJS;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.impleri.blockskills.BlockHelper;
import net.impleri.blockskills.BlockSkills;
import net.impleri.playerskills.utils.RegistrationType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class RestrictionsRegistrationEventJS extends ServerEventJS {
    private final MinecraftServer server;

    public RestrictionsRegistrationEventJS(MinecraftServer server) {
        this.server = server;
    }

    public void restrict(String blockName, @NotNull Consumer<RestrictionJS.Builder> consumer) {
        RegistrationType<Block> registrationType = new RegistrationType<Block>(blockName, net.minecraft.core.Registry.BLOCK_REGISTRY);

        registrationType.ifNamespace(namespace -> restrictNamespace(namespace, consumer));
        registrationType.ifName(name -> restrictBlock(name, consumer));
        registrationType.ifTag(tag -> restrictTag(tag, consumer));
    }

    @HideFromJS
    public void restrictBlock(ResourceLocation name, @NotNull Consumer<RestrictionJS.Builder> consumer) {
        var builder = new RestrictionJS.Builder(name, server);

        consumer.accept(builder);

        var block = BlockHelper.getBlock(name);
        if (BlockHelper.isEmptyBlock(block)) {
            ConsoleJS.SERVER.warn("Could not find any block named " + name);
            return;
        }

        var restriction = builder.createObject(block.defaultBlockState());
        ConsoleJS.SERVER.info("Created block restriction for " + name);
        BlockSkills.RESTRICTIONS.add(name, restriction);
    }

    @HideFromJS
    private void restrictNamespace(String namespace, @NotNull Consumer<RestrictionJS.Builder> consumer) {
        ConsoleJS.SERVER.info("Creating block restrictions for namespace " + namespace);
        net.minecraft.core.Registry.BLOCK.keySet()
                .stream()
                .filter(blockName -> blockName.getNamespace().equals(namespace))
                .forEach(blockName -> restrictBlock(blockName, consumer));
    }

    @HideFromJS
    private void restrictTag(TagKey<Block> tag, @NotNull Consumer<RestrictionJS.Builder> consumer) {
        ConsoleJS.SERVER.info("Creating block restrictions for tag " + tag.location());
        net.minecraft.core.Registry.BLOCK.stream()
                .filter(block -> block.defaultBlockState().is(tag))
                .forEach(block -> restrictBlock(BlockHelper.getBlockName(block), consumer));
    }
}
