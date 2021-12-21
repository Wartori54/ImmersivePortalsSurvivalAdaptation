package net.wartori.imm_ptl_surv_adapt.mixin;

import net.minecraft.world.gen.decorator.PlacementModifier;
import net.minecraft.world.gen.feature.OrePlacedFeatures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(OrePlacedFeatures.class)
public interface OrePlacedFeaturesAccessor {
    @Invoker("modifiersWithCount")
    static List<PlacementModifier> CallModifiersWithCount(int count, PlacementModifier heightModfier) {
        throw new IllegalStateException("Mixin not applied");
    }
}
