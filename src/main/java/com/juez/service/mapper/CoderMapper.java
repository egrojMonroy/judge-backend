package com.juez.service.mapper;

import com.juez.domain.*;
import com.juez.service.dto.CoderDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Coder and its DTO CoderDTO.
 */
@Mapper(componentModel = "spring", uses = {ContestMapper.class, })
public interface CoderMapper extends EntityMapper <CoderDTO, Coder> {
    
    
    default Coder fromId(Long id) {
        if (id == null) {
            return null;
        }
        Coder coder = new Coder();
        coder.setId(id);
        return coder;
    }
}
