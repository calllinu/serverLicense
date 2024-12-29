package com.Server.repository.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @Column(name = "subsidiary_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long subsidiaryId;

    @Column(name = "subsidiary_code")
    private String subsidiaryCode;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "address")
    private String address;

    @ManyToOne
    @JoinColumn(name = "organization_id", referencedColumnName = "organization_id")
    @JsonBackReference
    private Organization organization;

    @OneToMany(mappedBy = "subsidiary", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Employee> employees;
}
