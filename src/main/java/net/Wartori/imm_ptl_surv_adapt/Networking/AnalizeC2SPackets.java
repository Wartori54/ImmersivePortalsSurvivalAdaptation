package net.Wartori.imm_ptl_surv_adapt.Networking;

import net.Wartori.imm_ptl_surv_adapt.Global;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class AnalizeC2SPackets {
    public static void executePacketUpdatePortalModificator(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        ItemStack stack = buf.readItemStack();
//        Global.log(stack.getOrCreateTag());
//        System.out.println(stack.getTag().getString("facesToDelete"));
        player.setStackInHand(player.getActiveHand(), stack);
    }

    public static void executePacketUpdatePortalCreator(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        ItemStack stack = buf.readItemStack();
        player.setStackInHand(player.getActiveHand(), stack);
    }

    public static void executePacketUpdatePortalCompleter(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        ItemStack stack = buf.readItemStack();
        player.setStackInHand(player.getActiveHand(), stack);
    }
}