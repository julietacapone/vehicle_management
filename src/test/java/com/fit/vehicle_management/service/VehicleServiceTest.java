package com.fit.vehicle_management.service;

import com.fit.vehicle_management.exception.VehicleAlreadyExistsException;
import com.fit.vehicle_management.exception.VehicleNotFoundException;
import com.fit.vehicle_management.model.Vehicle;
import com.fit.vehicle_management.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleService vehicleService;

    @Test
    void testAddVehicleSuccessfully() {
        // Arrange
        Vehicle vehicle = new Vehicle(null, "ABC123", "Red", "Toyota", "Corolla", LocalDateTime.now(), 1L);
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);

        // Act
        Vehicle result = vehicleService.addVehicle(vehicle);

        // Assert
        assertNotNull(result);
        assertEquals("ABC123", result.getLicensePlate());
        verify(vehicleRepository).save(vehicle);
    }

    @Test
    void testAddVehicleThrowsExceptionWhenLicensePlateExists() {
        // Arrange
        Vehicle vehicle = new Vehicle(null, "XYZ789", "Blue", "Ford", "Focus", LocalDateTime.now(), 2L);
        when(vehicleRepository.save(vehicle))
                .thenThrow(new DataIntegrityViolationException("Duplicate"));

        // Act & Assert
        try {
            vehicleService.addVehicle(vehicle);
            fail("Expected VehicleAlreadyExistsException");
        } catch (VehicleAlreadyExistsException ex) {
            assertEquals("El vehículo con la placa 'XYZ789' ya existe.", ex.getMessage());
        }

        verify(vehicleRepository).save(vehicle);
    }

    @Test
    void testDeleteVehicleSuccessfully() {
        // Arrange
        String licensePlate = "ABC123";
        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate(licensePlate);
        when(vehicleRepository.findByLicensePlate(licensePlate)).thenReturn(vehicle);

        // Act
        vehicleService.deleteVehicle(licensePlate);

        // Assert
        verify(vehicleRepository).delete(vehicle);
    }

    @Test
    void testDeleteVehicleThrowsExceptionWhenNotFound() {
        // Arrange
        String licensePlate = "NOTFOUND123";
        when(vehicleRepository.findByLicensePlate(licensePlate)).thenReturn(null);

        // Act & Assert
        try {
            vehicleService.deleteVehicle(licensePlate);
            fail("Expected VehicleNotFoundException");
        } catch (VehicleNotFoundException ex) {
            assertEquals("Vehículo no encontrado.", ex.getMessage());
        }

        verify(vehicleRepository, never()).delete(any());
    }

    @Test
    void testUpdateVehicleSuccessfully() {
        // Arrange
        String licensePlate = "ABC123";
        Vehicle existing = new Vehicle(1L, licensePlate, "Red", "Toyota", "Corolla", null, 1L);
        Vehicle updated = new Vehicle(null, licensePlate, "Blue", "Honda", "Civic", null, 2L);

        when(vehicleRepository.findByLicensePlate(licensePlate)).thenReturn(existing);
        when(vehicleRepository.save(existing)).thenReturn(existing);

        // Act
        Vehicle result = vehicleService.updateVehicle(licensePlate, updated);

        // Assert
        assertNotNull(result);
        assertEquals("Blue", result.getColor());
        assertEquals("Honda", result.getBrand());
        assertEquals("Civic", result.getModel());
        assertEquals(2L, result.getOwnerId());
        verify(vehicleRepository).save(existing);
    }

    @Test
    void testUpdateVehicleThrowsExceptionWhenNotFound() {
        // Arrange
        String licensePlate = "NOTFOUND123";
        Vehicle updated = new Vehicle(null, licensePlate, "Blue", "Honda", "Civic", null, 2L);

        when(vehicleRepository.findByLicensePlate(licensePlate)).thenReturn(null);

        // Act & Assert
        try {
            vehicleService.updateVehicle(licensePlate, updated);
            fail("Expected VehicleNotFoundException");
        } catch (VehicleNotFoundException ex) {
            assertEquals("Vehículo no encontrado.", ex.getMessage());
        }

        verify(vehicleRepository, never()).save(any());
    }

    @Test
    void testGetAllVehiclesSuccessfully() {
        // Arrange
        List<Vehicle> vehicles = List.of(
                new Vehicle(1L, "AAA111", "Red", "Toyota", "Corolla", null, 1L),
                new Vehicle(2L, "BBB222", "Blue", "Ford", "Focus", null, 2L)
        );

        when(vehicleRepository.findAll()).thenReturn(vehicles);

        // Act
        List<Vehicle> result = vehicleService.getAllVehicles();

        // Assert
        assertEquals(2, result.size());
        assertEquals("AAA111", result.get(0).getLicensePlate());
        assertEquals("BBB222", result.get(1).getLicensePlate());
        verify(vehicleRepository).findAll();
    }

    @Test
    void testGetVehicleByLicensePlateSuccessfully() {
        // Arrange
        String licensePlate = "ABC123";
        Vehicle vehicle = new Vehicle(1L, licensePlate, "Red", "Toyota", "Corolla", null, 1L);

        when(vehicleRepository.findByLicensePlate(licensePlate)).thenReturn(vehicle);

        // Act
        Vehicle result = vehicleService.getVehicleByLicensePlate(licensePlate);

        // Assert
        assertNotNull(result);
        assertEquals("ABC123", result.getLicensePlate());
        verify(vehicleRepository).findByLicensePlate(licensePlate);
    }

    @Test
    void testGetVehicleByLicensePlateThrowsExceptionWhenNotFound() {
        // Arrange
        String licensePlate = "NOTFOUND123";
        when(vehicleRepository.findByLicensePlate(licensePlate)).thenReturn(null);

        // Act & Assert
        try {
            vehicleService.getVehicleByLicensePlate(licensePlate);
            fail("Expected VehicleNotFoundException");
        } catch (VehicleNotFoundException ex) {
            assertEquals("Vehículo no encontrado.", ex.getMessage());
        }

        verify(vehicleRepository).findByLicensePlate(licensePlate);
    }
}