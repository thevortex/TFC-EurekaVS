package com.thevortex.tfceureka.client;

import com.thevortex.tfceureka.TFCEureka;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;
import org.valkyrienskies.eureka.block.ShipHelmBlock;

/**
 * Client-side event handler to ensure TFC ship helm controls work
 */
@Mod.EventBusSubscriber(modid = TFCEureka.MODID, value = net.minecraftforge.api.distmarker.Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientShipControlHandler {
    
    private static boolean wasOnShipHelm = false;
    
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return;
        
        Level level = player.level();
        BlockPos playerPos = player.blockPosition();
        
        // Check if player is standing on/near a TFC ship helm
        for (BlockPos checkPos : BlockPos.betweenClosed(
            playerPos.offset(-1, -1, -1),
            playerPos.offset(1, 1, 1))) {
            
            BlockState state = level.getBlockState(checkPos);
            Block block = state.getBlock();
            
            // Check if it's a TFC ship helm
            if (block instanceof ShipHelmBlock) {
                String blockId = block.getClass().getName();
                if (blockId.contains("tfceureka") || 
                    net.minecraftforge.registries.ForgeRegistries.BLOCKS.getKey(block).getNamespace().equals("tfceureka")) {
                    
                    if (!wasOnShipHelm) {
                        TFCEureka.LOGGER.info("Player is near TFC ship helm at {}", checkPos);
                        wasOnShipHelm = true;
                    }
                    
                    // Verify block entity exists
                    BlockEntity be = level.getBlockEntity(checkPos);
                    if (be != null) {
                        TFCEureka.LOGGER.debug("Ship helm block entity: {}", be.getClass().getSimpleName());
                    }
                    
                    return;
                }
            }
        }
        
        if (wasOnShipHelm) {
            TFCEureka.LOGGER.info("Player left TFC ship helm area");
            wasOnShipHelm = false;
        }
    }
    
    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        // Log when space is pressed while on ship
        if (event.getKey() == GLFW.GLFW_KEY_SPACE && event.getAction() == GLFW.GLFW_PRESS) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null && wasOnShipHelm) {
                TFCEureka.LOGGER.info("Space pressed while on TFC ship helm - checking if Eureka handles it");
            }
        }
    }
}

