package com.juez.service;

import com.juez.domain.Coder;
import com.juez.domain.Contest;
import com.juez.domain.User;
import com.juez.repository.CoderRepository;
import com.juez.repository.ContestRepository;
import com.juez.service.dto.CoderDTO;
import com.juez.service.mapper.CoderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.juez.service.UserService;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import com.juez.repository.ContestRepository;
/**
 * Service Implementation for managing Coder.
 */
@Service
@Transactional
public class CoderService {

    private final Logger log = LoggerFactory.getLogger(CoderService.class);

    private final CoderRepository coderRepository;

    private final CoderMapper coderMapper;

    private final ContestRepository contestRepository;
private final UserService userService;
    public CoderService(
        CoderRepository coderRepository, 
        CoderMapper coderMapper,
        UserService userService,
        ContestRepository contestRepository) {
        this.userService = userService;
        this.coderRepository = coderRepository;
        this.coderMapper = coderMapper;
        this.contestRepository = contestRepository;
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
    public CoderDTO create(Long contestId) {
        User user = userService.getUserWithAuthorities();
        Coder actual = coderRepository.findOneByUser_id(user.getId());
        if( actual == null) {
            actual = new Coder();
            actual.setUser(user);
            coderRepository.save(actual);
        } 
        Contest contest = contestRepository.findOne(contestId);
        actual.addContest(contest);
        coderRepository.save(actual);
		return coderMapper.toDto(actual);
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
