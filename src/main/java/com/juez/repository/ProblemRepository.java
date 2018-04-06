package com.juez.repository;

import com.juez.domain.Problem;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Problem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {

}
