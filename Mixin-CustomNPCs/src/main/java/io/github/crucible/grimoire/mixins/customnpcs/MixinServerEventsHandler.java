package io.github.crucible.grimoire.mixins.customnpcs;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import noppes.npcs.ServerEventsHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = ServerEventsHandler.class, remap = false)
public abstract class MixinServerEventsHandler {

    /**
     * @author EverNife
     * @reason
     *
     * If you do not use CustomNPCs's script, its not worthy to spend processing in this
     *
     *
     * If you have a modpack that uses lots of machines and crafting processing, like the mod EnderIO,
     *  this part of the CustomNPCs's scripts can create an overhead on most of your "itemTransfers",
     *  so, if you do not use CustomNPCs's scripts, its better to disable it!
     */
    @Overwrite
    @SubscribeEvent
    public void attachItem(AttachCapabilitiesEvent<ItemStack> event) {
        //ItemStackWrapper.register(event);
    }

}