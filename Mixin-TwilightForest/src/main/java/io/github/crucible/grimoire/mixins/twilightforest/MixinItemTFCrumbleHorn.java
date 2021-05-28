package io.github.crucible.grimoire.mixins.twilightforest;

import com.gamerforea.eventhelper.util.EventUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.ForgeEventFactory;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import twilightforest.item.ItemTFCrumbleHorn;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

@Mixin(value = ItemTFCrumbleHorn.class, remap = false)
public abstract class MixinItemTFCrumbleHorn {


    @Shadow protected abstract void postTrigger(EntityLivingBase living, ItemStack stack, World world, BlockPos pos);

    @Shadow @Final private List<Pair<Predicate<IBlockState>, UnaryOperator<IBlockState>>> crumbleTransforms;

    @Shadow @Final private static int CHANCE_CRUMBLE;

    @Shadow @Final private List<Predicate<IBlockState>> harvestedStates;

    @Shadow @Final private static int CHANCE_HARVEST;


    @Shadow protected abstract int crumbleBlocksInAABB(ItemStack stack, World world, EntityLivingBase living, AxisAlignedBB box);

    /**
     * @author EverNife
     * @reason
     *
     * Only allow real players to do crumble
     * Fire break-event before doing crumble at the player's location
     */
    private int doCrumble(ItemStack stack, World world, EntityLivingBase living) {

        if ( !(living instanceof EntityPlayerMP) || living instanceof FakePlayer){
            return 0; //Deny FakePlayers to crumble
        }

        EntityPlayerMP playerMP = (EntityPlayerMP) living;

        if (EventUtils.cantBreak(playerMP, playerMP.getPosition())){
            return 0; //Stop crumble if can't break at its own location
        }

        final double range = 3.0D;
        final double radius = 2.0D;

        Vec3d srcVec = new Vec3d(living.posX, living.posY + living.getEyeHeight(), living.posZ);
        Vec3d lookVec = living.getLookVec().scale(range);
        Vec3d destVec = srcVec.add(lookVec);

        AxisAlignedBB crumbleBox = new AxisAlignedBB(destVec.x - radius, destVec.y - radius, destVec.z - radius, destVec.x + radius, destVec.y + radius, destVec.z + radius);

        return crumbleBlocksInAABB(stack, world, living, crumbleBox);
    }

    /**
     * @author EverNife
     * @reason
     *
     * Fire break-event before doing crumble
     */
    @Nonnull
    @Overwrite
    private boolean crumbleBlock(ItemStack stack, World world, EntityLivingBase living, BlockPos pos) {

        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (block.isAir(state, world, pos)) return false;

        if (EventUtils.cantBreak((EntityPlayer) living, pos)){
            return false;
        }

        for (Pair<Predicate<IBlockState>, UnaryOperator<IBlockState>> transform : crumbleTransforms) {
            if (transform.getLeft().test(state) && world.rand.nextInt(CHANCE_CRUMBLE) == 0) {
                world.setBlockState(pos, transform.getRight().apply(state), 3);
                world.playEvent(2001, pos, Block.getStateId(state));

                postTrigger(living, stack, world, pos);

                return true;
            }
        }

        for (Predicate<IBlockState> predicate : harvestedStates) {
            if (predicate.test(state) && world.rand.nextInt(CHANCE_HARVEST) == 0) {
                if (living instanceof EntityPlayer) {
                    if (block.canHarvestBlock(world, pos, (EntityPlayer) living)) {
                        world.setBlockToAir(pos);
                        block.harvestBlock(world, (EntityPlayer) living, pos, state, world.getTileEntity(pos), ItemStack.EMPTY);
                        world.playEvent(2001, pos, Block.getStateId(state));

                        postTrigger(living, stack, world, pos);

                        return true;
                    }
                } else if (ForgeEventFactory.getMobGriefingEvent(world, living)) {
                    world.destroyBlock(pos, true);

                    postTrigger(living, stack, world, pos);

                    return true;
                }
            }
        }

        return false;
    }
}