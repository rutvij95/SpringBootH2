package com.spring.h2.api.repository;

import com.spring.h2.api.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    Optional<City> findByNameIgnoreCase(String name);
    List<City> findByState(String state);
    List<City> findByCountry(String country);
    boolean existsByNameIgnoreCase(String name);
}
