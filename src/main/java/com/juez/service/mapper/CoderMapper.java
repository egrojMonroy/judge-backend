package com.juez.service.mapper;

import com.juez.domain.*;
import com.juez.service.dto.CoderDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Coder and its DTO CoderDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface CoderMapper extends EntityMapper<CoderDTO, Coder> {

    @Mapping(source = "user.id", target = "userId")
    CoderDTO toDto(Coder coder);

    @Mapping(source = "userId", target = "user")
    @Mapping(target = "contests", ignore = true)
    Coder toEntity(CoderDTO coderDTO);

    default Coder fromId(Long id) {
        if (id == null) {
            return null;
        }
        Coder coder = new Coder();
        coder.setId(id);
        return coder;
    }
}
