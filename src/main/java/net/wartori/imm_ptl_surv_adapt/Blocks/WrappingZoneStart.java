package net.wartori.imm_ptl_surv_adapt.Blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class WrappingZoneStart extends FacingBlock implements Waterloggable {
    public static final BooleanProperty WATERLOGGED = BooleanProperty.of("waterlogged");
    public static final float pixel = 1f/16f;
    public static final VoxelShape[] DOWN_SHAPE = new VoxelShape[]{
            VoxelShapes.cuboid(pixel*3, 0F, pixel*3, pixel*13, pixel, pixel*13),
            VoxelShapes.cuboid(pixel*5, pixel, pixel*5, pixel*11, 1F, pixel*11)
    };
    public static final VoxelShape[] UP_SHAPE = new VoxelShape[]{
            VoxelShapes.cuboid(pixel*3, 1F-pixel, pixel*3, pixel*13, 1F, pixel*13),
            VoxelShapes.cuboid(pixel*5, 0F, pixel*5, pixel*11, 1F-pixel, pixel*11)
    };
    public static final VoxelShape[] NORTH_SHAPE = new VoxelShape[]{
            VoxelShapes.cuboid(pixel*3, pixel*3, 0F, pixel*13, pixel*13, pixel),
            VoxelShapes.cuboid(pixel*5, pixel*5, pixel, pixel*11, pixel*11, 1F)
    };
    public static final VoxelShape[] SOUTH_SHAPE = new VoxelShape[]{
            VoxelShapes.cuboid(pixel*3, pixel*3, 1F-pixel, pixel*13, pixel*13, 1F),
            VoxelShapes.cuboid(pixel*5, pixel*5, 0F, pixel*11, pixel*11, 1F-pixel)
    };
    public static final VoxelShape[] WEST_SHAPE = new VoxelShape[]{
            VoxelShapes.cuboid(0F, pixel*3, pixel*3, pixel, pixel*13, pixel*13),
            VoxelShapes.cuboid(pixel, pixel*5, pixel*5, 1F, pixel*11, pixel*11)
    };
    public static final VoxelShape[] EST_SHAPE = new VoxelShape[]{
            VoxelShapes.cuboid(1F-pixel, pixel*3, pixel*3, 1F, pixel*13, pixel*13),
            VoxelShapes.cuboid(0F, pixel*5, pixel*5, 1F-pixel, pixel*11, pixel*11)
    };

    public WrappingZoneStart() {
        super(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).sounds(BlockSoundGroup.STONE).strength(3, 3));
        setDefaultState(getStateManager().getDefaultState().with(WATERLOGGED, false).with(Properties.FACING, Direction.DOWN));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        switch (dir) {
            case UP:
                return VoxelShapes.union(UP_SHAPE[0], UP_SHAPE[1]);
            case NORTH:
                return VoxelShapes.union(NORTH_SHAPE[0], NORTH_SHAPE[1]);
            case SOUTH:
                return VoxelShapes.union(SOUTH_SHAPE[0], SOUTH_SHAPE[1]);
            case WEST:
                return VoxelShapes.union(WEST_SHAPE[0], WEST_SHAPE[1]);
            case EAST:
                return VoxelShapes.union(EST_SHAPE[0], EST_SHAPE[1]);
            default:
                return VoxelShapes.union(DOWN_SHAPE[0], DOWN_SHAPE[1]);
        }
    }

    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(WATERLOGGED);
        stateManager.add(Properties.FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction direction = ctx.getSide();
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        return (BlockState)this.getDefaultState().with(FACING, direction.getOpposite()).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
    }
}
