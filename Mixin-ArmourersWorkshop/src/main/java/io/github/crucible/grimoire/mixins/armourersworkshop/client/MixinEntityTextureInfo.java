package io.github.crucible.grimoire.mixins.armourersworkshop.client;

import moe.plushie.armourers_workshop.client.render.EntityTextureInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.awt.image.BufferedImage;

@Mixin(value = EntityTextureInfo.class, remap = false)
public abstract class MixinEntityTextureInfo {

    /**
     * @author EverNife
     * @reason Fix ArrayIndexOutOfBoundsException
     */
    @Overwrite
    private void paintTexture(BufferedImage texture, int x, int y, int rgb) {
        try {
            texture.setRGB(x, y, rgb);
        }catch (Exception ignored){
            return;
        }
        // Paint left leg.
        if (x < 16 & y >= 16 & y < 32) {
            if (y >= 20) {
                if (x < 12) {
                    texture.setRGB(15 + (12 - x), y + 32, rgb);
                } else {
                    // Back face
                    texture.setRGB(27 + (4 - (x - 12)), y + 32, rgb);
                }
            } else {
                // Top and bottom
                if (x < 8) {
                    texture.setRGB(15 + (8 - (x - 4)), y + 32, rgb);
                } else {
                    texture.setRGB(23 + (8 - (x - 4)), y + 32, rgb);
                }

            }
        }

        // Paint left arm.
        if (x >= 40 & x < 56 & y >= 16 & y < 32) {
            if (y >= 20) {
                if (x < 52) {
                    texture.setRGB(12 - (x - 40) + 31, y + 32, rgb);
                } else {
                    // Back face
                    texture.setRGB(4 - (x - 52) + 43, y + 32, rgb);
                }
            } else {
                // Top and bottom
                if (x < 48) {
                    texture.setRGB((8 - (x - 40) + 4) + 31, y + 32, rgb);
                } else {
                    texture.setRGB((8 - (x - 48) + 4) + 31, y + 32, rgb);
                }

            }
        }
    }

}
