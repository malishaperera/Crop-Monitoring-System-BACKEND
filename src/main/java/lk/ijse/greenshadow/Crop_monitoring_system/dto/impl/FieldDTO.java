package lk.ijse.greenshadow.Crop_monitoring_system.dto.impl;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lk.ijse.greenshadow.Crop_monitoring_system.customObj.FieldResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.dto.SuperDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldDTO implements SuperDTO, FieldResponse {
    @NotNull(message = "Field code cannot be null")
    @Size(min = 1, max = 50, message = "Field code must be between 1 and 50 characters")
    private String fieldCode;

    @NotNull(message = "Field name cannot be null")
    @Size(min = 1, max = 100, message = "Field name must be between 1 and 100 characters")
    private String fieldName;

    private Point fieldLocation;
    @Positive(message = "Field size must be a positive number")
    private double fieldSize;
    @Pattern(regexp = "^.+\\.(jpg|jpeg|png)$", message = "Field image 1 must be a valid image file (.jpg, .jpeg, .png)")
    private String fieldImage1;
    @Pattern(regexp = "^.+\\.(jpg|jpeg|png)$", message = "Field image 1 must be a valid image file (.jpg, .jpeg, .png)")
    private String fieldImage2;

    @Size(min = 1, message = "There must be at least one crop code")
    private List<String> cropCodes = new ArrayList<>();
    @Size(min = 1, message = "There must be at least one equipment ID")
    private List<String> equipmentIds = new ArrayList<>();

    //Associate
    private List<String> staffMemberIds = new ArrayList<>();
    @Size(min = 1, message = "There must be at least one log code")
    private List<String> logCodes = new ArrayList<>();
}