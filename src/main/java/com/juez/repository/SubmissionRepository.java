package com.juez.repository;

import com.juez.domain.Submission;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Submission entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    @Query("select submission from Submission submission where submission.submitter.login = ?#{principal.username}")
    List<Submission> findBySubmitterIsCurrentUser();

}
