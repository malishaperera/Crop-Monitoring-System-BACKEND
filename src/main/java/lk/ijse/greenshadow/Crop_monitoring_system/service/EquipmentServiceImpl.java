package lk.ijse.greenshadow.Crop_monitoring_system.service;

import jakarta.transaction.Transactional;
import lk.ijse.greenshadow.Crop_monitoring_system.customObj.EquipmentErrorResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.customObj.EquipmentResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.dao.EquipmentDao;
import lk.ijse.greenshadow.Crop_monitoring_system.dao.FieldDao;
import lk.ijse.greenshadow.Crop_monitoring_system.dao.StaffDao;
import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.EquipmentDTO;
import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.FieldDTO;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.EquipmentEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.FieldEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.StaffEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.exception.DataPersistFailedException;
import lk.ijse.greenshadow.Crop_monitoring_system.exception.EquipmentNotFoundException;
import lk.ijse.greenshadow.Crop_monitoring_system.util.AppUtil;
import lk.ijse.greenshadow.Crop_monitoring_system.util.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class EquipmentServiceImpl implements EquipmentService {

    @Autowired
    private final Mapping mapping;

    private final EquipmentDao equipmentDao;

    private final FieldDao fieldDao;

    private final StaffDao staffDao;


    @Override
    public void saveEquipment(EquipmentDTO equipmentDTO) {

        try {
            List<String> equipmentId = equipmentDao.findLastEquipmentId();
            String lastEquipmentId = equipmentId.isEmpty() ? null : equipmentId.get(0);
            equipmentDTO.setEquipmentId(AppUtil.generateNextEquipmentId(lastEquipmentId));

            if (equipmentDao.existsByStaff_StaffMemberId(equipmentDTO.getStaff().getStaffMemberId())) {
                throw new DataPersistFailedException("Staff member is already assigned to another equipment.");
            }

            FieldEntity field = fieldDao.findByFieldCode(equipmentDTO.getField().getFieldCode());
            if (field == null) {
                throw new DataPersistFailedException("Field not found");
            }

            StaffEntity staff = staffDao.findByStaffMemberId(equipmentDTO.getStaff().getStaffMemberId());
            if (staff == null) {
                throw new DataPersistFailedException("Staff not found");
            }

            equipmentDTO.setStaff(staff);
            equipmentDTO.setField(field);

            EquipmentEntity savedEquipment = equipmentDao.save(mapping.convertToEquipmentEntity(equipmentDTO));
            if (savedEquipment == null) {
                throw new DataPersistFailedException("Unable to save equipment");
            }

        } catch (DataPersistFailedException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred while saving equipment", e);
        }
    }

    @Override
    public void updateEquipment(String equipmentId, EquipmentDTO equipmentDTO) {
        try {
            // Check if the equipment with the given ID exists
            Optional<EquipmentEntity> tmpEquipmentEntity = equipmentDao.findById(equipmentId);
            if (!tmpEquipmentEntity.isPresent()) {
                throw new EquipmentNotFoundException("Equipment not found");
            }

            EquipmentEntity existingEquipment = tmpEquipmentEntity.get();

            // Get the staff member by staffMemberId
            StaffEntity staff = staffDao.findByStaffMemberId(equipmentDTO.getStaff().getStaffMemberId());
            if (staff == null) {
                throw new DataPersistFailedException("Staff not found");
            }

            // Check if the staff is assigned to any other equipment (other than the one being updated)
            // Find if there is any equipment assigned to this staff member (excluding the current one)
            Optional<EquipmentEntity> staffAssignedEquipment = equipmentDao.findByStaff_StaffMemberIdAndEquipmentIdNot(
                    equipmentDTO.getStaff().getStaffMemberId(), equipmentId);

            if (staffAssignedEquipment.isPresent()) {
                // If staff is assigned to another equipment (excluding the one being updated), block the update
                throw new DataPersistFailedException("Staff member is already assigned to another equipment");
            }

            // Update the existing equipment
            existingEquipment.setName(equipmentDTO.getName());
            existingEquipment.setEquipmentType(equipmentDTO.getEquipmentType());
            existingEquipment.setStatus(equipmentDTO.getStatus());
            existingEquipment.setField(equipmentDTO.getField());
            existingEquipment.setStaff(staff);

            // Save the updated equipment
            equipmentDao.save(existingEquipment);

        } catch (DataPersistFailedException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred while updating equipment", e);
        }
    }

    @Override
    public void deleteEquipment(String equipmentId) {
        Optional<EquipmentEntity> findId = equipmentDao.findById(equipmentId);
        if(!findId.isPresent()) {
            throw new EquipmentNotFoundException("Customer not found");
        }else {
            equipmentDao.deleteById(equipmentId);
        }
    }

    @Override
    public EquipmentResponse getSelectEquipment(String equipmentId) {
        if (equipmentDao.existsById(equipmentId)) {
           EquipmentEntity equipment  = equipmentDao.getEquipmentEntityByEquipmentId(equipmentId);
           return mapping.convertToEquipmentDTO(equipment);
        }else {
            return new EquipmentErrorResponse(0,"Equipment not found");
        }
    }

    @Override
    public List<EquipmentDTO> getAllEquipment() {
        List<EquipmentEntity> getAllEquipment = equipmentDao.findAll();
        return mapping.convertToEquipmentDTOList(getAllEquipment);
    }
}