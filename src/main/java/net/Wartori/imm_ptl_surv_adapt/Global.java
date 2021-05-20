package net.Wartori.imm_ptl_surv_adapt;

import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Global {
    public static String finalState = "0000";
    public static int[] WAndHPortalCreator = {3, 3};
    public static ItemStack portalModificatorC2S;
    public static ItemStack portalCreatorC2S;
    public static ItemStack portalCompleterC2S;
    public static boolean[] portalCompleterData = new boolean[3];
    public static String MOD_ID = "imm_ptl_surv_adapt";

    public static final Logger LOGGER = LogManager.getLogger("IP Survival Adaptation");

    public static void log(Object o) {
        LOGGER.info(o);
    }

    public static void warn(Object o) {
        LOGGER.warn(o);
    }

    public static void error(Object o) {
        LOGGER.error(o);
    }

    public static void fatal(Object o) {
        LOGGER.fatal(o);
    }

}
