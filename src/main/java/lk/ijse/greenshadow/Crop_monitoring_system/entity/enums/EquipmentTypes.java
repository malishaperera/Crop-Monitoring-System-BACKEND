package lk.ijse.greenshadow.Crop_monitoring_system.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum EquipmentTypes {
    MECHANICAL,
    ELECTRICAL,
    HYDRAULIC;

//
//    @JsonCreator
//    public static EquipmentTypes fromString(String value) {
//        try {
//            return EquipmentTypes.valueOf(value.toUpperCase());
//        } catch (IllegalArgumentException e) {
//            return null; // or throw custom exception if needed
//        }
//    }
}
