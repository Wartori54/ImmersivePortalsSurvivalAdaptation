package net.wartori.imm_ptl_surv_adapt.Items;

import net.wartori.imm_ptl_surv_adapt.CHelper;
import net.wartori.imm_ptl_surv_adapt.Global;
import net.wartori.imm_ptl_surv_adapt.Register;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.wartori.imm_ptl_surv_adapt.RegisterItemGroups;
import org.jetbrains.annotations.Nullable;
import qouteall.imm_ptl.core.portal.Portal;
import qouteall.imm_ptl.core.portal.PortalManipulation;

import java.util.List;


public class PortalCreatorOneWay extends Item {

    public PortalCreatorOneWay(Settings settings) {
        super(settings);
    }

    public static class Data {
        public Vec3d destination;
        public boolean destination_set;
        public String side;
        public boolean biFaced;
        public float width;
        public float height;

        public Data(Vec3d destination, boolean destination_set, String side, boolean biFaced, float width, float height) {
            this.destination = destination;
            this.destination_set = destination_set;
            this.side = side;
            this.biFaced = biFaced;
            this.width = width;
            this.height = height;
        }

        public NbtCompound serialize() {
            NbtCompound tag = new NbtCompound();
            tag.putDouble("DestinationX", destination.getX());
            tag.putDouble("DestinationY", destination.getY());
            tag.putDouble("DestinationZ", destination.getZ());
            tag.putBoolean("DestinationSet", destination_set);
            tag.putString("Side", side);
            tag.putBoolean("BiFaced", biFaced);
            tag.putFloat("width", width);
            tag.putFloat("height", height);
            return tag;
        }

        public static Data deserialize(NbtCompound tag) {
            return new Data(new Vec3d(tag.getDouble("DestinationX"),
                                      tag.getDouble("DestinationY"),
                                      tag.getDouble("DestinationZ")),
                            tag.getBoolean("DestinationSet"),
                            tag.getString("Side"),
                            tag.getBoolean("BiFaced"),
                            tag.getFloat("width"),
                            tag.getFloat("height"));
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient() || !user.isSneaking() || hand.equals(Hand.OFF_HAND)) {
            return super.use(world, user, hand);
        }
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        HitResult hit = minecraftClient.crosshairTarget;
        switch (hit.getType()) {
            case BLOCK:
                return super.use(world, user, hand);
            case ENTITY:
            case MISS:
                break;
        }
        CHelper.safeOpenScreenPortalCreator(user, hand);

