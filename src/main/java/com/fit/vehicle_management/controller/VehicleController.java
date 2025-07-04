package com.fit.vehicle_management.controller;

import com.fit.vehicle_management.model.Vehicle;
import com.fit.vehicle_management.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {
    @Autowired
    private VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<Vehicle> addVehicle(@RequestBody Vehicle vehicle) {
        return ResponseEntity.ok(vehicleService.addVehicle(vehicle));
    }

    @DeleteMapping("/{licensePlate}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable String licensePlate) {
        vehicleService.deleteVehicle(licensePlate);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{licensePlate}")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable String licensePlate, @RequestBody Vehicle vehicle) {
        return ResponseEntity.ok(vehicleService.updateVehicle(licensePlate, vehicle));
    }

    @GetMapping
    public ResponseEntity<?> getAllVehicles() {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();

        if (vehicles.isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "No hay vehículos registrados.");
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.ok(vehicles);
    }

    @GetMapping("/{licensePlate}")
    public ResponseEntity<Vehicle> getVehicleByLicensePlate(@PathVariable String licensePlate) {
        return ResponseEntity.ok(vehicleService.getVehicleByLicensePlate(licensePlate));
    }
}
