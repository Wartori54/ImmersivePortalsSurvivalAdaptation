package net.Wartori.imm_ptl_surv_adapt.Blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PortalBlock extends Block {

    public PortalBlock() {
        super(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES).sounds(BlockSoundGroup.STONE).strength(5, 6));
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
//        if (world.getBlockState(pos.add(0,-1,0)).getBlock().equals(this)) {
//            Portal portal = Portal.entityType.create(world);
//            portal.setOriginPos(new Vec3d(pos.getX(), pos.getY(), pos.getZ()));
//            portal.setDestinationDimension(World.OVERWORLD);
//            portal.setDestination(new Vec3d(pos.getX(), pos.getY() + 10, pos.getZ()));
//            portal.setOrientationAndSize(
//                    new Vec3d(1, 0, 0), // axisW
//                    new Vec3d(0, 1, 0), // axisH
//                    4, // width
//                    4 // height
//            );
//            portal.world.spawnEntity(portal);
//            Portal portal1 = PortalAPI.createFlippedPortal(portal);
//            Portal portal2 = PortalAPI.createReversePortal(portal);
//            Portal portal3 = PortalAPI.createFlippedPortal(portal2);
//            portal1.world.spawnEntity(portal1);
//            portal2.world.spawnEntity(portal2);
//            portal3.world.spawnEntity(portal3);
//        }
        super.onPlaced(world, pos, state, placer, itemStack);
    }
}
