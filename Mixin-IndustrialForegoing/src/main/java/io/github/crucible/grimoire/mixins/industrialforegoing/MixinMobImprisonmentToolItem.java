package io.github.crucible.grimoire.mixins.industrialforegoing;

import com.buuz135.industrial.item.MobImprisonmentToolItem;
import com.buuz135.industrial.proxy.BlockRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = MobImprisonmentToolItem.class, remap = false)
public abstract class MixinMobImprisonmentToolItem {

    /**
     * @author EverNife
     * @reason For some obscure reason this method always return false, so
     * even if you blacklist a mob on the MobDuplicator, the MobImprisonmentTool will
     * still be able to capture mobs...
     */
    @Overwrite
    public boolean isBlacklisted(String entity) {
        return BlockRegistry.mobDuplicatorBlock.blacklistedEntities.contains(entity);
    }
}