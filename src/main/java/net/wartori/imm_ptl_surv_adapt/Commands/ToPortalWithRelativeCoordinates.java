package net.wartori.imm_ptl_surv_adapt.Commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.wartori.imm_ptl_surv_adapt.Portals.PortalMirrorWithRelativeCoordinates;
import net.wartori.imm_ptl_surv_adapt.Portals.PortalWithRelativeCoordinates;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import qouteall.imm_ptl.core.commands.PortalCommand;
import qouteall.imm_ptl.core.portal.Mirror;
import qouteall.imm_ptl.core.portal.Portal;

import java.util.UUID;

public class ToPortalWithRelativeCoordinates {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> builder = CommandManager.literal("make_portal_use_relative_coordinates").requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2));
        builder.executes(ToPortalWithRelativeCoordinates::makePortalUserRelativeCoordinates);
        dispatcher.register(builder);
    }

    public static int makePortalUserRelativeCoordinates(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Portal portal = PortalCommand.getPlayerPointingPortal(context.getSource().getPlayer(), false);
        if (portal == null) {
            throw new SimpleCommandExceptionType(new LiteralText("Player not pointing to a non-global portal")).create();
        }

        if (portal instanceof Mirror) {
            PortalMirrorWithRelativeCoordinates newPortal = PortalMirrorWithRelativeCoordinates.entityType.create(context.getSource().getWorld());
            UUID newUuid = newPortal.getUuid();

            NbtCompound portalTag = new NbtCompound();
            portal.writeNbt(portalTag);
            newPortal.readNbt(portalTag);
            newPortal.setDestination(portal.getDestPos());
            portal.remove(Entity.RemovalReason.KILLED);
            newPortal.setUuid(newUuid);
            newPortal.world.spawnEntity(newPortal);
        } else {
            PortalWithRelativeCoordinates newPortal = PortalWithRelativeCoordinates.entityType.create(context.getSource().getWorld());
            UUID newUuid = newPortal.getUuid();

            NbtCompound portalTag = new NbtCompound();
            portal.writeNbt(portalTag);
            newPortal.readNbt(portalTag);
            newPortal.setDestination(portal.getDestPos());
            portal.remove(Entity.RemovalReason.KILLED);
            newPortal.setUuid(newUuid);
            newPortal.world.spawnEntity(newPortal);

        }
        return 1;


    }
}
