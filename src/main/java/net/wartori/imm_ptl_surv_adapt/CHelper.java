package net.wartori.imm_ptl_surv_adapt;

import net.wartori.imm_ptl_surv_adapt.Guis.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

@Environment(EnvType.CLIENT)
public class CHelper {
    public static boolean safeHasShiftDown() {
        return Screen.hasShiftDown();
    }

    public static void safeOpenScreenPortalModificator(PlayerEntity user, Hand hand) {
        MinecraftClient.getInstance().setScreen(new ConfigurePortalModificatorDeleteScreen(new ConfigurePortalModificatorDeleteGui(user.getStackInHand(hand))));
    }

    public static void safeOpenScreenPortalCreator(PlayerEntity user, Hand hand) {
        MinecraftClient.getInstance().setScreen(new ConfigurePortalCreatorOneWayScreen(new ConfigurePortalCreatorOneWayGui(user.getStackInHand(hand))));
    }

    public static void safeOpenScreenPortalCompleter(PlayerEntity user, Hand hand) {
        MinecraftClient.getInstance().setScreen(new ConfigurePortalCompleterScreen(new ConfigurePortalCompleterGui(user.getStackInHand(hand))));
    }
}
