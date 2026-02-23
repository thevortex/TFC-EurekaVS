package com.thevortex.tfceureka.datagen;

import net.minecraft.data.loot.packs.VanillaBlockLoot;

public class TFCEurekaLootTables extends VanillaBlockLoot {
    @Override
    public void generate() {
        getKnownBlocks().forEach(this::dropSelf);
    }
}
