package lk.ijse.greenshadow.Crop_monitoring_system.service;

import jakarta.transaction.Transactional;
import lk.ijse.greenshadow.Crop_monitoring_system.customObj.CropErrorResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.customObj.CropResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.dao.CropDao;
import lk.ijse.greenshadow.Crop_monitoring_system.dao.FieldDao;
import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.CropDTO;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.CropEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.FieldEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.exception.CropNotFoundException;
import lk.ijse.greenshadow.Crop_monitoring_system.exception.DataPersistFailedException;
import lk.ijse.greenshadow.Crop_monitoring_system.util.AppUtil;
import lk.ijse.greenshadow.Crop_monitoring_system.util.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CropServiceImpl implements CropService {

    private final CropDao cropDao;



    @Autowired
    private final Mapping mapping;

    private final FieldDao fieldDao;

    @Override
    public void saveCrop(CropDTO cropDTO,String fieldCode) {
        List<String> cropCode = cropDao.findLastCropCode();
        String lastCropCode = cropCode.isEmpty() ? null : cropCode.get(0);
        cropDTO.setCropCode(AppUtil.generateNextCropId(lastCropCode));

        if (cropDao.existsById(cropDTO.getCropCode())) {
            throw new DataPersistFailedException("Crop not save");
        }

        cropDTO.setField(mapping.convertToFieldDTO(fieldDao.getFieldEntityByFieldCode(fieldCode)));
        CropEntity cropEntity = cropDao.save(mapping.convertToCropEntity(cropDTO));
        if (cropEntity.getCropCode() == null) {
            throw new DataPersistFailedException("Crop not save");
        }





        // Fetch the FieldEntity using fieldCode
//        FieldEntity field = fieldDao.findByFieldCode(cropDTO.getField().getFieldCode());
//        if (field == null) {
//            throw new DataPersistFailedException("Field not found");
//        }
//
//        cropDTO.setField(field);  // Set the FieldEntity into CropDTO
//
//        CropEntity isSaveCrop = cropDao.save(mapping.convertToCropEntity(cropDTO));
//
//        if (isSaveCrop == null) {
//            throw new DataPersistFailedException("Cannot save data");
//        }
//        System.out.println("Saving CropEntity:"+isSaveCrop.getCropCode()+isSaveCrop.getCropCommonName());



    }

    @Override
    public void updateCrop(CropDTO updateCrop, String fieldCode) {
        // Check if the CropEntity exists
        Optional<CropEntity> existingCropOptional = cropDao.findById(updateCrop.getCropCode());
        if (existingCropOptional.isEmpty()) {
            throw new CropNotFoundException("Crop with code " + updateCrop.getCropCode() + " not found");
        }

        CropEntity existingCrop = existingCropOptional.get();

        // Validate and fetch FieldEntity using fieldCode
        FieldEntity fieldEntity = fieldDao.getFieldEntityByFieldCode(fieldCode);
        if (fieldEntity == null) {
            throw new DataPersistFailedException("Field with code " + fieldCode + " not found");
        }

        // Update the CropEntity with new data
        existingCrop.setCropCommonName(updateCrop.getCropCommonName());
        existingCrop.setCropScientificName(updateCrop.getCropScientificName());
        existingCrop.setCropImage(updateCrop.getCropImage());
        existingCrop.setCategory(updateCrop.getCategory());
        existingCrop.setCropSeason(updateCrop.getCropSeason());
        existingCrop.setField(fieldEntity);

        // Save the updated CropEntity to the database
        CropEntity updatedCropEntity = cropDao.save(existingCrop);

        // Ensure the update was successful
        if (updatedCropEntity == null || updatedCropEntity.getCropCode() == null) {
            throw new DataPersistFailedException("Failed to update crop with code " + updateCrop.getCropCode());
        }

        System.out.println("Updated CropEntity: " + updatedCropEntity.getCropCode() + " " + updatedCropEntity.getCropCommonName());

    }

    @Override
    public void deleteCrop(String cropCode) {
        Optional<CropEntity> selectedCropId = cropDao.findById(cropCode);
        if (!selectedCropId.isPresent()) {
            throw new CropNotFoundException("Crop not found");
        }else {
            cropDao.deleteById(cropCode);
        }
    }

    @Override
    public CropResponse getSelectCrop(String cropCode) {
        if (cropDao.existsById(cropCode)) {
            CropEntity  cropEntityByCropCode = cropDao.getCropEntityByCropCode(cropCode);
            return mapping.convertToCropDTO(cropEntityByCropCode);
        }else {
            return new CropErrorResponse(0,"Crop not found");
        }
    }

    @Override
    public List<CropDTO> getAllCrops() {
        List<CropEntity> getAllCrops = cropDao.findAll();
        return mapping.convertToCropDTOList(getAllCrops);
    }
}