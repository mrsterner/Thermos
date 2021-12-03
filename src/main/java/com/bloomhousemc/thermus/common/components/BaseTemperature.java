package com.bloomhousemc.thermus.common.components;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.CopyableComponent;
import net.minecraft.nbt.NbtCompound;

public class BaseTemperature implements ITemperature, Component, CopyableComponent<BaseTemperature> {
    private double MAX_TEMP = 2000;
    private double MIN_TEMP = -273;
    protected double temperature;
    protected double targetTemperature;
    protected double temperatureModifier = 1;

    public BaseTemperature(){
        this(0);
    }

    public BaseTemperature(double temperature) {
        this.temperature = temperature;
    }

    @Override
    public double getTemperature() {
        return this.temperature;
    }

    @Override
    public double getMaxTemperature() {
        return MAX_TEMP;
    }

    @Override
    public double getMinTemperature() {
        return MIN_TEMP;
    }

    @Override
    public void setTemperature(double value) {
        this.temperature = value;
    }

    @Override
    public void addTemperature(double value) {

    }

    @Override
    public void setTargetTemperature(double value) {
        this.targetTemperature = value;
    }

    @Override
    public double getTargetTemperature() {
        return targetTemperature;
    }

    @Override
    public void setTemperatureModifier(double value) {
        this.temperatureModifier = value;
    }

    @Override
    public double getTemperatureModifier() {
        return temperatureModifier;
    }


    @Override
    public void readFromNbt(NbtCompound tag) {
        this.temperature = tag.getDouble("temperature");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putDouble("temperature", this.temperature);
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
        return Double.hashCode(this.temperature);
    }
}