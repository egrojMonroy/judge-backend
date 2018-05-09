package com.juez.service;

import java.time.ZonedDateTime;

import com.juez.domain.Contest;
import com.juez.domain.User;
import com.juez.repository.ContestRepository;
import com.juez.service.dto.ContestDTO;
import com.juez.service.mapper.ContestMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Contest.
 */
@Service
@Transactional
public class ContestService {

    private final Logger log = LoggerFactory.getLogger(ContestService.class);

    private final ContestRepository contestRepository;

    private final ContestMapper contestMapper;
    private final UserService userService;
    public ContestService(
        ContestRepository contestRepository, 
        ContestMapper contestMapper,
        UserService userService) {
        this.contestRepository = contestRepository;
        this.contestMapper = contestMapper;
        this.userService = userService;
    }

    /**
     * Save a contest.
     *
     * @param contestDTO the entity to save
     * @return the persisted entity
     */
    public ContestDTO save(ContestDTO contestDTO) {
        log.debug("Request to save Contest : {}", contestDTO);
        Contest contest = contestMapper.toEntity(contestDTO);
        contest.setCreator((User) userService.getUserWithAuthorities());
        contest = contestRepository.save(contest);
        return contestMapper.toDto(contest);
    }

    /**
     * Get all the contests.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ContestDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Contests");
        return contestRepository.findAll(pageable)
            .map(contestMapper::toDto);
    }

    /**
     * Get one contest by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public ContestDTO findOne(Long id) {
        log.debug("Request to get Contest : {}", id);
        Contest contest = contestRepository.findOneWithEagerRelationships(id);
        return contestMapper.toDto(contest);
    }
    
    public Page<ContestDTO> findAllBeforeEndDate(Pageable pageable){
        ZonedDateTime dateTime = ZonedDateTime.now();
        dateTime = dateTime.minusHours(19);
        return contestRepository.findByEnddateBefore( dateTime, pageable).map(contestMapper::toDto);
    }
    public Page<ContestDTO> findAllRunning(Pageable pageable){
        ZonedDateTime dateTime = ZonedDateTime.now();
        dateTime = dateTime.minusHours(19);
        System.out.println("TUNNNIN"+dateTime);
        return contestRepository.findByStartdateAfterOrEnddateAfter(dateTime,dateTime, pageable).map(contestMapper::toDto);
    }
    /**
     * Delete the contest by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Contest : {}", id);
        contestRepository.delete(id);
    }
}
