package io.github.crucible.grimoire.data.forge.data;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

import java.util.Objects;

public class CraftMatrixCacheKey {

    final int hashCode; //Cache hashcode too

    public CraftMatrixCacheKey(InventoryCrafting craftMatrix) {
        Object[] objects = new Object[craftMatrix.getSizeInventory() * 2];
        for (int i = 0; i < craftMatrix.getSizeInventory(); i++) {
            ItemStack stack = craftMatrix.getStackInSlot(i);
            objects[i] = stack.getItem();
            objects[i + craftMatrix.getSizeInventory()] = objects[i] != Items.AIR ? stack.getItemDamage() : 0;
        }
        hashCode = Objects.hash(objects);
    }

    @Override
    public boolean equals(Object o) {
        CraftMatrixCacheKey that = (CraftMatrixCacheKey) o;
        return hashCode == that.hashCode;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }
}
