package io.github.crucible.grimoire.mixins.thaumcraft;

import com.gamerforea.eventhelper.util.EventUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.block.BlockStructure;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.lib.utils.BlockUtils;

import java.util.Map;

@Mixin(value = BlockUtils.class, remap = false)
public abstract class MixinBlockUtils {

    @Shadow private static boolean removeBlock(EntityPlayer player, BlockPos pos){
        return false;
    }

    @Shadow private static boolean removeBlock(EntityPlayer player, BlockPos pos, boolean canHarvest){
        return false;
    }

    /**
     * @author EverNife
     * @reason Fix almost all griefs of brek events using thaumCraft, mainly the golem ones!
     *
     * Fire break-event before doing breaks at any case
     */
    @Overwrite
    public static boolean harvestBlock(World world, EntityPlayer p, BlockPos pos, boolean alwaysDrop, boolean silkOverride, int fortuneOverride, boolean skipEvent) {
        if (!world.isRemote && p instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP)p;

            int exp = skipEvent ? 0 : ForgeHooks.onBlockBreakEvent(world, player.interactionManager.getGameType(), player, pos);
            if (exp == -1){ //Forge Event
                return false;
            }

            //[GRIMOIRE START]
            if (EventUtils.cantBreak(p, pos)){
                return false;//Always fire Bukkit Event
            }
            //[GRIMOIRE END]

            IBlockState iblockstate = world.getBlockState(pos);
            TileEntity tileentity = world.getTileEntity(pos);
            Block block = iblockstate.getBlock();
            if ((block instanceof BlockCommandBlock || block instanceof BlockStructure) && !player.canUseCommandBlock()) {
                world.notifyBlockUpdate(pos, iblockstate, iblockstate, 3);
                return false;
            } else {
                world.playEvent((EntityPlayer)null, 2001, pos, Block.getStateId(iblockstate));
                boolean flag1 = false;
                if (player.interactionManager.isCreative()) {
                    flag1 = removeBlock(player, pos);
                    player.interactionManager.player.connection.sendPacket(new SPacketBlockChange(world, pos));
                } else {
                    ItemStack itemstack1 = player.getHeldItemMainhand();
                    boolean flag = alwaysDrop || iblockstate.getBlock().canHarvestBlock(world, pos, player);
                    flag1 = removeBlock(player, pos, flag);
                    if (flag1 && flag) {
                        ItemStack fakeStack = itemstack1.copy();
                        if (silkOverride || fortuneOverride > EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FORTUNE, player)) {
                            if (alwaysDrop || fakeStack.isEmpty()) {
                                fakeStack = new ItemStack(ItemsTC.enchantedPlaceholder);
                            }

                            Map<Enchantment, Integer> enchMap = EnchantmentHelper.getEnchantments(itemstack1);
                            if (silkOverride) {
                                enchMap.put(Enchantments.SILK_TOUCH, 1);
                            }

                            int fort = Math.max(fortuneOverride, enchMap.get(Enchantments.FORTUNE) != null ? (Integer)enchMap.get(Enchantments.FORTUNE) : 0);
                            if (fort > 0) {
                                enchMap.put(Enchantments.FORTUNE, fort);
                            }

                            EnchantmentHelper.setEnchantments(enchMap, fakeStack);
                        }

                        iblockstate.getBlock().harvestBlock(world, player, pos, iblockstate, tileentity, fakeStack);
                    }
                }

                if (!player.interactionManager.isCreative() && flag1 && exp > 0) {
                    iblockstate.getBlock().dropXpOnBlockBreak(world, pos, exp);
                }

                return flag1;
            }

        } else {
            return false;
        }
    }
}