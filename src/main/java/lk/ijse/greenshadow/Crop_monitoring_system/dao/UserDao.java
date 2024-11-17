package lk.ijse.greenshadow.Crop_monitoring_system.dao;

import lk.ijse.greenshadow.Crop_monitoring_system.entity.MonitoringLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<MonitoringLogEntity, String> {
}
