package com.bloomhousemc.thermus.common.components;

import com.bloomhousemc.thermus.common.registry.ThermusComponents;
import dev.onyxstudios.cca.api.v3.component.ComponentV3;

public interface ITemperature extends ComponentV3 {

    static <T> ITemperature get(T provider) {
        return ThermusComponents.TEMPERATURE_COMPONENT.get(provider);
    }
    double getTemperature();

    double getMaxTemperature();

    double getMinTemperature();

    void setTemperature(double value);

    void addTemperature(double value);

    void setTargetTemperature(double value);

    double getTargetTemperature();

    void setTemperatureModifier(double value);

    double getTemperatureModifier();

    default void transferTo(ITemperature dest, double amount) {
        double sourceTemperature = this.getTemperature();
        double actualAmount = Math.min(sourceTemperature, amount);
        this.setTemperature(sourceTemperature - actualAmount);
        dest.setTemperature(dest.getTemperature() + actualAmount);
    }
}