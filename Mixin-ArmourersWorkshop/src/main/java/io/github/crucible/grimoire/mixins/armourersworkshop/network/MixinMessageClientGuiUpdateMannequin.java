package io.github.crucible.grimoire.mixins.armourersworkshop.network;

import io.github.crucible.grimoire.data.armourersworkshop.IHasID;
import moe.plushie.armourers_workshop.common.network.messages.client.MessageClientGuiUpdateMannequin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = MessageClientGuiUpdateMannequin.class, remap = false)
public abstract class MixinMessageClientGuiUpdateMannequin implements IHasID {

    @Shadow private int id;

    @Override
    public int getID() {
        return id;
    }

}
