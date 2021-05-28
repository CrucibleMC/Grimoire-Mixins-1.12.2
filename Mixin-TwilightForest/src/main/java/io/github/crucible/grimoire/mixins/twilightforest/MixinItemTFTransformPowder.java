package io.github.crucible.grimoire.mixins.twilightforest;

import com.gamerforea.eventhelper.util.EventUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import twilightforest.TwilightForestMod;
import twilightforest.item.ItemTF;
import twilightforest.item.ItemTFTransformPowder;

import java.util.Map;
import java.util.UUID;

@Mixin(value = ItemTFTransformPowder.class, remap = false)
public abstract class MixinItemTFTransformPowder extends ItemTF {

    @Shadow @Final private Map<ResourceLocation, ResourceLocation> transformMap;

    /**
     * @author EverNife
     * @reason
     *
     * Only allow real players to do crumble
     * Fire break-event before doing crumble at the player's location
     */
    @Overwrite//itemInteractionForEntity
    public boolean func_111207_a(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {

        if (target.isDead) return false;

        ResourceLocation location = transformMap.get(EntityList.getKey(target));
        if (location == null) return false;

        if (target.world.isRemote) {
            return EntityList.isRegistered(location);
        }

        if (EventUtils.cantBreak(player, target.getPosition())){
            return true;
        }

        Entity newEntity = EntityList.createEntityByIDFromName(location, target.world);
        if (newEntity == null) return false;

        newEntity.setLocationAndAngles(target.posX, target.posY, target.posZ, target.rotationYaw, target.rotationPitch);
        if (newEntity instanceof EntityLiving) {
            ((EntityLiving) newEntity).onInitialSpawn(target.world.getDifficultyForLocation(new BlockPos(target)), null);
        }

        try { // try copying what can be copied
            UUID uuid = newEntity.getUniqueID();
            newEntity.readFromNBT(target.writeToNBT(newEntity.writeToNBT(new NBTTagCompound())));
            newEntity.setUniqueId(uuid);
        } catch (Exception e) {
            TwilightForestMod.LOGGER.warn("Couldn't transform entity NBT data: {}", e);
        }

        target.world.spawnEntity(newEntity);
        target.setDead();
        stack.shrink(1);

        if (target instanceof EntityLiving) {
            ((EntityLiving) target).spawnExplosionParticle();
            ((EntityLiving) target).spawnExplosionParticle();
        }
        target.playSound(SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, 1.0F + itemRand.nextFloat(), itemRand.nextFloat() * 0.7F + 0.3F);

        return true;
    }

}