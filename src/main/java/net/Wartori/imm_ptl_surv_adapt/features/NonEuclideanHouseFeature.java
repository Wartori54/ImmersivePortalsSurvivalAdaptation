package net.Wartori.imm_ptl_surv_adapt.features;

import com.mojang.serialization.Codec;
import net.Wartori.imm_ptl_surv_adapt.generators.NonEuclideanHouseGenerator;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.Random;

public class NonEuclideanHouseFeature extends StructureFeature<DefaultFeatureConfig> {

    public NonEuclideanHouseFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public StructureStartFactory<DefaultFeatureConfig> getStructureStartFactory() {
        return Start::new;
    }


    public static class Start extends StructureStart<DefaultFeatureConfig> {

        public Start(StructureFeature<DefaultFeatureConfig> feature, int chunkX, int chunkZ, BlockBox box, int references, long seed) {
            super(feature, chunkX, chunkZ, box, references, seed);
        }

        @Override
        public void init(DynamicRegistryManager registryManager, ChunkGenerator chunkGenerator, StructureManager manager, int chunkX, int chunkZ, Biome biome, DefaultFeatureConfig config) {
            int x = chunkX * 16;
            int z = chunkZ * 16;
            x += random.nextInt(16);
            z += random.nextInt(16);
            int startY = chunkGenerator.getHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG);
            int y = startY-20;
            BlockPos pos = new BlockPos(x, y, z);
            NonEuclideanHouseGenerator.addPieces(manager, pos, BlockRotation.NONE, this.children);
            this.setBoundingBoxFromChildren();

        }
    }
}
