package lk.ijse.greenshadow.Crop_monitoring_system.repository;


import lk.ijse.greenshadow.Crop_monitoring_system.embedded.FieldLogDetailsPK;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.association.FieldLogDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldLogDetailsRepository extends JpaRepository<FieldLogDetailsEntity, FieldLogDetailsPK> {

    //Details
}
