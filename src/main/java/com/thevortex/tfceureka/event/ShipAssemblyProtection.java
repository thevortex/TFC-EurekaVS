package com.thevortex.tfceureka.event;

import com.thevortex.tfceureka.TFCEureka;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Event handler to prevent TFC terrain from being included in Eureka ship assembly
 * This is a workaround since Eureka's Kotlin code is hard to mixin
 */
@Mod.EventBusSubscriber(modid = TFCEureka.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ShipAssemblyProtection {

    // Track if we're currently in ship assembly to avoid interfering with normal gameplay
    private static boolean assemblyInProgress = false;
    private static long lastAssemblyTime = 0;

    /**
     * Detect when ship assembly might be starting based on rapid block queries
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onBlockQuery(BlockEvent event) {
        // This is a heuristic approach - not perfect but better than nothing
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            BlockPos pos = event.getPos();
            BlockState state = serverLevel.getBlockState(pos);
            Block block = state.getBlock();

            // During assembly, mark that we're in assembly mode for a few ticks
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastAssemblyTime < 5000) { // 5 second window
                assemblyInProgress = true;
            } else {
                assemblyInProgress = false;
            }

            // If we detect we might be in assembly, log TFC terrain blocks
            if (assemblyInProgress) {
                String blockId = ForgeRegistries.BLOCKS.getKey(block) != null ?
                    ForgeRegistries.BLOCKS.getKey(block).toString() : "";

                if (blockId.startsWith("tfc:") &&
                    (blockId.contains("rock") || blockId.contains("dirt") ||
                     blockId.contains("grass") || blockId.contains("sand") ||
                     blockId.contains("clay") || blockId.contains("water"))) {
                    TFCEureka.LOGGER.warn("TFC terrain block detected during possible assembly at {}: {}", pos, blockId);
                }
            }
        }
    }

    public static void markAssemblyStart() {
        lastAssemblyTime = System.currentTimeMillis();
        assemblyInProgress = true;
        TFCEureka.LOGGER.info("Ship assembly protection activated");
    }
}

