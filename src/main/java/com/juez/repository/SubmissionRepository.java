package com.juez.repository;

import com.juez.domain.Problem;
import com.juez.domain.Submission;
import com.juez.domain.User;

import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

/**
 * Spring Data JPA repository for the Submission entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    @Query("select submission from Submission submission where submission.submitter.login = ?#{principal.username}")
    List<Submission> findBySubmitterIsCurrentUser();
    
    Page<Submission> findAllOrderByDateupload(Pageable pageable);

    List<Submission> findAllByDateuploadBetween(ZonedDateTime start, ZonedDateTime end);
    List<Submission> findAllByDateuploadBetweenAndSubmitterInAndProblemIn(ZonedDateTime start, ZonedDateTime end,Set<User> submitters, Set<Problem> problems);
    List<Submission> findAllByDateuploadBetweenAndSubmitterAndProblemIn(ZonedDateTime start, ZonedDateTime end,User submitter, Set<Problem> problems);
       
}

