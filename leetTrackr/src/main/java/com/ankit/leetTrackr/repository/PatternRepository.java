package com.ankit.leetTrackr.repository;

import com.ankit.leetTrackr.entity.Pattern;
import com.ankit.leetTrackr.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatternRepository extends JpaRepository<Pattern,Long> {
    Optional<Pattern> findByName(String name);
}
