package com.spring.h2.api.controller;

import com.spring.h2.api.model.CityName;
import com.spring.h2.api.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/city")
public class CityController {

    @Autowired
    private CityRepository cityRepository;

    @PostMapping
    public ResponseEntity<CityName> saveCity(@RequestBody CityName cityDetails) {
        return ResponseEntity.ok(cityRepository.save(cityDetails));
    }
}
