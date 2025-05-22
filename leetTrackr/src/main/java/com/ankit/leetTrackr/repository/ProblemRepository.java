package com.ankit.leetTrackr.repository;

import com.ankit.leetTrackr.entity.Problem;
import com.ankit.leetTrackr.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProblemRepository extends JpaRepository<Problem,Long>, JpaSpecificationExecutor<Problem> {
    boolean existsByUserAndUrl(User user, String url);
}