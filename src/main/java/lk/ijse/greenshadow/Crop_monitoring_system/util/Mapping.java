package lk.ijse.greenshadow.Crop_monitoring_system.util;

import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.*;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.*;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.association.FieldStaffDetailsEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class Mapping {
    @Autowired
    private ModelMapper modelMapper;

    /*------------------------------------------------Field---------------------------------------------------*/

//    public FieldDTO convertToFieldDTO(FieldEntity field) {
//        if (field == null) {
//            throw new IllegalArgumentException("FieldEntity is null");
//        }
//
//        // Map basic field properties
//        FieldDTO fieldDTO = modelMapper.map(field, FieldDTO.class);
//
//        // Handle crops associated with the field
//        if (field.getCropList() == null || field.getCropList().isEmpty()) {
//            System.out.println("No crops found for this field.");
//        } else {
//            System.out.println("Number of crops found: " + field.getCropList().size());
//
//            // Map crop codes from crop list
//            List<String> cropCodes = field.getCropList().stream()
//                    .map(CropEntity::getCropCode)
//                    .collect(Collectors.toList());
//            fieldDTO.setCropCodes(cropCodes);
//        }
//
//        return fieldDTO;
//    }


    //EquipmentDTO covert EquipmentEntity
    public FieldEntity convertToFieldEntity(FieldDTO dto) {
        return modelMapper.map(dto, FieldEntity.class);
    }


    //EquipmentEntity list convert EquipmentDTO
    public List<FieldDTO> convertToFieldDTOList(List<FieldEntity> fields) {
//        return modelMapper.map(fields, List.class);
        return fields.stream()
                .map(this::convertToFieldDTO)
                .toList();
    }


    /*------------------------------------------------Crop---------------------------------------------------*/
    // CropEntity to CropDTO
    public CropDTO convertToCropDTO(CropEntity crop) {
        return modelMapper.map(crop, CropDTO.class);
    }

    // CropDTO to CropEntity
    public CropEntity convertToCropEntity(CropDTO dto) {
        return modelMapper.map(dto, CropEntity.class);
    }

    // List<CropEntity> to List<CropDTO>
    public List<CropDTO> convertToCropDTOList(List<CropEntity> cropList) {
        return cropList.stream()
                .map(this::convertToCropDTO)
                .toList();
    }
//
//    /*-----------------------------------------------Equipment---------------------------------------------------*/
//    // EquipmentEntity to EquipmentDTO
//    public EquipmentDTO convertToEquipmentDTO(EquipmentEntity equipment) {
//        return modelMapper.map(equipment, EquipmentDTO.class);
//    }

    // EquipmentEntity to EquipmentDTO
    public EquipmentDTO convertToEquipmentDTO(EquipmentEntity equipment) {
        if (equipment == null) {
            throw new IllegalArgumentException("EquipmentEntity is null");
        }

        // Map basic equipment properties
        EquipmentDTO equipmentDTO = modelMapper.map(equipment, EquipmentDTO.class);

        // Handle associated FieldEntity and StaffEntity
        if (equipment.getField() != null) {
            equipmentDTO.setFieldCode(equipment.getField().getFieldCode());
//            equipmentDTO.setFieldName(equipment.getField().getFieldName());
        }

        if (equipment.getStaff() != null) {
            equipmentDTO.setStaffMemberId(equipment.getStaff().getStaffMemberId());
//            equipmentDTO.setStaffName(equipment.getStaff().getName()); // Assuming staff name exists
        }

        return equipmentDTO;
    }



    // EquipmentDTO to EquipmentEntity
    public EquipmentEntity convertToEquipmentEntity(EquipmentDTO dto) {
        return modelMapper.map(dto, EquipmentEntity.class);
    }

    // List<EquipmentEntity> to List<EquipmentDTO>
    public List<EquipmentDTO> convertToEquipmentDTOList(List<EquipmentEntity> equipmentList) {
        return equipmentList.stream()
                .map(this::convertToEquipmentDTO)
                .toList();
    }
//
//    /*------------------------------------------------Staff---------------------------------------------------*/
    // StaffEntity to StaffDTO
    public StaffDTO convertToStaffDTO(StaffEntity staff) {
        return modelMapper.map(staff, StaffDTO.class);
    }

    // StaffDTO to StaffEntity
    public StaffEntity convertToStaffEntity(StaffDTO dto) {
        return modelMapper.map(dto, StaffEntity.class);
    }

    // List<StaffEntity> to List<StaffDTO>
    public List<StaffDTO> convertToStaffDTOList(List<StaffEntity> staffList) {
        return staffList.stream()
                .map(this::convertToStaffDTO)
                .toList();
    }
//
//    /*------------------------------------------------Vehicle---------------------------------------------------*/
    // VehicleEntity to VehicleDTO
    public VehicleDTO convertToVehicleDTO(VehicleEntity vehicle) {
        return modelMapper.map(vehicle, VehicleDTO.class);
    }

    // VehicleDTO to VehicleEntity
    public VehicleEntity convertToVehicleEntity(VehicleDTO dto) {
        return modelMapper.map(dto, VehicleEntity.class);
    }

    // List<VehicleEntity> to List<VehicleDTO>
    public List<VehicleDTO> convertToVehicleDTOList(List<VehicleEntity> vehicleList) {
        return vehicleList.stream()
                .map(this::convertToVehicleDTO)
                .toList();
    }

//    /*------------------------------------------------MonitoringLog---------------------------------------------------*/
    // MonitoringLogEntity to MonitoringLogDTO
    public MonitoringLogDTO convertToMonitoringLogDTO(MonitoringLogEntity log) {
        return modelMapper.map(log, MonitoringLogDTO.class);
    }

    // MonitoringLogDTO to MonitoringLogEntity
    public MonitoringLogEntity convertToMonitoringLogEntity(MonitoringLogDTO dto) {
        return modelMapper.map(dto, MonitoringLogEntity.class);
    }

    // List<MonitoringLogEntity> to List<MonitoringLogDTO>
    public List<MonitoringLogDTO> convertToMonitoringLogDTOList(List<MonitoringLogEntity> logList) {
        return logList.stream()
                .map(this::convertToMonitoringLogDTO)
                .toList();
    }









    public FieldDTO convertToFieldDTO(FieldEntity field) {
        if (field == null) {
            throw new IllegalArgumentException("FieldEntity is null");
        }

        // Map basic field properties
        FieldDTO fieldDTO = modelMapper.map(field, FieldDTO.class);

        // Handle crops associated with the field
        if (field.getCropList() != null && !field.getCropList().isEmpty()) {
            List<String> cropCodes = field.getCropList().stream()
                    .map(CropEntity::getCropCode) // Extract crop codes
                    .collect(Collectors.toList());
            fieldDTO.setCropCodes(cropCodes);
        } else {
            System.out.println("No crops found for this field.");
        }

        // Handle equipment associated with the field
        if (field.getEquipmentList() != null && !field.getEquipmentList().isEmpty()) {
            List<String> equipmentCodes = field.getEquipmentList().stream()
                    .map(EquipmentEntity::getEquipmentId) // Extract equipment IDs
                    .collect(Collectors.toList());
            fieldDTO.setEquipmentIds(equipmentCodes);
        }

        // Handle staff associated with the field
        if (field.getFieldStaffDetailsList() != null && !field.getFieldStaffDetailsList().isEmpty()) {
            // Assuming `FieldStaffDetailsEntity` contains a `getStaff()` method returning a `StaffEntity`
            List<String> staffIds = field.getFieldStaffDetailsList().stream()
                    .map(FieldStaffDetailsEntity::getStaff)        // Get `StaffEntity`
                    .map(StaffEntity::getStaffMemberId)            // Extract staff member ID
                    .collect(Collectors.toList());
            fieldDTO.setStaffMemberIds(staffIds);
        }

        return fieldDTO;
    }


}