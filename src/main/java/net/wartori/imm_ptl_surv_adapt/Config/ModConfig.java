package net.wartori.imm_ptl_surv_adapt.Config;

import net.wartori.imm_ptl_surv_adapt.Global;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import qouteall.q_misc_util.MiscHelper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Collectors;

public class ModConfig {

    public int maxPortalModificatorMoveDistance = 300;
    public int maxPortalCreatorDistance = 100;
    public int maxPortalWrappingBoxSize = 50;
    public boolean enablePortalClaiming = true;

    public static ModConfig readConfig() {
        File configFile = new File(getGameDir(), "config/imm_ptl_surv_adapt_config.json");

        if (configFile.exists()) {
            try {
                String config = Files.lines(configFile.toPath()).collect(Collectors.joining());
                ModConfig result = Global.gson.fromJson(config, ModConfig.class);
                if (result == null) {
                    return new ModConfig();
                }
                return result;
            } catch (IOException ex) {
                ex.printStackTrace();
                return new ModConfig();
            }
        } else {
            return new ModConfig();
        }
    }

    public void saveConfig() {
        File configFile = new File(getGameDir(), "config/imm_ptl_surv_adapt_config.json");
        try {
            configFile.createNewFile();
            FileWriter configWriter = new FileWriter(configFile);

            configWriter.write(Global.gson.toJson(this));
            configWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File getGameDir() {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            return MinecraftClient.getInstance().runDirectory;
        } else {
            return MiscHelper.getServer().getRunDirectory();
        }
    }
}
