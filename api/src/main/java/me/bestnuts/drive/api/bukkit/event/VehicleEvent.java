package me.bestnuts.drive.api.bukkit.event;

import lombok.RequiredArgsConstructor;
import me.bestnuts.drive.api.model.vehicle.Vehicle;
import org.bukkit.event.Event;

@RequiredArgsConstructor
public abstract class VehicleEvent extends Event {

    private final Vehicle vehicle;
}
