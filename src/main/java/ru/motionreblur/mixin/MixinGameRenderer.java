package ru.motionreblur.mixin;

import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.motionreblur.Module;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {
    
    @Inject(method = "renderHand", at = @At("HEAD"))
    private void beforeRenderHand(CallbackInfo ci) {
        // Применяем motion blur ДО рендеринга рук
        Module.getInstance().shader.applyMotionBlurIfNeeded();
    }
}
