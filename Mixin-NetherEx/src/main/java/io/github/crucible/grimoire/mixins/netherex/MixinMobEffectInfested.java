package io.github.crucible.grimoire.mixins.netherex;

import logictechcorp.netherex.mobeffect.MobEffectInfested;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MobEffectInfested.class, remap = false)
public abstract class MixinMobEffectInfested {

    /**
     * @author EverNife
     * @reason
     *
     * Disable Spore Spread and Spore Effect Spawn outside of Nether
     */
    @Inject(method = {"canSpreadSpores"}, at = @At("HEAD"), cancellable = true)
    public void canSpreadSpores(EntityLivingBase entity, CallbackInfoReturnable<Boolean> callbackInfo) {
        if (entity.world.provider.getDimensionType() != DimensionType.NETHER){
            callbackInfo.setReturnValue(false);
        }
    }

}