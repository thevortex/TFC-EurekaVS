package com.thevortex.tfceureka.datagen;

import com.thevortex.tfceureka.TFCEureka;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = TFCEureka.MODID)
public class TFCEurekaDatagen {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) throws IOException {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();

            TFCEurekaBlockTags blockTags1 = new TFCEurekaBlockTags(packOutput,event.getLookupProvider(),TFCEureka.MODID, fileHelper);
            generator.addProvider(true, blockTags1);

            generator.run();
    }
}
