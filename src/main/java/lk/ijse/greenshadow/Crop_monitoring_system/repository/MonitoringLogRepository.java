package lk.ijse.greenshadow.Crop_monitoring_system.repository;

import lk.ijse.greenshadow.Crop_monitoring_system.entity.MonitoringLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonitoringLogRepository extends JpaRepository<MonitoringLogEntity, String> {

//    @Query("SELECT i.logCode FROM MonitoringLogEntity i ORDER BY i.logCode DESC")
//    List<String> findLastMonitoringLogCode();
//
//    MonitoringLogEntity getMonitoringLogEntityByLogCode(String logCode);
}
