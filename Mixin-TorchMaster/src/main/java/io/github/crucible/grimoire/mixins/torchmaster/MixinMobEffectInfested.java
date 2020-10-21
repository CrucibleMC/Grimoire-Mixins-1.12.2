package io.github.crucible.grimoire.mixins.torchmaster;

import net.xalcon.torchmaster.common.tiles.TileEntityFeralFlareLantern;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = TileEntityFeralFlareLantern.class, remap = false)
public abstract class MixinMobEffectInfested {

    /**
     * @author EverNife
     * @reason
     *
     * Disable FeralFlare Lantern because it works as a ChunkLoader in some
     * cases
     */
    @Overwrite //update()
    public void func_73660_a() {
        // Entirely disable FeralFlareLanter

        // Maybe someone in the future wants to fix it, or even open a issue for the mod itself, as it is still being developed.
    }
}