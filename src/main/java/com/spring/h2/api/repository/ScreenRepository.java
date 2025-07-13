package com.spring.h2.api.repository;

import com.spring.h2.api.model.Screen;
import com.spring.h2.api.model.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScreenRepository extends JpaRepository<Screen, Long> {
    List<Screen> findByTheater(Theater theater);
    List<Screen> findByTheaterId(Long theaterId);
    List<Screen> findByNameContainingIgnoreCase(String name);
}
