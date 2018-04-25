package com.juez.service.mapper;

import com.juez.domain.*;
import com.juez.service.dto.CodeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Code and its DTO CodeDTO.
 */
@Mapper(componentModel = "spring", uses = {SubmissionMapper.class})
public interface CodeMapper extends EntityMapper<CodeDTO, Code> {

    @Mapping(source = "submission.id", target = "submissionId")
    CodeDTO toDto(Code code);

    @Mapping(source = "submissionId", target = "submission")
    Code toEntity(CodeDTO codeDTO);

    default Code fromId(Long id) {
        if (id == null) {
            return null;
        }
        Code code = new Code();
        code.setId(id);
        return code;
    }
}
