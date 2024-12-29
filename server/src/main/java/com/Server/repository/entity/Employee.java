package com.Server.repository.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "employee_id")
    private Long employeeId;

    @Column(name = "employee_cnp", nullable = true)
    private Long employeeCNP;

    @Column(name = "date_of_birth", nullable = true)
    private Date dateOfBirth;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "qualification", nullable = true)
    private Qualification qualification;

    @Column(name = "years_of_experience", nullable = true)
    private Integer yearsOfExperience;

    @ManyToOne
    @JoinColumn(name = "subsidiary_code")
    @JsonBackReference
    private Subsidiary subsidiary;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @JsonManagedReference
    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
    private Feedback feedback;
}
