package com.juez.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.juez.domain.Contest;

import com.juez.repository.ContestRepository;
import com.juez.web.rest.util.HeaderUtil;
import com.juez.web.rest.util.PaginationUtil;
import com.juez.service.dto.ContestDTO;
import com.juez.service.mapper.ContestMapper;
import io.swagger.annotations.ApiParam;
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
public class ContestResource {

    private final Logger log = LoggerFactory.getLogger(ContestResource.class);

    private static final String ENTITY_NAME = "contest";

    private final ContestRepository contestRepository;

    private final ContestMapper contestMapper;

    public ContestResource(ContestRepository contestRepository, ContestMapper contestMapper) {
        this.contestRepository = contestRepository;
        this.contestMapper = contestMapper;
    }

    /**
     * POST  /contests : Create a new contest.
     *
     * @param contestDTO the contestDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new contestDTO, or with status 400 (Bad Request) if the contest has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/contests")
    @Timed
    public ResponseEntity<ContestDTO> createContest(@RequestBody ContestDTO contestDTO) throws URISyntaxException {
        log.debug("REST request to save Contest : {}", contestDTO);
        if (contestDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new contest cannot already have an ID")).body(null);
        }
        Contest contest = contestMapper.toEntity(contestDTO);
        contest = contestRepository.save(contest);
        ContestDTO result = contestMapper.toDto(contest);
        return ResponseEntity.created(new URI("/api/contests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /contests : Updates an existing contest.
     *
     * @param contestDTO the contestDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated contestDTO,
     * or with status 400 (Bad Request) if the contestDTO is not valid,
     * or with status 500 (Internal Server Error) if the contestDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/contests")
    @Timed
    public ResponseEntity<ContestDTO> updateContest(@RequestBody ContestDTO contestDTO) throws URISyntaxException {
        log.debug("REST request to update Contest : {}", contestDTO);
        if (contestDTO.getId() == null) {
            return createContest(contestDTO);
        }
        Contest contest = contestMapper.toEntity(contestDTO);
        contest = contestRepository.save(contest);
        ContestDTO result = contestMapper.toDto(contest);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, contestDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /contests : get all the contests.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of contests in body
     */
    @GetMapping("/contests")
    @Timed
    public ResponseEntity<List<ContestDTO>> getAllContests(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Contests");
        Page<Contest> page = contestRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/contests");
        return new ResponseEntity<>(contestMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /contests/:id : get the "id" contest.
     *
     * @param id the id of the contestDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the contestDTO, or with status 404 (Not Found)
     */
    @GetMapping("/contests/{id}")
    @Timed
    public ResponseEntity<ContestDTO> getContest(@PathVariable Long id) {
        log.debug("REST request to get Contest : {}", id);
        Contest contest = contestRepository.findOneWithEagerRelationships(id);
        ContestDTO contestDTO = contestMapper.toDto(contest);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(contestDTO));
    }

    /**
     * DELETE  /contests/:id : delete the "id" contest.
     *
     * @param id the id of the contestDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/contests/{id}")
    @Timed
    public ResponseEntity<Void> deleteContest(@PathVariable Long id) {
        log.debug("REST request to delete Contest : {}", id);
        contestRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
