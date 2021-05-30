package net.Wartori.imm_ptl_surv_adapt;

import com.mojang.datafixers.util.Pair;
import com.qouteall.immersive_portals.commands.PortalCommand;
import com.qouteall.immersive_portals.portal.Portal;
import com.qouteall.immersive_portals.portal.PortalManipulation;
import net.Wartori.imm_ptl_surv_adapt.miscellaneous.Quartet;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Random;

public class Utils {

    public static int[] boolArray2IntArray(boolean[] boolArray) {
        int[] intArray = new int[boolArray.length];

        for (int i = 0; i < boolArray.length; i++) {
            intArray[i] = boolArray[i] ? 1 : 0;
        }
        return intArray;
    }

    public static boolean[] intArray2boolArray(int[] intArray) {
        boolean[] boolArray = new boolean[intArray.length];

        for (int i = 0; i < intArray.length; i++) {
            boolArray[i] = intArray[i] != 0;
        }
        return boolArray;
    }

    public static void damageIt(ServerPlayerEntity user, Hand hand) {
        if (user.isCreative()) {
            return;
        }
        ItemStack stack = user.getStackInHand(hand);
        CompoundTag tag = stack.getOrCreateTag();
        if (stack.damage(1, new Random(), user)) {
            stack.setCount(0);
//            System.out.println("remove");
        } else {
            stack.setTag(tag);
        }
    }

    public static Identifier myId(String name) {
        return new Identifier(Global.MOD_ID, name);
    }

    public static Portal getPortalPlayerPointing(PlayerEntity player, boolean includeGlobalPortal) {
        float reach = 3f;
        if (player.isCreative()) {
            reach = 4.5f;
        }
        return PortalCommand.getPlayerPointingPortalRaw(player, 1f, reach, includeGlobalPortal)
                .map(Pair::getFirst)
                .filter(p -> p.specificPlayerId == null || player.getUuid().equals(p.specificPlayerId))
                .orElse(null);
    }

    public static Quartet<List<Portal>, List<Portal>, List<Portal>, List<Portal>> getConnectedPortals(Portal portal) {
        List<Portal> portals1 = PortalManipulation.getPortalClutter(portal.world, portal.getOriginPos(), portal.getNormal(), p -> true);
        List<Portal> portals2 = PortalManipulation.getPortalClutter(portal.world, portal.getOriginPos(), portal.getNormal().multiply(-1), p -> true);
        List<Portal> portals3 = PortalManipulation.getPortalClutter(portal.world, portal.getDestPos(), portal.transformLocalVecNonScale(portal.getNormal()), p -> true);
        List<Portal> portals4 = PortalManipulation.getPortalClutter(portal.world, portal.getDestPos(), portal.transformLocalVecNonScale(portal.getNormal().multiply(-1)), p -> true);
        return Quartet.of(portals1, portals2, portals3, portals4);
    }

}
