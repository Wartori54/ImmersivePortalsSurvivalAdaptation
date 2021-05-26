package net.Wartori.imm_ptl_surv_adapt.mixin;

import com.qouteall.immersive_portals.portal.Portal;
import net.Wartori.imm_ptl_surv_adapt.Register;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Portal.class)
public class PortalMixin {
    @Inject(method = "canTeleportEntity", at = @At(value = "HEAD"), cancellable = true, remap = false)
    public void canTeleportEntityWithEffect(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof LivingEntity) {
            if (((LivingEntity) entity).hasStatusEffect(Register.PORTAL_BY_PASS)) {
                cir.setReturnValue(false);
            }
        }
    }
}
