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
    public void saveCrop(CropDTO cropDTO) {
        List<String> cropCode = cropDao.findLastCropCode();
        String lastCropCode = cropCode.isEmpty() ? null : cropCode.get(0);
        cropDTO.setCropCode(AppUtil.generateNextCropId(lastCropCode));

        // Fetch the FieldEntity using fieldCode
        FieldEntity field = fieldDao.findByFieldCode(cropDTO.getField().getFieldCode());
        if (field == null) {
            throw new DataPersistFailedException("Field not found");
        }

        cropDTO.setField(field);  // Set the FieldEntity into CropDTO

        CropEntity isSaveCrop = cropDao.save(mapping.convertToCropEntity(cropDTO));

        if (isSaveCrop == null) {
            throw new DataPersistFailedException("Cannot save data");
        }
        System.out.println("Saving CropEntity:"+isSaveCrop.getCropCode()+isSaveCrop.getCropCommonName());

    }

    @Override
    public void updateCrop(CropDTO updateCrop) {
        Optional<CropEntity> tmpCrop = cropDao.findById(updateCrop.getCropCode());
        if (!tmpCrop.isPresent()) {
            throw new CropNotFoundException("Crop with code " + updateCrop.getCropCode() + " not found");
        }

        // Fetch the FieldEntity using fieldCode
        FieldEntity field = fieldDao.findByFieldCode(updateCrop.getField().getFieldCode());
        if (field == null) {
            throw new DataPersistFailedException("Field with code " + updateCrop.getField().getFieldCode() + " not found");
        }

        System.out.println("Field found: " + field.getFieldCode());

        // Update the CropEntity with new values
        CropEntity cropEntity = tmpCrop.get();
        cropEntity.setCropCommonName(updateCrop.getCropCommonName());
        cropEntity.setCropScientificName(updateCrop.getCropScientificName());
        cropEntity.setCropImage(updateCrop.getCropImage());
        cropEntity.setCategory(updateCrop.getCategory());
        cropEntity.setCropSeason(updateCrop.getCropSeason());
        cropEntity.setField(field);
        // Save the updated CropEntity
        cropDao.save(cropEntity);

        System.out.println("Updated CropEntity: " + cropEntity.getCropCode() + " " + cropEntity.getCropCommonName());
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