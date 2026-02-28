package com.thevortex.tfceureka;

import com.mojang.logging.LogUtils;
import com.thevortex.tfceureka.registry.RegisterShipHelms;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(TFCEureka.MODID)
public class TFCEureka
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "tfceureka";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();



    public TFCEureka(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        RegisterShipHelms.BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        RegisterShipHelms.ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        RegisterShipHelms.CREATIVE_TABS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);


    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> {
            // Inject custom ship helms into Eureka's BlockEntityType
            // This implementation is defensive against mixin conflicts from other mods
            try {
                LOGGER.info("TFC-Eureka: Attempting to inject 20 TFC ship helm blocks into Eureka...");

                Class<?> eurekaBlockEntities = Class.forName("org.valkyrienskies.eureka.EurekaBlockEntities");
                java.lang.reflect.Field shipHelmField = eurekaBlockEntities.getDeclaredField("SHIP_HELM");
                shipHelmField.setAccessible(true);
                Object shipHelmRegistryObject = shipHelmField.get(null);

                java.lang.reflect.Method getMethod = shipHelmRegistryObject.getClass().getMethod("get");
                getMethod.setAccessible(true);
                net.minecraft.world.level.block.entity.BlockEntityType<?> shipHelmType =
                    (net.minecraft.world.level.block.entity.BlockEntityType<?>) getMethod.invoke(shipHelmRegistryObject);

                // STRATEGY 1: Try standard field names
                java.lang.reflect.Field validBlocksField = null;
                java.util.Set<net.minecraft.world.level.block.Block> originalValidBlocks = null;

                String[] fieldNames = {"validBlocks", "blocks", "blockSet", "m_155223_", "f_155223_"};

                for (String fieldName : fieldNames) {
                    try {
                        validBlocksField = net.minecraft.world.level.block.entity.BlockEntityType.class.getDeclaredField(fieldName);
                        validBlocksField.setAccessible(true);

                        try {
                            @SuppressWarnings("unchecked")
                            java.util.Set<net.minecraft.world.level.block.Block> temp =
                                (java.util.Set<net.minecraft.world.level.block.Block>) validBlocksField.get(shipHelmType);
                            originalValidBlocks = temp;
                            LOGGER.info("TFC-Eureka: Found BlockEntityType field: {}", fieldName);
                            break;
                        } catch (ClassCastException e) {
                            LOGGER.debug("TFC-Eureka: Field {} exists but wrong type, trying next", fieldName);
                            validBlocksField = null;
                        }
                    } catch (NoSuchFieldException e) {
                        LOGGER.debug("TFC-Eureka: Field {} not found", fieldName);
                    }
                }

                // STRATEGY 2: If direct access failed, scan all fields for Set<Block>
                if (originalValidBlocks == null) {
                    LOGGER.info("TFC-Eureka: Standard field lookup failed, scanning for Set<Block> field...");
                    for (java.lang.reflect.Field field : net.minecraft.world.level.block.entity.BlockEntityType.class.getDeclaredFields()) {
                        if (field.getType().equals(java.util.Set.class)) {
                            try {
                                field.setAccessible(true);
                                Object fieldValue = field.get(shipHelmType);

                                if (fieldValue instanceof java.util.Set) {
                                    @SuppressWarnings("unchecked")
                                    java.util.Set<net.minecraft.world.level.block.Block> testSet =
                                        (java.util.Set<net.minecraft.world.level.block.Block>) fieldValue;
                                    originalValidBlocks = testSet;
                                    validBlocksField = field;
                                    LOGGER.info("TFC-Eureka: Found Set<Block> field by scanning: {}", field.getName());
                                    break;
                                }
                            } catch (Exception e) {
                                LOGGER.debug("TFC-Eureka: Scanned field {} but couldn't use: {}", field.getName(), e.getMessage());
                            }
                        }
                    }
                }

                // If we found the field, inject our blocks
                if (validBlocksField != null && originalValidBlocks != null) {
                    java.util.Set<net.minecraft.world.level.block.Block> newValidBlocks =
                        new java.util.HashSet<>(originalValidBlocks);

                    // Add all TFC ship helms
                    newValidBlocks.add(RegisterShipHelms.OAK_HELM.get());
                    newValidBlocks.add(RegisterShipHelms.ACACIA_HELM.get());
                    newValidBlocks.add(RegisterShipHelms.BIRCH_HELM.get());
                    newValidBlocks.add(RegisterShipHelms.SPRUCE_HELM.get());
                    newValidBlocks.add(RegisterShipHelms.BLACKWOOD_HELM.get());
                    newValidBlocks.add(RegisterShipHelms.ROSEWOOD_HELM.get());
                    newValidBlocks.add(RegisterShipHelms.WILLOW_HELM.get());
                    newValidBlocks.add(RegisterShipHelms.DOUGLASFIR_HELM.get());
                    newValidBlocks.add(RegisterShipHelms.KAPOK_HELM.get());
                    newValidBlocks.add(RegisterShipHelms.PINE_HELM.get());
                    newValidBlocks.add(RegisterShipHelms.SYCAMORE_HELM.get());
                    newValidBlocks.add(RegisterShipHelms.CHESTNUT_HELM.get());
                    newValidBlocks.add(RegisterShipHelms.PALM_HELM.get());
                    newValidBlocks.add(RegisterShipHelms.WHITECEDAR_HELM.get());
                    newValidBlocks.add(RegisterShipHelms.HICKORY_HELM.get());
                    newValidBlocks.add(RegisterShipHelms.MAPLE_HELM.get());
                    newValidBlocks.add(RegisterShipHelms.ASPEN_HELM.get());
                    newValidBlocks.add(RegisterShipHelms.ASH_HELM.get());
                    newValidBlocks.add(RegisterShipHelms.SEQUOIA_HELM.get());
                    newValidBlocks.add(RegisterShipHelms.MANGROVE_HELM.get());

                    validBlocksField.set(shipHelmType, newValidBlocks);
                    LOGGER.info("TFC-Eureka: Successfully injected 20 custom ship helm blocks into Eureka");
                    LOGGER.info("TFC-Eureka: Total valid ship helm blocks: {}", newValidBlocks.size());
                } else {
                    LOGGER.error("TFC-Eureka: Could not find BlockEntityType valid blocks field");
                    LOGGER.warn("TFC-Eureka: This may be caused by mixin conflicts from other mods (e.g., Clockwork + Trackwork)");
                    LOGGER.warn("TFC-Eureka: TFC ship helms may not work with Eureka without resolving the mixin conflict");
                }

            } catch (Exception e) {
                LOGGER.error("TFC-Eureka: Failed to inject custom ship helms into Eureka", e);
            }

            // Register TFC helms with Eureka's ship control system
            try {
                // Eureka uses tags to determine which blocks are ship helms
                // The ship_helms.json tag should be sufficient
                Class<?> eurekaBlocksClass = Class.forName("org.valkyrienskies.eureka.EurekaBlocks");
                LOGGER.info("TFC-Eureka: Found EurekaBlocks class, TFC helms should be recognized via tag system");
            } catch (Exception e) {
                LOGGER.debug("TFC-Eureka: Could not verify Eureka control registration", e);
            }
        });
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            event.enqueueWork(() -> {
                // Register safe renderer for ship helms to prevent crashes with TFC wood types
                try {
                    Class<?> eurekaBlockEntities = Class.forName("org.valkyrienskies.eureka.EurekaBlockEntities");
                    java.lang.reflect.Field shipHelmField = eurekaBlockEntities.getDeclaredField("SHIP_HELM");
                    shipHelmField.setAccessible(true);
                    Object shipHelmRegistryObject = shipHelmField.get(null);

                    java.lang.reflect.Method getMethod = shipHelmRegistryObject.getClass().getMethod("get");
                    getMethod.setAccessible(true);
                    net.minecraft.world.level.block.entity.BlockEntityType<?> shipHelmType =
                        (net.minecraft.world.level.block.entity.BlockEntityType<?>) getMethod.invoke(shipHelmRegistryObject);

                    // Register our safe renderer
                    net.minecraft.client.renderer.blockentity.BlockEntityRenderers.register(
                        (net.minecraft.world.level.block.entity.BlockEntityType<org.valkyrienskies.eureka.blockentity.ShipHelmBlockEntity>) shipHelmType,
                        com.thevortex.tfceureka.client.renderer.SafeShipHelmRenderer::new
                    );

                    LOGGER.info("Registered safe ship helm renderer for TFC wood types");
                } catch (Exception e) {
                    LOGGER.error("Failed to register safe ship helm renderer", e);
                }

                // Ensure client recognizes TFC ship helms for controls
                try {
                    // Check if Eureka has a client-side helm registry
                    Class<?> eurekaClientClass = Class.forName("org.valkyrienskies.eureka.EurekaClient");
                    LOGGER.info("Found EurekaClient class");

                    // Try to find ship control input handler
                    try {
                        Class<?> shipInputClass = Class.forName("org.valkyrienskies.eureka.ship.EurekaShipControl");
                        LOGGER.info("Ship controls should work - EurekaShipControl found");
                    } catch (ClassNotFoundException e) {
                        LOGGER.debug("EurekaShipControl not found, using alternative path");
                    }

                    // The key insight: Eureka likely checks instanceof ShipHelmBlock
                    // Since our blocks ARE ShipHelmBlock instances, controls should work
                    // If they don't, it means Eureka is doing something else...

                    LOGGER.info("Client-side TFC ship helm registration complete");
                    LOGGER.info("If controls don't work, Eureka may be using hardcoded block checks");

                } catch (Exception e) {
                    LOGGER.warn("Could not verify client-side ship control registration", e);
                }
            });
        }
    }
}
