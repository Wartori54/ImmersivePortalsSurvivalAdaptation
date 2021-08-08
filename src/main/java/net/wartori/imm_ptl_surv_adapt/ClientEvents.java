package net.wartori.imm_ptl_surv_adapt;

import net.wartori.imm_ptl_surv_adapt.Util.NoArgedSignal;

public class ClientEvents {
    public static final NoArgedSignal onOpenWorld = new NoArgedSignal();

    public static void onWorldOpen() {
        onOpenWorld.run();
    }
}
