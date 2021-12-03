package com.bloomhousemc.thermus.common.components;

import com.bloomhousemc.thermus.Thermus;
import com.bloomhousemc.thermus.common.registry.ThermusComponents;
import com.bloomhousemc.thermus.common.registry.ThermusTags;
import com.bloomhousemc.thermus.common.utils.ThermusUtils;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.sync.PlayerSyncPredicate;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import dev.onyxstudios.cca.api.v3.entity.PlayerComponent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LightType;

import java.util.ArrayList;
import java.util.List;

public class PlayerTemperature extends EntityTemperature implements AutoSyncedComponent, ServerTickingComponent, PlayerComponent<BaseTemperature> {
    public static final int INCREASE_TEMP = 0b10;
    public static final int DECREASE_TEMP = 0b100;
    public static final float HEAT_EXCHANGE_CONSTANT = 0.0012F;

    public PlayerTemperature(PlayerEntity owner) {
        super(owner, 0);
    }

    @Override
    public void setTemperature(double value) {
        if (value != this.temperature) {
            double increase = value - this.temperature;
            super.setTemperature(value);
            ThermusComponents.TEMPERATURE_COMPONENT.sync(this.owner, (buf, recipient) -> this.writeSyncPacket(buf, recipient, increase), PlayerSyncPredicate.all());
        }
    }

    @Override
    public void setTargetTemperature(double temperature) {
        super.setTargetTemperature(temperature);
    }

    @Override
    public void setTemperatureModifier(double value) {
        super.setTemperatureModifier(value);
    }

    @Override
    public double getTemperatureModifier() {
        return super.getTemperatureModifier();
    }

    @Override
    public void serverTick() {
        if (this.owner.age % 20 == 0) {
            if(owner instanceof PlayerEntity player && !player.isSpectator() ){

                double currentTemp = 37.5;
                double finalTemp = 0;
                double chunkTemp = 0; //TODO chunkcomponent
                double sunTemp = player.world.getChunkManager().getLightingProvider().getLight(player.getBlockPos(), 0); //TODO check what ambient darkness is
                double dayNightTemp = ThermusUtils.dayTimeTemperatureFunction(ThermusUtils.simpleTimeOfDay(player.world), 10, 2, 10, 0);
                double temperatureTransferSpeed = Thermus.config.temperatureTransferSpeed;
                Difficulty difficulty = player.world.getDifficulty();
                switch (difficulty){
                    case EASY -> currentTemp += temperatureTransferSpeed*0.5F;
                    case NORMAL -> currentTemp += temperatureTransferSpeed*0.4F;
                    case HARD -> currentTemp += temperatureTransferSpeed*0.3F;
                }
                finalTemp += dayNightTemp;
                finalTemp += sunTemp > 5.0F ? (dayNightTemp * 5.0F) : (-1.0F * 5.0F);
                finalTemp += chunkTemp;
                finalTemp -= 37.5F;
                if(player.isOnFire()){
                    finalTemp += 50F;
                }
                Iterable<ItemStack> itemStacks = player.getArmorItems();
                for(ItemStack equippedStack : itemStacks){
                    if(ThermusTags.METAL_CLOTHING.contains(equippedStack.getItem())){
                        finalTemp += player.isTouchingWater() ? -2F : -1F;
                    }else if(ThermusTags.LEATHER_CLOTHING.contains(equippedStack.getItem())){
                        finalTemp += player.isTouchingWater() ? -0.5F : 1.5F;
                    }else if(ThermusTags.INSULATED_CLOTHING_TIER_1.contains(equippedStack.getItem())){
                        finalTemp += player.isTouchingWater() ? -1F : 2.3F;
                    }else if(ThermusTags.INSULATED_CLOTHING_TIER_2.contains(equippedStack.getItem())){
                        finalTemp += player.isTouchingWater() ? -1.5F : 2.6F;
                    }else if(ThermusTags.INSULATED_CLOTHING_TIER_3.contains(equippedStack.getItem())){
                        finalTemp += player.isTouchingWater() ? -2F : 4F;
                    }
                }
                double rangeCheck = 5;
                BlockPos blockPos = player.getBlockPos();
                Box box =new Box(
                blockPos.getX()+rangeCheck,blockPos.getY()+rangeCheck,blockPos.getZ()+ rangeCheck,
                blockPos.getX()-rangeCheck, blockPos.getY()-rangeCheck,blockPos.getZ()-rangeCheck);

                for (LivingEntity livingEntity : player.world.getEntitiesByClass(LivingEntity.class, box, EntityPredicates.VALID_ENTITY)) {
                    if(livingEntity != owner){
                        double entityTemperature = 37;//ThermusComponents.TEMPERATURE_COMPONENT.get(livingEntity).getTemperature();//TODO give entities a temp
                        double distance = Math.sqrt((livingEntity.getX() - player.getX())*(livingEntity.getX() - player.getX()) + (livingEntity.getY() - player.getY())*(livingEntity.getY() - player.getY()) + (livingEntity.getZ() - player.getZ())*(livingEntity.getZ() - player.getZ()));
                        finalTemp += entityTemperature * temperatureTransferSpeed * MathHelper.fastInverseSqrt(distance) * 2;
                        System.out.println("distance: "+distance);
                    }

                }
                List<Block> blockList = new ArrayList<>();
                for(double x = -rangeCheck; x <= rangeCheck; ++x) {
                    for (double y = -rangeCheck; y <= rangeCheck; ++y) {
                        for (double z = -rangeCheck; z <= rangeCheck; ++z) {
                            float blockTemp = 0;
                            BlockPos heatedBlock = new BlockPos(blockPos.getX() + x, blockPos.getY() + y, blockPos.getZ() + z);
                            float blockLight = player.world.getLightLevel(LightType.BLOCK, blockPos);
                            BlockState heatedState = player.world.getBlockState(heatedBlock);

                            blockList.add(player.world.getBlockState(heatedBlock).getBlock());
                            if(ThermusTags.TEMPERATURE_HOLDER.contains(heatedState.getBlock()) || heatedState.contains(Properties.LIT)){
                               int level = player.world.getLightLevel(LightType.BLOCK, blockPos);
                               finalTemp += level*1.2D;
                            }


                        }
                    }
                }
                //System.out.println(blockList);

                currentTemp += ThermusUtils.CapacityTypes.AIR.getCapacity() * temperatureTransferSpeed * (finalTemp - currentTemp);
                ThermusComponents.TEMPERATURE_COMPONENT.get(owner).setTemperature((currentTemp));
                ThermusComponents.TEMPERATURE_COMPONENT.sync(owner);
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

    private void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient, double increase) {
        boolean fullSync = recipient == this.owner;
        double flags = (fullSync ? 1 : 0) | (increase > 0 ? INCREASE_TEMP : increase < 0 ? DECREASE_TEMP : 0);
        buf.writeDouble(flags);
        if (fullSync) {
            buf.writeDouble(this.temperature);
        }
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        int flags = buf.readByte();
        if ((flags & 1) != 0) {
            this.temperature = buf.readVarInt();
        }
        if ((flags & INCREASE_TEMP) != 0) {
            //MinecraftClient.getInstance().particleManager.addEmitter(this.owner, ParticleTypes.TOTEM_OF_UNDYING, 30);
        } else if ((flags & DECREASE_TEMP) != 0) {
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