package io.github.crucible.grimoire.mixins.botania.rods;

import com.gamerforea.eventhelper.util.EventUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.botania.common.item.rod.ItemDirtRod;

@Mixin(value = ItemDirtRod.class, remap = false)
public abstract class MixinItemDirtRod {

    /**
     * @author EverNife
     * @reason
     *
     * Add EventHelper Check
     *
     * This method is used by these rods:
     *  ItemCobbleRod
     *  ItemDirtRod
     */
    @Inject(method = "place", at = @At("HEAD"), require = 1, cancellable = true)
    private static void place(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ, Block block, int cost, float r, float g, float b, CallbackInfoReturnable<EnumActionResult> cir) {
        if (EventUtils.cantBreak(player, pos)){
            cir.setReturnValue(EnumActionResult.PASS);
        }
    }
}