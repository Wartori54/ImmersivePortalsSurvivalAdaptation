package net.wartori.imm_ptl_surv_adapt.mixin;

import net.wartori.imm_ptl_surv_adapt.Register;
import net.wartori.imm_ptl_surv_adapt.status_effects.PortalByPass;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import qouteall.imm_ptl.core.IPCGlobal;
import qouteall.imm_ptl.core.portal.Portal;
import qouteall.imm_ptl.core.render.PortalRenderer;

import java.util.function.Supplier;

@Mixin(PortalRenderer.class)
public class PortalRendererMixin {
    @Shadow @Final public static MinecraftClient client;

    private static boolean hasRead = false;
    private static boolean prevDoUseAdvancedFrustumCulling = IPCGlobal.doUseAdvancedFrustumCulling;

    @Inject(method = "shouldSkipRenderingPortal", at = @At(value = "HEAD"), cancellable = true, remap = false)
    private void shouldSkipRenderingPortalInject(Portal portal, Supplier<Frustum> frustumSupplier, CallbackInfoReturnable<Boolean> cir) {
        if (client.player.hasStatusEffect(Register.PORTAL_BY_PASS)) {
            if (!hasRead) {
                prevDoUseAdvancedFrustumCulling = IPCGlobal.doUseAdvancedFrustumCulling;
                hasRead = true;
            }
            IPCGlobal.doUseAdvancedFrustumCulling = false;
            cir.setReturnValue(true);
            cir.cancel();
        } else {
            if (hasRead) {
                IPCGlobal.doUseAdvancedFrustumCulling = prevDoUseAdvancedFrustumCulling;
            }
            hasRead = false;
            prevDoUseAdvancedFrustumCulling = IPCGlobal.doUseAdvancedFrustumCulling;
        }
    }
}
