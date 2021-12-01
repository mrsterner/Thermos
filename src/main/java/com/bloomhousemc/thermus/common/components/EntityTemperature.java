package com.bloomhousemc.thermus.common.components;

import com.bloomhousemc.thermus.common.registry.ThermusComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class EntityTemperature extends BaseTemperature {
    protected LivingEntity owner;
    public EntityTemperature(LivingEntity owner, int baseTemperature) {
        this.owner = owner;
        this.temperature = baseTemperature;
    }

    @Override
    public void setTemperature(int value) {
        super.setTemperature(value);
        if (!this.owner.world.isClient) {
            if (this.getTemperature() == 0) {
                this.owner.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 200));
            } else if (this.getTemperature() > 10) {
                this.owner.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 200));
            }
        }
    }

    @Override
    public void addTemperature(int value) {
        if(getTemperature() + value < getMaxTemperature() && getTemperature() +value >getMinTemperature()){
            setTemperature(getTemperature() + value);
            ThermusComponents.TEMPERATURE_COMPONENT.sync(owner);
        }
    }

    public void setTargetTemperature(int temperature){

    }
}