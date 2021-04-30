package net.Wartori.imm_ptl_surv_adapt;

import net.fabricmc.api.ClientModInitializer;

public class MainClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Register.registerEntityRenderer();
    }
}
