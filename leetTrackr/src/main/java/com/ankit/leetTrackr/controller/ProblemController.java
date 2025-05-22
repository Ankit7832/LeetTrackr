package com.ankit.leetTrackr.controller;

import com.ankit.leetTrackr.dto.ProblemRequestDto;
import com.ankit.leetTrackr.dto.ProblemResponseDto;
import com.ankit.leetTrackr.service.ProblemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/problems")
@RequiredArgsConstructor
public class ProblemController {
    private final ProblemService problemService;

    @PostMapping("/add")
    public ResponseEntity<?> addProblem(@Valid @RequestBody ProblemRequestDto dto, Principal principal){
        ProblemResponseDto problemResponseDto=problemService.addProblem(dto, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(problemResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProblemById(@PathVariable Long id,Principal principal) throws AccessDeniedException {
        ProblemResponseDto problemResponseDto=problemService.getProblemById(id, principal.getName());
        return ResponseEntity.status(HttpStatus.OK).body(problemResponseDto);
    }
    @GetMapping("/")
    public ResponseEntity<?> getAllProblems(Principal principal){
        List<ProblemResponseDto> problemList=problemService.getAllProblemsForUser(principal.getName());
        return ResponseEntity.status(HttpStatus.OK).body(problemList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProblem(@PathVariable Long id, @Valid @RequestBody ProblemRequestDto dto,Principal principal) throws AccessDeniedException {
        ProblemResponseDto responseDto=problemService.updateProblem(id,dto, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProblem(@PathVariable Long id, Principal principal) throws AccessDeniedException {
        problemService.deleteProblem(id, principal.getName());
        return ResponseEntity.ok().body("Problem deleted successfully.");
    }

    @GetMapping("/filter")
    public ResponseEntity<?> getFilteredProblems(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size,
                                                 @RequestParam(required = false) String status,
                                                 @RequestParam(required = false) String difficulty,
                                                 @RequestParam(required = false) String patternName,
                                                 Principal principal){
        Page<ProblemResponseDto> pages=problemService.getFilteredProblemsForUser(principal.getName(),page,size,status,difficulty,patternName);
        return ResponseEntity.status(HttpStatus.OK).body(pages);

    }

}
