package com.ankit.leetTrackr.service;

import com.ankit.leetTrackr.dto.ProblemRequestDto;
import com.ankit.leetTrackr.dto.ProblemResponseDto;
import org.springframework.data.domain.Page;


import java.nio.file.AccessDeniedException;
import java.util.List;

public interface ProblemService {
    public ProblemResponseDto addProblem(ProblemRequestDto problemRequestDto,String username);

    public ProblemResponseDto getProblemById(Long id,String username) throws AccessDeniedException;

    public List<ProblemResponseDto> getAllProblemsForUser(String username);

    public ProblemResponseDto updateProblem(Long id, ProblemRequestDto dto, String username) throws AccessDeniedException;

    public void deleteProblem(Long id,String username) throws AccessDeniedException;

    public Page<ProblemResponseDto> getFilteredProblemsForUser(
            String username, int page, int size,
            String status, String difficulty, String patternName);
}
