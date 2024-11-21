package lk.ijse.greenshadow.Crop_monitoring_system.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.association.CropLogDetailsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "crops")
@Entity
public class CropEntity {
    @Id
    @Column(name = "crop_code")
    private String cropCode;

    @Column(name = "crop_common_name")
    private String cropCommonName;

    @Column(name = "crop_scientific_name")
    private String cropScientificName;

    @Column(name = "crop_image", columnDefinition = "LONGTEXT")
    private String cropImage;

    @Column(name = "category")
    private String category;

    @Column(name = "crop_season")
    private String cropSeason;

    // Many-to-one relationship with FieldEntity, nullable to allow removal
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "field_code", referencedColumnName = "field_code", nullable = true)
    private FieldEntity field;

    // Add this new field to store status
    @Column(name = "field_status")
    private String fieldStatus;

    @OneToMany(mappedBy = "crop", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CropLogDetailsEntity> cropLogDetailsList = new ArrayList<>();
}