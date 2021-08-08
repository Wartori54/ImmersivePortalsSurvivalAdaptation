package net.wartori.imm_ptl_surv_adapt.mixin;


import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.render.GameRenderer;
import net.wartori.imm_ptl_surv_adapt.ClientEvents;
import net.wartori.imm_ptl_surv_adapt.Guide.IPSAGuide;
import net.wartori.imm_ptl_surv_adapt.Items.PortalModificatorItem;
import net.wartori.imm_ptl_surv_adapt.Register;
import net.wartori.imm_ptl_surv_adapt.Utils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import qouteall.imm_ptl.core.portal.Portal;

import java.util.concurrent.atomic.AtomicReference;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public class MixinGameRenderer extends DrawableHelper {

    @Shadow @Final private MinecraftClient client;
    @Shadow private int scaledWidth;
    @Shadow private int scaledHeight;
    private static final Identifier sprite = new Identifier("imm_ptl_surv_adapt:textures/hud/overlay_direction.png");
    private boolean worldOpen = false;

    @Inject(method = "renderHotbar", at = @At("RETURN"))
    public void onRender(float f, MatrixStack matrixStack, CallbackInfo info) {
        if (client.player.isHolding(Register.PORTAL_MODIFICATOR_ITEM)) {
            Portal portal = Utils.getPortalPlayerPointing(client.player, true);
            if (portal != null) {
                Vec3d viewVec = client.player.getRotationVector();
                Direction facing = Direction.getFacing(viewVec.x, viewVec.y, viewVec.z);
                String actionType;
                AtomicReference<ItemStack> stack = new AtomicReference<>();
                stack.set(ItemStack.EMPTY);
                client.player.getItemsHand().iterator().forEachRemaining(itemStack -> {
                    if (itemStack.getItem() == Register.PORTAL_MODIFICATOR_ITEM) {
                        stack.set(itemStack);
                    }
                });
                if (stack.get() == ItemStack.EMPTY) {
                    return;
                }

                if (PortalModificatorItem.Data.deserialize(stack.get().getOrCreateNbt()).type == 1) {
                    actionType = "Move";
                } else if (PortalModificatorItem.Data.deserialize(stack.get().getOrCreateNbt()).type == 2) {
                    actionType = "Rotate";
                } else {
                    return;
                }
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.setShaderTexture(0, sprite);
                this.drawTexture(matrixStack, this.scaledWidth/2-66, scaledHeight/4, 0, 0, 132, 25);
//                RenderSystem.setShaderColor(1.0f,1.0f,1.0f,1.0f);
//                RenderSystem.enableBlend();
//                this.client.getTextureManager().bindTexture(sprite);
//                this.drawTexture(matrixStack, this.scaledWidth/2-66, scaledHeight/4, 0, 0, 132, 25);
//                RenderSystem.disableBlend();
                TextRenderer textRenderer = client.textRenderer;
                String axisDirection;

                if (facing.getDirection().equals(Direction.AxisDirection.POSITIVE)) {
                    axisDirection = "+";
                } else {
                    axisDirection = "-";
                }

                textRenderer.drawWithShadow(matrixStack, new TranslatableText("info.imm_ptl_surv_adapt.rotate", actionType, axisDirection, facing.getAxis().asString()), this.scaledWidth/2f-62, this.scaledHeight/4f+5, 0xFFFFFFFF);
            }
        }
    }

    @Inject(method = "render", at = @At(value = "HEAD"))
    private void beforeRendering(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (client.world != null) {
            if (!worldOpen)
                ClientEvents.onWorldOpen();
            worldOpen = true;
        } else {
            worldOpen = false;
        }
    }
}
