package io.github.crucible.grimoire.mixins.armourersworkshop;

import com.gamerforea.eventhelper.util.EventUtils;
import moe.plushie.armourers_workshop.ArmourersWorkshop;
import moe.plushie.armourers_workshop.common.GameProfileCache;
import moe.plushie.armourers_workshop.common.init.entities.EntityMannequin;
import moe.plushie.armourers_workshop.common.init.items.ModItems;
import moe.plushie.armourers_workshop.common.inventory.ModInventory;
import moe.plushie.armourers_workshop.common.lib.EnumGuiId;
import moe.plushie.armourers_workshop.utils.TrigUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = EntityMannequin.class, remap = false)
public abstract class MixinEntityMannequin extends Entity implements GameProfileCache.IGameProfileCallback, ModInventory.IInventoryCallback {

    @Shadow private int hitCount;

    @Shadow public abstract void setRotation(float value);

    public MixinEntityMannequin(String a) {
        super(null);
        throw new RuntimeException("This code should never run!");
    }

    /**
     * @author EverNife
     * @reason Fire a break-event before breaking the manequin with normal hits!
     */
    @Overwrite
    public boolean hitByEntity(Entity entityIn) {
        if (!this.getEntityWorld().isRemote && entityIn instanceof EntityPlayer) {
            EntityPlayer entityPlayer = (EntityPlayer)entityIn;
            ItemStack itemStack = entityPlayer.getHeldItem(entityPlayer.getActiveHand());
            if (!entityPlayer.canPlayerEdit(this.getPosition(), EnumFacing.UP, itemStack)) {
                return false;
            }

            this.playSound(SoundEvents.ENTITY_ARMORSTAND_HIT, 0.8F, 1.0F);
            if (entityPlayer.capabilities.isCreativeMode) {
                this.hitCount += 200;
            } else {
                this.hitCount += 20;
            }

            if (this.hitCount > 80){
                //Mixin Start
                if (EventUtils.cantBreak(entityPlayer, this.getPosition())){
                    this.hitCount = 0;
                }
                //Mixin End
            }
        }

        return true;
    }

    /**
     * @author EverNife
     * @reason Fire a break-event with the block before changing its Position with "SHIFT_CLICK"
     */
    @Overwrite
    public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, EnumHand hand) {
        ItemStack itemStack = player.getHeldItem(hand);

        if (!player.canPlayerEdit(this.getPosition(), EnumFacing.UP, itemStack)) {
            return EnumActionResult.PASS;
            //Mixin Start
        } if (EventUtils.cantBreak(player, getPosition())) {
            return EnumActionResult.SUCCESS;
            //Mixin End
        } else {
            if (player.isSneaking()) {
                if (!this.world.isRemote) {
                    double angle = TrigUtils.getAngleDegrees(player.posX, player.posZ, this.posX, this.posZ) + 90.0D;
                    this.setRotation((float)angle);
                }
            } else if (itemStack.getItem() != ModItems.MANNEQUIN_TOOL) {
                FMLNetworkHandler.openGui(player, ArmourersWorkshop.getInstance(), EnumGuiId.WARDROBE_ENTITY.ordinal(), this.getEntityWorld(), this.getEntityId(), 0, 0);
            }

            return EnumActionResult.PASS;
        }
    }

}
