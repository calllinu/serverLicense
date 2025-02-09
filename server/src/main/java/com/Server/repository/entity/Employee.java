package com.Server.repository.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDate dateOfBirth;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "qualification", nullable = true)
    @Enumerated(EnumType.STRING)
    private Qualification qualification;

    @Column(name = "years_of_experience", nullable = true)
    private Integer yearsOfExperience;

    @Column(name = "date_of_hiring", nullable = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDate dateOfHiring;

    @ManyToOne
    @JoinColumn(name = "subsidiary_id", referencedColumnName = "subsidiary_id")
    @JsonBackReference("subsidiaryReference")
    private Subsidiary subsidiary;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @JsonBackReference("userReference")
    private User user;

    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Feedback feedback;

}
