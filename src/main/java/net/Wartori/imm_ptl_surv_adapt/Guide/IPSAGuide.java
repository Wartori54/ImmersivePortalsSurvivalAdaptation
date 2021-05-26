package net.Wartori.imm_ptl_surv_adapt.Guide;

import com.qouteall.immersive_portals.McHelper;
import net.Wartori.imm_ptl_surv_adapt.ClientEvents;
import net.Wartori.imm_ptl_surv_adapt.Global;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@Environment(EnvType.CLIENT)
public class IPSAGuide {
    public static class GuideState {
        public boolean wikiInformed = false;
        public GuideState() {}
        public GuideState(boolean wikiInformed) {
            this.wikiInformed = wikiInformed;
        }
    }

    private static File getFile() {
        return new File(MinecraftClient.getInstance().runDirectory, "imm_ptl_surv_adapt_guide.json");
    }

    private static void writeToFile(GuideState guideState) {
        try (FileWriter fileWriter = new FileWriter(getFile())) {
            Global.gson.toJson(guideState, fileWriter);
        } catch (IOException e) {
            Global.error(e);
        }
    }

    private static GuideState readFromFile() {
        File storageFile = getFile();
        if (storageFile.exists()) {
            GuideState result;
            try (FileReader fileReader = new FileReader(storageFile)) {
                result = Global.gson.fromJson(fileReader, GuideState.class);
            } catch (IOException e) {
                Global.error(e);
                return new GuideState();
            }

            if (result == null) {
                return new GuideState();
            }
            return result;
        }
        return new GuideState();
    }

    public static GuideState guideState = new GuideState();

    public static void initClient() {
        guideState = readFromFile();

        if (!guideState.wikiInformed) {
            ClientEvents.onOpenWorld.connect(() -> {
                MinecraftClient.getInstance().inGameHud.addChatMessage(
                        MessageType.SYSTEM,
                        new TranslatableText("message.imm_ptl_surv_adapt.warn_how_to_use")
                                .append(Text.of(" "))
                                .append(
                                        McHelper.getLinkText("https://github.com/Wartori54/ImmersivePortalsSurvivalAdaptation/wiki")),
                        Util.NIL_UUID
                );
            });
            guideState.wikiInformed = true;
            writeToFile(guideState);
        }

    }
}
