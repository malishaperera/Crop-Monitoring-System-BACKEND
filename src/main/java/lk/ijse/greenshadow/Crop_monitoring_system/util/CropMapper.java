package lk.ijse.greenshadow.Crop_monitoring_system.util;

import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.CropDTO;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.CropEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
@Mapper(componentModel = "spring")
public interface CropMapper {
    CropMapper INSTANCE = Mappers.getMapper(CropMapper.class);

    CropDTO toDTO(CropEntity entity);

    CropEntity toEntity(CropDTO dto);

    List<CropDTO> toDTOList(List<CropEntity> entities);

    List<CropEntity> toEntityList(List<CropDTO> dtos);
}
