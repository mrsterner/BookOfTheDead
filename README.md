<div align="center">

<img alt="The Legemeton Icon" src="src/main/resources/assets/legemeton/icon.png" width="128">

# The Legemeton

<!-- todo: replace 494721 with your CurseForge project id -->
[![Release](https://img.shields.io/github/v/release/mrsterner/TheLegemeton?style=for-the-badge&include_prereleases&sort=semver)][releases]
[![Available For](https://img.shields.io/badge/dynamic/json?label=Available%20For&style=for-the-badge&color=34aa2f&query=gameVersionLatestFiles%5B0%5D.gameVersion&url=https%3A%2F%2Faddons-ecs.forgesvc.net%2Fapi%2Fv2%2Faddon%2F494721)][curseforge]
[![Downloads](https://img.shields.io/badge/dynamic/json?label=Downloads&style=for-the-badge&color=f16436&query=downloadCount&url=https%3A%2F%2Faddons-ecs.forgesvc.net%2Fapi%2Fv2%2Faddon%2F494721)][curseforge:files]
</div>



It is built on the [Quilt][quilt] mod loader and is available for modern
versions of [Minecraft][minecraft] Java Edition.

# Compat
To add butchering recipes, add entity to the butchering tag and make a recipe like example below. Up to 8 items can be added in the same recipe. chance is chance for item to actaully drop when a butchering attempt is made, values between 0 and 1, default a 1 (100%)
```
{
  "type": "legemeton:butchering",
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


## Licence
* Code
    - (c) 2022  [MrSterner]
    - [![License](https://img.shields.io/badge/License-MIT%201.0-cyan.svg?style=flat-square)](https://opensource.org/licenses/MIT)
* Models and Textures
    - (c) 2022  [MrSterner]
    - [![License](https://img.shields.io/badge/License-ARR-red.svg?style=flat-square)](https://opensource.org/licenses/ARR)


[curseforge]: https://curseforge.com/minecraft/mc-mods/legemeton/files
[curseforge:files]: https://curseforge.com/minecraft/mc-mods/legemeton/files
[quilt]: https://quiltmc.org/
[minecraft]: https://minecraft.net/
[releases]: https://github.com/mrsterner/TheLegemeton/releases
[mrsterner]: https://github.com/mrsterner
