package net.wartori.imm_ptl_surv_adapt.ModMenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import me.shedaniel.clothconfig2.gui.entries.IntegerSliderEntry;
import net.wartori.imm_ptl_surv_adapt.Config.ModConfig;
import net.wartori.imm_ptl_surv_adapt.Global;
import net.minecraft.text.TranslatableText;

public class ModMenuEntry implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create();

            Global.currConfig = ModConfig.readConfig();

            ConfigCategory mainCategory = builder.getOrCreateCategory(
                    new TranslatableText("config.imm_ptl_surv_adapt.mainCategory")
            );

            IntegerSliderEntry maxPortalModificatorMoveDistance = builder.entryBuilder().startIntSlider(
                    new TranslatableText("config.imm_ptl_surv_adapt.maxDistancePortalModificatorMove"),
                    Global.currConfig.maxPortalModificatorMoveDistance,
                    100,
                    1200
            ).setDefaultValue(300).setTooltip(new TranslatableText("config.imm_ptl_surv_adapt.intSliderRemember")).build();

            IntegerSliderEntry maxPortalCreatorDistance = builder.entryBuilder().startIntSlider(
                    new TranslatableText("config.imm_ptl_surv_adapt.maxDistancePortalCreator"),
                    Global.currConfig.maxPortalCreatorDistance,
                    50,
                    300
            ).setDefaultValue(100).setTooltip(new TranslatableText("config.imm_ptl_surv_adapt.intSliderRemember")).build();

            IntegerSliderEntry maxPortalWrappingBoxSize = builder.entryBuilder().startIntSlider(
                    new TranslatableText("config.imm_ptl_surv_adapt.maxDistancePortalWrappingBoxSize"),
                    Global.currConfig.maxPortalWrappingBoxSize,
                    5,
                    75
            ).setDefaultValue(50).setTooltip(new TranslatableText("config.imm_ptl_surv_adapt.intSliderRemember")).build();

            BooleanListEntry enablePortalClaimer = builder.entryBuilder().startBooleanToggle(
                    new TranslatableText("config.imm_ptl_surv_adapt.enablePortalClaiming"),
                    Global.currConfig.enablePortalClaiming
            ).setDefaultValue(true).build();

            mainCategory.addEntry(maxPortalModificatorMoveDistance);
            mainCategory.addEntry(maxPortalCreatorDistance);
            mainCategory.addEntry(maxPortalWrappingBoxSize);
            mainCategory.addEntry(enablePortalClaimer);

            return builder.setParentScreen(parent)
                    .setTitle(new TranslatableText("config.imm_ptl_surv_adapt.title"))
                    .setSavingRunnable(() -> {
                        ModConfig newConfig = new ModConfig();
                        newConfig.maxPortalModificatorMoveDistance = maxPortalModificatorMoveDistance.getValue();
                        newConfig.maxPortalCreatorDistance = maxPortalCreatorDistance.getValue();
                        newConfig.maxPortalWrappingBoxSize = maxPortalWrappingBoxSize.getValue();
                        newConfig.enablePortalClaiming = enablePortalClaimer.getValue();
                        newConfig.saveConfig();
                        Global.currConfig = newConfig;
                    }).build();
        };
    }
}
