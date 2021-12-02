package com.bloomhousemc.thermus.client.model;

import com.bloomhousemc.thermus.Thermus;
import com.bloomhousemc.thermus.common.blocks.boiler.BoilerBlockEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class CoalModel extends AnimatedGeoModel<BoilerBlockEntity> {

    @Override
    public Identifier getModelLocation(BoilerBlockEntity object) {
        return new Identifier(Thermus.MODID, "geo/coal.geo.json");
    }

    @Override
    public Identifier getTextureLocation(BoilerBlockEntity object) {
        return new Identifier("textures/block/coal_block.png");
    }

    @Override
    public Identifier getAnimationFileLocation(BoilerBlockEntity animatable) {
        return new Identifier(Thermus.MODID, "animations/coil.animation.json");
    }
}