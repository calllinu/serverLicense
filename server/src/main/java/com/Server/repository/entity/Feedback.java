package com.Server.repository.entity;

import com.Server.repository.entity.enums.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "feedback")
@AllArgsConstructor
@NoArgsConstructor
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "feedback_id")
    private Integer feedbackId;

    @Column(name = "satisfy_salary")
    @Enumerated(EnumType.STRING)
    private Confirmation confirmationSalary;

    @Column(name = "type_of_engagement")
    @Enumerated(EnumType.STRING)
    private Engagement engagement;

    @Column(name = "overtime")
    @Enumerated(EnumType.STRING)
    private Confirmation confirmationOvertime;

    @Column(name = "protective_equipment_adequate")
    @Enumerated(EnumType.STRING)
    private Confirmation confirmationEquipmentAdequate;

    @Column(name = "safety_measures_applied")
    @Enumerated(EnumType.STRING)
    private Confirmation confirmationSafetyMeasures;

    @Column(name = "protection_measures_clear")
    @Enumerated(EnumType.STRING)
    private Confirmation confirmationProtectionMeasures;

    @Column(name = "time_expose_danger")
    @Enumerated(EnumType.STRING)
    private WorkTime workTime;

    @Column(name = "factors_workplace_safety")
    @Enumerated(EnumType.STRING)
    private FactorsWorkplaceSafety factorsWorkplaceSafety;

    @Column(name = "danger_type")
    @Enumerated(EnumType.STRING)
    private DangerType dangerType;

    @OneToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "employee_id")
    @JsonBackReference("feedbackReference")
    private Employee employee;
}


