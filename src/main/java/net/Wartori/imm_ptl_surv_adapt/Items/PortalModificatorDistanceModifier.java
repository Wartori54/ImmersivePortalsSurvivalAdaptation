package net.Wartori.imm_ptl_surv_adapt.Items;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PortalModificatorDistanceModifier extends Item {
    public PortalModificatorDistanceModifier(Settings settings) {

        super(settings);
    }

    public static class Data {
        public float distance;

        public Data(float distance) {
            this.distance = distance;
        }

        public CompoundTag serialize() {
            CompoundTag tag = new CompoundTag();
            tag.putFloat("Distance", distance);
            return tag;
        }

        public static Data deserialize(CompoundTag tag) {
            return new Data(tag.getFloat("Distance"));
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        Data dataItem = Data.deserialize(stack.getOrCreateTag());
        tooltip.add(1, new TranslatableText("tooltip.imm_ptl_surv_adapt.increase_distance_modificator", dataItem.distance));
    }
}
