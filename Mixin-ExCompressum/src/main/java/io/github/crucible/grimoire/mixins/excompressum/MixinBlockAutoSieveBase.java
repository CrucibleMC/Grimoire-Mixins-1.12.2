package io.github.crucible.grimoire.mixins.excompressum;

import com.mojang.authlib.GameProfile;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.block.BlockAutoSieveBase;
import net.blay09.mods.excompressum.handler.GuiHandler;
import net.blay09.mods.excompressum.tile.TileAutoSieveBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = BlockAutoSieveBase.class, remap = false)
public abstract class MixinBlockAutoSieveBase {

    /**
     * @author EverNife
     * @reason Entirely disable FoodBoost from AutoSieves!
     */
    @Overwrite//onBlockActivated
    public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            ItemStack heldItem = player.getHeldItem(hand);
            if (!heldItem.isEmpty()) {
                TileAutoSieveBase tileEntity = (TileAutoSieveBase) world.getTileEntity(pos);
                if (tileEntity != null) {
                    // Disable ItemFood handling
                    /*if (heldItem.getItem() instanceof ItemFood) {
                        ItemFood itemFood = (ItemFood) heldItem.getItem();
                        if (tileEntity.getFoodBoost() <= 1f) {
                            tileEntity.setFoodBoost((int) (itemFood.getSaturationModifier(heldItem) * 640), Math.max(1f, itemFood.getHealAmount(heldItem) * 0.75f));
                            if (!player.capabilities.isCreativeMode) {
                                ItemStack returnStack = itemFood.onItemUseFinish(heldItem, world, FakePlayerFactory.getMinecraft((WorldServer) world));
                                if (returnStack != heldItem) {
                                    player.setHeldItem(hand, returnStack);
                                }
                            }
                            world.playEvent(2005, pos, 0);
                        }
                        return true;
                    } else */
                    if (heldItem.getItem() == Items.NAME_TAG && heldItem.hasDisplayName()) {
                        tileEntity.setCustomSkin(new GameProfile(null, heldItem.getDisplayName()));
                        if (!player.capabilities.isCreativeMode) {
                            heldItem.shrink(1);
                        }
                        return true;
                    }
                }
            }
            if (!player.isSneaking()) {
                player.openGui(ExCompressum.instance, GuiHandler.GUI_AUTO_SIEVE, world, pos.getX(), pos.getY(), pos.getZ());
            }
        }
        return true;
    }
}