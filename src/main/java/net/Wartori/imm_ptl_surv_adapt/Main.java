package net.Wartori.imm_ptl_surv_adapt;

import net.fabricmc.api.ModInitializer;

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
		Register.registerPackets();
		Register.registerStatusEffects();
	}
}
