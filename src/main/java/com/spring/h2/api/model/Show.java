package com.spring.h2.api.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long showId;
    private String timing;

    @ManyToOne
    @JoinColumn(name = "theater_id", nullable = false)
    private Theater theater;
}
