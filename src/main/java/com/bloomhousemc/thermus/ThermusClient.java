package com.bloomhousemc.thermus;

import com.bloomhousemc.thermus.client.UserHud;
import com.bloomhousemc.thermus.client.renderer.BoilerBlockEntityRenderer;
import com.bloomhousemc.thermus.client.renderer.PipeBlockEntityRenderer;
import com.bloomhousemc.thermus.common.registry.ThermusObjects;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;

public class ThermusClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		HudRenderCallback.EVENT.register(new UserHud());
		BlockEntityRendererRegistry.register(ThermusObjects.BOILER_BLOCK_ENTITY, (BlockEntityRendererFactory.Context rendererDispatcherIn) -> new BoilerBlockEntityRenderer());
		BlockEntityRendererRegistry.register(ThermusObjects.PIPE_BLOCK_ENTITY, (BlockEntityRendererFactory.Context rendererDispatcherIn) -> new PipeBlockEntityRenderer());
		BlockRenderLayerMap.INSTANCE.putBlock(ThermusObjects.PIPE_BLOCK, RenderLayer.getCutout());
	}
}
