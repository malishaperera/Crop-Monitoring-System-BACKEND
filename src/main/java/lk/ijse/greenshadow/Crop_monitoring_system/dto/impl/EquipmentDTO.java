package lk.ijse.greenshadow.Crop_monitoring_system.dto.impl;


import jakarta.validation.constraints.NotNull;
import lk.ijse.greenshadow.Crop_monitoring_system.customObj.EquipmentResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.dto.SuperDTO;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.FieldEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.StaffEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.enums.EquipmentTypes;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentDTO implements SuperDTO, EquipmentResponse {
    @NotNull(message = "Equipment ID cannot be null")
    private String equipmentId;
    @NotNull(message = "Name cannot be null")
    private String name;
    @NotNull(message = "Equipment type cannot be null")
    private EquipmentTypes equipmentType;
    @NotNull(message = "Status cannot be null")
    private Status status;
    private String fieldCode;
    private String staffMemberId;
}