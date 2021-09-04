package net.wartori.imm_ptl_surv_adapt.Guis;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import net.wartori.imm_ptl_surv_adapt.Global;
import net.wartori.imm_ptl_surv_adapt.Guis.widget.WNumberField;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.TranslatableText;

public class ConfigurePortalCreatorOneWayGui extends LightweightGuiDescription {

    public ConfigurePortalCreatorOneWayGui(ItemStack stack) {
        WGridPanel root = new WGridPanel();
        root.setSize(170, 100);
        setRootPanel(root);
        NbtCompound tag = stack.getOrCreateNbt();

        Global.portalCreatorC2S = stack;

        WLabel widthLabel = new WLabel(new TranslatableText("tooltip.imm_ptl_surv_adapt.portal_creator_one_way_width"));
        root.add(widthLabel, 1, 1);

        WLabel heightLabel = new WLabel(new TranslatableText("tooltip.imm_ptl_surv_adapt.portal_creator_one_way_height"));
        root.add(heightLabel, 1, 3);

        WNumberField widthEntry = new WNumberField();
        widthEntry.setText(String.valueOf(tag.getInt("width")));
        widthEntry.setOnNumberTyped(((num, value) -> {
            if (value > Global.currConfig.maxPortalCreatorWidth) {
                return false;
            } else {
                if (value == 0) {
                    value = 1;
                }
                Global.WAndHPortalCreator = new int[]{value, Global.WAndHPortalCreator[1]};
                return true;
            }
        }));
        root.add(widthEntry, 3, 1, 2, 1);

        WNumberField heightEntry = new WNumberField();
        heightEntry.setText(String.valueOf(tag.getInt("height")));
        heightEntry.setOnNumberTyped(((num, value) -> {
            if (value > Global.currConfig.maxPortalCreatorHeight) {
                return false;
            } else {
                if (value == 0) {
                    value = 1;
                }
                Global.WAndHPortalCreator = new int[]{Global.WAndHPortalCreator[0], value};
                return true;
            }
        }));
        root.add(heightEntry, 3, 3, 2, 1);

        root.validate(this);
    }
}
