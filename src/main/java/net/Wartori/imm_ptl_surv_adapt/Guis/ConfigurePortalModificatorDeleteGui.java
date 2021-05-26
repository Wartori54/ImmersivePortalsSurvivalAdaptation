package net.Wartori.imm_ptl_surv_adapt.Guis;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WToggleButton;
import net.Wartori.imm_ptl_surv_adapt.Global;
import net.Wartori.imm_ptl_surv_adapt.Items.PortalModificatorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;

public class ConfigurePortalModificatorDeleteGui extends LightweightGuiDescription {
    public ConfigurePortalModificatorDeleteGui(ItemStack stack) {
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize((int) (256*1.25), 100);
        boolean[] facesToDelete = PortalModificatorItem.decodeFacesToDelete(stack.getOrCreateTag().getString("facesToDelete"));
        Global.finalState = stack.getOrCreateTag().getString("facesToDelete");
        Global.portalModificatorC2S = stack;

        WToggleButton intPortal = new WToggleButton(new TranslatableText("tooltip.imm_ptl_surv_adapt.remove_portal_0", ""));
        intPortal.setToggle(facesToDelete[0]);
        intPortal.setOnToggle(on -> {
            int onAsInt = on ? 1 : 0;
            int[] newState = {onAsInt, 2, 2 , 2};
            Global.finalState = PortalModificatorItem.encodeFacesToDelete(newState, Global.finalState);
        });
        root.add(intPortal, 0, 1);

        WToggleButton intBackPortal = new WToggleButton(new TranslatableText("tooltip.imm_ptl_surv_adapt.remove_portal_1", ""));
        intBackPortal.setToggle(facesToDelete[1]);
        intBackPortal.setOnToggle(on -> {
            int onAsInt = on ? 1 : 0;
            int[] newState = {2, onAsInt, 2, 2};
            Global.finalState = PortalModificatorItem.encodeFacesToDelete(newState, Global.finalState);
        });
        root.add(intBackPortal, 0, 2);

        WToggleButton intOutPortal = new WToggleButton(new TranslatableText("tooltip.imm_ptl_surv_adapt.remove_portal_3", ""));
        intOutPortal.setToggle(facesToDelete[3]);
        intOutPortal.setOnToggle(on -> {
            int onAsInt = on ? 1 : 0;
            int[] newState = {2, 2, 2, onAsInt};
            Global.finalState = PortalModificatorItem.encodeFacesToDelete(newState, Global.finalState);
        });
        root.add(intOutPortal, 0, 4);

        WToggleButton intBackOutPortal = new WToggleButton(new TranslatableText("tooltip.imm_ptl_surv_adapt.remove_portal_2", ""));
        intBackOutPortal.setToggle(facesToDelete[2]);
        intBackOutPortal.setOnToggle(on -> {
            int onAsInt = on ? 1 : 0;
            int[] newState = {2, 2, onAsInt, 2};
            Global.finalState = PortalModificatorItem.encodeFacesToDelete(newState, Global.finalState);
        });
        root.add(intBackOutPortal, 0, 3);
        root.validate(this);
    }


}
