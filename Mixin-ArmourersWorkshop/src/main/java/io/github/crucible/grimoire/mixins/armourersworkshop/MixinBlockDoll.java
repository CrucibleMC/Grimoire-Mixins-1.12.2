package io.github.crucible.grimoire.mixins.armourersworkshop;

import com.gamerforea.eventhelper.util.EventUtils;
import moe.plushie.armourers_workshop.common.init.blocks.AbstractModBlockContainer;
import moe.plushie.armourers_workshop.common.init.blocks.BlockMannequin;
import moe.plushie.armourers_workshop.common.lib.EnumGuiId;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = BlockMannequin.class, remap = false)
public abstract class MixinBlockDoll extends AbstractModBlockContainer {

    public MixinBlockDoll(Object a) {
        super(null, null, null, false);
        throw new RuntimeException("This code should never run!");
    }

    /**
     * @author EverNife
     * @reason Fire a break-event before opening a manequin!
     */
    @Overwrite
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!playerIn.canPlayerEdit(pos, facing, playerIn.getHeldItem(hand))) {
            return false;
        }
        //Mixin Start
        if (EventUtils.cantBreak(playerIn, pos)){
            return true;
        }
        //Mixin End
        openGui(playerIn, EnumGuiId.MANNEQUIN, worldIn, pos, state, facing);
        return true;
    }

}
