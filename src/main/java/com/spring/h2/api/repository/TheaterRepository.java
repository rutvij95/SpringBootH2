package com.spring.h2.api.repository;

import com.spring.h2.api.model.Theater;
import com.spring.h2.api.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TheaterRepository extends JpaRepository<Theater, Long> {
    List<Theater> findByCity(City city);
    List<Theater> findByCityId(Long cityId);
    List<Theater> findByNameContainingIgnoreCase(String name);

    @Query("SELECT t FROM Theater t WHERE t.city.id = :cityId")
    List<Theater> findTheatersByCity(@Param("cityId") Long cityId);
}
