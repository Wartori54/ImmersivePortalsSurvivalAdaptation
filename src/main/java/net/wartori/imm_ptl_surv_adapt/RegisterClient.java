package net.wartori.imm_ptl_surv_adapt;

import net.wartori.imm_ptl_surv_adapt.Portals.PortalMirrorWithRelativeCoordinates;
import net.wartori.imm_ptl_surv_adapt.Portals.PortalWithRelativeCoordinates;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.EntityRendererFactory;
import qouteall.imm_ptl.core.render.PortalEntityRenderer;

@Environment(EnvType.CLIENT)
public class RegisterClient {

    protected static void registerEntityRenderer() {

        EntityRendererRegistry.INSTANCE.register(
                PortalWithRelativeCoordinates.entityType,
                (EntityRendererFactory) PortalEntityRenderer::new
        );

        EntityRendererRegistry.INSTANCE.register(
                PortalMirrorWithRelativeCoordinates.entityType,
                (EntityRendererFactory) PortalEntityRenderer::new
        );
    }
}
