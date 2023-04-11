# Block Skills

A library mod to control how players see and interact with fluids using skill-based restrictions created in KubeJS
scripts. This is similar to Ore Stages.

[![CurseForge](https://cf.way2muchnoise.eu/short_737102.svg)](https://www.curseforge.com/minecraft/mc-mods/block-skills)
[![Modrinth](https://img.shields.io/modrinth/dt/block-skills?color=bcdeb7&label=%20&logo=modrinth&logoColor=096765&style=plastic)](https://modrinth.com/mod/block-skills)
[![MIT license](https://img.shields.io/github/license/impleri/block-skills?color=bcdeb7&label=Source&logo=github&style=flat)](https://github.com/impleri/block-skills)
[![Discord](https://img.shields.io/discord/1093178610950623233?color=096765&label=Community&logo=discord&logoColor=bcdeb7&style=plastic)](https://discord.com/invite/avxJgbaUmG)
[![1.19.2](https://img.shields.io/maven-metadata/v?label=1.19.2&color=096765&metadataUrl=https%3A%2F%2Fmaven.impleri.org%2Fminecraft%2Fnet%2Fimpleri%2Fblock-skills-1.19.2%2Fmaven-metadata.xml&style=flat)](https://github.com/impleri/block-skills#developers)
[![1.18.2](https://img.shields.io/maven-metadata/v?label=1.18.2&color=096765&metadataUrl=https%3A%2F%2Fmaven.impleri.org%2Fminecraft%2Fnet%2Fimpleri%2Fblock-skills-1.18.2%2Fmaven-metadata.xml&style=flat)](https://github.com/impleri/block-skills#developers)

### xSkills Mods

[Player Skills](https://github.com/impleri/player-skills)
| [Block Skills](https://github.com/impleri/block-skills)
| [Dimension Skills](https://github.com/impleri/dimension-skills)
| [Fluid Skills](https://github.com/impleri/fluid-skills)
| [Item Skills](https://github.com/impleri/item-skills)
| [Mob Skills](https://github.com/impleri/mob-skills)

## Concepts

This mod leans extensively on Player Skills by creating and consuming the Skill-based Restrictions. Out of the box, this
mod can restrict whether a block can be mined, "used", or if it drops anything when mined. It also provides a way to
mask blocks in world by replacing them with either air or another block (e.g. make all diamond ore appear **and drop**
as stone). Note that this does not alter interactions based on what items the player is holding or how the player can
use the block as an item. However, Item Skills provides that complementary functionality.

## KubeJS API

When creating restrictions, you have two avenues: `replace` the block with a different block or `modify` what a player
can do with the actual block. Replacements will trump other modifications. One example where you may want to use both is
in cascading progress: when the player starts, the block is replaced with something basic. After meeting some criteria,
the player can then see and use the block (e.g. a bed) but cannot break it. Even later after meeting additional
criteria, the player can finally break the bed as well.

### Register

We use the `BlockSkillEvents.register` ***server*** event to register block restrictions. If the player ***matches***
the criteria, the following restrictions are applied. This can cascade with other restrictions, so any restrictions
which replaces a block will trump any which only add restrictions to the block. Also, any restrictions which deny the
ability will trump any which allow it. We also expose these methods to indicate what restrictions are in place for when
a player meets that condition. By default, no restrictions are set, so be sure to set actual
restrictions. [See Player Skills documentation for the shared API](https://github.com/impleri/player-skills#kubejs-restrictions-api).

#### Replacement methods

- `replaceWithBlock(block: string)` - Replace the targeted block with the named replacement. The replacement block's
  default block state will be used.
- `replaceWithAir()` - Replaces the block with air (it's completely hidden!)

#### Allow Restriction Methods

- `nothing()` - shorthand to apply all "allow" restrictions
- `breakable()` - the block is breakable and players can break the block using the appropriate tools (if any)
- `harvestable()` - the block can be harvested as an item when broken if using the appropriate tool (if any)
- `usable()` - the block can be used (if applicable), e.g. right clicking a crafting table will open the crafting menu

#### Deny Restriction Methods

- `everything()` - shorthand to apply the below "deny" abilities
- `unbreakable()` - the block cannot be broken even with the appropriate tool
- `unharvestable()` - the block will not be dropped as an item when broken even if using the appropriate tool
- `unusable()` - the block cannot be used (if applicable)

### Examples

```js
BlockSkillEvents.register(event => {
  // Vanilla MC blocks won't drop anything if player is at stage 1 or below
  event.restrict(
    'minecraft:*',
    r => r.unharvestable().if(player => player.cannot('skills:stage', 2))
  );

  // Vanilla MC blocks are unbreakable
  event.restrict("@minecraft", (is) => {
    is.unbreakable();
  });

  // Every block tagged as a log is unbreakable
  event.restrict("#logs", (is) => {
    is.unbreakable();
  });

  // Coal ore blocks cannot be mined and won't drop anything if player is at stage 1 or below
  event.restrict(
    'minecraft:coal_ore',
    r => r.everything().if(player => player.cannot('skills:stage', 2))
  );

  // Iron ore blocks will look and act like stone
  event.restrict(
    'minecraft:iron_ore',
    r => r.replaceWithBlock('minecraft:stone').unless(player => player.can('skills:stage', 2))
  );
});
```

### Caveat

Because of how events are fired, KubeJS `BlockEvents.rightClicked` events will only be triggered with the _original_
block. Make sure that you reuse the same `player.can()` or `player.cannot()` restrictions on any relevant skill change
handlers connected to interacting with a block. For example, if you are hiding `minecraft:diamond_ore`
as `minecraft:stone` but are also triggering a skill change when a player uses redstone on diamond_ore, right clicking
on the hidden diamond ore with redstone in hand will still trigger the skill update if it's not restricted with the same
conditions that the hidden block is using.

## Developers

Add the following to your `build.gradle`. I depend
on [Architectury API](https://github.com/architectury/architectury-api), [KubeJS](https://github.com/KubeJS-Mods/KubeJS),
and [PlayerSkills](https://github.com/impleri/player-skills), so you'll need those as well.

```groovy
dependencies {
    // Common should always be included 
    modImplementation "net.impleri:block-skills-${minecraft_version}:${blockskills_version}"
    // Plus forge
    modApi "net.impleri:block-skills-${minecraft_version}-forge:${blockskills_version}"
    // Or fabric
    modApi "net.impleri:block-skills-${minecraft_version}-fabric:${blockskills_version}"
}
repositories {
    maven {
        url = "https://maven.impleri.org/minecraft"
        name = "Impleri Mods"
        content {
            includeGroup "net.impleri"
        }
    }
}
```

## Modpacks

Want to use this in a modpack? Great! This was designed with modpack developers in mind. No need to ask.
