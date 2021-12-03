package com.bloomhousemc.thermus;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.RequiresRestart;

@Config(name = Thermus.MODID)
public class ThermusConfig implements ConfigData {
    @RequiresRestart
    public int boilerFuelBurnTimeMinutes = 10;
    @RequiresRestart
    public double temperatureTransferSpeed = 0.5D;
}