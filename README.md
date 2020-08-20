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

##### IndustrialForegoing-Mixin

* Disable BlackHoleUnit EMPTY and FILL buttons, to prevent dupes on some rare cases.
* Requirements:
  * IndustrialForegoing Mod

###### WorldEdit-Mixin

* When using some specific ID on a command, if the ID is not from a BLOCK, try to check if the ID is from an ItemBlock, if it is, get it's correct block id and replace it!
* Requirements:
  * WorldEdit Mod