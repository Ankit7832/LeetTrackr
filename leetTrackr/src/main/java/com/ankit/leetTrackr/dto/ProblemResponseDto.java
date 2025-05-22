package com.ankit.leetTrackr.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ProblemResponseDto {
    private Long id;
    private String title;
    private String url;
    private String difficulty;
    private String notes;
    private String status;
    private LocalDate lastSolved;
    private int solveCount;
    private List<String> patternNames;
}
