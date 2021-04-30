package net.Wartori.imm_ptl_surv_adapt.Portals;

import com.qouteall.immersive_portals.Helper;
import com.qouteall.immersive_portals.portal.Portal;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

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
    protected void writeCustomDataToTag(CompoundTag compoundTag) {
        super.writeCustomDataToTag(compoundTag);
        Helper.putVec3d(compoundTag, "relativeDestination", relativeDestination);
    }

    @Override
    protected void readCustomDataFromTag(CompoundTag tag) {
        super.readCustomDataFromTag(tag);
        relativeDestination = Helper.getVec3d(tag, "relativeDestination");
        setRelativeDestination(relativeDestination);
    }

    @Override
    public void tick() {
        super.tick();
        this.destination = getOriginPos().add(relativeDestination);
    }
}
