package ru.motionreblur;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class MotionBlurCommand {
    
    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            registerCommand(dispatcher);
        });
    }
    
    private static void registerCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("motionreblur")
            .executes(ctx -> openGui(ctx))
            .then(ClientCommandManager.literal("on")
                .executes(ctx -> enable(ctx)))
            .then(ClientCommandManager.literal("off")
                .executes(ctx -> disable(ctx)))
            .then(ClientCommandManager.literal("toggle")
                .executes(ctx -> toggle(ctx)))
            .then(ClientCommandManager.literal("strength")
                .then(ClientCommandManager.argument("value", FloatArgumentType.floatArg(-2.0f, 2.0f))
                    .executes(ctx -> setStrength(ctx, FloatArgumentType.getFloat(ctx, "value")))))
            .then(ClientCommandManager.literal("rrc")
                .executes(ctx -> toggleRRC(ctx)))
            .then(ClientCommandManager.literal("status")
                .executes(ctx -> showStatus(ctx)))
            .then(ClientCommandManager.literal("help")
                .executes(ctx -> showHelp(ctx)))
        );
    }
    
    private static int openGui(CommandContext<FabricClientCommandSource> ctx) {
        MinecraftClient client = MinecraftClient.getInstance();
        client.execute(() -> {
            client.setScreen(new MotionBlurConfigScreen(null));
        });
        ctx.getSource().sendFeedback(Text.literal("§aОткрываю меню настроек..."));
        return 1;
    }
    
    private static int showStatus(CommandContext<FabricClientCommandSource> ctx) {
        MotionBlurModule mb = MotionBlurModule.getInstance();
        ctx.getSource().sendFeedback(Text.literal("§7§m                    "));
        ctx.getSource().sendFeedback(Text.literal("§6Motion ReBlur"));
        ctx.getSource().sendFeedback(Text.literal("§7Состояние: " + (mb.isEnabled() ? "§aВКЛ" : "§cВЫКЛ")));
        ctx.getSource().sendFeedback(Text.literal("§7Сила: §f" + String.format("%.1f", mb.getStrength())));
        ctx.getSource().sendFeedback(Text.literal("§7RRC: " + (mb.isUseRRC() ? "§aВКЛ" : "§cВЫКЛ")));
        ctx.getSource().sendFeedback(Text.literal("§7Refresh Rate: §f" + MonitorInfoProvider.getRefreshRate() + " Hz"));
        ctx.getSource().sendFeedback(Text.literal("§7§m                    "));
        return 1;
    }
    
    private static int enable(CommandContext<FabricClientCommandSource> ctx) {
        MotionBlurModule.getInstance().setEnabled(true);
        ctx.getSource().sendFeedback(Text.literal("§aMotion Blur включен"));
        return 1;
    }
    
    private static int disable(CommandContext<FabricClientCommandSource> ctx) {
        MotionBlurModule.getInstance().setEnabled(false);
        ctx.getSource().sendFeedback(Text.literal("§cMotion Blur выключен"));
        return 1;
    }
    
    private static int toggle(CommandContext<FabricClientCommandSource> ctx) {
        MotionBlurModule mb = MotionBlurModule.getInstance();
        boolean newState = !mb.isEnabled();
        mb.setEnabled(newState);
        ctx.getSource().sendFeedback(Text.literal("§7Motion Blur: " + (newState ? "§aВКЛ" : "§cВЫКЛ")));
        return 1;
    }
    
    private static int setStrength(CommandContext<FabricClientCommandSource> ctx, float value) {
        MotionBlurModule.getInstance().setStrength(value);
        ctx.getSource().sendFeedback(Text.literal("§aСила установлена на §f" + String.format("%.1f", value)));
        return 1;
    }
    
    private static int toggleRRC(CommandContext<FabricClientCommandSource> ctx) {
        MotionBlurModule mb = MotionBlurModule.getInstance();
        boolean newState = !mb.isUseRRC();
        mb.setUseRRC(newState);
        ctx.getSource().sendFeedback(Text.literal("§7Адаптация к частоте монитора: " + (newState ? "§aВКЛ" : "§cВЫКЛ")));
        return 1;
    }
    
    private static int showHelp(CommandContext<FabricClientCommandSource> ctx) {
        ctx.getSource().sendFeedback(Text.literal("§7§m                              "));
        ctx.getSource().sendFeedback(Text.literal("§6Motion ReBlur - Команды"));
        ctx.getSource().sendFeedback(Text.literal("§7§m                              "));
        ctx.getSource().sendFeedback(Text.literal("§e/motionreblur §7- открыть GUI"));
        ctx.getSource().sendFeedback(Text.literal("§e/motionreblur on §7- включить"));
        ctx.getSource().sendFeedback(Text.literal("§e/motionreblur off §7- выключить"));
        ctx.getSource().sendFeedback(Text.literal("§e/motionreblur toggle §7- переключить"));
        ctx.getSource().sendFeedback(Text.literal("§e/motionreblur strength <значение> §7- установить силу"));
        ctx.getSource().sendFeedback(Text.literal("§7  Диапазон: от -2.0 до 2.0"));
        ctx.getSource().sendFeedback(Text.literal("§e/motionreblur rrc §7- переключить адаптацию к частоте"));
        ctx.getSource().sendFeedback(Text.literal("§e/motionreblur status §7- показать статус"));
        ctx.getSource().sendFeedback(Text.literal("§7§m                              "));
        return 1;
    }
}
