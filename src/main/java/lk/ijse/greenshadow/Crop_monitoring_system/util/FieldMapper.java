package lk.ijse.greenshadow.Crop_monitoring_system.util;

import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.FieldDTO;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.FieldEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FieldMapper {
    FieldMapper INSTANCE = Mappers.getMapper(FieldMapper.class);

    @Mapping(target = "cropDTOList", source = "cropList")
    FieldDTO toDTO(FieldEntity entity);

    FieldEntity toEntity(FieldDTO dto);

    List<FieldDTO> toDTOList(List<FieldEntity> entities);

    List<FieldEntity> toEntityList(List<FieldDTO> dtos);

}
