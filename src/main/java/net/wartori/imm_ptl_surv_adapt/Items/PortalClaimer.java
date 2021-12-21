package net.wartori.imm_ptl_surv_adapt.Items;

import net.minecraft.item.ItemGroup;
import net.minecraft.util.collection.DefaultedList;
import net.wartori.imm_ptl_surv_adapt.Global;
import net.wartori.imm_ptl_surv_adapt.RegisterItemGroups;
import net.wartori.imm_ptl_surv_adapt.Utils;
import net.wartori.imm_ptl_surv_adapt.miscellaneous.Quartet;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import qouteall.imm_ptl.core.portal.Portal;

import java.util.List;
import java.util.stream.Collectors;

public class PortalClaimer extends Item {
    public PortalClaimer(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient()) {
            if (!Global.currConfig.enablePortalClaiming) {
                user.sendMessage(new TranslatableText("bar.imm_ptl_surv_adapt.portal_claiming_disabled"), true);
                return TypedActionResult.fail(stack);
            }
            Portal portal = Utils.getPortalPlayerPointing(user, false);
            if (portal != null) {
                if (portal.specificPlayerId == null || portal.specificPlayerId.equals(user.getUuid())) {

                    Quartet<List<Portal>, List<Portal>, List<Portal>, List<Portal>> allPortals = Utils.getConnectedPortals(portal);


                    Quartet<List<Portal>, List<Portal>, List<Portal>, List<Portal>> portals = Quartet.of(
                            allPortals.getFirst().stream().filter(p -> user.getUuid().equals(portal.specificPlayerId) || portal.specificPlayerId == null).collect(Collectors.toList()),
                            allPortals.getSecond().stream().filter(p -> user.getUuid().equals(portal.specificPlayerId) || portal.specificPlayerId == null).collect(Collectors.toList()),
                            allPortals.getThird().stream().filter(p -> user.getUuid().equals(portal.specificPlayerId) || portal.specificPlayerId == null).collect(Collectors.toList()),
                            allPortals.getFourth().stream().filter(p -> user.getUuid().equals(portal.specificPlayerId) || portal.specificPlayerId == null).collect(Collectors.toList())
                    );

                    portals.getFirst().forEach(p -> p.specificPlayerId = user.getUuid());

                    portals.getSecond().forEach(p -> p.specificPlayerId = user.getUuid());

                    portals.getThird().forEach(p -> p.specificPlayerId = user.getUuid());

                    portals.getFourth().forEach(p -> p.specificPlayerId = user.getUuid());

                    if (!user.isCreative()) {
                        user.setStackInHand(hand, ItemStack.EMPTY);
                    }

                    return TypedActionResult.success(stack);
                }
            }
        }
        return TypedActionResult.fail(stack);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(new TranslatableText("tooltip.imm_ptl_surv_adapt.portal_claimer_desc"));
        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        if (this.isIn(group) || RegisterItemGroups.IMMERSIVE_PORTALS_SURVIVAL_ADAPTATION_GROUP == group) {
            stacks.add(new ItemStack(this));
        }
    }
}
