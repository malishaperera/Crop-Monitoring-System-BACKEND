package lk.ijse.greenshadow.Crop_monitoring_system.service;

import jakarta.transaction.Transactional;
import lk.ijse.greenshadow.Crop_monitoring_system.dao.EquipmentDao;
import lk.ijse.greenshadow.Crop_monitoring_system.dao.FieldDao;
import lk.ijse.greenshadow.Crop_monitoring_system.dao.StaffDao;
import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.EquipmentDTO;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.EquipmentEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.FieldEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.StaffEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.exception.DataPersistFailedException;
import lk.ijse.greenshadow.Crop_monitoring_system.util.AppUtil;
import lk.ijse.greenshadow.Crop_monitoring_system.util.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
            // Rethrow the exception to be handled in the controller
            throw e;
        } catch (Exception e) {
            // Handle other unexpected exceptions
            throw new RuntimeException("Unexpected error occurred while saving equipment", e);
        }
    }
}