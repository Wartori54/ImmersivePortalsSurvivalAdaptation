package net.Wartori.imm_ptl_surv_adapt;

import net.Wartori.imm_ptl_surv_adapt.Guis.ConfigurePortalCreatorOneWayGui;
import net.Wartori.imm_ptl_surv_adapt.Guis.ConfigurePortalCreatorOneWayScreen;
import net.Wartori.imm_ptl_surv_adapt.Guis.ConfigurePortalModificatorDeleteGui;
import net.Wartori.imm_ptl_surv_adapt.Guis.ConfigurePortalModificatorDeleteScreen;
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
        MinecraftClient.getInstance().openScreen(new ConfigurePortalModificatorDeleteScreen(new ConfigurePortalModificatorDeleteGui(user.getStackInHand(hand))));
    }

    public static void safeOpenScreenPortalCreator(PlayerEntity user, Hand hand) {
        MinecraftClient.getInstance().openScreen(new ConfigurePortalCreatorOneWayScreen(new ConfigurePortalCreatorOneWayGui(user.getStackInHand(hand))));
    }
}
