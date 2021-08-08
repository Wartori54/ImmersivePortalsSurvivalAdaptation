package net.wartori.imm_ptl_surv_adapt.mixin;

import it.unimi.dsi.fastutil.objects.ObjectList;
import net.wartori.imm_ptl_surv_adapt.Register;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import qouteall.imm_ptl.core.portal.PortalLike;
import qouteall.imm_ptl.core.render.MyGameRenderer;

@Mixin(MyGameRenderer.class)
public class MyGameRendererMixin {
    @Shadow public static MinecraftClient client;

    @Inject(method = "cullRenderingSections", at = @At(value = "HEAD"), remap = false, cancellable = true)
    private static void cullRenderingSections(
            ObjectList<?> visibleChunks, PortalLike renderingPortal, CallbackInfo ci
    ) {
        if (client.player != null && client.player.hasStatusEffect(Register.PORTAL_BY_PASS)) {
            ci.cancel();
        }
    }
}
