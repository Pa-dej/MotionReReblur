package ru.motionreblur;

public class Module {
    private static final Module instance = new Module();
    public final Shader shader;

    private boolean enabled = false;
    private float strength = -0.8f;
    private boolean useRRC = true;
    private int quality = 2;

    private Module() {
        shader = new Shader(this);
        shader.registerShaderCallbacks();
    }

    public static Module getInstance() {
        return instance;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        MotionReBlur.LOGGER.info("Motion Blur " + (enabled ? "enabled" : "disabled"));
    }

    public float getStrength() {
        return strength;
    }

    public void setStrength(float strength) {
        this.strength = Math.max(-2.0f, Math.min(2.0f, strength));
        shader.updateBlurStrength(this.strength);
        MotionReBlur.LOGGER.info("Motion Blur strength set to " + this.strength);
    }

    public boolean isUseRRC() {
        return useRRC;
    }

    public void setUseRRC(boolean useRRC) {
        this.useRRC = useRRC;
        MotionReBlur.LOGGER.info("Refresh Rate Scaling " + (useRRC ? "enabled" : "disabled"));
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = Math.max(0, Math.min(3, quality));
        MotionReBlur.LOGGER.info("Motion Blur quality set to " + getQualityName());
    }

    public String getQualityName() {
        return switch (quality) {
            case 0 -> "Низкое";
            case 1 -> "Среднее";
            case 2 -> "Высокое";
            case 3 -> "Ультра";
            default -> "Среднее";
        };
    }
}

