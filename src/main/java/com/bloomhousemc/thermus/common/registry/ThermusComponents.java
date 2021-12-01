package com.bloomhousemc.thermus.common.registry;

import com.bloomhousemc.thermus.Thermus;
import com.bloomhousemc.thermus.common.components.AmbientTemperature;
import com.bloomhousemc.thermus.common.components.ITemperature;
import com.bloomhousemc.thermus.common.components.PlayerTemperature;
import dev.onyxstudios.cca.api.v3.block.BlockComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.block.BlockComponentInitializer;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.world.WorldComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.world.WorldComponentInitializer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class ThermusComponents implements EntityComponentInitializer, WorldComponentInitializer, BlockComponentInitializer {
    public static final ComponentKey<ITemperature> TEMPERATURE_COMPONENT = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier(Thermus.MODID, "temperature"), ITemperature.class);


    @Override
    public void registerBlockComponentFactories(BlockComponentFactoryRegistry registry) {

    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.beginRegistration(PlayerEntity.class, TEMPERATURE_COMPONENT).impl(PlayerTemperature.class).end(PlayerTemperature::new);
    }

    @Override
    public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
        registry.register(TEMPERATURE_COMPONENT, AmbientTemperature.WorldTemperature.class, AmbientTemperature.WorldTemperature::new);
    }


}
