package lk.ijse.greenshadow.Crop_monitoring_system.dao;

import lk.ijse.greenshadow.Crop_monitoring_system.entity.EquipmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentDao extends JpaRepository<EquipmentEntity, String> {

    @Query("SELECT i.equipmentId FROM EquipmentEntity i ORDER BY i.equipmentId DESC")
    List<String> findLastEquipmentId();

    boolean existsByStaff_StaffMemberId(String staffMemberId);


}
