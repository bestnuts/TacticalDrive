package me.bestnuts.api.bukkit.event;

import lombok.RequiredArgsConstructor;
import me.bestnuts.api.model.vehicle.Vehicle;
import org.bukkit.event.Event;

@RequiredArgsConstructor
public abstract class VehicleEvent extends Event {

    private final Vehicle vehicle;
}
