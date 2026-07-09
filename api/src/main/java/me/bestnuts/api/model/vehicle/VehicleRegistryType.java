package me.bestnuts.api.model.vehicle;

import lombok.Getter;

@Getter
public enum VehicleRegistryType {
    CAR("car"),
    HELI("heli");

    private final String name;

    VehicleRegistryType(String name) {
        this.name = name;
    }
}