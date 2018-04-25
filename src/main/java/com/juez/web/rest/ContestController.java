package com.juez.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.juez.domain.Contest;
import com.juez.domain.Problem;
import com.juez.repository.ContestRepository;
import com.juez.web.rest.errors.BadRequestAlertException;
import com.juez.web.rest.util.HeaderUtil;
import com.juez.web.rest.util.PaginationUtil;
import com.juez.service.dto.ContestDTO;
import com.juez.service.dto.ProblemDTO;
import com.juez.service.mapper.ContestMapper;
import com.juez.service.mapper.ProblemMapper;

import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Contest.
 */
@RestController
@RequestMapping("/api")
public class ContestController {

    private final Logger log = LoggerFactory.getLogger(ContestController.class);

    private static final String ENTITY_NAME = "contest";

    private final ContestRepository contestRepository;
    private final ProblemMapper problemMapper;

    private final ContestMapper contestMapper;

    public ContestController(ContestRepository contestRepository, ContestMapper contestMapper, ProblemMapper problemMapper ) {
        this.contestRepository = contestRepository;
        this.contestMapper = contestMapper;
        this.problemMapper = problemMapper;
    }

    /**
     * POST  /contests : Create a new contest.
     *
     * @param contestDTO the contestDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new contestDTO, or with status 400 (Bad Request) if the contest has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/contests/add-problem")
    @Timed
    public ResponseEntity<ContestDTO> addProblem(@RequestBody ProblemDTO problemDTO, @RequestParam Long contestId ) throws URISyntaxException {
        log.debug("REST request to save Contest : {}", problemDTO);
   
        Problem problem = problemMapper.toEntity(problemDTO);
        Contest contest = contestRepository.findById(contestId);
        contest.addProblem(problem);
        contest = contestRepository.save(contest);
        ContestDTO result = contestMapper.toDto(contest);
        
        return ResponseEntity.created(new URI("/api/contests/add-problem" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

}
