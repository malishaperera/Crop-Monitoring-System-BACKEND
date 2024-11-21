package lk.ijse.greenshadow.Crop_monitoring_system.entity;

import jakarta.persistence.PreRemove;

public class FieldEntityListener {
    @PreRemove
    public void preRemove(FieldEntity field) {
        for (CropEntity crop : field.getCropList()) {
            crop.setField(null);
            crop.setFieldStatus("field removed");
        }
    }
}
