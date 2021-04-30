package net.Wartori.imm_ptl_surv_adapt.Guis;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import net.Wartori.imm_ptl_surv_adapt.Global;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class ConfigurePortalModificatorDeleteScreen extends CottonClientScreen {
    public ConfigurePortalModificatorDeleteScreen(GuiDescription description) {
        super(new TranslatableText("gui.imm_ptl_surv_adapt.configure_portal_modificator_title"), description);
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
        CompoundTag newTag = Global.portalModificatorC2S.getOrCreateTag();
        newTag.putString("facesToDelete", Global.finalState);
        Global.portalModificatorC2S.setTag(newTag);
        buf.writeItemStack(Global.portalModificatorC2S);
        System.out.println(Global.finalState);
        ClientPlayNetworking.send(new Identifier("imm_ptl_surv_adapt","update_portal_modificator"), buf);
    }
}
