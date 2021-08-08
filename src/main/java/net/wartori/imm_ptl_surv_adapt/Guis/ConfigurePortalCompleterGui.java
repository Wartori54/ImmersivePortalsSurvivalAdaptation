package net.wartori.imm_ptl_surv_adapt.Guis;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WToggleButton;
import net.wartori.imm_ptl_surv_adapt.Global;
import net.wartori.imm_ptl_surv_adapt.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.TranslatableText;

public class ConfigurePortalCompleterGui extends LightweightGuiDescription {
    public ConfigurePortalCompleterGui(ItemStack itemStack) {
        WGridPanel root =  new WGridPanel();
        root.setSize(170, 100);
        setRootPanel(root);
        Global.portalCompleterC2S = itemStack;
        NbtCompound tag = itemStack.getOrCreateNbt();
        boolean[] state = Utils.intArray2boolArray(tag.getIntArray("portalsToComplete"));
        if (state.length != 3) {
            state = new boolean[]{false, false, false};
        }

        WToggleButton backPortal = new WToggleButton();
        backPortal.setLabel(new TranslatableText("gui.imm_ptl_surv_adapt.back_portal"));
        backPortal.setToggle(state[0]);
        backPortal.setOnToggle(on -> Global.portalCompleterData[0] = on);
        root.add(backPortal, 1, 1);

        WToggleButton backExitPortal = new WToggleButton();
        backExitPortal.setLabel(new TranslatableText("gui.imm_ptl_surv_adapt.back_exit_portal"));
        backExitPortal.setToggle(state[1]);
        backExitPortal.setOnToggle(on -> Global.portalCompleterData[1] = on);

        root.add(backExitPortal, 1, 2);

        WToggleButton exitPortal = new WToggleButton();
        exitPortal.setLabel(new TranslatableText("gui.imm_ptl_surv_adapt.exit_portal"));
        exitPortal.setToggle(state[2]);
        exitPortal.setOnToggle(on -> Global.portalCompleterData[2] = on);
        root.add(exitPortal, 1, 3);

        root.validate(this);
    }
}
