package net.wartori.imm_ptl_surv_adapt.Guis;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import net.wartori.imm_ptl_surv_adapt.Global;
import net.wartori.imm_ptl_surv_adapt.Utils;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.TranslatableText;
import org.lwjgl.glfw.GLFW;

public class ConfigurePortalCompleterScreen extends CottonClientScreen {
    public ConfigurePortalCompleterScreen(GuiDescription description) {
        super(new TranslatableText("gui.imm_ptl_surv_adapt.configure_portal_completer_title"), description);
    }

    @Override
    public boolean keyPressed(int ch, int keyCode, int modifiers) {
        //System.out.println("Key " + Integer.toHexString(ch)+" "+Integer.toHexString(keyCode));
        if (ch == GLFW.GLFW_KEY_ESCAPE) {
            this.client.player.closeHandledScreen();
            onScreenClosed();
            return true;
        } else if (ch == GLFW.GLFW_KEY_TAB) {
            changeFocus(!hasShiftDown());
            return true;
        } else {
            //if (super.keyPressed(ch, keyCode, modifiers)) return true;
            if (description.getFocus() == null) {
                if (client.options.keyInventory.matchesKey(ch, keyCode)) {
                    this.client.player.closeHandledScreen();
                    onScreenClosed();
                    return true;
                }
                return false;
            } else {
                description.getFocus().onKeyPressed(ch, keyCode, modifiers);
                return true;
            }
        }
    }

    public void onScreenClosed() {
        PacketByteBuf buf = PacketByteBufs.create();
//        buf.writeString(Global.finalState);
        NbtCompound tag = Global.portalCompleterC2S.getOrCreateNbt();
        tag.putIntArray("portalsToComplete", Utils.boolArray2IntArray(Global.portalCompleterData));
        Global.portalCompleterC2S.setNbt(tag);

        buf.writeItemStack(Global.portalCompleterC2S);
        ClientPlayNetworking.send(Utils.myId("update_portal_completer"), buf);
    }
}
