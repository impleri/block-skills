package net.impleri.blockskills.integrations.kubejs.events;

import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.impleri.blockskills.BlockHelper;
import net.impleri.blockskills.BlockSkills;
import net.impleri.blockskills.restrictions.Restriction;
import net.impleri.playerskills.restrictions.AbstractRegistrationEventJS;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class RestrictionsRegistrationEventJS extends AbstractRegistrationEventJS<Block, Restriction, RestrictionJS.Builder> {

    @HideFromJS
    public RestrictionsRegistrationEventJS(MinecraftServer server) {
        super(server, "block", Registry.BLOCK);
    }

    @Override
    @HideFromJS
    public void restrictOne(ResourceLocation name, @NotNull Consumer<RestrictionJS.Builder> consumer) {
        var builder = new RestrictionJS.Builder(name, server);

        consumer.accept(builder);

        var block = BlockHelper.getBlock(name);
        if (BlockHelper.isEmptyBlock(block)) {
            ConsoleJS.SERVER.warn("Could not find any block named " + name);
            return;
        }

        var restriction = builder.createObject(block);
        ConsoleJS.SERVER.info("Created block restriction for " + name);
        BlockSkills.RESTRICTIONS.add(name, restriction);
    }

    @Override
    @HideFromJS
    public Predicate<Block> isTagged(TagKey<Block> tag) {
        return block -> block.defaultBlockState().is(tag);
    }

    @Override
    @HideFromJS
    public ResourceLocation getName(Block resource) {
        return BlockHelper.getBlockName(resource);
    }
}
