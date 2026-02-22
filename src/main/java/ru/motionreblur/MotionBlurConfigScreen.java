package ru.motionreblur;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

public class MotionBlurConfigScreen extends Screen {
    private final Screen parent;
    private SliderWidget strengthSlider;
    private ButtonWidget toggleButton;
    private ButtonWidget rrcButton;
    private ButtonWidget qualityButton;
    
    public MotionBlurConfigScreen(Screen parent) {
        super(Text.literal("Motion ReBlur Settings"));
        this.parent = parent;
    }
    
    @Override
    protected void init() {
        MotionBlurModule mb = MotionBlurModule.getInstance();
        
        int centerX = this.width / 2;
        int startY = this.height / 2 - 60;
        
        // Кнопка включения/выключения
        toggleButton = ButtonWidget.builder(
            Text.literal("Motion Blur: " + (mb.isEnabled() ? "§aВКЛ" : "§cВЫКЛ")),
            button -> {
                mb.setEnabled(!mb.isEnabled());
                button.setMessage(Text.literal("Motion Blur: " + (mb.isEnabled() ? "§aВКЛ" : "§cВЫКЛ")));
            }
        ).dimensions(centerX - 100, startY, 200, 20).build();
        
        // Слайдер силы размытия
        strengthSlider = new SliderWidget(
            centerX - 100, startY + 30, 200, 20,
            Text.literal("Сила: " + String.format("%.1f", mb.getStrength())),
            (mb.getStrength() + 2.0) / 4.0 // Нормализация от -2..2 до 0..1
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
        
        // Кнопка RRC
        rrcButton = ButtonWidget.builder(
            Text.literal("Адаптация к частоте монитора: " + (mb.isUseRRC() ? "§aВКЛ" : "§cВЫКЛ")),
            button -> {
                mb.setUseRRC(!mb.isUseRRC());
                button.setMessage(Text.literal("Адаптация к частоте монитора: " + (mb.isUseRRC() ? "§aВКЛ" : "§cВЫКЛ")));
            }
        ).dimensions(centerX - 100, startY + 60, 200, 20).build();
        
        // Кнопка качества
        qualityButton = ButtonWidget.builder(
            Text.literal("Качество: " + mb.getQualityName()),
            button -> {
                int newQuality = (mb.getQuality() + 1) % 4;
                mb.setQuality(newQuality);
                button.setMessage(Text.literal("Качество: " + mb.getQualityName()));
            }
        ).dimensions(centerX - 100, startY + 90, 200, 20).build();
        
        // Кнопка "Готово"
        ButtonWidget doneButton = ButtonWidget.builder(
            Text.translatable("gui.done"),
            button -> this.close()
        ).dimensions(centerX - 100, startY + 120, 200, 20).build();
        
        addDrawableChild(toggleButton);
        addDrawableChild(strengthSlider);
        addDrawableChild(rrcButton);
        addDrawableChild(qualityButton);
        addDrawableChild(doneButton);
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        
        // Заголовок
        context.drawCenteredTextWithShadow(
            this.textRenderer,
            this.title,
            this.width / 2,
            20,
            0xFFFFFF
        );
        
        // Информация о refresh rate
        MotionBlurModule mb = MotionBlurModule.getInstance();
        int refreshRate = MonitorInfoProvider.getRefreshRate();
        String info = "Refresh Rate: " + refreshRate + " Hz";
        context.drawCenteredTextWithShadow(
            this.textRenderer,
            Text.literal(info),
            this.width / 2,
            this.height / 2 + 80,
            0x808080
        );
        
        // Подсказка
        if (mb.isEnabled()) {
            String hint = "Двигайте камеру, чтобы увидеть эффект";
            context.drawCenteredTextWithShadow(
                this.textRenderer,
                Text.literal(hint),
                this.width / 2,
                this.height / 2 + 95,
                0x808080
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
