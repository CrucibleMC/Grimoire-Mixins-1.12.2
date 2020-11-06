package io.github.crucible.grimoire.mixins.excompressum;

import io.github.crucible.grimoire.data.excompressum.ItemDependentCache;
import net.blay09.mods.excompressum.api.ExNihiloProvider;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.HashMap;
import java.util.Map;

@Mixin(value = ExRegistro.class, remap = false)
public abstract class MixinExRegistro {

    @Shadow public static ExNihiloProvider instance;

    private static final Map<Item, ItemDependentCache<Boolean>> siftableCachedResults = new HashMap<>();

    private static boolean default_isSiftable(ItemStack itemStack) {
        IBlockState state = StupidUtils.getStateFromItemStack(itemStack);
        return state != null && instance.isSiftable(state);
    }

    /**
     * @author EverNife
     * @reason Create a cacheResult map to improve CPU over Memory
     */
    @Overwrite
    public static boolean isSiftable(ItemStack itemStack) {
        Item item = itemStack.getItem();
        int meta = itemStack.getItemDamage();

        ItemDependentCache<Boolean> siftableCache = siftableCachedResults.get(item);
        if (siftableCache != null){
            ItemDependentCache.OptionalContent<Boolean> optionalContent = siftableCache.getFromMeta(meta);
            if (optionalContent == null){
                optionalContent = siftableCache.addMeta(meta, default_isSiftable(itemStack));
            }
            return optionalContent.getContent();
        }else {
            siftableCache = new ItemDependentCache(item);
            siftableCachedResults.put(item, siftableCache);
        }

        return siftableCache.addMeta(meta, default_isSiftable(itemStack)).getContent();
    }

    /*@Overwrite
    public static boolean isSiftableWithMesh(ItemStack itemStack, SieveMeshRegistryEntry sieveMesh) {

        From my tests, isSiftableWithMesh does not impact performance as isSiftable do. Need a few more testing

    }*/



}