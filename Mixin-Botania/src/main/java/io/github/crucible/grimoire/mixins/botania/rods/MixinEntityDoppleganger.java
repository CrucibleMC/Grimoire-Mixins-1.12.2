package io.github.crucible.grimoire.mixins.botania.rods;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketRemoveEntityEffect;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import vazkii.botania.api.boss.IBotaniaBoss;
import vazkii.botania.common.entity.EntityDoppleganger;

import java.util.List;
import java.util.stream.Collectors;

@Mixin(value = EntityDoppleganger.class, remap = false)
public abstract class MixinEntityDoppleganger extends EntityLiving implements IBotaniaBoss, IEntityAdditionalSpawnData {

    public MixinEntityDoppleganger(String s) {
        super(null);
        throw new RuntimeException("This should never Run!");
    }

    /**
     * @author EverNife
     * @reason
     *
     * Fix NPE on clear potions
     */
    @Overwrite
    private void clearPotions(EntityPlayer player) {
        int posXInt = MathHelper.floor(posX);
        int posZInt = MathHelper.floor(posZ);

        List<Potion> potionsToRemove = player.getActivePotionEffects().stream()
                .filter(effect -> effect != null && effect.getPotion() != null && effect.getDuration() < 160 && effect.getIsAmbient() && !effect.getPotion().isBadEffect())
                .map(PotionEffect::getPotion)
                .distinct()
                .collect(Collectors.toList());

        for (Potion potion : potionsToRemove) {
            player.removePotionEffect(potion);
            ((WorldServer) world).getPlayerChunkMap().getEntry(posXInt >> 4, posZInt >> 4).sendPacket(new SPacketRemoveEntityEffect(player.getEntityId(), potion));
        }
    }
}