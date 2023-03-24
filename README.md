<div align="center">

<img alt="The BotD Icon" src="src/main/resources/assets/book_of_the_dead/icon.png" width="128">

# The Book of the Dead

<!-- todo: replace 494721 with your CurseForge project id -->
[![Release](https://img.shields.io/github/v/release/mrsterner/TheBotD?style=for-the-badge&include_prereleases&sort=semver)][releases]
[![Available For](https://img.shields.io/badge/dynamic/json?label=Available%20For&style=for-the-badge&color=34aa2f&query=gameVersionLatestFiles%5B0%5D.gameVersion&url=https%3A%2F%2Faddons-ecs.forgesvc.net%2Fapi%2Fv2%2Faddon%2F494721)][curseforge]
[![Downloads](https://img.shields.io/badge/dynamic/json?label=Downloads&style=for-the-badge&color=f16436&query=downloadCount&url=https%3A%2F%2Faddons-ecs.forgesvc.net%2Fapi%2Fv2%2Faddon%2F494721)][curseforge:files]
</div>



It is built on the [Quilt][quilt] mod loader and is available for modern
versions of [Minecraft][minecraft] Java Edition.

# Compat
To add butchering recipes, add entity to the butchering tag and make a recipe like example below. Up to 8 items can be added in the same recipe. chance is chance for item to actaully drop when a butchering attempt is made, values between 0 and 1, default a 1 (100%)
```
{
  "type": "book_of_the_dead:butchering",
  "entity_type": "minecraft:sheep",
  "results": [
    {
      "item": "minecraft:stick",
      "count": 1,
      "chance": 1
    },
    {
      "item": "minecraft:iron_sword",
      "count": 1,
      "nbt": {
        "Damage": 240
      },
      "chance": 1
    }
  ]
}
```
For rituals the following recipe is an example
```
{
  "type": "book_of_the_dead:ritual",
  "ritual": "book_of_the_dead:basic",
  "requireBotD": false,
  "requireEmeraldTablet": true,
  "duration": 160,

  "inputs": [
    {
      "item" : "minecraft:stick"
    }
  ],
  "outputs": [
    {
      "item" : "minecraft:stick"
    }
  ],
  "sacrifices": [],
  "summons": [
    {
      "entity": "minecraft:sheep"
    }
  ],
  "commands": [
    {
      "command": "execute positioned {pos} run setblock ~ ~10 ~ minecraft:grass_block",
      "type": "start"
    }
  ],
  "statusEffects": [
    {
      "id" : "minecraft:speed",
      "amplifier" : 1,
      "duration": 20
    }
  ]
}

```

## Licence
* Code
    - (c) 2022  [MrSterner]
    - [![License](https://img.shields.io/badge/License-MIT%201.0-cyan.svg?style=flat-square)](https://opensource.org/licenses/MIT)
* Models and Textures
    - (c) 2022  [MrSterner]
    - [![License](https://img.shields.io/badge/License-ARR-red.svg?style=flat-square)](https://opensource.org/licenses/ARR)


[curseforge]: https://curseforge.com/minecraft/mc-mods/book_of_the_dead/files
[curseforge:files]: https://curseforge.com/minecraft/mc-mods/book_of_the_dead/files
[quilt]: https://quiltmc.org/
[minecraft]: https://minecraft.net/
[releases]: https://github.com/mrsterner/TheBotD/releases
[mrsterner]: https://github.com/mrsterner
