package com.Server.repository.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.common.aliasing.qual.Unique;

import java.util.List;

@Data
@Entity
@Table(name = "organizations")
@AllArgsConstructor
@NoArgsConstructor
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "organization_id")
    private Long organizationId;

    @Column(name = "organization_code", nullable = false, unique = true)
    private String organizationCode;

    @Column(name = "name")
    private String name;

    @Column(name = "year_of_establishment")
    private Integer yearOfEstablishment;

    @Column(name = "industry")
    @Enumerated(EnumType.STRING)
    private Industry industry;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("organizationReference") // Allow serialization from Organization to Subsidiary
    private List<Subsidiary> subsidiaries;
}


