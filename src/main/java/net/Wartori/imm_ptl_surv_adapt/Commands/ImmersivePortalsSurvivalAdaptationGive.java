package net.Wartori.imm_ptl_surv_adapt.Commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.Wartori.imm_ptl_surv_adapt.Commands.ArgumentTypes.DirectionArgumentType;
import net.Wartori.imm_ptl_surv_adapt.Items.PortalCreatorOneWay;
import net.Wartori.imm_ptl_surv_adapt.Items.PortalModificatorDistanceModifier;
import net.Wartori.imm_ptl_surv_adapt.Items.PortalModificatorItem;
import net.Wartori.imm_ptl_surv_adapt.Items.PortalModificatorRotationModifier;
import net.Wartori.imm_ptl_surv_adapt.Register;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

public class ImmersivePortalsSurvivalAdaptationGive {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> builder = CommandManager.literal("give_IP_survival_adaptation").requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2));
        builder.then(CommandManager.argument("targets", EntityArgumentType.players())
                .then(CommandManager.literal("portal_modificator")
                .then(CommandManager.literal("move_portal")
                        .then(CommandManager.argument("distance", DoubleArgumentType.doubleArg()).executes(context -> {
                            for (ServerPlayerEntity serverPlayerEntity : EntityArgumentType.getPlayers(context, "targets")) {
                                ItemStack itemStack = new ItemStack(Register.PORTAL_MODIFICATOR_ITEM);
                                itemStack.setTag(new PortalModificatorItem.Data(1, (float) DoubleArgumentType.getDouble(context, "distance"), 0, "0000")
                                        .serialize());
                                serverPlayerEntity.giveItemStack(itemStack);
                            }
                            return 1;
                        })))
                .then(CommandManager.literal("rotate_portal")
                    .then(CommandManager.argument("degrees", FloatArgumentType.floatArg())
                        .executes(context -> {
                            for (ServerPlayerEntity serverPlayerEntity : EntityArgumentType.getPlayers(context, "targets")) {
                                ItemStack itemStack = new ItemStack(Register.PORTAL_MODIFICATOR_ITEM);
                                itemStack.setTag(new PortalModificatorItem.Data(2, 0, FloatArgumentType.getFloat(context, "degrees"), "0000")
                                        .serialize());
                                serverPlayerEntity.giveItemStack(itemStack);
                            }
                            return 1;
                        })))
                .then(CommandManager.literal("nothing").executes(context -> {
                    for (ServerPlayerEntity serverPlayerEntity : EntityArgumentType.getPlayers(context, "targets")) {
                        ItemStack itemStack = new ItemStack(Register.PORTAL_MODIFICATOR_ITEM);
                        itemStack.setTag(new PortalModificatorItem.Data(0, 0, 0, "0000")
                                .serialize());
                        serverPlayerEntity.giveItemStack(itemStack);
                    }
                    return 1;
                }))
                .then(CommandManager.literal("delete_portals")
                                .then(CommandManager.argument("interacted portal", BoolArgumentType.bool())
                                .then(CommandManager.argument("back of interacted portal", BoolArgumentType.bool())
                                .then(CommandManager.argument("exit portal", BoolArgumentType.bool())
                                .then(CommandManager.argument("back of exit portal", BoolArgumentType.bool())
                                .executes(context -> {
                                    for (ServerPlayerEntity serverPlayerEntity : EntityArgumentType.getPlayers(context, "targets")) {
                                        ItemStack itemStack = new ItemStack(Register.PORTAL_MODIFICATOR_ITEM);

                                        String facesToDelete = (BoolArgumentType.getBool(context, "interacted portal") ? "1" : "0") +
                                                (BoolArgumentType.getBool(context, "back of interacted portal") ? "1" : "0") +
                                                (BoolArgumentType.getBool(context, "exit portal") ? "1" : "0") +
                                                (BoolArgumentType.getBool(context, "back of exit portal") ? "1" : "0");
                                        itemStack.setTag(new PortalModificatorItem.Data(3, 0, 0, facesToDelete)
                                                .serialize());
                                        serverPlayerEntity.giveItemStack(itemStack);
                                    }
                                    return 1;
                                }))))))));
        builder.then(CommandManager.argument("targets", EntityArgumentType.players())
                .then(CommandManager.literal("portal_modificator_distance_modifier")
                .then(CommandManager.argument("distance", FloatArgumentType.floatArg())
                        .executes(context -> {
                            for (ServerPlayerEntity serverPlayerEntity : EntityArgumentType.getPlayers(context, "targets")) {
                                ItemStack itemStack = new ItemStack(Register.PORTAL_MODIFICATOR_DISTANCE_MODIFIER_ITEM);
                                itemStack.setTag(new PortalModificatorDistanceModifier.Data(FloatArgumentType.getFloat(context, "distance"))
                                        .serialize());
                                serverPlayerEntity.giveItemStack(itemStack);
                            }
                            return 1;
                        }))));
        builder.then(CommandManager.argument("targets", EntityArgumentType.players())
                .then(CommandManager.literal("portal_modificator_rotation_modifier")
                .then(CommandManager.argument("degrees", FloatArgumentType.floatArg()).executes(context -> {
                    for (ServerPlayerEntity serverPlayerEntity : EntityArgumentType.getPlayers(context, "targets")) {
                        ItemStack itemStack = new ItemStack(Register.PORTAL_MODIFICATOR_ROTATION_MODIFIER_ITEM);
                        itemStack.setTag(new PortalModificatorRotationModifier.Data(FloatArgumentType.getFloat(context, "degrees"))
                                .serialize());
                        serverPlayerEntity.giveItemStack(itemStack);
                    }
                    return 1;
                }))));
        builder.then(CommandManager.argument("targets", EntityArgumentType.players())
                .then(CommandManager.literal("portal_creator_one_way")
                .then(CommandManager.literal("destination_set")
                        .then(CommandManager.argument("destination", BlockPosArgumentType.blockPos())
                                .then(CommandManager.argument("side", DirectionArgumentType.direction())
                                    .then(CommandManager.argument("bi_faced", BoolArgumentType.bool())
                                            .then(CommandManager.argument("width", FloatArgumentType.floatArg())
                                                .then(CommandManager.argument("height", FloatArgumentType.floatArg())
                                                    .executes(context -> {
                                        for (ServerPlayerEntity serverPlayerEntity : EntityArgumentType.getPlayers(context, "targets")) {
                                            ItemStack itemStack = new ItemStack(Register.PORTAL_CREATOR_ONE_WAY);
                                            itemStack.setTag(new PortalCreatorOneWay.Data(Vec3d.ofCenter(BlockPosArgumentType.getBlockPos(context, "destination")),
                                                    true,
                                                    DirectionArgumentType.getDirection(context, "side").toString(),
                                                    BoolArgumentType.getBool(context, "bi_faced"),
                                                    FloatArgumentType.getFloat(context,"width"),
                                                    FloatArgumentType.getFloat(context, "height"))
                                                    .serialize());
                                            serverPlayerEntity.giveItemStack(itemStack);
                                        }
                                        return 1;
                                    })))))))
                .then(CommandManager.literal("destination_not_set")
                        .then(CommandManager.argument("bi_faced", BoolArgumentType.bool())
                                .then(CommandManager.argument("width", FloatArgumentType.floatArg())
                                        .then(CommandManager.argument("height", FloatArgumentType.floatArg())
                                .executes(context -> {
                                    for (ServerPlayerEntity serverPlayerEntity : EntityArgumentType.getPlayers(context, "targets")) {
                                        ItemStack itemStack = new ItemStack(Register.PORTAL_CREATOR_ONE_WAY);
                                        itemStack.setTag(new PortalCreatorOneWay.Data(Vec3d.ZERO,
                                                false,
                                                "down",
                                                BoolArgumentType.getBool(context, "bi_faced"),
                                                FloatArgumentType.getFloat(context,"width"),
                                                FloatArgumentType.getFloat(context, "height"))
                                                .serialize());
                                        serverPlayerEntity.giveItemStack(itemStack);
                                    }
                                    return 1;
                                })))))));
        builder.then(CommandManager.argument("targets", EntityArgumentType.players())
                .then(CommandManager.literal("portal_modificator_delete")
                        .executes(context -> {
                            for (ServerPlayerEntity serverPlayerEntity : EntityArgumentType.getPlayers(context, "targets")) {
                                ItemStack itemStack = new ItemStack(Register.PORTAL_MODIFICATOR_DELETE_ITEM);
                                serverPlayerEntity.giveItemStack(itemStack);
                            }
                            return 1;
                        })));
        dispatcher.register(builder);

    }
}
