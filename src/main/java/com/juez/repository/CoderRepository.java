package com.juez.repository;

import com.juez.domain.Coder;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Coder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CoderRepository extends JpaRepository<Coder, Long> {
    @Query("select distinct coder from Coder coder left join fetch coder.contests")
    List<Coder> findAllWithEagerRelationships();

    @Query("select coder from Coder coder left join fetch coder.contests where coder.id =:id")
    Coder findOneWithEagerRelationships(@Param("id") Long id);

    Coder findOneByUser_id(Long id);
    Coder findOneByContests_IdAndUser_Login(Long id, String username);
    Long deleteByContests_Id(Long id);
}
