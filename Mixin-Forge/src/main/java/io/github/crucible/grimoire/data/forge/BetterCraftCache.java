package io.github.crucible.grimoire.data.forge;

import io.github.crucible.grimoire.data.forge.data.CraftMatrixCacheKey;
import io.github.crucible.grimoire.data.forge.data.OptionalContent;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class BetterCraftCache {

    public static boolean hasAnyNBT(InventoryCrafting craftMatrix){
        for (int i = 0; i < craftMatrix.getSizeInventory(); i++) {
            ItemStack itemStack = craftMatrix.getStackInSlot(i);
            if (itemStack.hasTagCompound()) return true;
        }
        return false;
    }

    private static Int2ObjectLinkedOpenHashMap<OptionalContent<IRecipe>> NON_NBT_CRAFT_CACHE = new Int2ObjectLinkedOpenHashMap();

    private static OptionalContent<IRecipe> getOrCreateCachedRecipe(InventoryCrafting craftMatrix, World worldInIgnored){
        CraftMatrixCacheKey matrixKey = new CraftMatrixCacheKey(craftMatrix);

        OptionalContent<IRecipe> optionalContent = NON_NBT_CRAFT_CACHE.getAndMoveToFirst(matrixKey.hashCode());

        if (optionalContent == null){
            optionalContent = new OptionalContent<>();
            NON_NBT_CRAFT_CACHE.putAndMoveToFirst(matrixKey.hashCode(), optionalContent);
        }

        //FMLLog.log.warn("Recipe HashCode: " + matrixKey.hashCode());
        return optionalContent;
    }

    public static IRecipe default_findMatchingRecipe(InventoryCrafting craftMatrix, World worldIn){
        for (IRecipe irecipe : CraftingManager.REGISTRY) {
            if (irecipe.matches(craftMatrix, worldIn)) {
                return irecipe;
            }
        }
        return null;
    }

    public static IRecipe findMatchingRecipe(InventoryCrafting craftMatrix, World worldIn){
        boolean hasNBT = BetterCraftCache.hasAnyNBT(craftMatrix);
        if (!hasNBT){
            OptionalContent<IRecipe> optionalContent = BetterCraftCache.getOrCreateCachedRecipe(craftMatrix, worldIn);
            if (!optionalContent.hasContent()){
                optionalContent.setContent(default_findMatchingRecipe(craftMatrix, worldIn));
            }
            return optionalContent.getContent();
        }
        IRecipe iRecipe = default_findMatchingRecipe(craftMatrix, worldIn);
        return iRecipe;
    }

}
