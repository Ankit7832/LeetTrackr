package com.ankit.leetTrackr.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Problem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title cannot be empty")
    private String title;

    @URL(message = "please provide valid url")
    @NotBlank
    private String url;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Difficulty must be set")
    private Difficulty difficulty;

    private String notes;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Problem Status must be provided")
    private Status status;

    private LocalDate lastSolved;

    private int solveCount;

    @ManyToMany
    @JoinTable(
            name = "problem_pattern",
            joinColumns = @JoinColumn(name = "problem_id"),
            inverseJoinColumns = @JoinColumn(name = "pattern_id")
    )
    private List<Pattern> patterns;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
