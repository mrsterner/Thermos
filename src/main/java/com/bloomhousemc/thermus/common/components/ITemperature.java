package com.bloomhousemc.thermus.common.components;

import com.bloomhousemc.thermus.common.registry.ThermusComponents;
import dev.onyxstudios.cca.api.v3.component.ComponentV3;

public interface ITemperature extends ComponentV3 {

    static <T> ITemperature get(T provider) {
        return ThermusComponents.TEMPERATURE_COMPONENT.get(provider);
    }
    int getTemperature();

    int getMaxTemperature();

    int getMinTemperature();

    void setTemperature(int value);

    void addTemperature(int value);

    void setTargetTemperature(int value);

    int getTargetTemperature();

    void setTemperatureModifier(int value);

    int getTemperatureModifier();

    default void transferTo(ITemperature dest, int amount) {
        int sourceTemperature = this.getTemperature();
        int actualAmount = Math.min(sourceTemperature, amount);
        this.setTemperature(sourceTemperature - actualAmount);
        dest.setTemperature(dest.getTemperature() + actualAmount);
    }
}