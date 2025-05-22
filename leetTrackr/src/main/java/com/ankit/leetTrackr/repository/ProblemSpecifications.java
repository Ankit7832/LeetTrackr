package com.ankit.leetTrackr.repository;

import com.ankit.leetTrackr.entity.Difficulty;
import com.ankit.leetTrackr.entity.Problem;
import com.ankit.leetTrackr.entity.Status;
import org.springframework.data.jpa.domain.Specification;

public class ProblemSpecifications {

    public static Specification<Problem> byUser(Long userId){
        Specification<Problem> spec= (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("user").get("id"),userId);
        return spec;
    }

    public static Specification<Problem> byStatus(String status){
        Specification<Problem> spec= (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), Status.valueOf(status.toUpperCase()));
        return spec;
    }
    public static Specification<Problem> byDifficulty(String difficulty){
        Specification<Problem> spec=(root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("difficulty"), Difficulty.valueOf(difficulty.toUpperCase()));
        return spec;
    }

    public static Specification<Problem> byPatternName(String patternName){
        Specification<Problem> spec =(root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.join("patterns").get("name"),patternName);
        return spec;
    }
}
