package com.bloomhousemc.thermus.common.components;

import com.bloomhousemc.thermus.common.registry.ThermusComponents;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.sync.PlayerSyncPredicate;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import dev.onyxstudios.cca.api.v3.entity.PlayerComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.MathHelper;

public class PlayerTemperature extends EntityTemperature implements AutoSyncedComponent, ServerTickingComponent, PlayerComponent<BaseTemperature> {
    public static final int INCREASE_VITA = 0b10;
    public static final int DECREASE_VITA = 0b100;

    public PlayerTemperature(PlayerEntity owner) {
        super(owner, 0);
    }

    @Override
    public void setTemperature(int value) {
        if (value != this.temperature) {
            int increase = value - this.temperature;
            super.setTemperature(value);
            ThermusComponents.TEMPERATURE_COMPONENT.sync(this.owner, (buf, recipient) -> this.writeSyncPacket(buf, recipient, increase), PlayerSyncPredicate.all());
        }
    }

    @Override
    public void setTargetTemperature(int temperature) {
        super.setTargetTemperature(temperature);
    }

    @Override
    public void serverTick() {
        if (this.owner.age % 12 == 0) {
            if(MathHelper.nextInt(owner.world.getRandom(),0,1)==1){
                if(ITemperature.get(owner.world).getTemperature() > ITemperature.get(owner).getTemperature()){
                    addTemperature(1);
                }else if(ITemperature.get(owner.world).getTemperature() < ITemperature.get(owner).getTemperature()){
                    addTemperature(-1);
                }
            }
        }
    }

    @Override
    public boolean shouldSyncWith(ServerPlayerEntity player) {
        return player == this.owner;
    }

    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
        this.writeSyncPacket(buf, recipient, 0);
    }

    private void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient, int increase) {
        boolean fullSync = recipient == this.owner;
        int flags = (fullSync ? 1 : 0) | (increase > 0 ? INCREASE_VITA : increase < 0 ? DECREASE_VITA : 0);
        buf.writeByte(flags);
        if (fullSync) {
            buf.writeVarInt(this.temperature);
        }
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        int flags = buf.readByte();
        if ((flags & 1) != 0) {
            this.temperature = buf.readVarInt();
        }
        if ((flags & INCREASE_VITA) != 0) {
            //MinecraftClient.getInstance().particleManager.addEmitter(this.owner, ParticleTypes.TOTEM_OF_UNDYING, 30);
        } else if ((flags & DECREASE_VITA) != 0) {
            //MinecraftClient.getInstance().particleManager.addEmitter(this.owner, ParticleTypes.ASH, 30);
        }
    }

    @Override
    public boolean shouldCopyForRespawn(boolean lossless, boolean keepInventory, boolean switchingCharacter) {
        return lossless || keepInventory;
    }

    @Override
    public void copyForRespawn(BaseTemperature original, boolean lossless, boolean keepInventory, boolean switchingCharacter) {
        PlayerComponent.super.copyForRespawn(original, lossless, keepInventory, switchingCharacter);
        if (!lossless && !keepInventory) {
            this.temperature -= 5;
        }
    }
}