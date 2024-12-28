package com.Server.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Table(name = "employees")
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    @Id
    @Column(name = "employee_cnp")
    private Long employeeCNP;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "qualification")
    private Qualification qualification;

    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    @ManyToOne
    @JoinColumn(name = "subsidiary_code")
    private Subsidiary subsidiary;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
    private Feedback feedback;
}
