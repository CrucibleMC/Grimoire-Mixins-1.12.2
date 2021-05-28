package io.github.crucible.grimoire.mixins.extrabotany;

import com.meteor.extrabotany.api.entity.IEntityWithShield;
import com.meteor.extrabotany.common.entity.gaia.EntityVoidHerrscher;
import net.minecraft.entity.EntityCreature;
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

import java.util.List;
import java.util.stream.Collectors;

@Mixin(value = EntityVoidHerrscher.class, remap = false)
public abstract class MixinEntityVoidHerrscher extends EntityCreature implements IBotaniaBoss, IEntityWithShield, IEntityAdditionalSpawnData {

    public MixinEntityVoidHerrscher(String s) {
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