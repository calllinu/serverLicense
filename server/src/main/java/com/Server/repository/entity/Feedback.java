package com.Server.repository.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    private Integer feedBackId;

    @Column(name = "satisfy_salary")
    private Confirmation confirmationSalary;

    @Column(name = "type_of_engagement")
    private Engagement engagement;

    @Column(name = "overtime")
    private Confirmation confirmationOvertime;

    @Column(name = "protective_equipment_adequate")
    private Confirmation confirmationEquipmentAdequate;

    @Column(name = "safety_measures_applied")
    private Confirmation confirmationSafetyMeasures;

    @Column(name = "protection_measures_clear")
    private Confirmation confirmationProtectionMeasures;

    @Column(name = "time_expose_danger")
    private WorkTime workTime;

    @OneToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "employee_id")
    private Employee employee;
}


