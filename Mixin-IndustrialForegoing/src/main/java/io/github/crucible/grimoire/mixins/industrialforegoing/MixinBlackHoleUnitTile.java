package io.github.crucible.grimoire.mixins.industrialforegoing;

import com.buuz135.industrial.proxy.client.infopiece.IHasDisplayStack;
import com.buuz135.industrial.tile.CustomSidedTileEntity;
import com.buuz135.industrial.tile.misc.BlackHoleUnitTile;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.ndrei.teslacorelib.netsync.SimpleNBTMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = BlackHoleUnitTile.class, remap = false)
public abstract class MixinBlackHoleUnitTile extends CustomSidedTileEntity implements IHasDisplayStack {

    protected MixinBlackHoleUnitTile(int entityTypeId) {
        super(entityTypeId);
    }

    /**
     * @author EverNife
     * @reason EMPTY and FILL function from BlackHoleUnit can dupe itens in some rare cases.
     */
    @Overwrite
    @Nullable
    protected SimpleNBTMessage processClientMessage(@Nullable String messageType, @Nullable EntityPlayerMP player, @NotNull NBTTagCompound compound) {
        return super.processClientMessage(messageType, player, compound);
    }
}