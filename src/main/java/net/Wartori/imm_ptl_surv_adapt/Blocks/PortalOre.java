package net.Wartori.imm_ptl_surv_adapt.Blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.OreBlock;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class PortalOre extends OreBlock {
    public PortalOre() {
        super(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).sounds(BlockSoundGroup.STONE).strength(3, 3));
    }

    @Override
    protected int getExperienceWhenMined(Random random) {
        return MathHelper.nextInt(random, 3, 7);
    }
}
