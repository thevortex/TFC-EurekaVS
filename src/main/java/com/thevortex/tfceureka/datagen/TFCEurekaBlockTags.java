package com.thevortex.tfceureka.datagen;

import com.thevortex.tfceureka.registry.RegisterShipHelms;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import org.valkyrienskies.eureka.forge.EurekaBlockTagsProvider;

import java.util.concurrent.CompletableFuture;

public class TFCEurekaBlockTags extends BlockTagsProvider {
   TagKey<Block> EUREKA_HELMS=TagKey.create(net.minecraft.core.registries.Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("vs_eureka","ship_helms"));
    public TFCEurekaBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_AXE).add(RegisterShipHelms.ACACIA_HELM.get());
        tag(BlockTags.MINEABLE_WITH_AXE).add(RegisterShipHelms.BIRCH_HELM.get());
        tag(BlockTags.MINEABLE_WITH_AXE).add(RegisterShipHelms.OAK_HELM.get());
        tag(BlockTags.MINEABLE_WITH_AXE).add(RegisterShipHelms.SPRUCE_HELM.get());
        tag(BlockTags.MINEABLE_WITH_AXE).add(RegisterShipHelms.BLACKWOOD_HELM.get());
        tag(BlockTags.MINEABLE_WITH_AXE).add(RegisterShipHelms.CHESTNUT_HELM.get());
        tag(BlockTags.MINEABLE_WITH_AXE).add(RegisterShipHelms.DOUGLASFIR_HELM.get());
        tag(BlockTags.MINEABLE_WITH_AXE).add(RegisterShipHelms.HICKORY_HELM.get());
        tag(BlockTags.MINEABLE_WITH_AXE).add(RegisterShipHelms.KAPOK_HELM.get());
        tag(BlockTags.MINEABLE_WITH_AXE).add(RegisterShipHelms.MAPLE_HELM.get());
        tag(BlockTags.MINEABLE_WITH_AXE).add(RegisterShipHelms.PALM_HELM.get());
        tag(BlockTags.MINEABLE_WITH_AXE).add(RegisterShipHelms.SYCAMORE_HELM.get());
        tag(BlockTags.MINEABLE_WITH_AXE).add(RegisterShipHelms.WHITECEDAR_HELM.get());
        tag(BlockTags.MINEABLE_WITH_AXE).add(RegisterShipHelms.WILLOW_HELM.get());
        tag(BlockTags.MINEABLE_WITH_AXE).add(RegisterShipHelms.ASPEN_HELM.get());
        tag(BlockTags.MINEABLE_WITH_AXE).add(RegisterShipHelms.ASH_HELM.get());
        tag(BlockTags.MINEABLE_WITH_AXE).add(RegisterShipHelms.SEQUOIA_HELM.get());
        tag(BlockTags.MINEABLE_WITH_AXE).add(RegisterShipHelms.MANGROVE_HELM.get());
        tag(BlockTags.MINEABLE_WITH_AXE).add(RegisterShipHelms.ROSEWOOD_HELM.get());
        tag(EUREKA_HELMS).add(RegisterShipHelms.ACACIA_HELM.get());
        tag(EUREKA_HELMS).add(RegisterShipHelms.ASH_HELM.get());
        tag(EUREKA_HELMS).add(RegisterShipHelms.ASPEN_HELM.get());
        tag(EUREKA_HELMS).add(RegisterShipHelms.BIRCH_HELM.get());
        tag(EUREKA_HELMS).add(RegisterShipHelms.BLACKWOOD_HELM.get());
        tag(EUREKA_HELMS).add(RegisterShipHelms.CHESTNUT_HELM.get());
        tag(EUREKA_HELMS).add(RegisterShipHelms.DOUGLASFIR_HELM.get());
        tag(EUREKA_HELMS).add(RegisterShipHelms.HICKORY_HELM.get());
        tag(EUREKA_HELMS).add(RegisterShipHelms.KAPOK_HELM.get());
        tag(EUREKA_HELMS).add(RegisterShipHelms.MAPLE_HELM.get());
        tag(EUREKA_HELMS).add(RegisterShipHelms.OAK_HELM.get());
        tag(EUREKA_HELMS).add(RegisterShipHelms.PALM_HELM.get());
        tag(EUREKA_HELMS).add(RegisterShipHelms.SPRUCE_HELM.get());
        tag(EUREKA_HELMS).add(RegisterShipHelms.SYCAMORE_HELM.get());
        tag(EUREKA_HELMS).add(RegisterShipHelms.WHITECEDAR_HELM.get());
        tag(EUREKA_HELMS).add(RegisterShipHelms.WILLOW_HELM.get());
        tag(EUREKA_HELMS).add(RegisterShipHelms.SEQUOIA_HELM.get());
        tag(EUREKA_HELMS).add(RegisterShipHelms.MANGROVE_HELM.get());
        tag(EUREKA_HELMS).add(RegisterShipHelms.ROSEWOOD_HELM.get());

    }
}
