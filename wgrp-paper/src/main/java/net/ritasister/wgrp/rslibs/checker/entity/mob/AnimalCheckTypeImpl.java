package net.ritasister.wgrp.rslibs.checker.entity.mob;

import net.ritasister.wgrp.WorldGuardRegionProtectPaperPlugin;
import net.ritasister.wgrp.api.model.entity.EntityCheckType;
import net.ritasister.wgrp.util.config.ConfigFields;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

public final class AnimalCheckTypeImpl implements EntityCheckType<Entity, EntityType> {

    private final WorldGuardRegionProtectPaperPlugin wgrpPlugin;

    public AnimalCheckTypeImpl(final WorldGuardRegionProtectPaperPlugin wgrpPlugin) {
        this.wgrpPlugin = wgrpPlugin;
    }

    @Override
    public boolean check(final @NotNull Entity entity) {
        final Animals animals = (Animals) entity;
        final EntityType animalsType = animals.getType();
        return ConfigFields.ANIMAL_TYPE.getList(wgrpPlugin.getWgrpPaperBase()).contains(animalsType.name().toLowerCase());
    }

    @Override
    public EntityType[] getEntityType() {
        return new EntityType[] {
            //Misc
            EntityType.VILLAGER,
            //Water creature
            EntityType.AXOLOTL,
            //Land creature
            EntityType.DONKEY,
            EntityType.PARROT,
            EntityType.TRADER_LLAMA,
            EntityType.HORSE,
            EntityType.POLAR_BEAR,
            EntityType.OCELOT,
            EntityType.CAT,
            EntityType.FROG,
            EntityType.COW,
            EntityType.FOX,
            EntityType.MOOSHROOM,
            EntityType.ZOMBIE_HORSE,
            EntityType.PANDA,
            EntityType.BEE,
            EntityType.SKELETON_HORSE,
            EntityType.PIG,
            EntityType.LLAMA,
            EntityType.GOAT,
            EntityType.WOLF,
            EntityType.CHICKEN,
            EntityType.SNIFFER,
            EntityType.SHEEP,
            EntityType.RABBIT,
            EntityType.TURTLE,
            EntityType.WANDERING_TRADER,
            EntityType.MULE,
            EntityType.TADPOLE,
            EntityType.CAMEL,
            EntityType.HOGLIN,
            EntityType.ARMADILLO
        };
    }

}
