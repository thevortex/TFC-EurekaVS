package com.thevortex.tfceureka.mixin.client;

import com.thevortex.tfceureka.TFCEureka;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.valkyrienskies.eureka.block.ShipHelmBlock;

/**
 * Mixin to ensure TFC ship helms work identically to vanilla Eureka helms
 *
 * This ensures all ShipHelmBlock behavior is consistent regardless of registry namespace
 */
@Mixin(value = ShipHelmBlock.class, remap = false, priority = 1100)
public abstract class ShipHelmControlMixin {

    /**
     * Log when TFC ship helms are used to verify they trigger Eureka's logic
     */
    @Inject(method = "use", at = @At("HEAD"), remap = false, require = 0)
    private void tfceureka$logTFCHelmUse(BlockState state, Level level, BlockPos pos,
                                         Player player, InteractionHand hand,
                                         BlockHitResult hit,
                                         CallbackInfoReturnable<InteractionResult> cir) {
        String namespace = ForgeRegistries.BLOCKS.getKey(state.getBlock()).getNamespace();
        if ("tfceureka".equals(namespace)) {
            TFCEureka.LOGGER.info("TFC ship helm used at {} - should open ship GUI", pos);
        }
    }
}

