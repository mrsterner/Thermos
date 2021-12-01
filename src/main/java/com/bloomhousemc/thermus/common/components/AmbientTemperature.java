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
    long t = 0;
    public abstract void syncWithAll(MinecraftServer server);

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        int temperature = buf.readInt();
        this.setTemperature(temperature);
    }


    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity player) {
        buf.writeInt(this.getTemperature());
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
                setTemperature(ThermusUtils.dayTimeTemperatureFunction(ThermusUtils.simpleTimeOfDay(world), 10, 2, 10, 0));
                this.syncWithAll(world.getServer());
            }
        }


    }
}