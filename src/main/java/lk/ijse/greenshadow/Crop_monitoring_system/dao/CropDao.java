package lk.ijse.greenshadow.Crop_monitoring_system.dao;

import lk.ijse.greenshadow.Crop_monitoring_system.entity.CropEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CropDao extends JpaRepository<CropEntity, String> {

    @Query("SELECT i.cropCode FROM CropEntity i ORDER BY i.cropCode DESC")
    List<String> findLastCropCode();

    CropEntity getCropEntityByCropCode(String cropCode);
}
