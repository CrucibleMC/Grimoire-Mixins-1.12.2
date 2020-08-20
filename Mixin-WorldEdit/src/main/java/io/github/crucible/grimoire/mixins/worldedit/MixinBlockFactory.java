package io.github.crucible.grimoire.mixins.worldedit;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.extension.factory.BlockFactory;
import com.sk89q.worldedit.internal.registry.AbstractFactory;
import io.github.crucible.grimoire.data.worldedit.DefaultBlockParserFixed;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BlockFactory.class, remap = false)
public abstract class MixinBlockFactory extends AbstractFactory<BaseBlock> {

    /**
     * @author EverNife
     * @reason Add a new functionallity, being able to read the block
     * from an ItemBlock, as it's ids are different on the 1.12.2.
     */
    @Inject(method = "<init>", at = @At("RETURN"))
    public void onInit(WorldEdit worldEdit, CallbackInfo callbackInfo) {
        this.parsers.remove(this.parsers.size() - 1);//Remove original BlocKParser
        this.parsers.add(new DefaultBlockParserFixed(worldEdit));
    }

    public MixinBlockFactory(WorldEdit worldEdit) {
        super(worldEdit);
    }
}