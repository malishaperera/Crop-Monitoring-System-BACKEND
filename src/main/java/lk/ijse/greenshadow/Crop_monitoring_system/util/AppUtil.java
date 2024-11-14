package lk.ijse.greenshadow.Crop_monitoring_system.util;

import java.util.Base64;

public class AppUtil {

    //Field ID Generate
    public static String generateNextFieldId(String lastFieldId) {
        if (lastFieldId != null && lastFieldId.startsWith("FIELD-")) {
            int lastIdNumber = Integer.parseInt(lastFieldId.substring(6));
            return String.format("FIELD-%03d", lastIdNumber + 1);
        }
        return "FIELD-001";
    }

    //Crop ID Generate
    public static String generateNextCropId(String lastCropId) {
        if (lastCropId != null && lastCropId.startsWith("CROP-")) {
            int lastIdNumber = Integer.parseInt(lastCropId.substring(5));
            return String.format("CROP-%03d", lastIdNumber + 1);
        }
        return "CROP-001";
    }

    //Equipment ID Generate
    public static String generateNextEquipmentId(String lastEquipmentId) {
        if (lastEquipmentId != null && lastEquipmentId.startsWith("EQI-")) {
            int lastIdNumber = Integer.parseInt(lastEquipmentId.substring(4));
            return String.format("EQI-%03d", lastIdNumber + 1);
        }
        return "EQI-001";
    }

    public static String toBase64ProfilePic(byte[] profilePic) {
        // Convert image bytes to Base64 string
        return Base64.getEncoder().encodeToString(profilePic);
    }

    // Staff ID Generate
    public static String generateNextStaffId(String lastStaffId) {
        if (lastStaffId != null && lastStaffId.startsWith("STAFF-")) {
            int lastIdNumber = Integer.parseInt(lastStaffId.substring(6));
            return String.format("STAFF-%03d", lastIdNumber + 1);
        }
        return "STAFF-001";
    }

    // Vehicle ID Generate
    public static String generateVehicleId(String lastVehicleId) {
        if (lastVehicleId != null && lastVehicleId.startsWith("VEHICLE-")) {
            int lastIdNumber = Integer.parseInt(lastVehicleId.substring(8));
            return String.format("VEHICLE-%03d", lastIdNumber + 1);
        }
        return "VEHICLE-001";
    }
}