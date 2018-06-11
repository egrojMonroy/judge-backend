package com.juez.repository;

import com.juez.domain.Contest;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for the Contest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContestRepository extends JpaRepository<Contest, Long> {

    @Query("select contest from Contest contest where contest.creator.login = ?#{principal.username} and contest.active = true" )
    Page<Contest> findByCreatorIsCurrentUser(Pageable pageable);
    @Query("select distinct contest from Contest contest left join fetch contest.problems")
    List<Contest> findAllWithEagerRelationships();

    @Query("select contest from Contest contest left join fetch contest.problems where contest.id =:id")
    Contest findOneWithEagerRelationships(@Param("id") Long id);

    Contest findById(Long id);

    Page<Contest> findByEnddateBeforeAndActiveIs(ZonedDateTime zonedDateTime,Boolean active ,Pageable pageable);
    Page<Contest> findByStartdateAfterAndActiveIsOrEnddateAfterAndActiveIs(ZonedDateTime zonedDateTime,Boolean active1 ,ZonedDateTime zonedDateTimed, Boolean active2, Pageable pageable);
}
