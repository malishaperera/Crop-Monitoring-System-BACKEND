package lk.ijse.greenshadow.Crop_monitoring_system.service;

import jakarta.transaction.Transactional;
import lk.ijse.greenshadow.Crop_monitoring_system.customObj.EquipmentErrorResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.customObj.EquipmentResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.repository.EquipmentRepository;
import lk.ijse.greenshadow.Crop_monitoring_system.repository.FieldRepository;
import lk.ijse.greenshadow.Crop_monitoring_system.repository.StaffRepository;
import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.EquipmentDTO;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.EquipmentEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.FieldEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.StaffEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.exception.DataPersistFailedException;
import lk.ijse.greenshadow.Crop_monitoring_system.exception.EquipmentNotFoundException;
import lk.ijse.greenshadow.Crop_monitoring_system.util.AppUtil;
import lk.ijse.greenshadow.Crop_monitoring_system.util.Mapping;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EquipmentServiceImpl implements EquipmentService {

    @Autowired
    private final Mapping mapping;

    private final EquipmentRepository equipmentDao;

    private final FieldRepository fieldDao;

    private final StaffRepository staffDao;

    @Override
    public void saveEquipment(EquipmentDTO equipmentDTO) {
        log.info("Saving equipment: {}", equipmentDTO.getName());
        try {
            List<String> equipmentId = equipmentDao.findLastEquipmentId();
            String lastEquipmentId = equipmentId.isEmpty() ? null : equipmentId.get(0);
            equipmentDTO.setEquipmentId(AppUtil.generateNextEquipmentId(lastEquipmentId));

            //this equipment table exist
            if (equipmentDao.existsByStaff_StaffMemberId(equipmentDTO.getStaffMemberId())) {
                log.warn("Staff member with ID {} is already assigned to another equipment", equipmentDTO.getStaffMemberId());
                throw new DataPersistFailedException("Staff member is already assigned to another equipment.");
            }

            FieldEntity field = fieldDao.findByFieldCode(equipmentDTO.getFieldCode());
            if (field == null) {
                log.error("Field with code {} not found", equipmentDTO.getFieldCode());
                throw new DataPersistFailedException("Field not found");
            }

            StaffEntity staff = staffDao.findByStaffMemberId(equipmentDTO.getStaffMemberId());
            if (staff == null) {
                log.error("Staff with ID {} not found", equipmentDTO.getStaffMemberId());
                throw new DataPersistFailedException("Staff not found");
            }

            EquipmentEntity savedEquipment = equipmentDao.save(mapping.convertToEquipmentEntity(equipmentDTO));
            if (savedEquipment == null) {
                log.error("Unable to save equipment");
                throw new DataPersistFailedException("Unable to save equipment");
            }
            log.info("Equipment saved successfully: {}", savedEquipment.getEquipmentId());
        } catch (DataPersistFailedException e) {
            log.error("Error occurred while saving equipment: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error occurred while saving equipment", e);
            throw new RuntimeException("Unexpected error occurred while saving equipment", e);
        }
    }

    @Override
    public ResponseEntity<String> updateEquipment(String equipmentId, EquipmentDTO equipmentDTO) {
        log.info("Updating equipment with ID: {}", equipmentId);

        try {
            //Check if the equipment with the given ID exists
            Optional<EquipmentEntity> tmpEquipmentEntity = equipmentDao.findById(equipmentId);
            if (!tmpEquipmentEntity.isPresent()) {
                log.error("Equipment with ID {} not found", equipmentId);
                throw new EquipmentNotFoundException("Equipment not found");
            }

            // Get the existing equipment entity
            EquipmentEntity existingEquipment = tmpEquipmentEntity.get();

            //Get the staff member by staffMemberId
            StaffEntity staff = staffDao.findByStaffMemberId(equipmentDTO.getStaffMemberId());
            if (staff == null) {
                log.error("Staff with ID {} not found", equipmentDTO.getStaffMemberId());
                throw new DataPersistFailedException("Staff not found");
            }

            //Ensure that the field exists in the database
            FieldEntity field = fieldDao.findByFieldCode(equipmentDTO.getFieldCode());
            if (field == null) {
                log.error("Field with code {} not found", equipmentDTO.getFieldCode());
                throw new DataPersistFailedException("Field not found");
            }

            //Check if the staff is assigned to another equipment, but not the current one
            Optional<EquipmentEntity> staffAssignedEquipment = equipmentDao.findByStaff_StaffMemberIdAndEquipmentIdNot(
                    equipmentDTO.getStaffMemberId(), equipmentId);


            if (staffAssignedEquipment.isPresent()) {
                log.warn("Staff member with ID {} is already assigned to another equipment: {}", equipmentDTO.getStaffMemberId(),
                        staffAssignedEquipment.get().getEquipmentId());
                throw new DataPersistFailedException("Staff member is already assigned to another equipment");
            }

            existingEquipment.setName(equipmentDTO.getName());
            existingEquipment.setEquipmentType(equipmentDTO.getEquipmentType());
            existingEquipment.setStatus(equipmentDTO.getStatus());
            existingEquipment.setField(field);
            existingEquipment.setStaff(staff);

            equipmentDao.save(existingEquipment);
            log.info("Equipment updated successfully with ID: {}", equipmentId);

            return new ResponseEntity<>("Equipment updated successfully", HttpStatus.OK);
        } catch (DataPersistFailedException e) {
            log.error("Error occurred while updating equipment: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error occurred while updating equipment", e);
            throw new RuntimeException("Unexpected error occurred while updating equipment", e);
        }
    }

    @Override
    public void deleteEquipment(String equipmentId) {
        log.info("Deleting equipment with ID: {}", equipmentId);
        Optional<EquipmentEntity> findId = equipmentDao.findById(equipmentId);
        if (!findId.isPresent()) {
            log.error("Equipment with ID {} not found for deletion", equipmentId);
            throw new EquipmentNotFoundException("Customer not found");
        } else {
            equipmentDao.deleteById(equipmentId);
            log.info("Equipment deleted successfully with ID: {}", equipmentId);
        }
    }

    @Override
    public EquipmentResponse getSelectEquipment(String equipmentId) {
        log.info("Fetching equipment with ID: {}", equipmentId);
        if (equipmentDao.existsById(equipmentId)) {
            return mapping.convertToEquipmentDTO(equipmentDao.getReferenceById(equipmentId));
        }else {
            log.error("Equipment with ID {} not found", equipmentId);
            return new EquipmentErrorResponse(0,"Equipment not found");
        }
    }

    @Override
    public List<EquipmentDTO> getAllEquipment() {
        log.info("Fetching all equipment.");
        List<EquipmentEntity> getAllEquipment = equipmentDao.findAll();
        log.info("Total equipment found: {}", getAllEquipment.size());
        return mapping.convertToEquipmentEntityDTOList(getAllEquipment);
    }
}