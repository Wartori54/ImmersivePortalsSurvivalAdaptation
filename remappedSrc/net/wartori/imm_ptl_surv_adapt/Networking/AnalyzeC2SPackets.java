package net.wartori.imm_ptl_surv_adapt.Networking;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;

public class AnalyzeC2SPackets {
    public static void executePacketUpdatePortalModificator(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        ItemStack stack = buf.readItemStack();
        player.setStackInHand(Hand.MAIN_HAND, stack);
    }

    public static void executePacketUpdatePortalCreator(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        ItemStack stack = buf.readItemStack();
        player.setStackInHand(Hand.MAIN_HAND, stack);
    }

    public static void executePacketUpdatePortalCompleter(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        ItemStack stack = buf.readItemStack();
        player.setStackInHand(Hand.MAIN_HAND, stack);
    }
}
