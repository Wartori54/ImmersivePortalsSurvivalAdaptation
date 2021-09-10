package net.wartori.imm_ptl_surv_adapt.Blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;

public class PortalBlock extends Block {
    // TODO: 9/9/21 Make this portal brick, and create a new portal block, made of 9 portal ingots, like vanilla iron
    public PortalBlock() {
        super(FabricBlockSettings.of(Material.METAL).requiresTool().breakByTool(FabricToolTags.PICKAXES).sounds(BlockSoundGroup.STONE).strength(5, 6));
    }
}
