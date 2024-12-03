package lk.ijse.greenshadow.Crop_monitoring_system.repository;

import lk.ijse.greenshadow.Crop_monitoring_system.embedded.CropLogDetailsPK;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.association.CropLogDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CropLogDetailsRepository extends JpaRepository<CropLogDetailsEntity, CropLogDetailsPK> {
}
