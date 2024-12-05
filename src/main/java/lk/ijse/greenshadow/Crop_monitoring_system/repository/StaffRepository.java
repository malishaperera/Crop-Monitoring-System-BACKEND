package lk.ijse.greenshadow.Crop_monitoring_system.repository;

import jakarta.transaction.Transactional;
import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.StaffDTO;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.StaffEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<StaffEntity, String> {

    @Query("SELECT i.staffMemberId FROM StaffEntity i ORDER BY i.staffMemberId DESC")
    List<String> findLastStaffMemberId();

    StaffEntity findByStaffMemberId(String staffMemberId);

    //custom

    List<StaffEntity> findByStaffMemberIdIn(List<String> staffMemberIds);

    // Add a method to fetch by ID and update the role
    @Modifying
    @Transactional
    @Query("UPDATE StaffEntity s SET s.role = :role WHERE s.staffMemberId = :staffMemberId")
    void updateRoleByStaffMemberId(@Param("role") String role, @Param("staffMemberId") String staffMemberId);
}
