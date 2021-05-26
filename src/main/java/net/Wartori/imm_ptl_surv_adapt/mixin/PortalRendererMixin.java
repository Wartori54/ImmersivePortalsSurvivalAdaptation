package net.Wartori.imm_ptl_surv_adapt.mixin;

import com.qouteall.immersive_portals.CGlobal;
import com.qouteall.immersive_portals.portal.Portal;
import com.qouteall.immersive_portals.render.PortalRenderer;
import net.Wartori.imm_ptl_surv_adapt.Register;
import net.Wartori.imm_ptl_surv_adapt.status_effects.PortalByPass;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

@Mixin(PortalRenderer.class)
public class PortalRendererMixin {
    @Shadow @Final public static MinecraftClient client;

    private static boolean hasRead = false;
    private static boolean prevDoUseAdvancedFrustumCulling = CGlobal.doUseAdvancedFrustumCulling;

    @Inject(method = "shouldSkipRenderingPortal", at = @At(value = "HEAD"), cancellable = true, remap = false)
    private void shouldSkipRenderingPortalInject(Portal portal, Supplier<Frustum> frustumSupplier, CallbackInfoReturnable<Boolean> cir) {
        if (client.player.hasStatusEffect(Register.PORTAL_BY_PASS)) {
            if (!hasRead) {
                prevDoUseAdvancedFrustumCulling = CGlobal.doUseAdvancedFrustumCulling;
                hasRead = true;
            }
            CGlobal.doUseAdvancedFrustumCulling = false;
            cir.setReturnValue(true);
            cir.cancel();
        } else {
            if (hasRead) {
                CGlobal.doUseAdvancedFrustumCulling = prevDoUseAdvancedFrustumCulling;
            }
            hasRead = false;
            prevDoUseAdvancedFrustumCulling = CGlobal.doUseAdvancedFrustumCulling;
        }
    }
}
