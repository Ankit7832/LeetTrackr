package com.ankit.leetTrackr.service.serviceImpl;

import com.ankit.leetTrackr.dto.ProblemRequestDto;
import com.ankit.leetTrackr.dto.ProblemResponseDto;
import com.ankit.leetTrackr.entity.*;
import com.ankit.leetTrackr.exception.DuplicateResourceException;
import com.ankit.leetTrackr.exception.ResourceNotFoundException;
import com.ankit.leetTrackr.repository.ProblemRepository;
import com.ankit.leetTrackr.repository.ProblemSpecifications;
import com.ankit.leetTrackr.repository.UserRepository;
import com.ankit.leetTrackr.service.PatternService;
import com.ankit.leetTrackr.service.ProblemService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProblemServiceImpl implements ProblemService {
    private final UserRepository userRepository;
    private final ProblemRepository problemRepository;
    private final PatternService patternService;

    @Override
    public ProblemResponseDto addProblem(ProblemRequestDto problemRequestDto, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("User Doesnt exist"));
        boolean exists = problemRepository.existsByUserAndUrl(user, problemRequestDto.getUrl());
        if (exists) {
            throw new DuplicateResourceException("You have already added this problem.");
        }

        List<Pattern> patterns=patternService.resolvePatternsByNames(problemRequestDto.getPatternNames());

        Problem problem = new Problem();
        problem.setTitle(problemRequestDto.getTitle());
        problem.setUrl(problemRequestDto.getUrl());
        problem.setDifficulty(Difficulty.valueOf(problemRequestDto.getDifficulty()));
        problem.setNotes(problemRequestDto.getNotes());
        problem.setStatus(Status.valueOf(problemRequestDto.getStatus()));
        problem.setPatterns(patterns);
        if (Status.valueOf(problemRequestDto.getStatus()) == Status.DONE) {
            problem.setSolveCount(1);
        } else {
            problem.setSolveCount(0);
        }

        problem.setLastSolved(LocalDate.now());
        problem.setUser(user);
        
        Problem saved=problemRepository.save(problem);
        return mapToDto(saved);
    }




    @Override
    public ProblemResponseDto getProblemById(Long id, String username) throws AccessDeniedException {
        Problem problem= problemRepository.findById(id).orElseThrow((()-> new ResourceNotFoundException("Problem NOt Found")));

        if(!problem.getUser().getUsername().equals(username)){
            throw new AccessDeniedException("Problem does not belong to user");
        }

        return mapToDto(problem);
    }

    @Override
    public List<ProblemResponseDto> getAllProblemsForUser(String username) {
        User user= userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("User not found"));
        List<ProblemResponseDto> problemList =user.getProblems()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return problemList;
    }

    @Override
    public ProblemResponseDto updateProblem(Long id, ProblemRequestDto dto , String username) throws AccessDeniedException {
       Problem problem= problemRepository.findById(id).orElseThrow((()-> new ResourceNotFoundException("Problem NOt Found")));

       if(!problem.getUser().getUsername().equals(username)){
           throw new AccessDeniedException("Problem does not belong to user");
       }
       List<Pattern> patterns=patternService.resolvePatternsByNames(dto.getPatternNames());
        problem.setTitle(dto.getTitle());
        problem.setUrl(dto.getUrl());
        problem.setDifficulty(Difficulty.valueOf(dto.getDifficulty()));
        problem.setNotes(dto.getNotes());

        Status status = Status.valueOf(dto.getStatus());
        problem.setStatus(status);

        if (status == Status.DONE) {
            problem.setSolveCount(problem.getSolveCount() + 1);
            problem.setLastSolved(LocalDate.now());
        }
        Problem saved=problemRepository.save(problem);
        return mapToDto(saved);
    }

    @Override
    public void deleteProblem(Long id, String username) throws AccessDeniedException {
        Problem problem= problemRepository.findById(id).orElseThrow((()-> new ResourceNotFoundException("Problem NOt Found")));

        if(!problem.getUser().getUsername().equals(username)){
            throw new AccessDeniedException("Problem does not belong to user");
        }
        problemRepository.delete(problem);
    }

    @Override
    public Page<ProblemResponseDto> getFilteredProblemsForUser(String username,
                                                               int page, int size,
                                                               String status,
                                                               String difficulty,
                                                               String patternName) {
        User user=userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("Usern not found"));

        Pageable pageable = PageRequest.of(page,size, Sort.Direction.DESC,"lastSolved");
        Specification<Problem> spec= ProblemSpecifications.byUser(user.getId());

        if(status!=null && !status.isEmpty()){
            spec=spec.and(ProblemSpecifications.byStatus(status));
        }
        if(difficulty!=null && !difficulty.isEmpty()){
            spec=spec.and(ProblemSpecifications.byDifficulty(difficulty));
        }
        if(patternName!=null && !patternName.isEmpty()){
            spec=spec.and(ProblemSpecifications.byPatternName(patternName));
        }
        return problemRepository.findAll(spec,pageable)
                .map(this::mapToDto);
    }

    private ProblemResponseDto mapToDto(Problem saved) {
        ProblemResponseDto dto=new ProblemResponseDto();
        dto.setId(saved.getId());
        dto.setTitle(saved.getTitle());
        dto.setUrl(saved.getUrl());
        dto.setDifficulty(String.valueOf(saved.getDifficulty()));
        dto.setNotes(saved.getNotes());
        dto.setStatus(String.valueOf(saved.getStatus()));
        dto.setPatternNames(saved.getPatterns().stream().map(Pattern::getName).toList());
        dto.setLastSolved(saved.getLastSolved());
        dto.setSolveCount(saved.getSolveCount());
        return dto;
    }

}
