package com.juez.repository;

import com.juez.domain.TestCase;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



/**
 * Spring Data JPA repository for the TestCase entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestCaseRepository extends JpaRepository<TestCase, Long> {
    Page<TestCase> findAllByProblemId(Long id, Pageable pageable);
}
