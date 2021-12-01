package com.bloomhousemc.thermus.common.registry;

import com.bloomhousemc.thermus.Thermus;
import com.bloomhousemc.thermus.common.components.ITemperature;
import dev.onyxstudios.cca.api.v3.block.BlockComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.block.BlockComponentInitializer;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.world.WorldComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.world.WorldComponentInitializer;
import net.minecraft.util.Identifier;

public class ThermusComponents implements EntityComponentInitializer, WorldComponentInitializer, BlockComponentInitializer {
    public static final ComponentKey<ITemperature> TEMPERATURE_COMPONENT = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier(Thermus.MODID, "temperature"), ITemperature.class);


    @Override
    public void registerBlockComponentFactories(BlockComponentFactoryRegistry registry) {

    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {

    }

    @Override
    public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {

    }


}
