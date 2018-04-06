package com.juez.repository;

import com.juez.domain.Contest;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Contest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContestRepository extends JpaRepository<Contest, Long> {
    @Query("select distinct contest from Contest contest left join fetch contest.problems")
    List<Contest> findAllWithEagerRelationships();

    @Query("select contest from Contest contest left join fetch contest.problems where contest.id =:id")
    Contest findOneWithEagerRelationships(@Param("id") Long id);

}
