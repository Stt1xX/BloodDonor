package com.bloodlink.entities;

import com.bloodlink.entities.enums.BloodGroup;
import com.bloodlink.entities.enums.RequestStatus;
import com.bloodlink.entities.enums.RhFactor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@With
@Table(name = "blood_requests")
@NoArgsConstructor
public class BloodRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @SequenceGenerator(
            name = "blood_request_group_id_seq",
            sequenceName = "blood_request_group_id_seq",
            allocationSize = 1,
            initialValue = 1
    )
    @Column(columnDefinition = "serial")
    private Long requestGroupId;

    @Enumerated(EnumType.STRING)
    private BloodGroup bloodGroup;

    @Enumerated(EnumType.STRING)
    private RhFactor rhFactor;

    private Double volumeNeeded;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    private Boolean isEmergency;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "blood_bank_id")
    private Organization bloodBank;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "medical_institution_id")
    private Organization medicalInstitution;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String rejectionReason;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        status = RequestStatus.PENDING;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
} 