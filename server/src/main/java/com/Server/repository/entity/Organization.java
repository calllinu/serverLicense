package com.Server.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.testcontainers.shaded.org.checkerframework.common.aliasing.qual.Unique;

import java.util.List;

@Data
@Entity
@Table(name = "organizations")
@AllArgsConstructor
@NoArgsConstructor
public class Organization {

    @Id
    @Column(name = "register_code")
    private Long registerCode;

    @Column(name = "name")
    private String name;

    @Column(name = "year_of_establishment")
    private Integer yearOfEstablishment;

    @Column(name = "industry")
    private Industry industry;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Subsidiary> subsidiaries;
}
