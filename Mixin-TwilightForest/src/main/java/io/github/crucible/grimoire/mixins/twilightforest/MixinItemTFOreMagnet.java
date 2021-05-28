package io.github.crucible.grimoire.mixins.twilightforest;

import com.gamerforea.eventhelper.util.EventUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import twilightforest.item.ItemTFOreMagnet;

import javax.annotation.Nonnull;

@Mixin(value = ItemTFOreMagnet.class, remap = false)
public abstract class MixinItemTFOreMagnet {

    @Shadow protected abstract Vec3d getOffsetLook(EntityLivingBase living, float yawOffset, float pitchOffset);

    @Shadow
    public static int doMagnet(World world, BlockPos usePos, BlockPos destPos) {
        return 0;
    }

    /**
     * @author EverNife
     * @reason
     *
     * Only allow real players to do magnet
     * Fire break-event before doing magnet
     */
    @Nonnull
    @Overwrite
    private int doMagnet(World world, EntityLivingBase living, float yawOffset, float pitchOffset) {

        if ( !(living instanceof EntityPlayerMP) || living instanceof FakePlayer){
            return 1; //Needs to return 1 to stop the process!
        }

        // find vector 32 blocks from look
        double range = 32.0D;
        Vec3d srcVec = new Vec3d(living.posX, living.posY + living.getEyeHeight(), living.posZ);
        Vec3d lookVec = getOffsetLook(living, yawOffset, pitchOffset);
        Vec3d destVec = srcVec.add(lookVec.x * range, lookVec.y * range, lookVec.z * range);

        BlockPos srcPos = new BlockPos(srcVec);
        BlockPos destPos = new BlockPos(destVec);

        EntityPlayerMP playerMP = (EntityPlayerMP) living;
        if (EventUtils.cantBreak(playerMP, srcPos)
                || EventUtils.cantBreak(playerMP, destPos)){
            return 1; //Needs to return 1 to stop the process!
        }

        return doMagnet(world, srcPos, destPos);
    }
}