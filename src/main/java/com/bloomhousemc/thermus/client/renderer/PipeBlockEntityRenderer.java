package com.bloomhousemc.thermus.client.renderer;

import com.bloomhousemc.thermus.client.model.PipeModel;
import com.bloomhousemc.thermus.common.blocks.pipe.PipeBlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class PipeBlockEntityRenderer extends GeoBlockRenderer<PipeBlockEntity> {
    public PipeBlockEntityRenderer() {
        super(new PipeModel());
    }

    @Override
    public RenderLayer getRenderType(PipeBlockEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        return RenderLayer.getEntityTranslucent(getTextureLocation(animatable));
    }


}
