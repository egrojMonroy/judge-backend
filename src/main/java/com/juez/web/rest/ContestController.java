package com.juez.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.juez.domain.Contest;
import com.juez.domain.Problem;
import com.juez.repository.ContestRepository;
import com.juez.repository.ProblemRepository;
import com.juez.web.rest.errors.BadRequestAlertException;
import com.juez.web.rest.util.HeaderUtil;
import com.juez.web.rest.util.PaginationUtil;
import com.juez.service.dto.ContestDTO;
import com.juez.service.dto.ProblemDTO;
import com.juez.service.mapper.ContestMapper;
import com.juez.service.mapper.ProblemMapper;
import com.juez.service.ContestService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.juez.repository.ProblemRepository;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    private final ProblemRepository problemRepository;
    private final ContestService contestService;
    public ContestController(
        ContestRepository contestRepository, 
        ContestMapper contestMapper, 
        ProblemMapper problemMapper,
        ProblemRepository problemRepository,
        ContestService contestService ) {
        this.contestRepository = contestRepository;
        this.contestMapper = contestMapper;
        this.problemMapper = problemMapper;
        this.problemRepository = problemRepository;
        this.contestService = contestService;
    }

  /**
     * GET  /contests : get all the contests.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of contests in body
     */
    @GetMapping("/contests/before")
    @Timed
    public ResponseEntity<List<ContestDTO>> getAllContests(Pageable pageable) {
        log.debug("REST request to get a page of Contests");
        Page<ContestDTO> page = contestService.findAllBeforeEndDate(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/contests/before");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

/**
     * GET  /contests : get all the contests.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of contests in body
     */
    @GetMapping("/contests/after")
    @Timed
    public ResponseEntity<List<ContestDTO>> getAllContestsAfter(Pageable pageable) {
        log.debug("REST request to get a page of Contests");
        Page<ContestDTO> page = contestService.findAllRunning(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/contests/after");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

/**
     * GET  /contests : get all the contests.
     * @param contesId
     * @return the ResponseEntity with status 200 (OK) and the list of contests in body
     */
    @GetMapping("/contests/problems")
    @Timed
    public ResponseEntity<List<ProblemDTO>> getAllProblems(@RequestParam Long contestId) {
        log.debug("REST request to get a page of Contests");
        List<ProblemDTO> page = contestService.getProblemsByContest(contestId);
        return new ResponseEntity<>(page, HttpStatus.OK);   
    }
   /**
     * GET  /contests : get all the contests.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of contests in body
     */
    @GetMapping("/contests/creator")
    @Timed
    public ResponseEntity<List<ContestDTO>> getAllContestsByCreator(Pageable pageable) {
        log.debug("REST request to get a page of Contests");
        Page<ContestDTO> page = contestService.findAllByCreator(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/contests");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

     /**
     * POST  /contests : C\\
     *
     * @param contestDTO the contestDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new contestDTO, or with status 400 (Bad Request) if the contest has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/asdf")
    @Timed
    public ResponseEntity<ContestDTO> createContest(@RequestBody ContestDTO contestDTO) throws URISyntaxException {
        log.debug("REST request to save Contest : {}", contestDTO);
        if (contestDTO.getId() != null) {
            throw new BadRequestAlertException("A new contest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContestDTO result = contestService.save(contestDTO);
        return ResponseEntity.created(new URI("/api/contests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    /**
     * POST  /contests : C\\
     *
     * @param contestDTO the contestDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new contestDTO, or with status 400 (Bad Request) if the contest has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/contests/verify")
    @Timed
    public boolean checkPassword(@RequestParam Long contestId, @RequestParam String password) throws URISyntaxException {
        boolean check = contestService.checkPass(contestId, password);
        return check;
    }
}
