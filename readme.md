# Block Skills

A library mod that exposes KubeJS methods to restrict how players see and interact with blocks. Built around
[Player Skills](https://github.com/impleri/player-skills). This interacts with both Jade, TheOneProbe (Forge), and WTHIT
(Fabric). Note that this does not alter interactions based on what items the player is holding (use `Item Skills` for
that).

## KubeJS API

When creating restrictions, you have two avenues: `replace` the block with a different block or `modify` what a player
can do with the actual block. Replacements will trump other modifications. One example where you may want to use both is
in cascading progress: when the player starts, the block is replaced with something basic. After meeting some criteria,
the player can then see and use the block (e.g. a bed) but cannot break it. Even later after meeting additional
criteria, the player can finally break the bed as well.

### Helper Utility

We provide a `BlockStateRegistry` utility object for looking up block states using the following methods:

- `for(blockName)`: Returns all block states for the given block ResourceLocation

### Register

We use the `BlockSkillEvents.register` ***startup*** event to register block restrictions. Registration requires a
replacement (`replaceWith` or `replaceWithState`). Optionally, a test (`if` or `unless`) can be supplied as a callback
function which uses a player skills condition object (`can` and `cannot` methods). If the player ***matches*** the
criteria, the following restrictions are applied. This can cascade with other restrictions, so any restrictions which
replaces a block will trump any which only add restrictions to the block. Also, any restrictions which deny the ability
will trump any which allow it. We also expose these methods to indicate what restrictions are in place for when a player
meets that condition. By default, no restrictions are set, so be sure to set actual restrictions.

#### Replacement methods

- `replaceWith`: ResourceLocation/string referencing a block. The replacement block's default block state will be used
- `replaceWithState`: BlockState ID that will replace the block (See `BlockStateRegistry.for()` above)

#### Allow Restriction Methods

- `nothing`: shorthand to apply all "allow" restrictions
- `breakable`: the block is breakable and players can break the block using the appropriate tools (if any)
- `droppable`: the block can be dropped as an item when broken if using the appropriate tool (if any)
- `usable`: the block can be used (if applicable), e.g. right clicking a crafting table will open the crafting menu

#### Deny Restriction Methods

- `everything`: shorthand to apply the below "deny" abilities
- `unbreakable`: the block cannot be broken even with the appropriate tool
- `undroppable`: the block will not be dropped as an item when broken even if using the appropriate tool
- `unusable`: the block cannot be used (if applicable)

### Examples

```js
  // Coal ore blocks cannot be mined and won't drop anything if player is at stage 1 or below
BlockSkillEvents.register(event => {
  event.restrict(
    'minecraft:coal_ore',
    r => r.everything().if(player => player.cannot('skills:stage', 2))
  );
});

// Iron ore blocks will look and act like stone
ItemSkillEvents.register(event => {
  event.restrict(
    'minecraft:iron_ore',
    r => r.replaceWithBlock('minecraft:stone').unless(player => player.can('skills:stage', 2))
  );
});
```

### Caveat

Because of how events are fired, KubeJS `BlockEvents.rightClicked` events will only be triggered with the _original_
block. Make sure that you reuse the same `player.can()` or `player.cannot()` restrictions on any skill change handlers.
For example, if you are hiding `minecraft:diamond_ore` as `minecraft:stone` but are also triggering a skill change when
a player uses redstone on diamond_ore, right clicking on the hidden diamond ore with redstone in hand will still trigger
the skill update if it's not restricted with the same conditions that the hidden block is using.

## Modpacks

Want to use this in a modpack? Great! This was designed with modpack developers in mind. No need to ask.

## TODO

- Implement tag matching
- Implement mod id matching
