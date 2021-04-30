package net.Wartori.imm_ptl_surv_adapt;

import net.Wartori.imm_ptl_surv_adapt.Networking.AnalizeC2SPackets;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;


public class Main implements ModInitializer {

	@Override
	public void onInitialize() {
		Register.registerItems();
		Register.registerBlocks();
		Register.registerWorldGen();
		Register.registerArgumentTypes();
		Register.registerCommands();
		Register.registerEntity();
		Register.registerStructures();
		ServerPlayNetworking.registerGlobalReceiver(new Identifier("imm_ptl_surv_adapt","update_portal_modificator"), (AnalizeC2SPackets::executePacketUpdatePortalModificator));
	}
}
