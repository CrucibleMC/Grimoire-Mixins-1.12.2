package io.github.crucible.grimoire.mixins.extrabotany;

import com.meteor.extrabotany.common.item.relic.ItemFirstFractal;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = ItemFirstFractal.class, remap = false)
public abstract class MixinItemFirstFractal {

    /**
     * @author EverNife
     * @reason
     *
     * This should only be execute on the ClientSide
     */
    @Overwrite
    @SubscribeEvent
    public void leftClick(PlayerInteractEvent.LeftClickEmpty evt) {
        //Do nothing
    }
}