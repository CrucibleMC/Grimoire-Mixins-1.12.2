package io.github.crucible.grimoire.mixins.techreborn;

import com.gamerforea.eventhelper.util.EventUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.ItemHandlerHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import reborncore.common.util.FluidUtils;
import techreborn.items.ItemDynamicCell;

@Mixin(value = ItemDynamicCell.class, remap = false)
public abstract class MixinItemDynamicCell extends Item {

    /**
     * @author EverNife
     * @reason Fire bukkit break-event on fill or unfill fluid cells
     * to prevent grief on protected regions.
     */
    @Overwrite
    public ActionResult<ItemStack> func_77659_a(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack heldItem = playerIn.getHeldItem(handIn);

        RayTraceResult target = this.rayTrace(worldIn, playerIn, true);
        if (target == null || target.typeOfHit != RayTraceResult.Type.BLOCK)
            return ActionResult.newResult(EnumActionResult.PASS, heldItem);

        //GRIMOIRE Start
        if (EventUtils.cantBreak(playerIn, target.getBlockPos())){
            return ActionResult.newResult(EnumActionResult.PASS, heldItem);
        }
        //GRIMOIRE END

        ItemStack singleContainer = heldItem.copy();
        singleContainer.setCount(1);

        // try to pick up a fluid from the world
        FluidActionResult filledResult = FluidUtil.tryPickUpFluid(singleContainer, playerIn, worldIn, target.getBlockPos(), target.sideHit);
        if (filledResult.isSuccess()) {
            ItemHandlerHelper.giveItemToPlayer(playerIn, filledResult.result);

            if (!playerIn.capabilities.isCreativeMode)
                heldItem.shrink(1); // Remove consumed empty container

            return ActionResult.newResult(EnumActionResult.SUCCESS, heldItem);
        }

        // try to place a fluid in the world
        FluidActionResult emptiedResult = FluidUtil.tryPlaceFluid(playerIn, worldIn, target.getBlockPos().offset(target.sideHit), singleContainer, FluidUtils.getFluidHandler(singleContainer).drain(ItemDynamicCell.CAPACITY, false));
        if (emptiedResult.isSuccess()) {
            ItemHandlerHelper.giveItemToPlayer(playerIn, emptiedResult.result);

            if (!playerIn.capabilities.isCreativeMode)
                heldItem.shrink(1); // Remove consumed empty container

            return ActionResult.newResult(EnumActionResult.SUCCESS, heldItem);
        }

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}