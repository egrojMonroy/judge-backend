package com.juez.service;

import com.juez.domain.Coder;
import com.juez.repository.CoderRepository;
import com.juez.service.dto.CoderDTO;
import com.juez.service.mapper.CoderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Coder.
 */
@Service
@Transactional
public class CoderService {

    private final Logger log = LoggerFactory.getLogger(CoderService.class);

    private final CoderRepository coderRepository;

    private final CoderMapper coderMapper;

    public CoderService(CoderRepository coderRepository, CoderMapper coderMapper) {
        this.coderRepository = coderRepository;
        this.coderMapper = coderMapper;
    }

    /**
     * Save a coder.
     *
     * @param coderDTO the entity to save
     * @return the persisted entity
     */
    public CoderDTO save(CoderDTO coderDTO) {
        log.debug("Request to save Coder : {}", coderDTO);
        Coder coder = coderMapper.toEntity(coderDTO);
        coder = coderRepository.save(coder);
        return coderMapper.toDto(coder);
    }

    /**
     * Get all the coders.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<CoderDTO> findAll() {
        log.debug("Request to get all Coders");
        return coderRepository.findAllWithEagerRelationships().stream()
            .map(coderMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one coder by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public CoderDTO findOne(Long id) {
        log.debug("Request to get Coder : {}", id);
        Coder coder = coderRepository.findOneWithEagerRelationships(id);
        return coderMapper.toDto(coder);
    }

    /**
     * Delete the coder by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Coder : {}", id);
        coderRepository.delete(id);
    }
}
