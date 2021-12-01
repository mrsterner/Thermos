package com.bloomhousemc.thermus.common.blocks.pipe;

import com.bloomhousemc.thermus.common.registry.ThermusObjects;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import static com.bloomhousemc.thermus.common.blocks.pipe.PipeBlock.*;

public class PipeBlockEntity extends BlockEntity implements IAnimatable {
    private int transition = 4;
    private final AnimationFactory factory = new AnimationFactory(this);
    public PipeBlockEntity(BlockPos pos, BlockState state) {
        super(ThermusObjects.PIPE_BLOCK_ENTITY, pos, state);
    }





    private <E extends BlockEntity & IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.pipe.center", true));
        return PlayState.CONTINUE;
    }

    private <E extends BlockEntity & IAnimatable> PlayState predicateUp(AnimationEvent<E> event) {
        if(event.getAnimatable().getWorld().getBlockState(event.getAnimatable().getPos()).getBlock() instanceof PipeBlock &&event.getAnimatable().getWorld().getBlockState(event.getAnimatable().getPos()).get(UP)){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.pipe.up", true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }
    private <E extends BlockEntity & IAnimatable> PlayState predicateDown(AnimationEvent<E> event) {
        if(event.getAnimatable().getWorld().getBlockState(event.getAnimatable().getPos()).getBlock() instanceof PipeBlock &&event.getAnimatable().getWorld().getBlockState(event.getAnimatable().getPos()).get(DOWN)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.pipe.down", true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends BlockEntity & IAnimatable> PlayState predicateNorth(AnimationEvent<E> event) {
        if(event.getAnimatable().getWorld().getBlockState(event.getAnimatable().getPos()).getBlock() instanceof PipeBlock && event.getAnimatable().getWorld().getBlockState(event.getAnimatable().getPos()).get(NORTH)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.pipe.north", true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }
    private <E extends BlockEntity & IAnimatable> PlayState predicateSouth(AnimationEvent<E> event) {
        if(event.getAnimatable().getWorld().getBlockState(event.getAnimatable().getPos()).getBlock() instanceof PipeBlock &&event.getAnimatable().getWorld().getBlockState(event.getAnimatable().getPos()).get(SOUTH)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.pipe.south", true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }
    private <E extends BlockEntity & IAnimatable> PlayState predicateEast(AnimationEvent<E> event) {
        if(event.getAnimatable().getWorld().getBlockState(event.getAnimatable().getPos()).getBlock() instanceof PipeBlock &&event.getAnimatable().getWorld().getBlockState(event.getAnimatable().getPos()).get(EAST)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.pipe.east", true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }
    private <E extends BlockEntity & IAnimatable> PlayState predicateWest(AnimationEvent<E> event) {
        if(event.getAnimatable().getWorld().getBlockState(event.getAnimatable().getPos()).getBlock() instanceof PipeBlock &&event.getAnimatable().getWorld().getBlockState(event.getAnimatable().getPos()).get(WEST)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.pipe.west", true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "centerController", 0, this::predicate));

        animationData.addAnimationController(new AnimationController<>(this, "upController", transition, this::predicateUp));
        animationData.addAnimationController(new AnimationController<>(this, "downController", transition, this::predicateDown));

        animationData.addAnimationController(new AnimationController<>(this, "northController", transition, this::predicateNorth));
        animationData.addAnimationController(new AnimationController<>(this, "southController", transition, this::predicateSouth));
        animationData.addAnimationController(new AnimationController<>(this, "eastController", transition, this::predicateEast));
        animationData.addAnimationController(new AnimationController<>(this, "westController", transition, this::predicateWest));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
