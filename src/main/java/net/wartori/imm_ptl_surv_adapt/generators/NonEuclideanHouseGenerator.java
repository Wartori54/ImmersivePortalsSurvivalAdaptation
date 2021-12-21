package net.wartori.imm_ptl_surv_adapt.generators;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.wartori.imm_ptl_surv_adapt.Register;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.structure.*;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;

import java.util.Random;

public class NonEuclideanHouseGenerator {
    private static final Identifier STRUCTURE = new Identifier("imm_ptl_surv_adapt:noneucledianhouse");

    public static void addPieces(StructureManager manager, BlockPos pos, BlockRotation rotation, StructurePiecesHolder holder) {
        holder.addPiece(new NonEuclideanHousePiece(manager, STRUCTURE, pos, rotation, 0));
    }

    public static class NonEuclideanHousePiece extends SimpleStructurePiece {

        public NonEuclideanHousePiece(StructureManager manager, NbtCompound nbt) {
            super(Register.NON_EUCLIDEAN_HOUSE_PIECE, nbt, manager, (identifier) -> createPlacementData());
        }

        public NonEuclideanHousePiece(StructureManager manager, Identifier identifier, BlockPos pos, BlockRotation rotation, int yOffset) {
            super(Register.NON_EUCLIDEAN_HOUSE_PIECE, 0, manager, identifier, identifier.toString(),
                    NonEuclideanHousePiece.createPlacementData(), pos);
        }

        @Override
        protected void handleMetadata(String metadata, BlockPos pos, ServerWorldAccess serverWorldAccess, Random random, BlockBox boundingBox) {

        }

        private static StructurePlacementData createPlacementData() {
            return new StructurePlacementData()
                    .setRotation(BlockRotation.NONE)
                    .setMirror(BlockMirror.NONE)
                    .addProcessor(BlockIgnoreStructureProcessor.IGNORE_STRUCTURE_BLOCKS);
        }

        @Override
        protected void writeNbt(StructureContext context, NbtCompound tag) {
            super.writeNbt(context, tag);
            tag.putString("Rot", this.placementData.getRotation().name());
        }

        @Override
        public void generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox chunkBox, ChunkPos chunkPos, BlockPos pos) {
            int i = world.getTopY(Heightmap.Type.WORLD_SURFACE_WG, this.pos.getX(), this.pos.getZ());
            //i = 70;
            this.pos = this.pos.add(0,i - 90 - 20, 0);
            super.generate(world, structureAccessor, chunkGenerator, random, chunkBox, chunkPos, pos);
        }
    }
}
