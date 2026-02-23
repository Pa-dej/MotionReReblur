package ru.motionreblur;

import net.fabricmc.loader.api.FabricLoader;

/**
 * Класс для проверки совместимости с Iris Shaders
 */
public class IrisCompat {
    private static Boolean irisLoaded = null;
    private static Boolean shadersEnabled = null;
    
    /**
     * Проверяет, загружен ли мод Iris
     */
    public static boolean isIrisLoaded() {
        if (irisLoaded == null) {
            irisLoaded = FabricLoader.getInstance().isModLoaded("iris");
        }
        return irisLoaded;
    }
    
    /**
     * Проверяет, активны ли шейдеры Iris в данный момент
     * Использует рефлексию для доступа к API Iris
     */
    public static boolean areShadersEnabled() {
        if (!isIrisLoaded()) {
            return false;
        }
        
        // Кешируем результат только если шейдеры уже были включены
        // Это позволяет пользователю включить/выключить шейдеры во время игры
        try {
            Class<?> irisApiClass = Class.forName("net.irisshaders.iris.api.v0.IrisApi");
            Object irisApiInstance = irisApiClass.getMethod("getInstance").invoke(null);
            Object config = irisApiClass.getMethod("getConfig").invoke(irisApiInstance);
            
            Class<?> configClass = Class.forName("net.irisshaders.iris.api.v0.IrisApiConfig");
            boolean enabled = (boolean) configClass.getMethod("areShadersEnabled").invoke(config);
            
            shadersEnabled = enabled;
            return enabled;
        } catch (Exception e) {
            // Если не удалось получить статус через API, считаем что шейдеры выключены
            MotionReBlur.LOGGER.debug("Could not check Iris shader status: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Получает название текущего шейдерпака (если активен)
     */
    public static String getCurrentShaderPackName() {
        if (!isIrisLoaded() || !areShadersEnabled()) {
            return null;
        }
        
        try {
            Class<?> irisApiClass = Class.forName("net.irisshaders.iris.api.v0.IrisApi");
            Object irisApiInstance = irisApiClass.getMethod("getInstance").invoke(null);
            Object config = irisApiClass.getMethod("getConfig").invoke(irisApiInstance);
            
            Class<?> configClass = Class.forName("net.irisshaders.iris.api.v0.IrisApiConfig");
            Object shaderPackName = configClass.getMethod("getShaderPackName").invoke(config);
            
            return shaderPackName != null ? shaderPackName.toString() : null;
        } catch (Exception e) {
            MotionReBlur.LOGGER.debug("Could not get shader pack name: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Проверяет, активны ли шейдеры Iris (для информационных целей)
     * Motion blur работает вместе с Iris
     */
    public static boolean isIrisActive() {
        return isIrisLoaded() && areShadersEnabled();
    }
}
