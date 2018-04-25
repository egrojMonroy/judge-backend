package com.juez.service.mapper;

import com.juez.domain.*;
import com.juez.service.dto.ProblemDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Problem and its DTO ProblemDTO.
 */
@Mapper(componentModel = "spring", uses = {CodeMapper.class, UserMapper.class})
public interface ProblemMapper extends EntityMapper<ProblemDTO, Problem> {

    @Mapping(source = "solution.id", target = "solutionId")
    @Mapping(source = "creator.id", target = "creatorId")
    ProblemDTO toDto(Problem problem);

    @Mapping(source = "solutionId", target = "solution")
    @Mapping(target = "submissions", ignore = true)
    @Mapping(target = "tests", ignore = true)
    @Mapping(target = "contests", ignore = true)
    @Mapping(source = "creatorId", target = "creator")
    Problem toEntity(ProblemDTO problemDTO);

    default Problem fromId(Long id) {
        if (id == null) {
            return null;
        }
        Problem problem = new Problem();
        problem.setId(id);
        return problem;
    }
}
