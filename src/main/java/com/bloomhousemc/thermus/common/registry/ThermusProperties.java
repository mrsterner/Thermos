package com.bloomhousemc.thermus.common.registry;

import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;

public class ThermusProperties {
    public static final IntProperty COAL = IntProperty.of("coal",0,4);
    public static final BooleanProperty LIT = BooleanProperty.of("lit");
    public static final IntProperty COIL = IntProperty.of("coil",0,4);
}
