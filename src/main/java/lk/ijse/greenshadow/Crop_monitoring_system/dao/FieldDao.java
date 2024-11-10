package lk.ijse.greenshadow.Crop_monitoring_system.dao;

import lk.ijse.greenshadow.Crop_monitoring_system.entity.FieldEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FieldDao extends JpaRepository<FieldEntity, String> {

    @Query("SELECT i.fieldCode FROM FieldEntity i ORDER BY i.fieldCode DESC")
    List<String> findLastFieldCode();

    FieldEntity getFieldEntityByFieldCode(String fieldCode);
}