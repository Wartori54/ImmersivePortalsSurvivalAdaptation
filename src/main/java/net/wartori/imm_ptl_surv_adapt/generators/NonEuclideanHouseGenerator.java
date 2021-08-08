package net.wartori.imm_ptl_surv_adapt.generators;

import net.wartori.imm_ptl_surv_adapt.Register;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.*;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;

import java.util.List;
import java.util.Random;

public class NonEuclideanHouseGenerator {
    private static final Identifier STRUCTURE = new Identifier("imm_ptl_surv_adapt:noneucledianhouse");

    public static void addPieces(StructureManager manager, BlockPos pos, BlockRotation rotation, List<StructurePiece> pieces) {
        pieces.add(new NonEuclideanHousePiece(manager, STRUCTURE, pos, rotation, 0));
    }

    public static class NonEuclideanHousePiece extends SimpleStructurePiece {

        public NonEuclideanHousePiece(ServerWorld world, NbtCompound nbt) {
            super(Register.NON_EUCLIDEAN_HOUSE_PIECE, nbt, world, (identifier) -> createPlacementData());
        }

        public NonEuclideanHousePiece(StructureManager manager, Identifier identifier, BlockPos pos, BlockRotation rotation, int yOffset) {
            super(Register.NON_EUCLIDEAN_HOUSE_PIECE, 0, manager, identifier, identifier.toString(),
                    createPlacementData(), pos);
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
        protected void writeNbt(ServerWorld world, NbtCompound tag) {
            super.writeNbt(world, tag);
            tag.putString("Rot", this.placementData.getRotation().name());
        }
    }
}
