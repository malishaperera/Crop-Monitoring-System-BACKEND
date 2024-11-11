package lk.ijse.greenshadow.Crop_monitoring_system.util;

import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.CropDTO;
import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.EquipmentDTO;
import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.FieldDTO;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.CropEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.EquipmentEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.FieldEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Mapping {
    @Autowired
    private ModelMapper modelMapper;

    /*------------------------------------------------Field---------------------------------------------------*/

    //EquipmentEntity covert EquipmentDTO
    public FieldDTO convertToFieldDTO(FieldEntity field) {
        return modelMapper.map(field, FieldDTO.class);
    }

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


    /*------------------------------------------------Equipment---------------------------------------------------*/
    // EquipmentEntity to EquipmentDTO
    public EquipmentDTO convertToEquipmentDTO(EquipmentEntity equipment) {
        return modelMapper.map(equipment, EquipmentDTO.class);
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
}