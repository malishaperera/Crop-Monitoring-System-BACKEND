package lk.ijse.greenshadow.Crop_monitoring_system.repository;


import lk.ijse.greenshadow.Crop_monitoring_system.embedded.FieldStaffDetailsPK;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.association.FieldStaffDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldStaffDetailsRepository extends JpaRepository<FieldStaffDetailsEntity, FieldStaffDetailsPK> {
}
