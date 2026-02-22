package ru.motionreblur;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MotionReBlur implements ClientModInitializer {
    public static final String MOD_ID = "motionreblur";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        LOGGER.info("Motion ReBlur initialized!");
        
        // Инициализация модуля
        MotionBlurModule.getInstance();
        
        // Регистрация команды
        MotionBlurCommand.register();
        
        // Регистрация клавиши
        MotionBlurKeyBinding.register();
    }
}
