package com.thevortex.tfceureka.mixin;

import com.thevortex.tfceureka.TFCEureka;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin to prevent TFC terrain blocks from being included in ship assembly
 * Targets the BFS (breadth-first search) method that scans blocks during assembly
 */
@Mixin(targets = "org.valkyrienskies.eureka.util.ShipAssemblerKt", remap = false, priority = 500)
public class ShipAssemblyMixin {

    private static boolean hasLoggedActivation = false;
    private static int blocksFiltered = 0;

    /**
     * Intercept the BFS method that scans blocks during ship assembly
     * Return false for TFC terrain to prevent scanning
     */
    @Inject(
        method = "bfs",
        at = @At("HEAD"),
        cancellable = true,
        remap = false,
        require = 0
    )
    private static void tfceureka$filterTerrainFromBFS(
        ServerLevel serverLevel,
        BlockPos centerPos,
        Object ignoreParameters,
        CallbackInfoReturnable<Object> cir
    ) {
        try {
            if (!hasLoggedActivation) {
                TFCEureka.LOGGER.info("TFC-Eureka terrain filter is ACTIVE - will exclude terrain from ship assembly");
                hasLoggedActivation = true;
            }

            // This intercepts every block check during BFS
            BlockState state = serverLevel.getBlockState(centerPos);

            if (shouldExcludeBlock(state)) {
                blocksFiltered++;
                if (blocksFiltered % 100 == 0) {
                    TFCEureka.LOGGER.debug("Filtered {} terrain blocks from ship assembly", blocksFiltered);
                }
                // Return empty/null to stop this branch of the BFS
                cir.setReturnValue(null);
            }
        } catch (Exception e) {
            // Silently fail to avoid breaking Eureka
            TFCEureka.LOGGER.warn("Error in terrain filter: {}", e.getMessage());
        }
    }

    private static boolean shouldExcludeBlock(BlockState state) {
        Block block = state.getBlock();

        // Exclude air
        if (state.isAir()) return true;

        // Exclude vanilla fluids
        if (block == Blocks.WATER || block == Blocks.LAVA) {
            return true;
        }

        String blockId = ForgeRegistries.BLOCKS.getKey(block) != null ?
            ForgeRegistries.BLOCKS.getKey(block).toString() : "";

        // Exclude TFC fluids
        if (blockId.startsWith("tfc:")) {
            if (blockId.contains("salt_water") || blockId.contains("spring_water") || blockId.contains("river_water")) {
                TFCEureka.LOGGER.debug("Excluding TFC water: {}", blockId);
                return true;
            }
        }

        // Exclude TFC terrain by class name
        String className = block.getClass().getName();
        if (className.startsWith("net.dries007.tfc.common.blocks")) {
            // Rock blocks
            if (className.contains(".rock.Rock") || className.contains("RockBlock")) {
                return true;
            }
            // Soil/terrain blocks
            if (className.contains(".soil.") ||
                className.contains("SoilBlock") ||
                className.contains("GrassBlock") ||
                className.contains("DirtBlock") ||
                className.contains("SandBlock") ||
                className.contains("GravelBlock") ||
                className.contains("ClayBlock") ||
                className.contains("PeatBlock") ||
                className.contains("MudBlock")) {
                return true;
            }
        }

        return false;
    }

    public static void resetStats() {
        blocksFiltered = 0;
        hasLoggedActivation = false;
    }
}

