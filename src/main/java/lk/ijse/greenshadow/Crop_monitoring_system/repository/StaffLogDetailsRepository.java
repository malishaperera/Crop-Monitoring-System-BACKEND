package lk.ijse.greenshadow.Crop_monitoring_system.repository;


import lk.ijse.greenshadow.Crop_monitoring_system.embedded.StaffLogDetailsPK;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.association.StaffLogDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffLogDetailsRepository extends JpaRepository<StaffLogDetailsEntity, StaffLogDetailsPK> {
}
