package com.juez.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.juez.service.CoderService;
import com.juez.web.rest.errors.BadRequestAlertException;
import com.juez.web.rest.util.HeaderUtil;
import com.juez.service.dto.CoderDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Coder.
 */
@RestController
@RequestMapping("/api")
public class CoderController {

    private final Logger log = LoggerFactory.getLogger(CoderController.class);

    private static final String ENTITY_NAME = "coder";

    private final CoderService coderService;

    public CoderController(CoderService coderService) {
        this.coderService = coderService;
    }

    /**
     * POST  /coders : Create a new coder.
     *
     * @param coderDTO the coderDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new coderDTO, or with status 400 (Bad Request) if the coder has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/coders/check")
    @Timed
    public ResponseEntity<CoderDTO> createCoder(@RequestParam Long contestId) throws URISyntaxException {
        CoderDTO result = coderService.create(contestId);
        return ResponseEntity.created(new URI("/api/coders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    @GetMapping("/coders/contest")
    @Timed
    public ResponseEntity<Boolean> coderInContest(@RequestParam Long contestId) throws URISyntaxException {
        Boolean res = coderService.coderRegistered(contestId);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

   
}
