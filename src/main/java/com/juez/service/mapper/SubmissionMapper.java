package com.juez.service.mapper;

import com.juez.domain.*;
import com.juez.service.dto.SubmissionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Submission and its DTO SubmissionDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, ProblemMapper.class})
public interface SubmissionMapper extends EntityMapper<SubmissionDTO, Submission> {

    @Mapping(source = "submitter.id", target = "submitterId")
    @Mapping(source = "problem.id", target = "problemId")
    SubmissionDTO toDto(Submission submission);

    @Mapping(source = "submitterId", target = "submitter")
    @Mapping(source = "problemId", target = "problem")
    Submission toEntity(SubmissionDTO submissionDTO);

    default Submission fromId(Long id) {
        if (id == null) {
            return null;
        }
        Submission submission = new Submission();
        submission.setId(id);
        return submission;
    }
}
