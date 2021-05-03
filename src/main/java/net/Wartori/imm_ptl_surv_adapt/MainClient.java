package net.Wartori.imm_ptl_surv_adapt;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class MainClient implements ClientModInitializer {
    @Environment(EnvType.CLIENT)
    @Override
    public void onInitializeClient() {
        RegisterClient.registerEntityRenderer();
    }
}
