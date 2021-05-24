package io.github.crucible.grimoire.mixins.extrabotany;

import com.meteor.extrabotany.api.ExtraBotanyAPI;
import com.meteor.extrabotany.common.entity.EntitySubspace;
import com.meteor.extrabotany.common.entity.EntitySubspaceSpear;
import com.meteor.extrabotany.common.entity.EntityThrowableCopy;
import com.meteor.extrabotany.common.entity.gaia.EntityVoidHerrscher;
import com.meteor.extrabotany.common.item.ModItems;
import com.meteor.extrabotany.common.item.relic.ItemExcaliber;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import vazkii.botania.common.entity.EntityManaBurst;

@Mixin(value = EntitySubspace.class, remap = false)
public abstract class MixinEntitySubspace extends EntityThrowableCopy {

    @Shadow public abstract int getLiveTicks();

    @Shadow public abstract int getInterval();

    @Shadow public abstract int getCount();

    @Shadow public abstract int getDelay();

    @Shadow public abstract void setCount(int delay);

    @Shadow public abstract int getType();

    public MixinEntitySubspace(String s) {
        super(null);
        throw new RuntimeException("This should never Run!");
    }


    /**
     * @author EverNife
     * @reason
     *
     * This should only be execute on the ClientSide
     */
    @Overwrite()//onUpdate
    public void func_70071_h_() {
        motionX = 0;
        motionY = 0;
        motionZ = 0;

        super.onUpdate();

        if (ticksExisted < getDelay())
            return;

        if (ticksExisted > getLiveTicks() + getDelay())
            setDead();
        EntityLivingBase thrower = getThrower();
        if (!world.isRemote && (thrower == null || thrower.isDead)) {
            setDead();
            return;
        }

        if (!world.isRemote)
            if (getType() == 0) {
                if (ticksExisted % getInterval() == 0 && getCount() < 5 && ticksExisted > getDelay() + 5
                        && ticksExisted < getLiveTicks() - getDelay() - 10) {
                    if (!(thrower instanceof EntityPlayer))
                        setDead();
                    EntityPlayer player = (EntityPlayer) getThrower();
                    if (ExtraBotanyAPI.cantAttack(player, player)) {
                        setDead();
                    }
                    EntityManaBurst burst = ItemExcaliber.getBurst(player, new ItemStack(ModItems.excaliber));
                    burst.setPosition(posX, posY, posZ);
                    burst.setColor(0XFFAF00);
                    player.world.spawnEntity(burst);
                    setCount(getCount() + 1);
                }
            } else if (getType() == 1) {
                if (ticksExisted > getDelay() + 8 && getCount() < 1) {
                    EntitySubspaceSpear spear = new EntitySubspaceSpear(world, thrower);
                    spear.setDamage(12);
                    if (thrower instanceof EntityVoidHerrscher) {
                        spear.setDamage(14);
                        spear.setLiveTicks(1);
                    }
                    spear.setLife(100);
                    spear.rotationYaw = thrower.rotationYaw;
                    spear.setPitch(-thrower.rotationPitch);
                    spear.setRotation(MathHelper.wrapDegrees(-thrower.rotationYaw + 180));
                    spear.shoot(thrower, thrower.rotationPitch, thrower.rotationYaw, 0.0F, 2.45F, 1.0F);
                    spear.setPosition(posX, posY - 0.75F, posZ);
                    thrower.world.spawnEntity(spear);
                    setCount(getCount() + 1);
                }
            } else if (getType() == 2) {
                if (ticksExisted % getInterval() == 0 && getCount() < 6 && ticksExisted > getDelay() + 5
                        && ticksExisted < getLiveTicks() - getDelay() - 10) {
                    if (!(thrower instanceof EntityVoidHerrscher))
                        setDead();
                    EntityVoidHerrscher herr = (EntityVoidHerrscher) getThrower();
                    if (herr.getPlayersAround().isEmpty())
                        setDead();
                    //[GRIMOIRE START]
                    else
                    //[GRIMOIRE END]
                        if (ExtraBotanyAPI.cantAttack(thrower, herr.getPlayersAround().get(0))) { //Missed an ELSE here
                        setDead();
                    }
                    EntityManaBurst burst = ItemExcaliber.getBurst(herr.getPlayersAround().get(0),
                            new ItemStack(ModItems.excaliber));
                    burst.setPosition(posX, posY, posZ);
                    burst.setColor(0XFFD700);
                    burst.shoot(thrower, thrower.rotationPitch + 15F, thrower.rotationYaw, 0F, 1F, 0F);
                    thrower.world.spawnEntity(burst);
                    setCount(getCount() + 1);
                }
            }
    }

}