package io.github.crucible.grimoire.mixins.excompressum;

import net.blay09.mods.excompressum.tile.TileAutoSieveBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = TileAutoSieveBase.class, remap = false)
public abstract class MixinTileAutoSieveBase {

    /**
     * @author EverNife
     * @reason Entirely disable FoodBoost from AutoSieves!
     *
     */
    @Overwrite
    public float getFoodBoost() {
        return 1F;
    }

    /**
     * @author EverNife
     * @reason Entirely disable FoodBoost from AutoSieves!
     *
     */
    @Overwrite
    public void setFoodBoost(int foodBoostTicks, float foodBoost) {
        //Do not set food boost
    }
}