package com.juez.service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.juez.domain.Contest;
import com.juez.domain.Problem;
import com.juez.domain.User;
import com.juez.repository.CoderRepository;
import com.juez.repository.ContestRepository;
import com.juez.repository.ProblemRepository;
import com.juez.service.dto.ContestDTO;
import com.juez.service.dto.ProblemDTO;
import com.juez.service.mapper.ContestMapper;
import com.juez.service.mapper.ProblemMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.juez.service.mapper.ProblemMapper;

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
    private final ProblemMapper problemMapper;
    private final CoderRepository coderRepository;
    private final ProblemRepository problemRepository;
    public ContestService(
        ContestRepository contestRepository, 
        ContestMapper contestMapper,
        UserService userService,
        ProblemMapper problemMapper,
        CoderRepository coderRepository,
        ProblemRepository problemRepository) {
        this.contestRepository = contestRepository;
        this.contestMapper = contestMapper;
        this.userService = userService;
        this.problemMapper = problemMapper;
        this.coderRepository = coderRepository;
        this.problemRepository = problemRepository;
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
     * Get all the contests.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ContestDTO> findAllByCreator(Pageable pageable) {
        log.debug("Request to get all Contests");
        return contestRepository.findByCreatorIsCurrentUser(pageable)
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
        // dateTime = dateTime.minusHours(19);
        return contestRepository.findByEnddateBeforeAndActiveIs( dateTime, true, pageable).map(contestMapper::toDto);
    }
    public Page<ContestDTO> findAllRunning(Pageable pageable){
        ZonedDateTime dateTime = ZonedDateTime.now();
        // dateTime = dateTime.minusHours(19);
        // System.out.println("TUNNNIN"+dateTime);
        return contestRepository.findByStartdateAfterAndActiveIsOrEnddateAfterAndActiveIs(dateTime,true,dateTime, true, pageable).map(contestMapper::toDto);
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


    public List<ProblemDTO> getProblemsByContest (Long contestId) {
        Contest contest = contestRepository.findOne(contestId);
        Set<Problem> problems = contest.getProblems();
        List<Problem> problemsList = new ArrayList<Problem>() ; 
        for(Problem pr: problems ) {
            if(pr.isActive())
                problemsList.add(pr);
        }
        return problemMapper.toDto(problemsList);
    } 
    public boolean checkPass(Long contestId, String password ) {
        Contest contest = contestRepository.findOne(contestId);
        System.out.println("contestid " + password);
        String passwordContet = contest.getPassword();
        
        if(passwordContet == null){
            System.out.println("IS NULL ******************************************");
        } else {
            System.out.println("NO NULL *****************************************");
        }

        if(password.equalsIgnoreCase(passwordContet) || passwordContet == null){
            System.out.println("PASSWORD TRUE ");
            return true;
        } else {
            System.out.println("PASWORD FALSE");
            return false;
        }
    }
    public Set<Long> getProblemsInContest() {
        Page<ContestDTO> contests = findAllRunning(createPageRequest());
        Set<Long> ids_problems = new HashSet<Long>();
        for( ContestDTO element: contests) {
            for( ProblemDTO e: element.getProblems()) {
                ids_problems.add(e.getId());
            } 
        }
        return ids_problems;
    }
    private Pageable createPageRequest() {
        return new PageRequest(0, 199999999);
    }
}
