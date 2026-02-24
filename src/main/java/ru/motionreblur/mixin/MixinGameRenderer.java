package ru.motionreblur.mixin;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.motionreblur.Module;

@Mixin(value = GameRenderer.class, priority = 1100)
public class MixinGameRenderer {

    // Применяем blur ПОСЛЕ рендеринга мира, но ДО рендеринга рук
    @Inject(
            method = "renderWorld",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/GameRenderer;renderHand(Lnet/minecraft/client/render/Camera;FLorg/joml/Matrix4f;)V",
                    shift = At.Shift.BEFORE
            )
    )
    private void beforeRenderHand(RenderTickCounter tickCounter, CallbackInfo ci) {
        if (!Module.getInstance().isEnabled()) {
            return;
        }

        // Применяем motion blur к текущему framebuffer (мир уже отрендерен, руки еще нет)
        Module.getInstance().shader.applyMotionBlurBeforeHands();
    }
}
