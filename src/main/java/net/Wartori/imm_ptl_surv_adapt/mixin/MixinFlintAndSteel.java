package net.Wartori.imm_ptl_surv_adapt.mixin;

import net.Wartori.imm_ptl_surv_adapt.Register;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FlintAndSteelItem.class)
public class MixinFlintAndSteel {

    @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
    private void onUseFlintAndSteel(
            ItemUsageContext context,
            CallbackInfoReturnable<ActionResult> cir
    ) {
        WorldAccess world = context.getWorld();
        if (!world.isClient()) {
            BlockPos targetPos = context.getBlockPos();
            Direction side = context.getSide();
            BlockPos firePos = targetPos.offset(side);
            BlockState targetBlockState = world.getBlockState(targetPos);
            Block targetBlock = targetBlockState.getBlock();
            if (targetBlock == Register.PORTAL_BLOCK) {
                boolean result = Register.portalBlock.perform(
                        ((ServerWorld) world),
                        firePos,
                        null
                );
//                System.out.println(result);
            }
        }
    }
}
