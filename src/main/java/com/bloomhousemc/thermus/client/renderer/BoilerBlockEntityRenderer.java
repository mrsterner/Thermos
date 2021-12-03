package com.bloomhousemc.thermus.client.renderer;

import com.bloomhousemc.thermus.Thermus;
import com.bloomhousemc.thermus.client.model.BoilerModel;
import com.bloomhousemc.thermus.client.model.CoalModel;
import com.bloomhousemc.thermus.client.model.CoilModel;
import com.bloomhousemc.thermus.common.blocks.boiler.BoilerBlock;
import com.bloomhousemc.thermus.common.blocks.boiler.BoilerBlockEntity;
import com.bloomhousemc.thermus.common.registry.ThermusPorperties;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

import static com.bloomhousemc.thermus.common.blocks.boiler.BoilerBlock.*;

public class BoilerBlockEntityRenderer extends GeoBlockRenderer<BoilerBlockEntity> {
    public static CoilModel coilModel = new CoilModel();
    public BoilerBlockEntityRenderer() {
        super(new BoilerModel());
    }


    @Override
    public RenderLayer getRenderType(BoilerBlockEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        return RenderLayer.getEntityTranslucent(getTextureLocation(animatable));
    }

    @Override
    public void render(BoilerBlockEntity entity, float partialTicks, MatrixStack matrixStack, VertexConsumerProvider bufferIn, int packedLightIn) {
        super.render(entity, partialTicks, matrixStack, bufferIn, packedLightIn);
        World world = entity.getWorld();
        if (world != null && world.getBlockState(entity.getPos()).getBlock() instanceof BoilerBlock) {
            BlockPos pos = entity.getPos();
            int coal = entity.getCachedState().get(ThermusPorperties.COAL);
            boolean lit = entity.getCachedState().get(ThermusPorperties.LIT);
            if (coal > 0) {
                if(lit && !MinecraftClient.getInstance().isPaused()){
                    Direction direction = world.getBlockState(pos).get(FACING);
                    world.addParticle(ParticleTypes.FLAME,
                    pos.getX() + 0.5 + MathHelper.nextDouble(world.random, -0.2, 0.2) + (direction == Direction.WEST ? 0.5 : direction == Direction.EAST ? -0.5 : 0),
                    pos.getY() + 0.1,
                    pos.getZ() + 0.5 + MathHelper.nextDouble(world.random, -0.2, 0.2) + (direction == Direction.NORTH ? 0.5 : direction == Direction.SOUTH ? -0.5 : 0),
                    0, 0, 0);

                }
            }
        }



        BlockState blockState = entity.getWorld().getBlockState(entity.getPos());
        if(blockState.getBlock() instanceof BoilerBlock && blockState.get(ThermusPorperties.COIL)!=0){
            matrixStack.push();

            Direction direction = blockState.get(FACING);
            switch (direction){
                case SOUTH -> matrixStack.translate(0.5,-0.5,0.9F);
                case NORTH -> matrixStack.translate(0.5,-0.5,0.1F);
                case WEST -> matrixStack.translate(0.1,-0.5,0.5F);
                case EAST -> matrixStack.translate(0.9,-0.5,0.5F);
            }
            matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(
            direction == Direction.NORTH ? 180 :
            direction == Direction.EAST ? 90 :
            direction == Direction.SOUTH ? 0 : 270));


            render(getGeoModelProvider().getModel(new Identifier(Thermus.MODID, "geo/coil.geo.json")), entity, partialTicks, RenderLayer.getEntityCutout(getTexture(blockState,"")), matrixStack,bufferIn, bufferIn.getBuffer(RenderLayer.getEntityCutout(coilModel.getTextureLocation(entity))) ,packedLightIn, OverlayTexture.DEFAULT_UV, 1,1,1,1);
            matrixStack.pop();

        }

    }
    public static Identifier getTexture(BlockState blockState, String string){
        if(blockState.get(ThermusPorperties.COIL) == 1){
            return new Identifier( "textures/block/copper_block.png");
        }else if(blockState.get(ThermusPorperties.COIL) == 2){
            return new Identifier( "textures/block/gold_block.png");
        }else if(blockState.get(ThermusPorperties.COIL) == 3){
            return new Identifier( "textures/block/iron_block.png");
        }else{
            return new Identifier(Thermus.MODID, "textures/block/steel_block");
        }
    }
}
