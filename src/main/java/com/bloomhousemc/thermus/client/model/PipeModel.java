package com.bloomhousemc.thermus.client.model;

import com.bloomhousemc.thermus.Thermus;
import com.bloomhousemc.thermus.common.blocks.pipe.PipeBlockEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PipeModel extends AnimatedGeoModel<PipeBlockEntity> {

    @Override
    public Identifier getModelLocation(PipeBlockEntity object) {
        return new Identifier(Thermus.MODID, "geo/pipe.geo.json");
    }

    @Override
    public Identifier getTextureLocation(PipeBlockEntity object) {
        return new Identifier(Thermus.MODID, "textures/block/pipe.png");
    }

    @Override
    public Identifier getAnimationFileLocation(PipeBlockEntity animatable) {
        return new Identifier(Thermus.MODID, "animations/pipe.animation.json");
    }
}