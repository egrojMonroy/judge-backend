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

    @Query("select problem from Problem problem where problem.creator.login = ?#{principal.username} and problem.active = true")
    Page<Problem> findByCreatorIsCurrentUser(Pageable pageable);
    Page<Problem> findByNameContainingIgnoreCaseAndActive(String name,Boolean active ,Pageable pageable); 
    Problem findById(Long id);
    Long deleteByContests_id(Long id);
    Page<Problem> findByActive(Boolean active, Pageable pageable);



    @Query(value = "select * from problem where id NOT IN (  select problems_id from contest_problem where contests_id IN (select id from contest where (startdate > now() AND active=true) OR (enddate > now() AND active=true))) /*#pageable*/ ORDER BY name ",
    countQuery = "select count(*) from problem where id NOT IN (  select problems_id from contest_problem where contests_id IN (select id from contest where (startdate > now() AND active=true) OR (enddate > now() AND active=true))) /*#pageable*/ ORDER BY name ",
    nativeQuery = true)
    Page<Problem> findAllWithouContest(Pageable pageable);
}
