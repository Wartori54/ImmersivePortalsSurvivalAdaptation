package net.wartori.imm_ptl_surv_adapt.Items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.wartori.imm_ptl_surv_adapt.RegisterItemGroups;

public class PortalIngot extends Item {
    public PortalIngot(Settings settings) {
        super(settings);
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        if (this.isIn(group) || RegisterItemGroups.IMMERSIVE_PORTALS_SURVIVAL_ADAPTATION_GROUP == group) {
            stacks.add(new ItemStack(this));
        }
    }
}
