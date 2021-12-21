package net.wartori.imm_ptl_surv_adapt.Commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.wartori.imm_ptl_surv_adapt.Portals.PortalWithRelativeCoordinates;
import net.minecraft.command.argument.DimensionArgumentType;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Pair;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import qouteall.imm_ptl.core.IPMcHelper;
import qouteall.imm_ptl.core.portal.Portal;
import qouteall.q_misc_util.Helper;

import java.util.List;


public class CreatePortalWithRelativeDestination {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> builder = CommandManager.literal("create_portal_with_relative_destination")
                .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2));
        builder.then(CommandManager.argument("width", DoubleArgumentType.doubleArg())
                .then(CommandManager.argument("height", DoubleArgumentType.doubleArg())
                    .then(CommandManager.argument("dimension_to", DimensionArgumentType.dimension())
                        .then(CommandManager.argument("destination", Vec3ArgumentType.vec3())
                            .executes(CreatePortalWithRelativeDestination::placePortal)))));
        dispatcher.register(builder);
    }

    public static int placePortal(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        try {
//            BlockPos origin = BlockPosArgumentType.getBlockPos(context, "origin");
            double width = DoubleArgumentType.getDouble(context, "width");
            double height = DoubleArgumentType.getDouble(context, "height");
            ServerWorld dimensionTo = DimensionArgumentType.getDimensionArgument(context, "dimension_to");
            Vec3d destination = Vec3ArgumentType.getVec3(context, "destination");

            PortalWithRelativeCoordinates portal = placeFromUser(width, height, context.getSource().getPlayer());
            if (portal == null) {
                System.out.println("Portal was null");
                return 0;
            }

            portal.setDestinationDimension(dimensionTo.getRegistryKey());
            portal.setDestination(destination);

            portal.world.spawnEntity(portal);
            return 1;
        } catch (Exception ex) {
//            System.out.println(ex.getMessage());
            throw new SimpleCommandExceptionType(new LiteralText(ex.getMessage())).create();
        }
    }

    /*
    Copy of method PortalManipulation.placePortal.
    Adapted it to create a PortalWithRelativeCoordinates.
     */
    public static PortalWithRelativeCoordinates placeFromUser(double width, double height, PlayerEntity entity) {
        Vec3d playerLook = entity.getRotationVector();

        Pair<BlockHitResult, List<Portal>> rayTrace =
                IPMcHelper.rayTrace(
                        entity.world,
                        new RaycastContext(
                                entity.getCameraPosVec(1.0f),
                                entity.getCameraPosVec(1.0f).add(playerLook.multiply(100.0)),
                                RaycastContext.ShapeType.OUTLINE,
                                RaycastContext.FluidHandling.NONE,
                                entity
                        ),
                        true
                );

        BlockHitResult hitResult = rayTrace.getLeft();
        List<Portal> hitPortals = rayTrace.getRight();

        if (IPMcHelper.hitResultIsMissedOrNull(hitResult)) {
            return null;
        }

        for (Portal hitPortal : hitPortals) {
            playerLook = hitPortal.transformLocalVecNonScale(playerLook);
        }

        Direction lookingDirection = Helper.getFacingExcludingAxis(
                playerLook,
                hitResult.getSide().getAxis()
        );

        // this should never happen...
        if (lookingDirection == null) {
            return null;
        }

        Vec3d axisH = Vec3d.of(hitResult.getSide().getVector());
        Vec3d axisW = axisH.crossProduct(Vec3d.of(lookingDirection.getOpposite().getVector()));
        Vec3d pos = Vec3d.ofCenter(hitResult.getBlockPos())
                .add(axisH.multiply(0.5 + height / 2));

        World world = hitPortals.isEmpty()
                ? entity.world
                : hitPortals.get(hitPortals.size() - 1).getDestinationWorld();

        PortalWithRelativeCoordinates portal = PortalWithRelativeCoordinates.entityType.create(world);

        if (portal == null) {
            return null;
        }

        portal.setPos(pos.x, pos.y, pos.z);

        portal.axisW = axisW;
        portal.axisH = axisH;

        portal.width = width;
        portal.height = height;

        return portal;
    }
}
