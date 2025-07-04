package com.fit.vehicle_management.service;

import com.fit.vehicle_management.exception.VehicleAlreadyExistsException;
import com.fit.vehicle_management.exception.VehicleNotFoundException;
import com.fit.vehicle_management.model.Vehicle;
import com.fit.vehicle_management.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;

    public Vehicle addVehicle(Vehicle vehicle) {
        try {
            return vehicleRepository.save(vehicle);
        } catch (DataIntegrityViolationException e) {
            throw new VehicleAlreadyExistsException("El vehículo con la placa '" + vehicle.getLicensePlate() + "' ya existe.");
        }
    }

    public void deleteVehicle(String licensePlate) {
        Vehicle vehicle = vehicleRepository.findByLicensePlate(licensePlate);
        if (vehicle != null) {
            vehicleRepository.delete(vehicle);
        } else {
            throw new VehicleNotFoundException("Vehículo no encontrado.");
        }
    }

    public Vehicle updateVehicle(String licensePlate, Vehicle updatedVehicle) {
        Vehicle vehicle = vehicleRepository.findByLicensePlate(licensePlate);
        if (vehicle != null) {
            vehicle.setColor(updatedVehicle.getColor());
            vehicle.setBrand(updatedVehicle.getBrand());
            vehicle.setOwnerId(updatedVehicle.getOwnerId());
            vehicle.setModel(updatedVehicle.getModel());
            return vehicleRepository.save(vehicle);
        } else {
            throw new VehicleNotFoundException("Vehículo no encontrado.");
        }
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Vehicle getVehicleByLicensePlate(String licensePlate) {
        Vehicle vehicle = vehicleRepository.findByLicensePlate(licensePlate);
        if (vehicle == null) {
            throw new VehicleNotFoundException("Vehículo no encontrado.");
        }
        return vehicle;
    }
}
