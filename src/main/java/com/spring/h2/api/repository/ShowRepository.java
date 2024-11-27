package com.spring.h2.api.repository;

import com.spring.h2.api.model.Show;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowRepository extends JpaRepository<Show, Long> {
}
