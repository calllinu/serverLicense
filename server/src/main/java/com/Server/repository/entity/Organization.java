package com.Server.repository.entity;

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
    @Column(name = "id")
    private Long id;

    @Unique
    @Column(name = "register_code")
    private String registerCode;

    @Column(name = "name")
    private String name;

    @Column(name = "year_of_establishment")
    private Integer yearOfEstablishment;

    @Column(name = "industry")
    @Enumerated(EnumType.STRING)
    private Industry industry;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Subsidiary> subsidiaries;
}
