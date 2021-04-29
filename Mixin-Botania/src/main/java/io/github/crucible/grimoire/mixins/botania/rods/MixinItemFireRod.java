package io.github.crucible.grimoire.mixins.botania.rods;

import com.gamerforea.eventhelper.util.EventUtils;
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
import vazkii.botania.common.entity.EntityFlameRing;
import vazkii.botania.common.item.rod.ItemFireRod;

import javax.annotation.Nonnull;

@Mixin(value = ItemFireRod.class, remap = true)
public abstract class MixinItemFireRod extends Item {

    /**
     * @author EverNife
     * @reason
     *
     * Add EventHelper Check
     */
    @Nonnull
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float par8, float par9, float par10) {
        ItemStack stack = player.getHeldItem(hand);
        if(!world.isRemote && ManaItemHandler.requestManaExactForTool(stack, player, 900, false)) {
            if (EventUtils.cantBreak(player, player.getPosition())){
                return EnumActionResult.SUCCESS;
            }
            EntityFlameRing entity = new EntityFlameRing(player.world);
            entity.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
            player.world.spawnEntity(entity);

            player.getCooldownTracker().setCooldown(this, IManaProficiencyArmor.Helper.hasProficiency(player, stack) ? 1200 / 2 : 1200);

            ManaItemHandler.requestManaExactForTool(stack, player, 900, true);
            world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_BLAZE_AMBIENT, SoundCategory.PLAYERS, 1F, 1F);

            // Fix up rods from old versions which used meta instead of cooldown tracker
            if (stack.getItemDamage() > 0)
                stack.setItemDamage(0);
        }

        return EnumActionResult.SUCCESS;
    }

}