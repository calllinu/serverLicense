package com.Server.repository.entity;

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

    @Column(name = "satisfaction_level")
    private Double satisfactionLevel;

    @Column(name = "last_evaluation")
    private Double lastEvaluation;

    @Column(name = "number_project")
    private Integer numberProject;

    @Column(name = "average_monthly_hours")
    private Integer averageMonthlyHours;

    @Column(name = "time_spend_company")
    private Integer timeSpendCompany;

    @Column(name = "Work_accident")
    private Integer workAccident;

    @Column(name = "promotion_last_5years")
    private Integer promotionLast5years;

    @Column(name = "dept")
    private String department;

    @Column(name = "salary")
    private String salary;

    @OneToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "employee_id")
    @JsonBackReference("feedbackReference")
    private Employee employee;
}


