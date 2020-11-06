package io.github.crucible.grimoire.mixins.forge;

import io.github.crucible.grimoire.data.forge.BetterCraftCache;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import javax.annotation.Nullable;

@Mixin(targets = "net.minecraft.item.crafting.CraftingManager")
public abstract class MixinCraftingManager {

    /**
     * @author EverNife
     * @reason Cache NON_NBT Crafting Matrix by [Item and Meta],
     * to prevent dispendious time on large modpacks with thousands of craftings
     */
    @Overwrite(remap = false)//findMatchingRecipe
    @Nullable
    public static IRecipe func_192413_b(InventoryCrafting craftMatrix, World worldIn) {
        return BetterCraftCache.findMatchingRecipe(craftMatrix, worldIn);
    }

    /**
     * @author EverNife
     * @reason Cache NON_NBT Crafting Matrix by [Item and Meta],
     * to prevent dispendious time on large modpacks with thousands of craftings
     */
    @Overwrite(remap = false)//findMatchingResult
    public static NonNullList<ItemStack> func_180303_b(InventoryCrafting craftMatrix, World worldIn) {
        IRecipe iRecipe = BetterCraftCache.findMatchingRecipe(craftMatrix, worldIn);
        if (iRecipe != null) return iRecipe.getRemainingItems(craftMatrix);

        NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack>withSize(craftMatrix.getSizeInventory(), ItemStack.EMPTY);

        for (int i = 0; i < nonnulllist.size(); ++i) {
            nonnulllist.set(i, craftMatrix.getStackInSlot(i));
        }

        return nonnulllist;
    }
}