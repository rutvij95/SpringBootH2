package com.spring.h2.api.controller;

import com.spring.h2.api.model.Theater;
import com.spring.h2.api.repository.TheaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/theater")
public class TheaterController {
    @Autowired
    TheaterRepository theaterRepository;

    @PostMapping
    public Theater saveTheater(@RequestBody Theater theaterDetails) {
        return theaterRepository.save(theaterDetails);
    }

    @GetMapping
    public List<Theater> getAllTheater() {
        return theaterRepository.findAll();
    }
}
