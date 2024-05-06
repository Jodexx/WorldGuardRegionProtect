package wgrp.rslibs.checker.entity.mob;

import org.bukkit.entity.Allay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import wgrp.WorldGuardRegionProtectBukkitPlugin;
import wgrp.rslibs.checker.entity.EntityCheckType;

public class AllayMobCheckTypeImpl implements EntityCheckType {

    private final WorldGuardRegionProtectBukkitPlugin worldGuardRegionProtectPlugin;

    public AllayMobCheckTypeImpl(final WorldGuardRegionProtectBukkitPlugin worldGuardRegionProtectPlugin) {
        this.worldGuardRegionProtectPlugin = worldGuardRegionProtectPlugin;
    }

    @Override
    public boolean check(final @NotNull Entity entity) {
        Allay allay = (Allay) entity;
        EntityType allayType = allay.getType();
        return worldGuardRegionProtectPlugin.getConfigLoader().getConfig().getAnimalType().contains(allayType.name().toLowerCase());
    }

    @Override
    public EntityType[] getEntityType() {
        return new EntityType[]{
                EntityType.ALLAY
        };
    }
}