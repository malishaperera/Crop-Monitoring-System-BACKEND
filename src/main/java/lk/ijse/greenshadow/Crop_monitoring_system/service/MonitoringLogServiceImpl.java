package lk.ijse.greenshadow.Crop_monitoring_system.service;

import jakarta.transaction.Transactional;
import lk.ijse.greenshadow.Crop_monitoring_system.dao.MonitoringLogDao;
import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.MonitoringLogDTO;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.CropEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.FieldEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.MonitoringLogEntity;
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
public class MonitoringLogServiceImpl implements MonitoringLogService {

    private final MonitoringLogDao monitoringLogDao;

    @Autowired
    private final Mapping mapping;

    @Override
    public void saveMonitoringLog(MonitoringLogDTO monitoringLogDTO) {
        try {
            List<String> monitoringLogCode = monitoringLogDao.findLastMonitoringLogCode();
            String lastMonitoringLogCode = monitoringLogCode.isEmpty() ? null : monitoringLogCode.get(0);
            monitoringLogDTO.setLogCode(AppUtil.generateMonitoringLogId(lastMonitoringLogCode));

            MonitoringLogEntity logEntity = mapping.convertToMonitoringLogEntity(monitoringLogDTO);
            monitoringLogDao.save(logEntity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unexpected error occurred while saving monitoring log", e);
        }
    }


    @Override
    public void updateMonitoringLog(MonitoringLogDTO updateMonitoringLogDTO) {
        Optional<MonitoringLogEntity> tmpMonitoringLog = monitoringLogDao.findById(updateMonitoringLogDTO.getLogCode());
        if (!tmpMonitoringLog.isPresent()) {
            throw new CropNotFoundException("Monitoring with code " + updateMonitoringLogDTO.getLogCode() + " not found");
        }

        MonitoringLogEntity monitoringLogEntity = tmpMonitoringLog.get();
        monitoringLogEntity.setLogObservation(updateMonitoringLogDTO.getLogObservation());
        monitoringLogEntity.setObservedImage(updateMonitoringLogDTO.getObservedImage());

        monitoringLogDao.save(monitoringLogEntity);
    }



}