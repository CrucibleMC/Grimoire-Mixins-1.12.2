package io.github.crucible.grimoire.mixins.botania.rods;

import com.gamerforea.eventhelper.util.EventUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.item.rod.ItemSmeltRod;

@Mixin(value = ItemSmeltRod.class, remap = false)
public abstract class MixinItemSmeltRod extends Item {

    /**
     * @author EverNife
     * @reason
     *
     * Add EventHelper Check
     * @return
     */
    @Redirect(method = "onUsingTick", at = @At(value = "INVOKE", target = "Lvazkii/botania/common/item/equipment/tool/ToolCommons;raytraceFromEntity(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;ZD)Lnet/minecraft/util/math/RayTraceResult;"), require = 1)
    private RayTraceResult place(World worldIn, Entity playerIn, boolean useLiquids, double range) {
        RayTraceResult result = ToolCommons.raytraceFromEntity(worldIn, playerIn, false, 32.0D);
        if (result != null && EventUtils.cantBreak((EntityPlayer) playerIn, result.getBlockPos())){
            return null;
        }
        return result;
    }

}