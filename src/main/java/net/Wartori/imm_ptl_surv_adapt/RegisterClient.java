package net.Wartori.imm_ptl_surv_adapt;

import com.qouteall.immersive_portals.render.PortalEntityRenderer;
import net.Wartori.imm_ptl_surv_adapt.Portals.PortalMirrorWithRelativeCoordinates;
import net.Wartori.imm_ptl_surv_adapt.Portals.PortalWithRelativeCoordinates;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;

@Environment(EnvType.CLIENT)
public class RegisterClient {

    protected static void registerEntityRenderer() {

        EntityRendererRegistry.INSTANCE.register(
                PortalWithRelativeCoordinates.entityType,
                ((manager, context) -> new PortalEntityRenderer(manager))
        );

        EntityRendererRegistry.INSTANCE.register(
                PortalMirrorWithRelativeCoordinates.entityType,
                (((manager, context) -> new PortalEntityRenderer(manager)))
        );
    }
}
