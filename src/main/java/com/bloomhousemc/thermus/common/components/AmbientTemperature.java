package com.bloomhousemc.thermus.common.components;

import com.bloomhousemc.thermus.common.registry.ThermusComponents;
import com.bloomhousemc.thermus.common.utils.ThermusUtils;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

public abstract class AmbientTemperature extends BaseTemperature implements AutoSyncedComponent {
    public abstract void syncWithAll(MinecraftServer server);

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        double temperature = buf.readDouble();
        this.setTemperature(temperature);
    }


    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity player) {
        buf.writeDouble(this.getTemperature());
    }

    public static class WorldTemperature extends AmbientTemperature implements ServerTickingComponent {
        private final World world;

        public WorldTemperature(World world) {
            this.world = world;
        }

        @Override
        public void syncWithAll(MinecraftServer server) {
            ThermusComponents.TEMPERATURE_COMPONENT.sync(this.world);
        }

        @Override
        public void serverTick() {
            if (this.world.getTime() % 10 == 0) {
                setTemperature(Math.round(ThermusUtils.dayTimeTemperatureFunction(ThermusUtils.simpleTimeOfDay(world), 10, 2, 10, 0)*100D)/100D);
                this.syncWithAll(world.getServer());
            }
        }


    }
}