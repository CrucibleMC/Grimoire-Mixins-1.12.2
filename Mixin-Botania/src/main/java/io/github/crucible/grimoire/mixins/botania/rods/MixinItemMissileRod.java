package io.github.crucible.grimoire.mixins.botania.rods;

import com.gamerforea.eventhelper.util.EventUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import vazkii.botania.api.item.IManaProficiencyArmor;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.entity.EntityFlameRing;
import vazkii.botania.common.item.rod.ItemFireRod;
import vazkii.botania.common.item.rod.ItemMissileRod;

import javax.annotation.Nonnull;

@Mixin(value = ItemMissileRod.class, remap = false)
public abstract class MixinItemMissileRod extends Item {

    @Shadow public abstract boolean spawnMissile(World world, EntityLivingBase thrower, double x, double y, double z);

    /**
     * @author EverNife
     * @reason
     *
     * Add EventHelper Check
     */
    @Overwrite
    public void onUsingTick(ItemStack stack, EntityLivingBase living, int count) {
        if(!(living instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) living;

        if (EventUtils.cantBreak(player, player.getPosition())){
            return;
        }

        if(count != getMaxItemUseDuration(stack) && count % (IManaProficiencyArmor.Helper.hasProficiency(player, stack) ? 1 : 2) == 0 && !player.world.isRemote && ManaItemHandler.requestManaExactForTool(stack, player, 120, false)) {
            if(spawnMissile(player.world, player, player.posX + (Math.random() - 0.5 * 0.1), player.posY + 2.4 + (Math.random() - 0.5 * 0.1), player.posZ + (Math.random() - 0.5 * 0.1)))
                ManaItemHandler.requestManaExactForTool(stack, player, 120, true);

            Botania.proxy.sparkleFX(player.posX, player.posY + 2.4, player.posZ, 1F, 0.4F, 1F, 6F, 6);
        }
    }

}