package io.github.crucible.grimoire.mixins.exnihilocreatio;

import exnihilocreatio.barrel.BarrelFluidHandler;
import exnihilocreatio.barrel.IBarrelMode;
import exnihilocreatio.config.ModConfig;
import exnihilocreatio.tiles.BaseTileEntity;
import exnihilocreatio.tiles.TileBarrel;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = TileBarrel.class, remap = false)
public abstract class MixinTileBarrel extends BaseTileEntity implements ITickable {

    @Shadow private IBarrelMode mode;

    @Shadow private BarrelFluidHandler tank;

    private int tickCount = 0;

    /**
     * @author EverNife
     * @reason This light logic can take up to 30% of server tps
     * on overcrowded servers... This alteration will make the tile tick
     * once every two ticks and will not update the light logic anymore.
     *
     * TO DO: Transfer the LightLogic to somewhere else
     */
    @Overwrite//onUpdate
    public void func_73660_a() {
        if (getWorld().isRemote)
            return;

        if (tickCount++ % 2 == 0){//[Grimoire] - Tick only once every two ticks
            return;
        }

        if (ModConfig.mechanics.shouldBarrelsFillWithRain && (mode == null || mode.getName().equalsIgnoreCase("fluid"))) {
            BlockPos plusY = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
            if (getWorld().isRainingAt(plusY)) {
                FluidStack stack = new FluidStack(FluidRegistry.WATER, 4); //double the fill amount as we have reduced the tick count
                tank.fill(stack, true);
            }
        }
        if (mode != null)
            mode.update((TileBarrel) (Object)this);

        /*[Grimoire] - Remove light logic
        if (getBlockType().getLightValue(getWorld().getBlockState(pos), getWorld(), pos) != getWorld().getLight(pos)) {
            getWorld().checkLight(pos);
            PacketHandler.sendToAllAround(new MessageCheckLight(pos), this);
        }
        */
    }
}