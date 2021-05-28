package net.Wartori.imm_ptl_surv_adapt;


import net.fabricmc.api.DedicatedServerModInitializer;

public class MainServer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        Global.log("Dedicated Server init");
        Register.registerPackets();
    }
}
