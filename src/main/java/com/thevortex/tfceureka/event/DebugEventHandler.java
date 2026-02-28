package com.thevortex.tfceureka.event;

import com.thevortex.tfceureka.TFCEureka;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Debug event handler to help diagnose ship assembly and control issues
 */
@Mod.EventBusSubscriber(modid = TFCEureka.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DebugEventHandler {

    private static int tickCounter = 0;

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        // Log when ship helm blocks are placed
        if (event.getPlacedBlock().getBlock().getClass().getSimpleName().contains("ShipHelm")) {
            TFCEureka.LOGGER.info("Ship helm block placed at {}: {}",
                event.getPos(),
                event.getPlacedBlock().getBlock().getClass().getName());

            // Check if block entity exists
            if (event.getLevel() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
                BlockEntity be = serverLevel.getBlockEntity(event.getPos());
                if (be != null) {
                    TFCEureka.LOGGER.info("Block entity created: {}", be.getClass().getName());
                } else {
                    TFCEureka.LOGGER.warn("No block entity created for ship helm!");
                }
            }
        }
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            tickCounter++;
            // Log every 100 ticks (5 seconds) about ship state if debug is enabled
            if (tickCounter % 100 == 0) {
                // This would log ship count if we had access to VS2 API
                // For now, just a periodic heartbeat
                // TFCEureka.LOGGER.debug("Server tick: {} (ships active)", tickCounter);
            }
        }
    }
}

