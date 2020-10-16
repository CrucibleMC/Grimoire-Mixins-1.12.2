# Grimoire-Mixins!

Collection of mixins for the core-mod [Grimoire](https://github.com/CrucibleMC/Grimoire)

### What is it?

Grimoire-Mixins are coremods that will be injected inside other mods trying to fix and\or change their natural behavior.

Some modules may require other modules.

### How to Install?

* 0 - Notice that the majority of these mixins are Server Side Only! The ones who have any Client Fix are pointed out in their own description below.
* 1 - Install [Grimoire](https://github.com/CrucibleMC/Grimoire) (Just download and put it inside mods folder)
* 2 - Start the server once then turn it off, a folder called grimoire will be generated at the side of the 'mods' and 'config' folders.
* 3 - Download any Grimoire-Mixins module you want from [here](https://github.com/CrucibleMC/Grimoire-Mixins-1.12.2/releases)
* 4 - Put all the Grimoire-Mixins you want to use inside the folder grimoire, remember to check out the mixins requirements for each one.
* 5 - Start the server again.

### Modules

There are several modules for different things... just use the ones you want :V
Seriously, don't throw all of them if you don't need them!

### Configuration

Most of these mixins features can be disabled, just open the "mixin.modname.json" inside the jars and delete the correspondent class you do not want to be applied.

###### Forge-Mixin

* Fix a bug ON THE CLIENT caused by mods that request ItemTooltiping before the client has fully started, in some cases where the user alt-tab on the launching process the startup can take from 3 minutes to 9 minutes long.
* Target:
  * CLIENT-SIDE
  
###### ExCompressum-Mixin

* Reload the mod after the POST_INIT event to fix some sync issues with drop chances and JEI
* Entirely disable AutoSieves Food Boost
* Requirements:
  * ExCompressum Mod
* Target:
  * CLIENT-SIDE & SERVER-SIDE
  
###### ExNihiloCreatio-Mixin

* Remove the light update logic from TileBarrel, this drastically improves the Barrel performance.
* Requirements:
  * ExNihiloCreatio Mod

###### IndustrialForegoing-Mixin

* Disable BlackHoleUnit EMPTY and FILL buttons, to prevent dupes on some rare cases.
* Requirements:
  * IndustrialForegoing Mod
  
###### NetherEX-Mixin

* Disable Spore Spread and Spore Effect Spawn outside of Nether
* Requirements:
  * NetherEX Mod
  
###### TechReborn-Mixin

* Add blockBreak-event on ItemDynamicCell use action to prevent grief.
* Requirements:
  * TechReborn Mod
  * EventHelper Mod
  
###### ThaumCraft-Mixin

* Attempt to "registerComplexObjectTags" assync, reducing StartupGame Time on large modpacks in more than 1 minute.
  * This is still being tested, may not be good for 'every case scenario'
* Requirements:
  * ThaumCraft Mod
* Target:
  * CLIENT-SIDE & SERVER-SIDE

###### WorldEdit-Mixin

* When using some specific ID on a command, if the ID is not from a BLOCK, try to check if the ID is from an ItemBlock, if it is, get it's correct block id and replace it!
* Requirements:
  * WorldEdit Mod