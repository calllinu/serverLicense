package com.Server.repository.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @JsonBackReference("organizationReference")
    private Organization organization;

    @OneToMany(mappedBy = "subsidiary", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference("subsidiaryReference")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Employee> employees;

    @OneToMany(mappedBy = "subsidiary", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference("subsidiaryReference")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<RegistrationRequest> registrationRequests;
}
