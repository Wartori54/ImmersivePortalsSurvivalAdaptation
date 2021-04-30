package net.Wartori.imm_ptl_surv_adapt.generators;

import net.Wartori.imm_ptl_surv_adapt.Register;
import net.minecraft.nbt.CompoundTag;
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
        pieces.add(new NonEuclideanHousePiece(manager, pos, STRUCTURE, rotation));
    }

    public static class NonEuclideanHousePiece extends SimpleStructurePiece {
        private final BlockRotation rotation;
        private final Identifier template;


        public NonEuclideanHousePiece(StructureManager structureManager, CompoundTag compoundTag) {
            super(Register.NON_EUCLIDEAN_HOUSE_PIECE, compoundTag);
            this.template = new Identifier(compoundTag.getString("Template"));
            this.rotation = BlockRotation.valueOf(compoundTag.getString("Rot"));
            this.initializeStructureData(structureManager);
        }

        public NonEuclideanHousePiece(StructureManager structureManager, BlockPos pos, Identifier template, BlockRotation rotation) {
            super(Register.NON_EUCLIDEAN_HOUSE_PIECE, 0);
            this.pos = pos;
            this.rotation = rotation;
            this.template = template;

            this.initializeStructureData(structureManager);
        }

        @Override
        protected void handleMetadata(String metadata, BlockPos pos, ServerWorldAccess serverWorldAccess, Random random, BlockBox boundingBox) {

        }

        @Override
        protected void toNbt(CompoundTag tag) {
            super.toNbt(tag);
            tag.putString("Template", this.template.toString());
            tag.putString("Rot", this.rotation.name());
        }

        private void initializeStructureData(StructureManager structureManager) {
            Structure structure = structureManager.getStructureOrBlank(this.template);
            StructurePlacementData placementData = (new StructurePlacementData())
                    .setRotation(this.rotation)
                    .setMirror(BlockMirror.NONE)
                    .addProcessor(BlockIgnoreStructureProcessor.IGNORE_STRUCTURE_BLOCKS);
            this.setStructureData(structure, this.pos, placementData);
        }
    }
}
