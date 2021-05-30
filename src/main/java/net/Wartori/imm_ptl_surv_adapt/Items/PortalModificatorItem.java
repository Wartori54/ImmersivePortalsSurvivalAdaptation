package net.Wartori.imm_ptl_surv_adapt.Items;

import com.qouteall.immersive_portals.portal.Portal;
import com.qouteall.immersive_portals.portal.PortalManipulation;
import net.Wartori.imm_ptl_surv_adapt.CHelper;
import net.Wartori.imm_ptl_surv_adapt.Global;
import net.Wartori.imm_ptl_surv_adapt.Register;
import net.Wartori.imm_ptl_surv_adapt.Utils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

import static net.Wartori.imm_ptl_surv_adapt.Utils.damageIt;


public class PortalModificatorItem extends Item {


    public static class Data {

        public int type; //1=move, 2=rotate, 3=delete
        public float distance;
        public float degrees;
        public String facesToDelete;

        public Data(int type, float distance, float degrees, String facesToDelete) {
            this.type = type;
            this.distance = distance;
            this.degrees = degrees;
            this.facesToDelete = facesToDelete;
        }

        public CompoundTag serialize() {
            CompoundTag tag = new CompoundTag();
            tag.putInt("Action", this.type);
            tag.putFloat("Distance", this.distance);
            tag.putFloat("Degrees", this.degrees);
            tag.putString("facesToDelete", facesToDelete);
            return tag;
        }

        public static Data deserialize(CompoundTag tag) {
            if (tag.getInt("Action") == 1) {
                return new Data(1, tag.getFloat("Distance"), 0f, "0000");
            } else if (tag.getInt("Action") == 2) {
                return new Data(2, 0f, tag.getFloat("Degrees"), "0000");
            } else if (tag.getInt("Action") == 3) {
                return new Data(3, 0, 0, tag.getString("facesToDelete"));
            } else {
                return new Data(-1, 0, 0, "0000");
            }

        }
    }

