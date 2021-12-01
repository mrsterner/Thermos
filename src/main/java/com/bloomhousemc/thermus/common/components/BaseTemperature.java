package com.bloomhousemc.thermus.common.components;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.CopyableComponent;
import net.minecraft.nbt.NbtCompound;

public class BaseTemperature implements ITemperature, Component, CopyableComponent<BaseTemperature> {
    private int MAX_TEMP = 2000;
    private int MIN_TEMP = -273;
    protected int temperature;
    protected int targetTemperature;
    protected int temperatureModifier = 0;

    public BaseTemperature(){
        this(0);
    }

    public BaseTemperature(int temperature) {
        this.temperature = temperature;
    }

    @Override
    public int getTemperature() {
        return this.temperature;
    }

    @Override
    public int getMaxTemperature() {
        return MAX_TEMP;
    }

    @Override
    public int getMinTemperature() {
        return MIN_TEMP;
    }

    @Override
    public void setTemperature(int value) {
        this.temperature = value;
    }

    @Override
    public void addTemperature(int value) {

    }

    @Override
    public void setTargetTemperature(int value) {
        this.targetTemperature = value;
    }

    @Override
    public int getTargetTemperature() {
        return targetTemperature;
    }

    @Override
    public void setTemperatureModifier(int value) {
        this.temperatureModifier = value;
    }

    @Override
    public int getTemperatureModifier() {
        return temperatureModifier;
    }


    @Override
    public void readFromNbt(NbtCompound tag) {
        this.temperature = tag.getInt("temperature");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt("temperature", this.temperature);
    }

    @Override
    public void copyFrom(BaseTemperature other) {
        this.temperature = other.getTemperature();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ITemperature)) return false;
        return this.temperature == ((ITemperature) o).getTemperature();
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(this.temperature);
    }
}