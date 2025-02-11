package com.Server.repository.entity;

import com.Server.repository.entity.enums.RequestStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "registration_requests")
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "request_id")
    private Long requestId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "admin_id", nullable = false)
    private Long adminId;

    @ManyToOne
    @JoinColumn(name = "subsidiary_id", referencedColumnName = "subsidiary_id")
    @JsonBackReference("subsidiaryReference")
    private Subsidiary subsidiary;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RequestStatus status;
}
