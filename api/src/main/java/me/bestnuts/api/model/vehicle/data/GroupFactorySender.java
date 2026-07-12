package me.bestnuts.api.model.vehicle.data;

import me.bestnuts.api.model.vehicle.configuration.VehicleConfiguration;

public record GroupFactorySender(VehicleConfiguration configuration, EntityFactorySender sender) {
}
