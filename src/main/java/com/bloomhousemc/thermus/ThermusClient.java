package com.bloomhousemc.thermus;

import com.bloomhousemc.thermus.client.UserHud;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class ThermusClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		HudRenderCallback.EVENT.register(new UserHud());
	}
}
