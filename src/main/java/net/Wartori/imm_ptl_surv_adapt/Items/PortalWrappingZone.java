package net.Wartori.imm_ptl_surv_adapt.Items;

import com.qouteall.immersive_portals.portal.Portal;
import com.qouteall.immersive_portals.portal.PortalManipulation;
import com.qouteall.immersive_portals.portal.global_portals.WorldWrappingPortal;
import net.Wartori.imm_ptl_surv_adapt.Global;
import net.Wartori.imm_ptl_surv_adapt.Register;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PortalWrappingZone extends Item {
    public PortalWrappingZone(Settings settings) {
        super(settings);
    }

    public static class Data {
        public WarpingType warpingType;
        public BlockPos startPos;
        public BlockPos endPos;
        public boolean isValidStart;
        public boolean isValidEnd;

        public Data(WarpingType warpingType, BlockPos startPos, BlockPos endPos, boolean validStart, boolean validEnd) {
            this.warpingType = warpingType;
            this.startPos = startPos;
            this.endPos = endPos;
            this.isValidStart = validStart;
            this.isValidEnd = validEnd;
        }

        public CompoundTag serialize() {
            CompoundTag tag = new CompoundTag();
            if (warpingType.toInt() == -1) {
                throw new NullPointerException();
            }
            tag.putInt("warpingType", warpingType.toInt());
            tag.putIntArray("startPos", new int[]{startPos.getX(), startPos.getY(), startPos.getZ()});
            tag.putIntArray("endPos", new int[]{endPos.getX(), endPos.getY(), endPos.getZ()});
            tag.putBoolean("isValidStart", isValidStart);
            tag.putBoolean("isValidEnd", isValidEnd);
            return tag;
        }

        public static Data deserialize(CompoundTag tag) {
            try {
                return new Data(WarpingType.fromInt(tag.getInt("warpingType")),
                        new BlockPos(tag.getIntArray("startPos")[0],
                                tag.getIntArray("startPos")[1],
                                tag.getIntArray("startPos")[2]),
                        new BlockPos(tag.getIntArray("endPos")[0],
                                tag.getIntArray("endPos")[1],
                                tag.getIntArray("endPos")[2]),
                        tag.getBoolean("isValidStart"),
                        tag.getBoolean("isValidEnd")
                );
            } catch (ArrayIndexOutOfBoundsException ignored) {
//                Global.warn("portal warping zone with invalid array");
                try {
                    return new Data(WarpingType.fromInt(tag.getInt("warpingType")),
                            new BlockPos(0, 0, 0),
                            new BlockPos(0, 0, 0),
                            false,
                            false);
                } catch (ClassCastException ignored1) {
                    Global.warn("portal warping zone with invalid data");
                    return new Data(WarpingType.IN,
                            new BlockPos(0, 0, 0),
                            new BlockPos(0, 0, 0),
                            false,
                            false);
                }
            }
        }
    }

    @Override
    public void onCraft(ItemStack stack, World world, PlayerEntity player) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putIntArray("startPos", new int[]{0, 0, 0});
        tag.putIntArray("endPos", new int[]{0, 0, 0});
        stack.setTag(tag);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        CompoundTag tag = stack.getOrCreateTag();
//        if (tag == null) {
//            tag = new Data(WarpingType.IN, new BlockPos(0,0,0), new BlockPos(0,0,0), false, false).serialize();
//        }
        Data data = Data.deserialize(tag);
        tooltip.add(new TranslatableText("tooltip.imm_ptl_surv_adapt.portal_wrapping_zone_desc"));
        tooltip.add(new TranslatableText("tooltip.imm_ptl_surv_adapt.portal_wrapping_zone_desc1"));
        tooltip.add(new TranslatableText("tooltip.imm_ptl_surv_adapt.portal_wrapping_zone_type", data.warpingType));
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        if (this.isIn(group)) {
            ItemStack stackIn = new ItemStack(this);
            stackIn.setTag(new Data(WarpingType.IN, new BlockPos(0,0,0), new BlockPos(0,0,0), false, false).serialize());
            stacks.add(stackIn);

            ItemStack stackOut = new ItemStack(this);
            stackOut.setTag(new Data(WarpingType.OUT, new BlockPos(0,0,0), new BlockPos(0,0,0), false, false).serialize());
            stacks.add(stackOut);

            ItemStack stackBoth = new ItemStack(this);
            stackBoth.setTag(new Data(WarpingType.BOTH, new BlockPos(0,0,0), new BlockPos(0,0,0), false, false).serialize());
            stacks.add(stackBoth);
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient()) {
            ItemStack stack = user.getStackInHand(hand);
            Data data = Data.deserialize(stack.getOrCreateTag());
            if (data.isValidStart && data.isValidEnd) {
                createWarpingBox(data.warpingType, data.startPos, data.endPos, (ServerWorld) world);
                return TypedActionResult.success(stack);
            }
        }
        return TypedActionResult.fail(user.getStackInHand(hand));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getPlayer() == null) {
            return ActionResult.FAIL;
        }
        if (!context.getWorld().isClient()) {
            ItemStack stack = context.getStack();
            Data data;
            if (context.getWorld().getBlockState(context.getBlockPos()).getBlock() == Register.WRAPPING_ZONE_START) {
                data = Data.deserialize(stack.getOrCreateTag());
                data.startPos = context.getBlockPos();
                data.isValidStart = true;
                stack.setTag(data.serialize());
                context.getPlayer().sendMessage(
                        new TranslatableText("message.imm_ptl_surv_adapt.warping_zone_start_set",
                                context.getBlockPos().getX(),
                                context.getBlockPos().getY(),
                                context.getBlockPos().getZ()),
                        false);
            } else if (context.getWorld().getBlockState(context.getBlockPos()).getBlock() == Register.WRAPPING_ZONE_END) {
                data = Data.deserialize(stack.getOrCreateTag());
                data.endPos = context.getBlockPos();
                data.isValidEnd = true;
                stack.setTag(data.serialize());
                context.getPlayer().sendMessage(
                        new TranslatableText("message.imm_ptl_surv_adapt.warping_zone_end_set",
                                context.getBlockPos().getX(),
                                context.getBlockPos().getY(),
                                context.getBlockPos().getZ()),
                        false);
            } else {
                if (Data.deserialize(stack.getOrCreateTag()).isValidStart && Data.deserialize(stack.getOrCreateTag()).isValidEnd) {
                    if (!context.getPlayer().isSneaking()) return ActionResult.FAIL;
                    data = Data.deserialize(stack.getOrCreateTag());
                    if (context.getWorld().getBlockState(data.startPos).getBlock() != Register.WRAPPING_ZONE_START ||
                        context.getWorld().getBlockState(data.endPos).getBlock() != Register.WRAPPING_ZONE_END) {
                        context.getPlayer().sendMessage(
                                new TranslatableText("message.imm_ptl_surv_adapt.missing_warping_zone_blocks"),
                                false
                        );
                        return ActionResult.FAIL;
                    }
                    Global.log(data.startPos.getSquaredDistance(data.endPos));
                    if (data.startPos.getSquaredDistance(data.endPos) > Math.pow(Global.currConfig.maxPortalWrappingBoxSize, 2)) {
                        context.getPlayer().sendMessage(new TranslatableText("message.imm_ptl_surv_adapt.wrapping_zone_too_big"), false);
                        return ActionResult.FAIL;
                    }
                    createWarpingBox(data.warpingType, data.startPos, data.endPos, (ServerWorld) context.getWorld());
                    context.getWorld().breakBlock(data.startPos, false);
                    context.getWorld().breakBlock(data.endPos, false);
                    if (!context.getPlayer().isCreative())
                        context.getStack().setCount(0);
                    return ActionResult.SUCCESS;
                }
                return ActionResult.FAIL;
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.FAIL;
    }

    public static void createWarpingBox(WarpingType warpingType, BlockPos start, BlockPos end, ServerWorld world) {

        Box box = createBox(start, end);
        for (Direction direction:
             Direction.values()) {
            Portal portal = Portal.entityType.create(world);
            if (warpingType == WarpingType.IN) {
                WorldWrappingPortal.initWrappingPortal(world, box, direction, true, portal);
                PortalManipulation.getPortalClutter(world, portal.getOriginPos(), portal.getNormal(), p -> true).forEach(Portal::remove);

                // to prevent z fighting
                portal.setOriginPos(portal.getOriginPos().add(direction.getVector().getX()*0.001, direction.getVector().getY()*0.001, direction.getVector().getZ()*0.001));
            } else if (warpingType == WarpingType.OUT) {
                WorldWrappingPortal.initWrappingPortal(world, box, direction, false, portal);
                PortalManipulation.getPortalClutter(world, portal.getOriginPos(), portal.getNormal(), p -> true).forEach(Portal::remove);

                // to prevent z fighting
                portal.setOriginPos(portal.getOriginPos().add(-direction.getVector().getX()*0.001, -direction.getVector().getY()*0.001, -direction.getVector().getZ()*0.001));
            } else {
                Portal portalOtherSide = Portal.entityType.create(world);
                WorldWrappingPortal.initWrappingPortal(world, box, direction, true, portal);
                WorldWrappingPortal.initWrappingPortal(world, box, direction, false, portalOtherSide);
                PortalManipulation.getPortalClutter(world, portal.getOriginPos(), portal.getNormal(), p -> true).forEach(Portal::remove);
                PortalManipulation.getPortalClutter(world, portalOtherSide.getOriginPos(), portalOtherSide.getNormal(), p -> true).forEach(Portal::remove);

                // to prevent z fighting
                portal.setOriginPos(portal.getOriginPos().add(direction.getVector().getX()*0.001, direction.getVector().getY()*0.001, direction.getVector().getZ()*0.001));
                portalOtherSide.setOriginPos(portalOtherSide.getOriginPos().add(-direction.getVector().getX()*0.001, -direction.getVector().getY()*0.001, -direction.getVector().getZ()*0.001));
                world.spawnEntity(portalOtherSide);
            }

            world.spawnEntity(portal);
        }
    }


    // to make the start block and end block always be in the box
    public static Box createBox(BlockPos start, BlockPos end) {
        if (start.getX()-end.getX() > 0) {
            start = new BlockPos(start.add(1, 0, 0));
        } else {
            end = new BlockPos(end.add(1, 0, 0));
        }
        if (start.getY()-end.getY() > 0) {
            start = new BlockPos(start.add(0, 1, 0));
        } else {
            end = new BlockPos(end.add(0, 1, 0));
        }
        if (start.getZ()-end.getZ() > 0) {
            start = new BlockPos(start.add(0, 0, 1));
        } else {
            end = new BlockPos(end.add(0, 0, 1));
        }
        return new Box(start, end);
    }

    public enum WarpingType {
        IN,
        OUT,
        BOTH;

        int toInt() {
            if (this == IN) {
                return 0;
            } else if (this == OUT) {
                return 1;
            } else if (this == BOTH) {
                return 2;
            }
            return 0;
        }

        public static WarpingType fromInt(int i) {
            if (i == 0) {
                return IN;
            } else if (i == 1) {
                return OUT;
            } else if (i == 2) {
                return BOTH;
            }
            Global.warn("Invalid mode");
            return IN;
        }
    }
}
