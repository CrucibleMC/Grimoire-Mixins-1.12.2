package io.github.crucible.grimoire.mixins.thaumcraft;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import thaumcraft.common.blocks.BlockTCDevice;
import thaumcraft.common.blocks.IBlockEnabled;
import thaumcraft.common.blocks.IBlockFacingHorizontal;
import thaumcraft.common.blocks.essentia.BlockSmelter;
import thaumcraft.common.lib.utils.BlockStateUtils;

@Mixin(value = BlockSmelter.class, remap = false)
public abstract class MixinBlockSmelter extends BlockTCDevice implements IBlockEnabled, IBlockFacingHorizontal {

    public MixinBlockSmelter(Integer wat) {
        super(null, null, null);
        throw new RuntimeException("This should never run!");
    }

    /**
     * @author EverNife
     * @reason Fix "Cannot set property PropertyBool[...] for air block"
     */
    @Overwrite
    public static void setFurnaceState(World world, BlockPos pos, boolean state) {
        //[GRIMOIRE] START
        if (world.isBlockLoaded(pos) && world.isAirBlock(pos)) return;
        //[GRIMOIRE] END
        if (state != BlockStateUtils.isEnabled(world.getBlockState(pos).getBlock().getMetaFromState(world.getBlockState(pos)))) {
            TileEntity tileentity = world.getTileEntity(pos);
            keepInventory = true;
            world.setBlockState(pos, world.getBlockState(pos).withProperty(IBlockEnabled.ENABLED, state), 3);
            world.setBlockState(pos, world.getBlockState(pos).withProperty(IBlockEnabled.ENABLED, state), 3);
            if (tileentity != null) {
                tileentity.validate();
                world.setTileEntity(pos, tileentity);
            }

            keepInventory = false;
        }
    }
}