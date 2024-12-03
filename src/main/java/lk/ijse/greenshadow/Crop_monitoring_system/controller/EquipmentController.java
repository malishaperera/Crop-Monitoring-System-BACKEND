package lk.ijse.greenshadow.Crop_monitoring_system.controller;

import lk.ijse.greenshadow.Crop_monitoring_system.customObj.EquipmentResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.EquipmentDTO;
import lk.ijse.greenshadow.Crop_monitoring_system.exception.DataPersistFailedException;
import lk.ijse.greenshadow.Crop_monitoring_system.exception.EquipmentNotFoundException;
import lk.ijse.greenshadow.Crop_monitoring_system.service.EquipmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/equipments")
@RequiredArgsConstructor
@Slf4j
public class EquipmentController {

    @Autowired
    private final EquipmentService equipmentService;

    @GetMapping("/health")
    public String healthCheck() {
        return "Equipment is running";
    }

    //Save Equipment
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveEquipment(@RequestBody EquipmentDTO equipmentDTO){
        try {
            // Call the service to save the equipment
            equipmentService.saveEquipment(equipmentDTO);
            return new ResponseEntity<>("Equipment saved successfully", HttpStatus.CREATED);

        } catch (DataPersistFailedException e) {
            // Return the specific exception message for debugging
            return new ResponseEntity<>("Staff member already exist: " , HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            return new ResponseEntity<>("Internal server error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Update Equipment
    @PatchMapping(value = "/{equipmentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateEquipment(@PathVariable("equipmentId") String equipmentId, @RequestBody EquipmentDTO equipmentDTO) {

        try {

            // Call the service to save the equipment
            equipmentService.updateEquipment(equipmentId,equipmentDTO);
            return new ResponseEntity<>("Equipment saved successfully", HttpStatus.CREATED);

        } catch (DataPersistFailedException e) {
            // Return the specific exception message for debugging
            return new ResponseEntity<>("Staff member already exist: " , HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            return new ResponseEntity<>("Internal server error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Delete Equipment
    @DeleteMapping(value = "/{equipmentId}")
    public ResponseEntity<Void> deleteEquipment(@PathVariable("equipmentId") String equipmentId){
        try {
            equipmentService.deleteEquipment(equipmentId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (EquipmentNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Get Equipment
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EquipmentResponse getSelectEquipment(@PathVariable("id") String equipmentId){
        return equipmentService.getSelectEquipment(equipmentId);
    }

    //Get All Equipment
    @GetMapping(value = "allEquipments", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EquipmentDTO> getAllEquipments(){
        return equipmentService.getAllEquipment();
    }
}