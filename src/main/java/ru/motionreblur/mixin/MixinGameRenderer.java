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
    
    @Inject(
        method = "renderWorld",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/WorldRenderer;render(Lnet/minecraft/client/util/ObjectAllocator;Lnet/minecraft/client/render/RenderTickCounter;ZLnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/GameRenderer;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;)V",
            shift = At.Shift.AFTER
        )
    )
    private void afterWorldRender(RenderTickCounter tickCounter, CallbackInfo ci) {
        // Применяем motion blur ПОСЛЕ рендеринга мира, но ДО рендеринга рук
        Module.getInstance().shader.applyMotionBlurIfNeeded();
    }
}
