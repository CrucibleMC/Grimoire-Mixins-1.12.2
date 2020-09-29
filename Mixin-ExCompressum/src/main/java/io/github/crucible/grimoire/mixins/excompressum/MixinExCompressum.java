package io.github.crucible.grimoire.mixins.excompressum;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.compat.ExCompressumReloadEvent;
import net.blay09.mods.excompressum.registry.AbstractRegistry;
import net.blay09.mods.excompressum.registry.chickenstick.ChickenStickRegistry;
import net.blay09.mods.excompressum.registry.compressedhammer.CompressedHammerRegistry;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveRegistry;
import net.blay09.mods.excompressum.registry.woodencrucible.WoodenCrucibleRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ExCompressum.class, remap = false)
public abstract class MixinExCompressum {

    @Shadow
    @Final
    public static Logger logger;

    /**
     * @author brunoxkk0
     * @reason Reload ExCompressum after PostINIT because some of its drops
     * will not bet loaded properly on other mods, like JEI
     *
     * As well as some rebalance on the weights must be done after post_init
     * when recipes are changed thought other mods
     */
    @Inject(method = {"postInit"}, at = {@At("RETURN")})
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event, CallbackInfo callbackInfo) {
        logger.info("[GRIMOIRE] ExCompressoum - Start Drops RELOAD");
        AbstractRegistry.registryErrors.clear();
        ChickenStickRegistry.INSTANCE.load(ExCompressum.configDir);
        CompressedHammerRegistry.INSTANCE.load(ExCompressum.configDir);
        HeavySieveRegistry.INSTANCE.load(ExCompressum.configDir);
        WoodenCrucibleRegistry.INSTANCE.load(ExCompressum.configDir);
        logger.info("[GRIMOIRE] ExCompressoum - RELOAD DONE");
        logger.info("[GRIMOIRE] ExCompressoum - Propagating Changes");
        if (AbstractRegistry.registryErrors.size() > 0) {
            logger.warn("There were errors loading the Ex Compressum registries:");
            for (String registryError : AbstractRegistry.registryErrors) {
                logger.warn("[GRIMOIRE] ExCompressoum ERROR -  " + registryError);
            }
        }
        MinecraftForge.EVENT_BUS.post(new ExCompressumReloadEvent());
        logger.info("[GRIMOIRE] ExCompressoum - Propagation DONE");
    }
}