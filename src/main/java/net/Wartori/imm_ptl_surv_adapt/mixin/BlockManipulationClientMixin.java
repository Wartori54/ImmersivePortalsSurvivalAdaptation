package net.Wartori.imm_ptl_surv_adapt.mixin;

import com.qouteall.immersive_portals.block_manipulation.BlockManipulationClient;
import net.Wartori.imm_ptl_surv_adapt.Register;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockManipulationClient.class)
public abstract class BlockManipulationClientMixin {
    @Shadow @Final private static MinecraftClient client;

    @Inject(method = "updatePointedBlock", at = @At(value = "INVOKE"), remap = false, cancellable = true)
    private static void isInteractableMixin(float tickDelta, CallbackInfo ci) {
        if (client.player.hasStatusEffect(Register.PORTAL_BY_PASS)) {
            ci.cancel();
        }
    }
}
