package net.Wartori.imm_ptl_surv_adapt.Commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.qouteall.immersive_portals.commands.PortalCommand;
import com.qouteall.immersive_portals.portal.Mirror;
import com.qouteall.immersive_portals.portal.Portal;
import net.Wartori.imm_ptl_surv_adapt.Portals.PortalMirrorWithRelativeCoordinates;
import net.Wartori.imm_ptl_surv_adapt.Portals.PortalWithRelativeCoordinates;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;

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

            CompoundTag portalTag = new CompoundTag();
            portal.toTag(portalTag);
            newPortal.fromTag(portalTag);
            newPortal.setDestination(portal.getDestPos());
            portal.remove();
            newPortal.setUuid(newUuid);
            newPortal.world.spawnEntity(newPortal);
//            System.out.println(newPortal.getPos());
//        System.out.println(portalTag);
        } else {
            PortalWithRelativeCoordinates newPortal = PortalWithRelativeCoordinates.entityType.create(context.getSource().getWorld());
            UUID newUuid = newPortal.getUuid();

            CompoundTag portalTag = new CompoundTag();
            portal.toTag(portalTag);
            newPortal.fromTag(portalTag);
            newPortal.setDestination(portal.getDestPos());
            portal.remove();
            newPortal.setUuid(newUuid);
            newPortal.world.spawnEntity(newPortal);
//            System.out.println(newPortal.getPos());
//        System.out.println(portalTag);

        }
        return 1;


    }
}