        return super.use(world, user, hand);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getWorld().isClient()) {
            return super.useOnBlock(context);
        }
        Data data = Data.deserialize(context.getStack().getOrCreateNbt());
        PlayerEntity user = context.getPlayer();
        if (user == null) {
            return super.useOnBlock(context);
        }
        World world = context.getWorld();
        if (context.getPlayer().isSneaking()) {
            Vec3d viewVector = user.getRotationVector();
            Direction facingHorizontal = Direction.getFacing(viewVector.x, 0, viewVector.z);
            if (!isAreaOfBlock(context.getBlockPos().add(truncateSafely(data.width*facingHorizontal.rotateYCounterclockwise().getVector().getX()/2),
                    1,
                    truncateSafely(data.width*facingHorizontal.rotateYCounterclockwise().getVector().getZ()/2)),
                    context.getBlockPos().add(truncateSafely(data.width*-facingHorizontal.rotateYCounterclockwise().getVector().getX()/2),
                            data.height,
                            truncateSafely(data.width*-facingHorizontal.rotateYCounterclockwise().getVector().getZ()/2)),
                    Blocks.AIR,
                    world)) {
                user.sendMessage(new TranslatableText("message.imm_ptl_surv_adapt.portal_obstructed_destination"), false);
                return ActionResult.FAIL;
            }
            Vec3d targetBlock = new Vec3d(context.getBlockPos().getX(), context.getBlockPos().getY(), context.getBlockPos().getZ());
            data.destination = targetBlock;
            data.destination_set = true;
            data.side = context.getSide().toString();
            NbtCompound tag = data.serialize();
            context.getStack().setNbt(tag);
            context.getPlayer().sendMessage(new TranslatableText("message.imm_ptl_surv_adapt.destination_set", targetBlock.x, targetBlock.y, targetBlock.z), false);
            return ActionResult.SUCCESS;
        } else {
            if (data.destination_set) {
                if (data.destination.distanceTo(Vec3d.ofCenter(context.getBlockPos())) > Global.currConfig.maxPortalCreatorDistance) {
                    user.sendMessage(new TranslatableText("message.imm_ptl_surv_adapt.place_too_far", Global.currConfig.maxPortalCreatorDistance), false);
                    return ActionResult.FAIL;

                }

                Vec3d viewVector = user.getRotationVector();
                Direction facingHorizontal = Direction.getFacing(viewVector.x, 0, viewVector.z);
                BlockPos destinationBlockPos = new BlockPos(data.destination.x, data.destination.y, data.destination.z);
                if (!isAreaOfBlock(context.getBlockPos().add(truncateSafely(data.width*facingHorizontal.rotateYCounterclockwise().getVector().getX()/2),
                                                             1,
                                                             truncateSafely(data.width*facingHorizontal.rotateYCounterclockwise().getVector().getZ()/2)),
                                   context.getBlockPos().add(truncateSafely(data.width*-facingHorizontal.rotateYCounterclockwise().getVector().getX()/2),
                                                             data.height,
                                                            truncateSafely(data.width*-facingHorizontal.rotateYCounterclockwise().getVector().getZ()/2)),
                                   Blocks.AIR,
                                   world)) {
                    user.sendMessage(new TranslatableText("message.imm_ptl_surv_adapt.portal_obstructed_origin"), false);
                    return ActionResult.FAIL;

                } else if (!isAreaOfBlock(destinationBlockPos.add(truncateSafely(data.width*facingHorizontal.rotateYCounterclockwise().getVector().getX()/2),
                        1,
                        truncateSafely(data.width*facingHorizontal.rotateYCounterclockwise().getVector().getZ()/2)),
                        destinationBlockPos.add(truncateSafely(data.width*-facingHorizontal.rotateYCounterclockwise().getVector().getX()/2),
                                data.height,
                                truncateSafely(data.width*-facingHorizontal.rotateYCounterclockwise().getVector().getZ()/2)),
                        Blocks.AIR,
                        world)) {
                    user.sendMessage(new TranslatableText("message.imm_ptl_surv_adapt.portal_obstructed_destination"), false);
                    return ActionResult.FAIL;

                }
                data.width = data.width > Global.currConfig.maxPortalCreatorWidth ? Global.currConfig.maxPortalCreatorWidth : data.width;
                data.height = data.height > Global.currConfig.maxPortalCreatorHeight ? Global.currConfig.maxPortalCreatorHeight : data.height;

                Portal portal = PortalManipulation.placePortal(data.width, data.height, user);
                if (portal == null) {
                    return ActionResult.FAIL;
                }
                portal.setDestinationDimension(world.getRegistryKey());
                Vec3d axisH2 = Vec3d.of(Direction.valueOf(data.side.toUpperCase()).getVector());
                portal.setDestination(data.destination.add(0.5,0.5,0.5).add(axisH2.multiply(0.5 + data.height / 2.0)));
                world.spawnEntity(portal);
                if (data.biFaced) {
                    Portal portalFlipped = PortalManipulation.createFlippedPortal(portal, Portal.entityType);
                    world.spawnEntity(portalFlipped);
                }
                if (!user.isCreative()) {
                    user.setStackInHand(context.getHand(), ItemStack.EMPTY);
                }
                return ActionResult.SUCCESS;

            } else {
                user.sendMessage(new TranslatableText("message.imm_ptl_surv_adapt.destination_not_set"), false);
            }
        }
        return ActionResult.FAIL;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(1, new TranslatableText("desc.imm_ptl_surv_adapt.portal_creator_one_way"));
        tooltip.add(2, new TranslatableText("desc.imm_ptl_surv_adapt-portal_creator_one_way_line_2"));
        if (CHelper.safeHasShiftDown()) {
            Data data = Data.deserialize(stack.getOrCreateNbt());
            if (data.destination_set) {
                tooltip.add(3, new TranslatableText("tooltip.imm_ptl_surv_adapt.portal_creator_one_way_destination_set", data.destination.x, data.destination.y, data.destination.z));
            } else {
                tooltip.add(3, new TranslatableText("tooltip.imm_ptl_surv_adapt.portal_creator_one_way_destination_not_set"));
            }
            if (data.biFaced) {
                tooltip.add(4, new TranslatableText("tooltip.imm_ptl_surv_adapt.portal_creator_one_way_bi_faced"));
            } else {
                tooltip.add(4, new TranslatableText("tooltip.imm_ptl_surv_adapt.portal_creator_one_way_not_bi_faced"));
            }
            tooltip.add(5, new TranslatableText("tooltip.imm_ptl_surv_adapt.portal_creator_one_way_width_and_number", data.width));
            tooltip.add(6, new TranslatableText("tooltip.imm_ptl_surv_adapt.portal_creator_one_way_height_and_number", data.height));
        } else {
            tooltip.add(3, new TranslatableText("tooltip.imm_ptl_surv_adapt.shift_for_more_info"));
            tooltip.add(4, new TranslatableText("tooltip.imm_ptl_surv_adapt.shift_use_to_configure"));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        if (this.isIn(group) || RegisterItemGroups.IMMERSIVE_PORTALS_SURVIVAL_ADAPTATION_GROUP == group) {
            for (boolean j :
                    new boolean[]{true, false}) {
                ItemStack itemStack = new ItemStack(Register.PORTAL_CREATOR_ONE_WAY);
                Data data = new Data(Vec3d.ZERO, false, "down", j, 3, 3);
                itemStack.setNbt(data.serialize());
                stacks.add(itemStack);
            }
        }

    }

    public static boolean isAreaOfBlock(BlockPos start, BlockPos end, Block block, World world) {
//        System.out.println(start + " " + end);
        BlockPos realStart = new BlockPos(Math.min(start.getX(), end.getX()),
                                          Math.min(start.getY(), end.getY()),
                                          Math.min(start.getZ(), end.getZ()));
        BlockPos realEnd = new BlockPos(Math.max(start.getX(), end.getX()),
                                        Math.max(start.getY(), end.getY()),
                                        Math.max(start.getZ(), end.getZ()));
        for (int i = realStart.getX(); i < realEnd.getX()+1; i++) {
            for (int j = realStart.getY(); j < realEnd.getY()+1; j++) {
                for (int k = realStart.getZ(); k < realEnd.getZ()+1; k++) {
//                    System.out.println(world.getBlockState(new BlockPos(i, j, k)).getBlock());
                    if (!world.getBlockState(new BlockPos(i, j, k)).getBlock().equals(block)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static double truncateSafely(double value) {
        // For negative numbers, use Math.ceil.
        // ... For positive numbers, use Math.floor.
        if (value < 0) {
            return Math.ceil(value);
        } else {
            return Math.floor(value);
        }
    }

}
