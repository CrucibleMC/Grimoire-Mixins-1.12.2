package io.github.crucible.grimoire.mixins.mobgrindingutils;

import mob_grinding_utils.blocks.BlockSaw;
import mob_grinding_utils.tile.TileEntityInventoryHelper;
import mob_grinding_utils.tile.TileEntitySaw;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = TileEntitySaw.class, remap = false)
public abstract class MixinTileEntitySaw extends TileEntityInventoryHelper{

    @Shadow public boolean active;
    @Shadow public int animationTicks;
    @Shadow public int prevAnimationTicks;

    @Shadow public abstract boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState);

    @Shadow protected abstract Entity activateBlock();

    public MixinTileEntitySaw(int invtSize) {
        super(invtSize);
    }

    /**
     * @author EverNife
     * @reason
     *
     * Apply fix to this: https://github.com/vadis365/Mob-Grinding-Utils/issues/126
     *
     * from this commit:
     * https://github.com/vadis365/Mob-Grinding-Utils/commit/2811f8703a5afa5e946a77b117c8db116da1ed04#diff-81474a6e4fe3b33ff3aa484fada7dcbbc0c6ad3d51358407fda1a01eec1a3c09L61
     *
     */
    @Overwrite //update()
    public void func_73660_a() {
        if (getWorld().isRemote && active) {
            prevAnimationTicks = animationTicks;
            if (animationTicks < 360)
                animationTicks += 18;
            if (animationTicks >= 360) {
                animationTicks -= 360;
                prevAnimationTicks -= 360;
            }
        }

        if (getWorld().isRemote && !active)
            prevAnimationTicks = animationTicks = 0;

        if (!getWorld().isRemote && getWorld().getTotalWorldTime() % 10 == 0 && getWorld().getBlockState(pos).getBlock() instanceof BlockSaw)
            if (getWorld().getBlockState(pos).getValue(BlockSaw.POWERED))
                activateBlock();
    }
}