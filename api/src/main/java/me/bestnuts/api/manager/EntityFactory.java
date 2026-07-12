package me.bestnuts.api.manager;

import me.bestnuts.api.model.vehicle.data.EntityFactorySender;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.jspecify.annotations.NonNull;

public class EntityFactory implements Factory<EntityFactorySender, Entity> {

    @Override
    public Entity generate(@NonNull EntityFactorySender sender) {
        Location location = sender.location();
        ConfigurationSection section = sender.section();
        EntityType entityType = EntityType.fromName(section.getString("entity-type"));
        entityType = entityType == null ? EntityType.ITEM_DISPLAY : entityType;
        return location.getWorld().spawnEntity(location, entityType);
    }
}
