package com.juez.service.mapper;

import com.juez.domain.*;
import com.juez.service.dto.ContestDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Contest and its DTO ContestDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, ProblemMapper.class})
public interface ContestMapper extends EntityMapper<ContestDTO, Contest> {

    @Mapping(source = "creator.id", target = "creatorId")
    ContestDTO toDto(Contest contest);

    @Mapping(source = "creatorId", target = "creator")
    @Mapping(target = "coders", ignore = true)
    Contest toEntity(ContestDTO contestDTO);

    default Contest fromId(Long id) {
        if (id == null) {
            return null;
        }
        Contest contest = new Contest();
        contest.setId(id);
        return contest;
    }
}
