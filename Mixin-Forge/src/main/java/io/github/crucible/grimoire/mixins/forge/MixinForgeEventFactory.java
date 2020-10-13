package io.github.crucible.grimoire.mixins.forge;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(value = ForgeEventFactory.class, remap = false)
public abstract class MixinForgeEventFactory {

    /**
     * @author EverNife
     * @reason In some rare cases when you start your modpack on alt-tab
     * the startup can take several minutes, because some mods (CraftTweaker)
     * will try to ToolTip some ItemStacks and some mods (EnderIO) can try to check
     * if the user is pressing a Keyboard button to show some info, but, as you are on alt-tab
     * this process can take a looong time (a modpack startup from 3 min can go up to 9 min)
     */
    @Overwrite
    public static ItemTooltipEvent onItemTooltip(ItemStack itemStack, @Nullable EntityPlayer entityPlayer, List<String> toolTip, ITooltipFlag flags) {
        ItemTooltipEvent event = new ItemTooltipEvent(itemStack, entityPlayer, toolTip, flags);
        if (!FMLClientHandler.instance().isLoading()){
            MinecraftForge.EVENT_BUS.post(event);
        }
        return event;
    }
}