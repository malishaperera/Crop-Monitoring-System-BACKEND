package lk.ijse.greenshadow.Crop_monitoring_system.dto.impl;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lk.ijse.greenshadow.Crop_monitoring_system.customObj.EquipmentResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.dto.SuperDTO;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.enums.EquipmentTypes;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentDTO implements SuperDTO, EquipmentResponse {
    private String equipmentId;
    private String name;
    private EquipmentTypes equipmentType;
    private Status status;
}