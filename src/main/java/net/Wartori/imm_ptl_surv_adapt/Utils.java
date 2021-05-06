package net.Wartori.imm_ptl_surv_adapt;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

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

}
