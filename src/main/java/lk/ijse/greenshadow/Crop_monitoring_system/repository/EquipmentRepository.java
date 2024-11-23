package lk.ijse.greenshadow.Crop_monitoring_system.repository;

import lk.ijse.greenshadow.Crop_monitoring_system.entity.EquipmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipmentRepository extends JpaRepository<EquipmentEntity, String> {


    @Query("SELECT i.equipmentId FROM EquipmentEntity i ORDER BY i.equipmentId DESC")
    List<String> findLastEquipmentId();

    boolean existsByStaff_StaffMemberId(String staffMemberId);

    Optional<EquipmentEntity> findByStaff_StaffMemberIdAndEquipmentIdNot(String staffMemberId, String equipmentId);
}