package net.Wartori.imm_ptl_surv_adapt.Guis;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import net.Wartori.imm_ptl_surv_adapt.Global;
import net.Wartori.imm_ptl_surv_adapt.Utils;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
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
        CompoundTag tag = Global.portalCompleterC2S.getOrCreateTag();
        tag.putIntArray("portalsToComplete", Utils.boolArray2IntArray(Global.portalCompleterData));
        Global.portalCompleterC2S.setTag(tag);

        buf.writeItemStack(Global.portalCompleterC2S);
        ClientPlayNetworking.send(Utils.myId("update_portal_completer"), buf);
    }
}
