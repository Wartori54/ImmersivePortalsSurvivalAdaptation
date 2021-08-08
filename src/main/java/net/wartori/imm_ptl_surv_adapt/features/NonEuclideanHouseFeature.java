package net.wartori.imm_ptl_surv_adapt.features;

import com.mojang.serialization.Codec;
import net.wartori.imm_ptl_surv_adapt.generators.NonEuclideanHouseGenerator;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;


public class NonEuclideanHouseFeature extends StructureFeature<DefaultFeatureConfig> {

    public NonEuclideanHouseFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public StructureStartFactory<DefaultFeatureConfig> getStructureStartFactory() {
        return Start::new;
    }


    public static class Start extends StructureStart<DefaultFeatureConfig> {

        public Start(StructureFeature<DefaultFeatureConfig> structureFeature, ChunkPos chunkPos, int i, long l) {
            super(structureFeature, chunkPos, i, l);
        }

        @Override
        public void init(DynamicRegistryManager registryManager, ChunkGenerator chunkGenerator, StructureManager manager, ChunkPos pos, Biome biome, DefaultFeatureConfig config, HeightLimitView world) {
            int x = pos.getStartX();
            int z = pos.getStartZ();
            x += random.nextInt(16);
            z += random.nextInt(16);
            int startY = chunkGenerator.getHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG, world);
            int y = startY - 20;
            x -= 39/2;
            z -= 20/2;
            BlockPos blockPos = new BlockPos(x, y, z);
            NonEuclideanHouseGenerator.addPieces(manager, blockPos, BlockRotation.NONE, this.children);
            this.setBoundingBoxFromChildren();
        }
    }
}
