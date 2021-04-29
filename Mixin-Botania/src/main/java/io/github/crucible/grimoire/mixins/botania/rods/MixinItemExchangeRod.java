package io.github.crucible.grimoire.mixins.botania.rods;

import com.gamerforea.eventhelper.util.EventUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.botania.common.item.rod.ItemExchangeRod;

@Mixin(value = ItemExchangeRod.class, remap = false)
public abstract class MixinItemExchangeRod {

    /**
     * @author EverNife
     * @reason
     *
     * Add EventHelper Check
     */
    @Inject(method = "exchange", at = @At("HEAD"), require = 1, cancellable = true)
    public void exchange(World world, EntityPlayer player, BlockPos pos, ItemStack stack, IBlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (EventUtils.cantBreak(player, pos)){
            cir.setReturnValue(false);
        }
    }
}