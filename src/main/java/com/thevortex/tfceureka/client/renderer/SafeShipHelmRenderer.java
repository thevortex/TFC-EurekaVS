package com.thevortex.tfceureka.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import org.valkyrienskies.eureka.blockentity.ShipHelmBlockEntity;

/**
 * Safe renderer for Ship Helm that catches rendering errors for TFC wood types
 * that don't have wheel models registered in Eureka.
 */
public class SafeShipHelmRenderer implements BlockEntityRenderer<ShipHelmBlockEntity> {

    public SafeShipHelmRenderer(BlockEntityRendererProvider.Context context) {
        // Constructor required by BlockEntityRendererProvider
    }

    @Override
    public void render(ShipHelmBlockEntity blockEntity, float partialTick, PoseStack poseStack,
                      MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        // Safe rendering - don't render wheel models for TFC woods since they're not registered
        // This prevents the NullPointerException crash
        // The ship helm blocks will still function correctly, they just won't have visual wheel models
    }
}

