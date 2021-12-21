package net.wartori.imm_ptl_surv_adapt.Config;

import net.wartori.imm_ptl_surv_adapt.Global;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;
import java.util.stream.Collectors;

public class ModConfig {

    public int maxPortalModificatorMoveDistance = 300;
    public int maxPortalCreatorDistance = 100;
    public int maxPortalCreatorWidth = 10;
    public int maxPortalCreatorHeight = 10;
    public int maxPortalWrappingBoxSize = 50;
    public boolean enablePortalClaiming = true;

    public static ModConfig readConfig() {
        File configFile = new File(getGameDir(), "config/imm_ptl_surv_adapt_config.json");

        if (configFile.exists()) {
            try {
                String config = Files.lines(configFile.toPath()).collect(Collectors.joining());
                ModConfig result = Global.gson.fromJson(config, ModConfig.class);
                return Objects.requireNonNullElseGet(result, ModConfig::new);
            } catch (IOException ex) {
                ex.printStackTrace();
                return new ModConfig();
            }
        } else {
            ModConfig newConfig = new ModConfig();
            newConfig.saveConfig();
            return newConfig;
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
        return new File(String.valueOf(FabricLoader.getInstance().getGameDir()));
    }
}
