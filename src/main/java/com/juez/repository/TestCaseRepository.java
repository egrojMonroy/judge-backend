package com.juez.repository;

import com.juez.domain.TestCase;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TestCase entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestCaseRepository extends JpaRepository<TestCase, Long> {

}
