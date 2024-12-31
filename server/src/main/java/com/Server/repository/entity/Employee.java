package com.Server.repository.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    private String employeeCNP;

    @Column(name = "date_of_birth", nullable = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date dateOfBirth;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "qualification", nullable = true)
    @Enumerated(EnumType.STRING)
    private Qualification qualification;

    @Column(name = "years_of_experience", nullable = true)
    private Integer yearsOfExperience;

    @ManyToOne
    @JoinColumn(name = "subsidiary_id", referencedColumnName = "subsidiary_id")
    @JsonBackReference("subsidiaryReference")
    private Subsidiary subsidiary;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @JsonBackReference("userReference")
    private User user;

    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
    private Feedback feedback;
}


