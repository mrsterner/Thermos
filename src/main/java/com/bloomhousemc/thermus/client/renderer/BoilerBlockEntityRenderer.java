package com.bloomhousemc.thermus.client.renderer;

import com.bloomhousemc.thermus.Thermus;
import com.bloomhousemc.thermus.client.model.BoilerModel;
import com.bloomhousemc.thermus.client.model.CoilModel;
import com.bloomhousemc.thermus.common.blocks.boiler.BoilerBlock;
import com.bloomhousemc.thermus.common.blocks.boiler.BoilerBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

import static com.bloomhousemc.thermus.common.blocks.boiler.BoilerBlock.COIL;
import static com.bloomhousemc.thermus.common.blocks.boiler.BoilerBlock.FACING;

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
    public void render(BoilerBlockEntity tile, float partialTicks, MatrixStack matrixStack, VertexConsumerProvider bufferIn, int packedLightIn) {
        super.render(tile, partialTicks, matrixStack, bufferIn, packedLightIn);
        BlockState blockState = tile.getWorld().getBlockState(tile.getPos());
        if(blockState.getBlock() instanceof BoilerBlock && blockState.get(COIL)!=0){
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


            render(getGeoModelProvider().getModel(new Identifier(Thermus.MODID, "geo/coil.geo.json")), tile, partialTicks, RenderLayer.getEntityCutout(getTexture(blockState,"")), matrixStack,bufferIn, bufferIn.getBuffer(RenderLayer.getEntityCutout(coilModel.getTextureLocation(tile))) ,packedLightIn, OverlayTexture.DEFAULT_UV, 1,1,1,1);
            matrixStack.pop();

        }

    }
    public static Identifier getTexture(BlockState blockState, String string){
        if(blockState.get(COIL) == 1){
            return new Identifier( "textures/block/copper_block.png");
        }else if(blockState.get(COIL) == 2){
            return new Identifier( "textures/block/gold_block.png");
        }else{
            return new Identifier( "textures/block/iron_block.png");
        }
    }
}
