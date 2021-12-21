package net.wartori.imm_ptl_surv_adapt.features;

import com.mojang.serialization.Codec;
import net.minecraft.structure.*;
import net.wartori.imm_ptl_surv_adapt.generators.NonEuclideanHouseGenerator;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;


public class NonEuclideanHouseFeature extends StructureFeature<DefaultFeatureConfig> {

    public NonEuclideanHouseFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec, StructureGeneratorFactory.simple(StructureGeneratorFactory.checkForBiomeOnTop(Heightmap.Type.WORLD_SURFACE_WG), NonEuclideanHouseFeature::addPieces));
    }

    public static void addPieces(StructurePiecesCollector collector, StructurePiecesGenerator.Context<DefaultFeatureConfig> context) {
        BlockPos blockPos = new BlockPos(context.chunkPos().getStartX(), 90, context.chunkPos().getStartZ());
        NonEuclideanHouseGenerator.addPieces(context.structureManager(), blockPos, BlockRotation.NONE, collector);
    }
}



