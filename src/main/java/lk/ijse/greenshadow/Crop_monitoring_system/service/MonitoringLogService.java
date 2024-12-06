package lk.ijse.greenshadow.Crop_monitoring_system.service;

import lk.ijse.greenshadow.Crop_monitoring_system.customObj.MonitoringLogResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.MonitoringLogDTO;

import java.util.List;

public interface MonitoringLogService {
    void saveMonitoringLog(MonitoringLogDTO monitoringLogDTO);
    void updateMonitoringLog(MonitoringLogDTO updateMonitoringLogDTO);
    void deleteMonitoringLog(String logCode);
    MonitoringLogResponse getSelectMonitoringLog(String logCode);
    List<MonitoringLogDTO> getAllMonitoringLogs();
}