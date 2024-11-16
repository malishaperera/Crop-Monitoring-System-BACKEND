package lk.ijse.greenshadow.Crop_monitoring_system.dao;

import lk.ijse.greenshadow.Crop_monitoring_system.entity.FieldEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.MonitoringLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonitoringLogDao extends JpaRepository<MonitoringLogEntity, String> {

    @Query("SELECT i.logCode FROM MonitoringLogEntity i ORDER BY i.logCode DESC")
    List<String> findLastMonitoringLogCode();

    MonitoringLogEntity getMonitoringLogEntityByLogCode(String logCode);
}
