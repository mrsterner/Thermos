package com.bloomhousemc.thermus.client.model;

import com.bloomhousemc.thermus.Thermus;
import com.bloomhousemc.thermus.common.blocks.boiler.BoilerBlockEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static com.bloomhousemc.thermus.common.blocks.boiler.BoilerBlock.LIT;

public class BoilerModel extends AnimatedGeoModel<BoilerBlockEntity> {

    @Override
    public Identifier getModelLocation(BoilerBlockEntity object) {
        return new Identifier(Thermus.MODID, "geo/boiler.geo.json");
    }

    @Override
    public Identifier getTextureLocation(BoilerBlockEntity object) {
        return new Identifier(Thermus.MODID, object.getCachedState().get(LIT) ? "textures/block/boiler_block_lit.png" : "textures/block/boiler_block.png");
    }

    @Override
    public Identifier getAnimationFileLocation(BoilerBlockEntity animatable) {
        return new Identifier(Thermus.MODID, "animations/boiler.animation.json");
    }
}