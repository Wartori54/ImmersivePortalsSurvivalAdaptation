package net.Wartori.imm_ptl_surv_adapt.mixin;


import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import com.qouteall.immersive_portals.commands.PortalCommand;
import com.qouteall.immersive_portals.portal.Portal;
import net.Wartori.imm_ptl_surv_adapt.Items.PortalModificatorItem;
import net.Wartori.imm_ptl_surv_adapt.Register;
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

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public class MixinGameRenderer extends DrawableHelper{

    @Shadow @Final private MinecraftClient client;
    @Shadow private int scaledWidth;
    @Shadow private int scaledHeight;
    private static final Identifier sprite = new Identifier("imm_ptl_surv_adapt:textures/hud/overlay_direction.png");

    @Inject(method = "renderHotbar", at = @At("RETURN"))
    public void onRender(float f, MatrixStack matrixStack, CallbackInfo info) {
//        System.out.println("i");
        if (MinecraftClient.getInstance().player.isHolding(Register.PORTAL_MODIFICATOR_ITEM)) {
            Portal portal = PortalCommand.getPlayerPointingPortalRaw(MinecraftClient.getInstance().player, 1, 4.5, true)
                    .map(Pair::getFirst).orElse(null);
            if (portal != null) {
                Vec3d viewVec = client.player.getRotationVector();
                Direction facing = Direction.getFacing(viewVec.x, viewVec.y, viewVec.z);
//                Tessellator tessellator = Tessellator.getInstance();
//                BufferBuilder bufferBuilder = tessellator.getBuffer();
//                bufferBuilder.begin(7, VertexFormats.POSITION_COLOR);
//                bufferBuilder.vertex(200, 100, 0.0).color(255, 255, 255, 255).next();
//                bufferBuilder.vertex(100, 100, 0.0).color(255, 255, 255, 255).next();
//                bufferBuilder.vertex(100, 200, 0.0).color(255, 255, 255, 255).next();
//                bufferBuilder.vertex(200, 200, 0.0).color(255, 255, 255, 255).next();
//                Sprite background = MinecraftClient.getInstance().getSpriteAtlas(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).apply(new Identifier("imm_ptl_surv_adapt", "textures/hud/overlay_direction.png"));
//                RenderSystem.disableLighting();
                String actionType;
                AtomicReference<ItemStack> stack = new AtomicReference<ItemStack>();
                stack.set(ItemStack.EMPTY);
//                if (client.player.getItemsHand().iterator().next() != Register.PORTAL_MODIFICATOR_ITEM.getDefaultStack()) {
//                    stack = client.player.getItemsHand().iterator().next();
//                }
                client.player.getItemsHand().iterator().forEachRemaining(itemStack -> {
                    if (itemStack.getItem() == Register.PORTAL_MODIFICATOR_ITEM) {
                        stack.set(itemStack);
                    }
                });
                if (stack.get() == ItemStack.EMPTY) {
                    return;
                }

                if (PortalModificatorItem.Data.deserialize(stack.get().getOrCreateTag()).type == 1) {
                    actionType = "Move";
                } else if (PortalModificatorItem.Data.deserialize(stack.get().getOrCreateTag()).type == 2) {
                    actionType = "Rotate";
                } else {
                    return;
                }
                RenderSystem.color4f(1.0f,1.0f,1.0f,1.0f);
                RenderSystem.enableBlend();
//                System.out.println(sprite);
                this.client.getTextureManager().bindTexture(sprite);
                this.drawTexture(matrixStack, this.scaledWidth/2-66, scaledHeight/4, 0, 0, 132, 25);
                RenderSystem.disableBlend();
                TextRenderer textRenderer = client.textRenderer;
                String axisDirection;
//                if (facing.getUnitVector().equals(Vector3f.POSITIVE_X)) {
//                    axisDirection = "+";
//                } else if (facing.getUnitVector().equals(Vector3f.NEGATIVE_X)) {
//                    axisDirection = "-";
//                } else if (facing.getUnitVector().equals(Vector3f.POSITIVE_Y)) {
//                    axisDirection = "+";
//                } else if (facing.getUnitVector().equals(Vector3f.NEGATIVE_Y)) {
//                    axisDirection = "-";
//                } else if (facing.getUnitVector().equals(Vector3f.POSITIVE_Z)) {
//                    axisDirection = "+";
//                } else if (facing.getUnitVector().equals(Vector3f.NEGATIVE_Z)) {
//                    axisDirection = "-";
//                }

                if (facing.getDirection().equals(Direction.AxisDirection.POSITIVE)) {
                    axisDirection = "+";
                } else {
                    axisDirection = "-";
                }

                textRenderer.drawWithShadow(matrixStack, new TranslatableText("info.imm_ptl_surv_adapt.rotate", actionType, axisDirection, facing.getAxis().asString()), this.scaledWidth/2-62, this.scaledHeight/4+5, 0xFFFFFFFF);
//        bufferBuilder.end();
//                tessellator.draw();
            }
        }
    }
}
