package com.Server.repository;

import com.Server.repository.entity.Subsidiary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubsidiaryRepository extends JpaRepository<Subsidiary, Long> {
}
