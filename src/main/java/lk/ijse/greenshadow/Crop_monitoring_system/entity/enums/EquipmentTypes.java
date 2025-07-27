package lk.ijse.greenshadow.Crop_monitoring_system.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum EquipmentTypes {
    MECHANICAL,
    ELECTRICAL,
    HYDRAULIC;


    @JsonCreator
    public static EquipmentTypes fromValue(String value) {
        return EquipmentTypes.valueOf(value.toUpperCase());
    }

}