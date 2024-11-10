package lk.ijse.greenshadow.Crop_monitoring_system.util;

import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.FieldDTO;
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

    //FieldEntity covert FieldDTO
    public FieldDTO convertToDTO(FieldEntity field) {
        return modelMapper.map(field, FieldDTO.class);
    }

    //FieldDTO covert FieldEntity
    public FieldEntity convertToEntity(FieldDTO dto) {
        return modelMapper.map(dto, FieldEntity.class);
    }

    //FieldEntity list convert FieldDTO
    public List<FieldDTO> convertToDTO(List<FieldEntity> fields) {
        return modelMapper.map(fields, List.class);
    }

}
