package com.juez.repository;

import com.juez.domain.Problem;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Problem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {

    @Query("select problem from Problem problem where problem.creator.login = ?#{principal.username}")
    List<Problem> findByCreatorIsCurrentUser();
    Page<Problem> findByNameContainingIgnoreCase(String name, Pageable pageable); 
    Problem findById(Long id);
}
