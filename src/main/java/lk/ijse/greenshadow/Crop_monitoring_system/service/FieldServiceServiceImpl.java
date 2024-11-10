package lk.ijse.greenshadow.Crop_monitoring_system.service;

import jakarta.transaction.Transactional;
import lk.ijse.greenshadow.Crop_monitoring_system.dao.FieldDao;
import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.FieldDTO;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.FieldEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.exception.DataPersistFailedException;
import lk.ijse.greenshadow.Crop_monitoring_system.util.AppUtil;
import lk.ijse.greenshadow.Crop_monitoring_system.util.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FieldServiceServiceImpl implements FieldService{

    private final FieldDao fieldDao;

    @Autowired
    private final Mapping mapping;

    @Override
    public void saveField(FieldDTO fieldDTO) {
        List<String> fieldCode = fieldDao.findLastFieldCode();
        String lastFieldCode = fieldCode.isEmpty() ? null : fieldCode.get(0);
        fieldDTO.setFieldCode(AppUtil.generateNextFieldId(lastFieldCode));

        FieldEntity isSaveField = fieldDao.save(mapping.convertToEntity(fieldDTO));

        if (isSaveField == null) {
            throw new DataPersistFailedException("Cannot save data");
        }

        System.out.println("Saving FieldEntity:"+isSaveField.getFieldCode()+isSaveField.getFieldName());

    }
}
