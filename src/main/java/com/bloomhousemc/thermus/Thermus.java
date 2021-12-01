package com.bloomhousemc.thermus;

import com.bloomhousemc.thermus.common.items.DebugThermusItem;
import com.bloomhousemc.thermus.common.registry.ThermusObjects;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Thermus implements ModInitializer {
	public static final String MODID = "thermus";
	public static final ItemGroup THERMUS_GROUP = FabricItemGroupBuilder.build(new Identifier(MODID, MODID), () -> new ItemStack(ThermusObjects.COPPER_COIL));
	public static final Item DEBUG_THERMO = new DebugThermusItem(new FabricItemSettings().group(ItemGroup.MISC));


	@Override
	public void onInitialize() {
		ThermusObjects.init();
	}
}
