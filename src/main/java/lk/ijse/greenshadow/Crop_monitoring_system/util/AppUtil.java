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


    public static String toBase64ProfilePic(byte[] profilePic) {
        // Convert image bytes to Base64 string
        return Base64.getEncoder().encodeToString(profilePic);
    }
}