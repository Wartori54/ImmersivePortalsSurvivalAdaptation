package net.Wartori.imm_ptl_surv_adapt;

import net.Wartori.imm_ptl_surv_adapt.Networking.AnalizeC2SPackets;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class MainServer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        ServerPlayNetworking.registerGlobalReceiver(new Identifier("imm_ptl_surv_adapt","update_portal_modificator"), (AnalizeC2SPackets::executePacketUpdatePortalModificator));
    }
}
