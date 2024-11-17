package lk.ijse.greenshadow.Crop_monitoring_system.service;

import jakarta.transaction.Transactional;
import lk.ijse.greenshadow.Crop_monitoring_system.customObj.FieldErrorResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.customObj.FieldResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.dao.FieldDao;
import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.FieldDTO;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.FieldEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.exception.DataPersistFailedException;
import lk.ijse.greenshadow.Crop_monitoring_system.exception.FieldNotFoundException;
import lk.ijse.greenshadow.Crop_monitoring_system.util.AppUtil;
import lk.ijse.greenshadow.Crop_monitoring_system.util.Mapping;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class FieldServiceServiceImpl implements FieldService{

    private static final Logger logger = LoggerFactory.getLogger(FieldServiceServiceImpl.class);

    private final FieldDao fieldDao;



    @Autowired
    private final Mapping mapping;

    //Save Field
    @Override
    public void saveField(FieldDTO fieldDTO) {
        logger.info("Attempting to save field: {}", fieldDTO.getFieldName());
        List<String> savedField = fieldDao.findLastFieldCode();
        String lastFieldCode = savedField.isEmpty() ? null : savedField.get(0);
        fieldDTO.setFieldCode(AppUtil.generateNextFieldId(lastFieldCode));

        FieldEntity isSaveField = fieldDao.save(mapping.convertToFieldEntity(fieldDTO));
//        fieldDao.save(mapping.convertToFi)
        if (isSaveField == null) {
            logger.error("Failed to save field: {}", fieldDTO.getFieldName());
            throw new DataPersistFailedException("Cannot save field data");
        }

        logger.info("Field saved successfully with code: {}", savedField.getClass());
    }

    //Update Field
    @Override
    public void updateField(FieldDTO updateField) {
        logger.info("Attempting to update field with code: {}", updateField.getFieldCode());

        Optional<FieldEntity> tmpField = fieldDao.findById(updateField.getFieldCode());
        if (!tmpField.isPresent()) {
            logger.warn("Field not found for code: {}", updateField.getFieldCode());
            throw new FieldNotFoundException("Field not found");
        }else {
            tmpField.get().setFieldName(updateField.getFieldName());
            tmpField.get().setFieldLocation(updateField.getFieldLocation());
            tmpField.get().setFieldSize(updateField.getFieldSize());
            tmpField.get().setFieldImage1(updateField.getFieldImage1());
            tmpField.get().setFieldImage2(updateField.getFieldImage2());
            tmpField.get().setFieldCode(updateField.getFieldCode());

            fieldDao.save(tmpField.get());
        }
    }

    //Field Delete
    @Override
    public void deleteField(String fieldCode) {
        logger.info("Attempting to delete field with code: {}", fieldCode);

        Optional<FieldEntity> selectedFieldId = fieldDao.findById(fieldCode);
        if (!selectedFieldId.isPresent()) {
            logger.warn("Field not found for deletion with code: {}", fieldCode);
            throw new FieldNotFoundException("Field not found");
        }else {
            fieldDao.deleteById(fieldCode);
            logger.info("Field deleted successfully with code: {}", fieldCode);
        }
    }

    //Field Get
    @Override
    public FieldResponse getSelectField(String fieldCode) {
        logger.info("Fetching field details for code: {}", fieldCode);
        if (!fieldDao.existsById(fieldCode)) {
            logger.warn("Field not found for code: {}", fieldCode);
            return new FieldErrorResponse(0, "Field not found");
        }

        FieldEntity fieldEntity = fieldDao.getFieldEntityByFieldCode(fieldCode);
        logger.info("Field retrieved successfully for code: {}", fieldCode);
        return mapping.convertToFieldDTO(fieldEntity);
    }

    //Field GetAll
    @Override
    public List<FieldDTO> getAllFields() {
        logger.info("Fetching all fields");
        List<FieldEntity> allFields = fieldDao.findAll();
        logger.info("Retrieved {} fields", allFields.size());
        return mapping.convertToFieldDTOList(allFields);
    }
}