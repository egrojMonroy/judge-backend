package com.juez.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.juez.service.CoderService;
import com.juez.web.rest.errors.BadRequestAlertException;
import com.juez.web.rest.util.HeaderUtil;
import com.juez.service.dto.CoderDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class CoderResource {

    private final Logger log = LoggerFactory.getLogger(CoderResource.class);

    private static final String ENTITY_NAME = "coder";

    private final CoderService coderService;

    public CoderResource(CoderService coderService) {
        this.coderService = coderService;
    }

    /**
     * POST  /coders : Create a new coder.
     *
     * @param coderDTO the coderDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new coderDTO, or with status 400 (Bad Request) if the coder has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/coders")
    @Timed
    public ResponseEntity<CoderDTO> createCoder(@RequestBody CoderDTO coderDTO) throws URISyntaxException {
        log.debug("REST request to save Coder : {}", coderDTO);
        if (coderDTO.getId() != null) {
            throw new BadRequestAlertException("A new coder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CoderDTO result = coderService.save(coderDTO);
        return ResponseEntity.created(new URI("/api/coders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /coders : Updates an existing coder.
     *
     * @param coderDTO the coderDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated coderDTO,
     * or with status 400 (Bad Request) if the coderDTO is not valid,
     * or with status 500 (Internal Server Error) if the coderDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/coders")
    @Timed
    public ResponseEntity<CoderDTO> updateCoder(@RequestBody CoderDTO coderDTO) throws URISyntaxException {
        log.debug("REST request to update Coder : {}", coderDTO);
        if (coderDTO.getId() == null) {
            return createCoder(coderDTO);
        }
        CoderDTO result = coderService.save(coderDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, coderDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /coders : get all the coders.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of coders in body
     */
    @GetMapping("/coders")
    @Timed
    public List<CoderDTO> getAllCoders() {
        log.debug("REST request to get all Coders");
        return coderService.findAll();
        }

    /**
     * GET  /coders/:id : get the "id" coder.
     *
     * @param id the id of the coderDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the coderDTO, or with status 404 (Not Found)
     */
    @GetMapping("/coders/{id}")
    @Timed
    public ResponseEntity<CoderDTO> getCoder(@PathVariable Long id) {
        log.debug("REST request to get Coder : {}", id);
        CoderDTO coderDTO = coderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(coderDTO));
    }

    /**
     * DELETE  /coders/:id : delete the "id" coder.
     *
     * @param id the id of the coderDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/coders/{id}")
    @Timed
    public ResponseEntity<Void> deleteCoder(@PathVariable Long id) {
        log.debug("REST request to delete Coder : {}", id);
        coderService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
