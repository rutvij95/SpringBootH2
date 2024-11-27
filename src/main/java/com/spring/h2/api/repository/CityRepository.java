package com.spring.h2.api.repository;

import com.spring.h2.api.model.CityName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<CityName, Long> {
}
