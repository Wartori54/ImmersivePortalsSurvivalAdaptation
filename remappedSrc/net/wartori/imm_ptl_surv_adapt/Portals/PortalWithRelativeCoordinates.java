package net.wartori.imm_ptl_surv_adapt.Portals;

import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import qouteall.imm_ptl.core.portal.Portal;
import qouteall.q_misc_util.Helper;

public class PortalWithRelativeCoordinates extends Portal {

    public static EntityType<PortalWithRelativeCoordinates> entityType;

    public Vec3d relativeDestination;

    public PortalWithRelativeCoordinates(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void setDestination(Vec3d destination) {
        this.destination = destination;
        this.relativeDestination = destination.subtract(getOriginPos());
        updateCache();
    }

    public void setRelativeDestination(Vec3d relativeDestination) {
        this.relativeDestination = relativeDestination;
        this.destination = getOriginPos().add(relativeDestination);
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbtCompound) {
        super.writeCustomDataToNbt(nbtCompound);
        Helper.putVec3d(nbtCompound, "relativeDestination", relativeDestination);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound tag) {
        super.readCustomDataFromNbt(tag);
        relativeDestination = Helper.getVec3d(tag, "relativeDestination");
        setRelativeDestination(relativeDestination);
    }

    @Override
    public void tick() {
        super.tick();
        this.destination = getOriginPos().add(relativeDestination);
    }
}
