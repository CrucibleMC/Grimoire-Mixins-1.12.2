package io.github.crucible.grimoire.mixins.template;

import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = Block.class, remap = false)
public abstract class MixinBlock {

}