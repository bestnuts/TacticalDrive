package me.bestnuts.drive.api.model.vehicle.data;

import me.bestnuts.drive.api.model.vehicle.configuration.VehicleConfiguration;

public record GroupFactorySender(VehicleConfiguration configuration, EntityFactorySender sender) {
}
