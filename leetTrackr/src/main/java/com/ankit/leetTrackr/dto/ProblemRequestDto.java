package com.ankit.leetTrackr.dto;

import lombok.Data;


import java.util.List;

@Data
public class ProblemRequestDto {
    private String title;
    private String url;
    private String difficulty;
    private String notes;
    private String status;


    private List<String> patternNames;
}
