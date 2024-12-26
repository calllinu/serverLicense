package com.Server.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "subsidiary")
@AllArgsConstructor
@NoArgsConstructor
public class Subsidiary {

    @Id
    @Column(name = "subsidiary_code")
    private Long subsidiaryCode;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "address")
    private String address;

    @Column(name = "nr_employees")
    private Integer nrOfEmployees;

    @ManyToOne
    @JoinColumn(name = "register_code")
    private Organization organization;

    @OneToMany(mappedBy = "subsidiary", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Employee> employees;
}
