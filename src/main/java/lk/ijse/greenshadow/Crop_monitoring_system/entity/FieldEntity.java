package lk.ijse.greenshadow.Crop_monitoring_system.entity;



import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.association.FieldLogDetailsEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.association.FieldStaffDetailsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "fields")
@Entity
public class FieldEntity {
    @Id
    @Column(name = "field_code")
    private String fieldCode;
    @Column(name = "field_name")
    private String fieldName;
    @Column(name = "field_location")
    private Point fieldLocation;
    @Column(name = "field_size")
    private double fieldSize;
    @Column(name = "field_image_1", columnDefinition = "LONGTEXT")
    private String fieldImage1;
    @Column(name = "field_image_2", columnDefinition = "LONGTEXT")
    private String fieldImage2;


    @JsonManagedReference
    @OneToMany(mappedBy = "field", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<CropEntity> cropList = new ArrayList<>();

    @OneToMany(mappedBy = "field", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<EquipmentEntity> equipmentList = new ArrayList<>();

    //Associate

    // Associate with FieldStaffDetailsEntity
    @OneToMany(mappedBy = "field", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<FieldStaffDetailsEntity> fieldStaffDetailsList = new ArrayList<>();


    // Associate with FieldLogDetailsEntity
    @OneToMany(mappedBy = "field", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FieldLogDetailsEntity> fieldLogDetailsList = new ArrayList<>();

}