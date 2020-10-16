package io.github.crucible.grimoire.data.excompressum;

import net.minecraft.item.Item;

import java.util.HashMap;

public class ItemDependentCache<T> {

    final Item item;
    final HashMap<Integer, OptionalContent> metamap = new HashMap<>();

    public ItemDependentCache(Item item) {
        this.item = item;
    }

    public OptionalContent<T> addMeta(int meta, T content){
        OptionalContent<T> optionalContent = new OptionalContent(content);
        metamap.put(meta, optionalContent);
        return optionalContent;
    }

    public OptionalContent<T> getFromMeta(int meta){
        return metamap.get(meta);
    }

    public static class OptionalContent<T>{
        T content;

        public OptionalContent(T content) {
            this.content = content;
        }

        public T getContent() {
            return content;
        }
    }
}