    public PortalModificatorItem(Settings settings) {
        super(settings);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        Data dataItem = Data.deserialize(stack.getOrCreateTag());
        if (dataItem.type == 1) {
            tooltip.add(1, new TranslatableText("tooltip.imm_ptl_surv_adapt.portal_modificator_action_1"));
            tooltip.add(2, Text.of("Distance: " + dataItem.distance + " blocks"));
        } else if (dataItem.type == 2) {
            tooltip.add(1, new TranslatableText("tooltip.imm_ptl_surv_adapt.portal_modificator_action_2"));
            tooltip.add(2, Text.of("Angle: " + dataItem.degrees + " degrees"));
        } else if (dataItem.type == 3) {
            tooltip.add(1, new TranslatableText("tooltip.imm_ptl_surv_adapt.portal_modificator_action_3"));
            if (CHelper.safeHasShiftDown()) {
                boolean[] out = decodeFacesToDelete(dataItem.facesToDelete);
                for (int i = 0; i < 4; i++) {
                    if (out[i]) {
                        tooltip.add(i + 2,
                                new TranslatableText("tooltip.imm_ptl_surv_adapt.remove_portal_" + i,
                                        ""));
                    } else {
                        tooltip.add(i + 2,
                                new TranslatableText("tooltip.imm_ptl_surv_adapt.remove_portal_" + i,
                                        new TranslatableText("word.imm_ptl_surv_adapt.not")));
                    }
                }
            } else {
                tooltip.add(2, new TranslatableText("tooltip.imm_ptl_surv_adapt.shift_for_more_info"));
                tooltip.add(3, new TranslatableText("tooltip.imm_ptl_surv_adapt.shift_use_to_configure"));

            }
        } else {
            tooltip.add(1, new TranslatableText("tooltip.imm_ptl_surv_adapt.portal_modificator_action_0"));
        }

    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        if (this.isIn(group)) {
            ItemStack nothingStack = new ItemStack(Register.PORTAL_MODIFICATOR_ITEM);
            Data nothingStackData = new Data(0, 0, 0, "0000");
            nothingStack.setTag(nothingStackData.serialize());
            stacks.add(nothingStack);

            ItemStack moveHalfStack = new ItemStack(Register.PORTAL_MODIFICATOR_ITEM);
            Data moveHalfStackData = new Data(1, 0.5F, 0, "0000");
            moveHalfStack.setTag(moveHalfStackData.serialize());
            stacks.add(moveHalfStack);

            ItemStack moveMinusHalfStack = new ItemStack(Register.PORTAL_MODIFICATOR_ITEM);
            Data moveMinusHalfStackData = new Data(1, -.5F, 0, "0000");
            moveMinusHalfStack.setTag(moveMinusHalfStackData.serialize());
            stacks.add(moveMinusHalfStack);

            ItemStack rotate15Stack = new ItemStack(Register.PORTAL_MODIFICATOR_ITEM);
            Data rotate15StackData = new Data(2, 0, 15, "0000");
            rotate15Stack.setTag(rotate15StackData.serialize());
            stacks.add(rotate15Stack);

            ItemStack rotate90Stack = new ItemStack(Register.PORTAL_MODIFICATOR_ITEM);
            Data rotate90StackData = new Data(2, 0, 90, "0000");
            rotate90Stack.setTag(rotate90StackData.serialize());
            stacks.add(rotate90Stack);

            ItemStack deleteStack = new ItemStack(Register.PORTAL_MODIFICATOR_ITEM);
            Data deleteStackData = new Data(3, 0, 0, "1111");
            deleteStack.setTag(deleteStackData.serialize());
            stacks.add(deleteStack);
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        Data data = Data.deserialize(user.getStackInHand(hand).getOrCreateTag());
        if (!world.isClient()) {
//            System.out.println(encodeFacesToDelete(new int[]{1, 2, 2, 2}, "0101"));
            Portal portal = Utils.getPortalPlayerPointing(user, true);
            if (portal != null) {
                if (data.type == 1) {

                    Vec3d viewVector = user.getRotationVector();
                    Direction facing = Direction.getFacing(viewVector.x, viewVector.y, viewVector.z);
                    Vec3d movedist = Vec3d.of(facing.getVector()).multiply(data.distance);
                    if (portal.getDestPos().distanceTo(portal.getOriginPos().add(movedist)) > Global.currConfig.maxPortalModificatorMoveDistance) {
                        user.sendMessage(new TranslatableText("message.imm_ptl_surv_adapt.too_far"), false);
                        return TypedActionResult.fail(user.getStackInHand(hand));
                    }
                    PortalManipulation.getPortalClutter(world, portal.getDestPos(), portal.transformLocalVecNonScale(portal.getNormal()),  p -> Objects.equals(p.specificPlayerId, portal.specificPlayerId) && portal.getDiscriminator() != (p.getDiscriminator()))
                            .forEach(e -> {
                                e.setDestination(new Vec3d(e.getDestPos().getX()+movedist.x, e.getDestPos().getY()+movedist.y, e.getDestPos().getZ()+movedist.z));
                                e.reloadAndSyncToClient();
//                                System.out.println("found portal 1");
                            });
                    PortalManipulation.getPortalClutter(world, portal.getDestPos(), portal.transformLocalVecNonScale(portal.getNormal().multiply(-1)),  p -> Objects.equals(p.specificPlayerId, portal.specificPlayerId) && portal.getDiscriminator() != (p.getDiscriminator()))
                            .forEach(e -> {
                                e.setDestination(new Vec3d(e.getDestPos().getX()+movedist.x, e.getDestPos().getY()+movedist.y, e.getDestPos().getZ()+movedist.z));
                                e.reloadAndSyncToClient();
//                                System.out.println("found portal 2");
                            });
                    PortalManipulation.getPortalClutter(world, portal.getOriginPos(), portal.getNormal().multiply(-1),  p -> Objects.equals(p.specificPlayerId, portal.specificPlayerId) && portal.getDiscriminator() != (p.getDiscriminator()))
                            .forEach(e -> e.setOriginPos(new Vec3d(e.getOriginPos().getX()+movedist.x, e.getOriginPos().getY()+movedist.y, e.getOriginPos().getZ()+movedist.z)));
                    portal.setOriginPos(new Vec3d(portal.getOriginPos().getX()+movedist.x, portal.getOriginPos().getY()+movedist.y, portal.getOriginPos().getZ()+movedist.z));
//                    portal.reloadAndSyncToClient();
                    damageIt((ServerPlayerEntity) user, hand);
                } else if (data.type == 2) {
//                    AtomicInteger portalsRotated = new AtomicInteger();
                    Vec3d viewVector = user.getRotationVector();
                    Direction facing = Direction.getFacing(viewVector.x, viewVector.y, viewVector.z);
                    Quaternion rotation = new Quaternion(vec3ito3f(facing.getVector()), data.degrees, true);
                    Quaternion rotationOtherWay = new Quaternion(vec3ito3f(facing.getVector()), -data.degrees, true);
                    Quaternion rotationInverseOtherWay = new Quaternion(vec3ito3f(facing.getOpposite().getVector()), -data.degrees, true);
                    Quaternion rotationInverse = new Quaternion(vec3ito3f(facing.getOpposite().getVector()), data.degrees, true);

                    //opposite portal of the destination portal of the interacted one
                    PortalManipulation.getPortalClutter(world, portal.getDestPos(), portal.transformLocalVecNonScale(portal.getNormal()),  p -> Objects.equals(p.specificPlayerId, portal.specificPlayerId) && portal.getDiscriminator() != (p.getDiscriminator()))
                            .forEach(e -> {
                                if (facing.getOpposite() == Direction.getFacing(e.getNormal().x, e.getNormal().y, e.getNormal().z)) {
                                    if (e.rotation != null) {
                                        e.rotation.hamiltonProduct(rotationOtherWay);
                                    } else {
                                        e.rotation = rotationOtherWay;
                                    }
                                    PortalManipulation.rotatePortalBody(e, rotationOtherWay);
                                } else {
                                    if (e.rotation != null) {
                                        e.rotation.hamiltonProduct(rotationOtherWay);
                                    } else {
                                        e.rotation = rotationOtherWay;
                                    }
                                }
                                CompoundTag portalNbt1 = e.toTag(new CompoundTag());
                                portalNbt1.putBoolean("adjustPositionAfterTeleport", true);
                                e.fromTag(portalNbt1);
                                e.reloadAndSyncToClient();
                            });
                    //destination portal of the interacted one
                    PortalManipulation.getPortalClutter(world, portal.getDestPos(), portal.transformLocalVecNonScale(portal.getNormal().multiply(-1)),  p -> Objects.equals(p.specificPlayerId, portal.specificPlayerId) && portal.getDiscriminator() != (p.getDiscriminator()))
                            .forEach(e -> {
                                if (facing == Direction.getFacing(e.getNormal().x, e.getNormal().y, e.getNormal().z)) {
                                    if (e.rotation != null) {
                                        e.rotation.hamiltonProduct(rotationInverse);

                                    } else {
                                        e.rotation = rotationInverse;
                                    }
                                    PortalManipulation.rotatePortalBody(e, rotationOtherWay);
                                } else  {
                                    if (e.rotation != null) {
                                        e.rotation.hamiltonProduct(rotationInverse);

                                    } else {
                                        e.rotation = rotationInverse;
                                    }
//                                    System.out.println("was y");
                                }
                                CompoundTag portalNbt1 = e.toTag(new CompoundTag());
                                portalNbt1.putBoolean("adjustPositionAfterTeleport", true);
                                e.fromTag(portalNbt1);

                                e.reloadAndSyncToClient();

                            });
                    //opposite portal of the interacted one
                    PortalManipulation.getPortalClutter(world, portal.getOriginPos(), portal.getNormal().multiply(-1),  p -> Objects.equals(p.specificPlayerId, portal.specificPlayerId) && portal.getDiscriminator() != (p.getDiscriminator()))
                            .forEach(e -> {
                                if (e.rotation != null) {
                                    e.rotation.hamiltonProduct(rotationInverseOtherWay);

                                } else {
                                    e.rotation = rotationInverseOtherWay;
                                }
                                PortalManipulation.rotatePortalBody(e, rotationOtherWay);
                                CompoundTag portalNbt1 = e.toTag(new CompoundTag());
                                portalNbt1.putBoolean("adjustPositionAfterTeleport", true);
                                e.fromTag(portalNbt1);
                                e.reloadAndSyncToClient();

                            });
                    //interacted portal
                    if (portal.rotation != null) {
                        portal.rotation.hamiltonProduct(rotation);
                    } else {
                        portal.rotation = rotation;
                    }
                    PortalManipulation.rotatePortalBody(portal, rotationOtherWay);

                    CompoundTag portalNbt = portal.toTag(new CompoundTag());
                    portalNbt.putBoolean("adjustPositionAfterTeleport", true);
                    portal.fromTag(portalNbt);
                    portal.reloadAndSyncToClient();
                    damageIt((ServerPlayerEntity) user, hand);
                    return TypedActionResult.success(user.getStackInHand(hand));

                } else if (data.type == 3) {
                    if (!user.isSneaking()) {
                        boolean[] facesToDeleteDecoded = decodeFacesToDelete(data.facesToDelete);
                        if (facesToDeleteDecoded[3]) {
                            PortalManipulation.getPortalClutter(world, portal.getDestPos(), portal.transformLocalVecNonScale(portal.getNormal()), p -> Objects.equals(p.specificPlayerId, portal.specificPlayerId) && portal.getDiscriminator() != (p.getDiscriminator()))
                                    .forEach(Portal::remove);
                        }
                        if (facesToDeleteDecoded[2]) {
                            PortalManipulation.getPortalClutter(world, portal.getDestPos(), portal.transformLocalVecNonScale(portal.getNormal().multiply(-1)), p -> Objects.equals(p.specificPlayerId, portal.specificPlayerId) && portal.getDiscriminator() != (p.getDiscriminator()))
                                    .forEach(Portal::remove);
                        }
                        if (facesToDeleteDecoded[1]) {
                            PortalManipulation.getPortalClutter(world, portal.getOriginPos(), portal.getNormal().multiply(-1), p -> Objects.equals(p.specificPlayerId, portal.specificPlayerId) && portal.getDiscriminator() != (p.getDiscriminator()))
                                    .forEach(Portal::remove);
                        }
                        if (facesToDeleteDecoded[0]) {
                            portal.remove();
                        }
                        damageIt((ServerPlayerEntity) user, hand);
                    }
                }
                return TypedActionResult.success(user.getStackInHand(hand));
            }
        } else {
            if (data.type == 3 && user.isSneaking() && hand.equals(Hand.MAIN_HAND)) {
                CHelper.safeOpenScreenPortalModificator(user, hand);
            }
        }
        return TypedActionResult.fail(user.getStackInHand(hand));
    }

    public static boolean[] decodeFacesToDelete(String facesToDelete) {
        boolean[] out = new boolean[4];
        for (int i = 0; i < out.length; i++) {
            if (facesToDelete.charAt(i) == "1".charAt(0)) {
                out[i] = true;
            }
        }
        return out;
    }

    public static String encodeFacesToDelete(int[] array, String old) {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (array[i] != 2) {
                out.append(array[i]);
            } else {
                out.append(old.charAt(i));
            }
        }
        return out.toString();
    }

    public static Vector3f vec3ito3f(Vec3i vec) {
        Vector3f vec3f = new Vector3f();
        vec3f.set(vec.getX(), vec.getY(), vec.getZ());
        return vec3f;
    }
}
