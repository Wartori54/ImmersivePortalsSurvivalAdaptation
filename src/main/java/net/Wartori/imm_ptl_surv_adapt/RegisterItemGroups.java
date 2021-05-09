package net.Wartori.imm_ptl_surv_adapt;

import net.Wartori.imm_ptl_surv_adapt.Items.PortalModificatorItem;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class RegisterItemGroups {
    public static final ItemGroup IMMERSIVE_PORTALS_SURVIVAL_ADAPTATION_GROUP = FabricItemGroupBuilder.build(
            Utils.myId("immersive_portals_survival_adaptation_group"),
            () -> new ItemStack(Register.PORTAL_MODIFICATOR_ITEM)
    );
}
