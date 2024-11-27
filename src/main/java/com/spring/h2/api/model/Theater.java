package com.spring.h2.api.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Theater {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long theaterId;
    private String name;

    @ManyToOne
    @JoinColumn(name = "city_name_id", nullable = false)
    // This city_name_id is not dependent on anything. This is telling what should be name of Column in Theater.
    private City city; //In City object Mapped by name should be same as this object Name;

    @OneToMany(mappedBy = "theater", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Show> showList = new ArrayList<>();
}
