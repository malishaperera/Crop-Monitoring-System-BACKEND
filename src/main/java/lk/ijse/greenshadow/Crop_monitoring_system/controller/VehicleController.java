package lk.ijse.greenshadow.Crop_monitoring_system.controller;

import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.StaffDTO;
import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.VehicleDTO;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.StaffEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.VehicleEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.exception.DataPersistFailedException;
import lk.ijse.greenshadow.Crop_monitoring_system.exception.StaffNotFoundException;
import lk.ijse.greenshadow.Crop_monitoring_system.exception.VehicleNotFoundException;
import lk.ijse.greenshadow.Crop_monitoring_system.service.StaffService;
import lk.ijse.greenshadow.Crop_monitoring_system.service.VehicleService;
import lk.ijse.greenshadow.Crop_monitoring_system.util.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static org.yaml.snakeyaml.nodes.NodeId.mapping;

@RestController
@RequestMapping("/api/v1/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private StaffService staffService;


//    @Autowired
//    private final Mapping mapping;


    @GetMapping("/health")
    public String healthCheck(){
        return "Vehicle is running";
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveVehicle(@RequestBody VehicleDTO vehicleDTO) {

        if (vehicleDTO == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vehicle cannot be null");
        }
        try {
            // Fetch StaffEntity based on staffMemberId
            StaffDTO staffDTO = staffService.existByStaffMember(vehicleDTO.getStaffMemberId());
            if (staffDTO == null) {
                return new ResponseEntity<>("Staff not found", HttpStatus.NOT_FOUND);
            }

            vehicleDTO.setStaffMemberId(staffDTO.getStaffMemberId());

            // Save vehicle
            vehicleService.saveVehicle(vehicleDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (DataPersistFailedException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PatchMapping(value = "/{vehicleCode}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateStaff(@PathVariable("vehicleCode") String vehicleCode , @RequestBody VehicleDTO vehicleDTO){
        try {
            if (vehicleDTO == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vehicle cannot be null");
            }
            vehicleService.updateVehicle(vehicleCode,vehicleDTO);

            return new ResponseEntity<>(HttpStatus.CREATED);

        }catch (StaffNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}