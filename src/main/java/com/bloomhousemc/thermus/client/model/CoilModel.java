package com.bloomhousemc.thermus.client.model;

import com.bloomhousemc.thermus.Thermus;
import com.bloomhousemc.thermus.common.blocks.boiler.BoilerBlockEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class CoilModel extends AnimatedGeoModel<BoilerBlockEntity> {

    @Override
    public Identifier getModelLocation(BoilerBlockEntity object) {
        return new Identifier(Thermus.MODID, "geo/coil.geo.json");
    }

    @Override
    public Identifier getTextureLocation(BoilerBlockEntity object) {
        String string = object.getCachedState().getBlock().getName().getString();
        String s = string.substring(string.lastIndexOf(".") + 1);
        return new Identifier("textures/block/" + s.replace("_coil", "") + ".png");
    }

    @Override
    public Identifier getAnimationFileLocation(BoilerBlockEntity animatable) {
        return new Identifier(Thermus.MODID, "animations/coil.animation.json");
    }
}