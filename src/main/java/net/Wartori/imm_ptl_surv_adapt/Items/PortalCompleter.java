package net.Wartori.imm_ptl_surv_adapt.Items;

import com.qouteall.immersive_portals.commands.PortalCommand;
import com.qouteall.immersive_portals.portal.Portal;
import com.qouteall.immersive_portals.portal.PortalManipulation;
import net.Wartori.imm_ptl_surv_adapt.CHelper;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.Wartori.imm_ptl_surv_adapt.Utils;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PortalCompleter extends Item {
    public PortalCompleter(Settings settings) {
        super(settings);
    }

    public static class Data {

        public static boolean[] portalsToComplete = {false, false, false};

        public static CompoundTag serialize() {
            CompoundTag tag = new CompoundTag();
            tag.putIntArray("portalsToComplete", Utils.boolArray2IntArray(portalsToComplete));
            return tag;
        }

        public static CompoundTag serialize(boolean[] boolArray) {
            CompoundTag tag = new CompoundTag();
            tag.putIntArray("portalsToComplete", Utils.boolArray2IntArray(boolArray));
            return tag;
        }

        public static void deserialize(CompoundTag tag) {
            portalsToComplete = Utils.intArray2boolArray(tag.getIntArray("portalsToComplete"));
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient()) {
            Portal portal = PortalCommand.getPlayerPointingPortalRaw(user, 1, 4.5, false).map(Pair::getFirst).orElse(null);
            if (portal != null) {
                Data.deserialize(user.getStackInHand(hand).getOrCreateTag());
                if (Data.portalsToComplete[0] && PortalManipulation.getPortalClutter(world,
                        portal.getOriginPos(),
                        portal.getNormal().multiply(-1),
                        p -> Objects.equals(p.specificPlayerId, portal.specificPlayerId)
                                && portal.getDiscriminator() != (p.getDiscriminator())).isEmpty()) {

                    Portal portalBack = PortalManipulation.createFlippedPortal(portal, portal.entityType);
                    portalBack.world.spawnEntity(portalBack);
                    Utils.damageIt((ServerPlayerEntity) user, hand);
                }
                if (Data.portalsToComplete[1] && PortalManipulation.getPortalClutter(world,
                        portal.getDestPos(),
                        portal.transformLocalVecNonScale(portal.getNormal()),
                        p -> Objects.equals(p.specificPlayerId, portal.specificPlayerId)
                                && portal.getDiscriminator() != (p.getDiscriminator())).isEmpty()) {
                    Portal portalExit = PortalManipulation.createReversePortal(portal, portal.entityType);
                    Portal portalExitBack = PortalManipulation.createFlippedPortal(portalExit, portal.entityType);
                    portalExitBack.world.spawnEntity(portalExitBack);
                    Utils.damageIt((ServerPlayerEntity) user, hand);
                }
                if (Data.portalsToComplete[2] && PortalManipulation.getPortalClutter(world,
                        portal.getDestPos(),
                        portal.transformLocalVecNonScale(portal.getNormal().multiply(-1)),
                        p -> Objects.equals(p.specificPlayerId, portal.specificPlayerId)
                                && portal.getDiscriminator() != (p.getDiscriminator())).isEmpty()) {
                    Portal portalExit = PortalManipulation.createReversePortal(portal, portal.entityType);
                    portalExit.world.spawnEntity(portalExit);
                    Utils.damageIt((ServerPlayerEntity) user, hand);
                }

            }
        } else {
            Portal portal = PortalCommand.getPlayerPointingPortalRaw(user, 1, 4.5, false).map(Pair::getFirst).orElse(null);
            if (portal == null && user.isSneaking() && hand.equals(Hand.MAIN_HAND)) {
                CHelper.safeOpenScreenPortalCompleter(user, hand);
            }
        }
        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(1, new TranslatableText("tooltip.imm_ptl_surv_adapt.portal_completer_desc"));
        tooltip.add(2, new TranslatableText("tooltip.imm_ptl_surv_adapt.shift_use_to_configure"));
        super.appendTooltip(stack, world, tooltip, context);
    }
}
