package ru.motionreblur;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

public class ConfigScreen extends Screen {
    private final Screen parent;

    public ConfigScreen(Screen parent) {
        super(Text.literal("Motion ReBlur Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        Module mb = Module.getInstance();

        int centerX = this.width / 2;
        int startY = this.height / 2 - 60;

        ButtonWidget toggleButton = ButtonWidget.builder(
                Text.literal("Motion Blur: " + (mb.isEnabled() ? "§aВКЛ" : "§cВЫКЛ")),
                button -> {
                    mb.setEnabled(!mb.isEnabled());
                    button.setMessage(Text.literal("Motion Blur: " + (mb.isEnabled() ? "§aВКЛ" : "§cВЫКЛ")));
                }
        ).dimensions(centerX - 100, startY, 200, 20).build();

        SliderWidget strengthSlider = new SliderWidget(
                centerX - 100, startY + 30, 200, 20,
                Text.literal("Сила: " + String.format("%.1f", mb.getStrength())),
                (mb.getStrength() + 2.0) / 4.0
        ) {
            @Override
            protected void updateMessage() {
                double value = this.value * 4.0 - 2.0;
                this.setMessage(Text.literal("Сила: " + String.format("%.1f", value)));
            }

            @Override
            protected void applyValue() {
                float value = (float) (this.value * 4.0 - 2.0);
                mb.setStrength(value);
            }
        };

        ButtonWidget rrcButton = ButtonWidget.builder(
                Text.literal("Адаптация к частоте монитора: " + (mb.isUseRRC() ? "§aВКЛ" : "§cВЫКЛ")),
                button -> {
                    mb.setUseRRC(!mb.isUseRRC());
                    button.setMessage(Text.literal("Адаптация к частоте монитора: " + (mb.isUseRRC() ? "§aВКЛ" : "§cВЫКЛ")));
                }
        ).dimensions(centerX - 100, startY + 60, 200, 20).build();

        ButtonWidget qualityButton = ButtonWidget.builder(
                Text.literal("Качество: " + mb.getQualityName()),
                button -> {
                    int newQuality = (mb.getQuality() + 1) % 4;
                    mb.setQuality(newQuality);
                    button.setMessage(Text.literal("Качество: " + mb.getQualityName()));
                }
        ).dimensions(centerX - 100, startY + 90, 200, 20).build();

        SliderWidget handThresholdSlider = new SliderWidget(
                centerX - 100, startY + 120, 200, 20,
                Text.literal("Порог глубины рук: " + String.format("%.2f", mb.getHandDepthThreshold())),
                mb.getHandDepthThreshold()
        ) {
            @Override
            protected void updateMessage() {
                this.setMessage(Text.literal("Порог глубины рук: " + String.format("%.2f", this.value)));
            }

            @Override
            protected void applyValue() {
                mb.setHandDepthThreshold((float) this.value);
            }
        };

        ButtonWidget doneButton = ButtonWidget.builder(
                Text.translatable("gui.done"),
                button -> this.close()
        ).dimensions(centerX - 100, startY + 150, 200, 20).build();

        addDrawableChild(toggleButton);
        addDrawableChild(strengthSlider);
        addDrawableChild(rrcButton);
        addDrawableChild(qualityButton);
        addDrawableChild(handThresholdSlider);
        addDrawableChild(doneButton);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);

        context.drawCenteredTextWithShadow(
                this.textRenderer,
                this.title,
                this.width / 2,
                20,
                0xFFFFFF
        );

        Module mb = Module.getInstance();
        int refreshRate = MonitorInfoProvider.getRefreshRate();
        String info = "Refresh Rate: " + refreshRate + " Hz";
        context.drawCenteredTextWithShadow(
                this.textRenderer,
                Text.literal(info),
                this.width / 2,
                this.height / 2 + 80,
                0x808080
        );

        // Показываем информацию если Iris активен
        if (IrisCompat.areShadersEnabled()) {
            String shaderPack = IrisCompat.getCurrentShaderPackName();
            String irisInfo = shaderPack != null 
                ? "§aIris: " + shaderPack
                : "§aIris shaders активны";
            context.drawCenteredTextWithShadow(
                    this.textRenderer,
                    Text.literal(irisInfo),
                    this.width / 2,
                    this.height / 2 + 95,
                    0x55FF55
            );
        }
        
        if (mb.isEnabled()) {
            String hint = "Двигайте камеру, чтобы увидеть эффект";
            context.drawCenteredTextWithShadow(
                    this.textRenderer,
                    Text.literal(hint),
                    this.width / 2,
                    this.height / 2 + 110,
                    0x808080
            );
            
            String thresholdHint = "Настройте порог, если руки размываются";
            context.drawCenteredTextWithShadow(
                    this.textRenderer,
                    Text.literal(thresholdHint),
                    this.width / 2,
                    this.height / 2 + 125,
                    0x606060
            );
        }
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(parent);
        }
    }
}
