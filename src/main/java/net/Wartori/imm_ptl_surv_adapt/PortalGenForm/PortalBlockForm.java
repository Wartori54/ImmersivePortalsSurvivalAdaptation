package net.Wartori.imm_ptl_surv_adapt.PortalGenForm;

import com.mojang.serialization.Codec;
import com.qouteall.immersive_portals.McHelper;
import com.qouteall.immersive_portals.portal.Portal;
import com.qouteall.immersive_portals.portal.PortalManipulation;
import com.qouteall.immersive_portals.portal.custom_portal_gen.PortalGenInfo;
import com.qouteall.immersive_portals.portal.custom_portal_gen.form.AbstractDiligentForm;
import com.qouteall.immersive_portals.portal.custom_portal_gen.form.PortalGenForm;
import com.qouteall.immersive_portals.portal.nether_portal.BlockPortalShape;
import net.Wartori.imm_ptl_surv_adapt.Register;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.function.Predicate;

public class PortalBlockForm extends AbstractDiligentForm {
    public PortalBlockForm() {
        super(false);
    }

    @Override
    public void generateNewFrame(ServerWorld fromWorld, BlockPortalShape fromShape, ServerWorld toWorld, BlockPortalShape toShape) {
        for (BlockPos blockPos : toShape.frameAreaWithoutCorner) {
            toWorld.setBlockState(blockPos, Register.PORTAL_BLOCK.getDefaultState());
        }
        McHelper.findEntitiesByBox(
                ServerPlayerEntity.class,
                fromWorld,
                new Box(fromShape.anchor).expand(10),
                2,
                e -> true
        ).forEach(player -> player.sendMessage(
                new LiteralText(
                        "No matchable portal block frame found, a new frame will be generated"
                ),
                false
        ));
    }

    @Override
    public Portal[] generatePortalEntitiesAndPlaceholder(PortalGenInfo info) {
        ServerWorld world = McHelper.getServerWorld(info.from);

        for (BlockPos blockPos : info.fromShape.area) {
            world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
        }
        for (BlockPos blockPos : info.fromShape.frameAreaWithoutCorner) {
            world.setBlockState(blockPos, Register.USED_PORTAL_BLOCK.getDefaultState());
        }
        for (BlockPos blockPos : info.toShape.frameAreaWithoutCorner) {
            world.setBlockState(blockPos, Register.USED_PORTAL_BLOCK.getDefaultState());
        }

        Portal portal = info.createTemplatePortal(Portal.entityType);
        Portal flipped = PortalManipulation.createFlippedPortal(portal, Portal.entityType);
        Portal reverse = PortalManipulation.createReversePortal(portal, Portal.entityType);
        Portal parallel = PortalManipulation.createReversePortal(flipped, Portal.entityType);

        Portal[] portals = {portal, flipped, reverse, parallel};

        for (Portal p : portals) {
            McHelper.spawnServerEntity(p);
        }

        return portals;
    }

    @Override
    public Predicate<BlockState> getOtherSideFramePredicate() {
        return blockState -> blockState.getBlock() == Register.PORTAL_BLOCK;
    }

    @Override
    public Predicate<BlockState> getThisSideFramePredicate() {
        return blockState -> blockState.getBlock() == Register.PORTAL_BLOCK;
    }

    @Override
    public Predicate<BlockState> getAreaPredicate() {
        return AbstractBlock.AbstractBlockState::isAir;
    }

    @Override
    public Codec<? extends PortalGenForm> getCodec() {
        throw new RuntimeException();
    }

    @Override
    public PortalGenForm getReverse() {
        return this;
    }
}
