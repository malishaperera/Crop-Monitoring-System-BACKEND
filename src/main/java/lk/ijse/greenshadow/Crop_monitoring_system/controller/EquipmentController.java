package lk.ijse.greenshadow.Crop_monitoring_system.controller;

import lk.ijse.greenshadow.Crop_monitoring_system.dao.FieldDao;
import lk.ijse.greenshadow.Crop_monitoring_system.dao.StaffDao;
import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.EquipmentDTO;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.FieldEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.StaffEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.enums.EquipmentTypes;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.enums.Status;
import lk.ijse.greenshadow.Crop_monitoring_system.exception.DataPersistFailedException;
import lk.ijse.greenshadow.Crop_monitoring_system.service.EquipmentService;
import lk.ijse.greenshadow.Crop_monitoring_system.service.FieldService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/equipments")
@RequiredArgsConstructor
public class EquipmentController {

    @Autowired
    private final EquipmentService equipmentService;

    private final FieldDao fieldDao;

    private final StaffDao staffDao;

    @GetMapping("/health")
    public String healthCheck() {
        return "Equipment is running";
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveCustomer(@RequestBody EquipmentDTO equipmentDTO){


        try {
            // Proceed with business logic
            FieldEntity field = fieldDao.findByFieldCode(equipmentDTO.getField().getFieldCode());
            if (field == null) {
                return new ResponseEntity<>("Field not found", HttpStatus.NOT_FOUND);
            }
            equipmentDTO.setField(field);

            StaffEntity staff = staffDao.findByStaffMemberId(equipmentDTO.getStaff().getStaffMemberId());
            if (staff == null) {
                return new ResponseEntity<>("Staff not found", HttpStatus.NOT_FOUND);
            }
            equipmentDTO.setStaff(staff);

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
}