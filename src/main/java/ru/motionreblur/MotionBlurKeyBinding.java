package ru.motionreblur;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class MotionBlurKeyBinding {
    private static KeyBinding openGuiKey;

    public static void register() {
        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.motionreblur.open_gui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_M,
                "category.motionreblur"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGuiKey.wasPressed()) {
                client.setScreen(new MotionBlurConfigScreen(client.currentScreen));
            }
        });
    }
}
