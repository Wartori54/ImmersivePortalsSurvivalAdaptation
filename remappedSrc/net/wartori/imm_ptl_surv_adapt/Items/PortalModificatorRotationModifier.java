package net.wartori.imm_ptl_surv_adapt.Items;

import net.wartori.imm_ptl_surv_adapt.Register;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import net.wartori.imm_ptl_surv_adapt.RegisterItemGroups;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PortalModificatorRotationModifier extends Item {
    public PortalModificatorRotationModifier(Settings settings) {
        super(settings);
    }

    public static class Data {
        public float rotation;

        public Data(float rotation) {
            this.rotation = rotation;
        }

        public NbtCompound serialize() {
            NbtCompound tag = new NbtCompound();
            tag.putFloat("Degrees", rotation);
            return tag;
        }

        public static Data deserialize(NbtCompound tag) {
            return new Data(tag.getFloat("Degrees"));
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        Data dataItem = Data.deserialize(stack.getOrCreateNbt());
        tooltip.add(1, new TranslatableText("tooltip.imm_ptl_surv_adapt.increase_rotation_modificator", dataItem.rotation));

    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        if (this.isIn(group) || RegisterItemGroups.IMMERSIVE_PORTALS_SURVIVAL_ADAPTATION_GROUP == group) {
            for (float i :
                    new float[]{5, 15, 90}) {
                ItemStack itemStack = new ItemStack(Register.PORTAL_MODIFICATOR_ROTATION_MODIFIER_ITEM);
                Data data = new Data(i);
                itemStack.setNbt(data.serialize());
                stacks.add(itemStack);
            }
        }
    }
}
