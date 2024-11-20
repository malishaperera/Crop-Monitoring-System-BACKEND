package lk.ijse.greenshadow.Crop_monitoring_system.service;

import lk.ijse.greenshadow.Crop_monitoring_system.customObj.EquipmentResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.EquipmentDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EquipmentService {
    void saveEquipment(EquipmentDTO equipmentDTO);

    ResponseEntity<String> updateEquipment(String equipmentId, EquipmentDTO equipmentDTO);

    void deleteEquipment(String equipmentId);

    EquipmentResponse getSelectEquipment(String equipmentId);
    List<EquipmentDTO> getAllEquipment();
}
