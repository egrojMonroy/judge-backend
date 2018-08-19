package com.juez.repository;

import com.juez.domain.Code;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Code entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CodeRepository extends JpaRepository<Code, Long> {
    Code findById(Long id);
    Code findBySubmission_Id(Long submissionId);
}
