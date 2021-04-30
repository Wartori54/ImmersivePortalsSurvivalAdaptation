package net.Wartori.imm_ptl_surv_adapt;

import net.minecraft.item.ItemStack;
import java.util.logging.LogManager;
import java.util.logging.Logger;


public class Global {
    public static String finalState = "0000";
    public static ItemStack portalModificatorC2S;
    public static final Logger LOGGER = LogManager.getLogManager().getLogger("imm_ptl_surv_adapt");

    public static void log(String log){
        LOGGER.info(log);
    }
}
