package lk.ijse.greenshadow.Crop_monitoring_system.controller;


import lk.ijse.greenshadow.Crop_monitoring_system.customObj.VehicleResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.VehicleDTO;
import lk.ijse.greenshadow.Crop_monitoring_system.exception.DataPersistFailedException;
import lk.ijse.greenshadow.Crop_monitoring_system.exception.VehicleNotFoundException;
import lk.ijse.greenshadow.Crop_monitoring_system.service.VehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequestMapping("/api/v1/vehicles")
@RequiredArgsConstructor
@Slf4j
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @GetMapping("/health")
    public String healthCheck(){
        return "Vehicle is running";
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveVehicle(@RequestBody VehicleDTO vehicleDTO) {
        log.info("Attempting to save vehicle with data: {}", vehicleDTO);

        if (vehicleDTO == null) {
            log.error("Failed to save vehicle: vehicle data is null");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vehicle cannot be null");
        }
        try {
            // Save vehicle
            vehicleService.saveVehicle(vehicleDTO);
            log.info("Vehicle saved successfully: {}", vehicleDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (DataPersistFailedException e) {
            log.error("Failed to save vehicle: {}", vehicleDTO, e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Unexpected error while saving vehicle: {}", vehicleDTO, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping(value = "/{vehicleCode}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateStaff(@PathVariable("vehicleCode") String vehicleCode , @RequestBody VehicleDTO vehicleDTO){
        log.info("Attempting to update vehicle with code: {} and data: {}", vehicleCode, vehicleDTO);

        try {
            if (vehicleDTO == null) {
                log.error("Failed to update vehicle: vehicle data is null");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vehicle cannot be null");
            }
            vehicleService.updateVehicle(vehicleCode,vehicleDTO);
            log.info("Vehicle updated successfully with code: {}", vehicleCode);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (VehicleNotFoundException e){
            log.error("Vehicle with code {} not found for update", vehicleCode, e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            log.error("Unexpected error while updating vehicle with code: {}", vehicleCode, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Delete Vehicle
    @DeleteMapping(value = "/{vehicleCode}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable("vehicleCode") String vehicleCode){
        log.info("Attempting to delete vehicle with code: {}", vehicleCode);

        try {
            vehicleService.deleteVehicle(vehicleCode);
            log.info("Vehicle deleted successfully with code: {}", vehicleCode);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (VehicleNotFoundException e){
            log.error("Vehicle with code {} not found for deletion", vehicleCode, e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            log.error("Unexpected error while deleting vehicle with code: {}", vehicleCode, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Get Vehicle
    @GetMapping(value = "/{vehicleCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    public VehicleResponse getSelectVehicle(@PathVariable("vehicleCode") String vehicleCode){
        log.info("Fetching details for vehicle with code: {}", vehicleCode);
        return vehicleService.getSelectVehicle(vehicleCode);
    }

    //Get All Vehicle
    @GetMapping(value = "allVehicles", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<VehicleDTO> getAllVehicle(){
        log.info("Fetching all vehicles");
        return vehicleService.getAllVehicle();
    }
}