package net.wartori.imm_ptl_surv_adapt.Blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;

public class PortalBlock extends Block {

    public PortalBlock() {
        super(FabricBlockSettings.of(Material.METAL).requiresTool().breakByTool(FabricToolTags.PICKAXES).sounds(BlockSoundGroup.STONE).strength(5, 6));
    }
}
