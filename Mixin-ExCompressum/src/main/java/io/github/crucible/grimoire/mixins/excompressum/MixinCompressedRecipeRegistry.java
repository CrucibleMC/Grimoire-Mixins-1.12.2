package io.github.crucible.grimoire.mixins.excompressum;

import io.github.crucible.grimoire.data.excompressum.ItemDependentCache;
import net.blay09.mods.excompressum.registry.compressor.CompressedRecipe;
import net.blay09.mods.excompressum.registry.compressor.CompressedRecipeRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(value = CompressedRecipeRegistry.class, remap = false)
public abstract class MixinCompressedRecipeRegistry {

    @Shadow @Final private static List<CompressedRecipe> recipesSmall;
    @Shadow @Final private static List<CompressedRecipe> recipes;
    private static final Map<Item, ItemDependentCache<CompressedRecipe>> alternativeCachedResults = new HashMap<>();

    /**
     * @author EverNife
     * @reason Clear alternative cache when reloaded;
     */
    @Inject(method = {"reload"}, at = {@At("INVOKE")})
    private static void reload(CallbackInfo callbackInfo) {
        alternativeCachedResults.clear();
    }

    /**
     * @author EverNife
     * @reason Change the current cacheResults map to a ItemDependentCache cached map
     */
    @Overwrite
    @Nullable
    public static CompressedRecipe getRecipe(ItemStack itemStack) {
        if(itemStack.hasTagCompound()) {
            return null;
        }
        Item item = itemStack.getItem();
        int meta = itemStack.getItemDamage();

        ItemDependentCache<CompressedRecipe> compressedRecipeCache = alternativeCachedResults.get(item);

        if (compressedRecipeCache != null){
            ItemDependentCache.OptionalContent<CompressedRecipe> optionalContent = compressedRecipeCache.getFromMeta(meta);
            if (optionalContent == null){
                optionalContent = compressedRecipeCache.addMeta(meta, getRecipeFrom(itemStack));
            }
            return optionalContent.getContent();
        }else {
            compressedRecipeCache = new ItemDependentCache(item);
            alternativeCachedResults.put(item, compressedRecipeCache);
        }

        return compressedRecipeCache.addMeta(meta, getRecipeFrom(itemStack)).getContent();
    }

    private static CompressedRecipe getRecipeFrom(ItemStack itemStack){
        for(CompressedRecipe recipe : recipes) {
            if(recipe.getIngredient().apply(itemStack)) {
                return recipe;
            }
        }
        for(CompressedRecipe recipe : recipesSmall) {
            if(recipe.getIngredient().apply(itemStack)) {
                return recipe;
            }
        }
        return null;
    }
}