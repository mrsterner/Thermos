package com.bloomhousemc.thermus.client;

import com.bloomhousemc.thermus.common.components.AmbientTemperature;
import com.bloomhousemc.thermus.common.components.ITemperature;
import com.bloomhousemc.thermus.common.registry.ThermusComponents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class UserHud extends DrawableHelper implements HudRenderCallback {
    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {
        MinecraftClient mc = MinecraftClient.getInstance();
        PlayerEntity player = mc.player;
        TextRenderer textRenderer = mc.textRenderer;
        int height = mc.getWindow().getScaledHeight();
        double temperature = ThermusComponents.TEMPERATURE_COMPONENT.get(player).getTemperature();
        AmbientTemperature.WorldTemperature worldTemp = (AmbientTemperature.WorldTemperature) ITemperature.get(player.world);
        matrixStack.push();
        renderText(matrixStack, textRenderer, new TranslatableText("hud.thermus.temperature.player", new TranslatableText(String.valueOf(temperature))), height, 4);
        renderText(matrixStack, textRenderer, new TranslatableText("hud.thermus.temperature.world", new TranslatableText(String.valueOf(worldTemp.getTemperature()))), height, 5);
        matrixStack.pop();
    }

    void renderText(MatrixStack stack, TextRenderer renderer, Text text, int height, int offset) {
        int textWidth = renderer.getWidth(text);
        drawCenteredText(stack, renderer, text, textWidth/2 + 10, height + 18 - offset*9, 0xffffff);
    }
}