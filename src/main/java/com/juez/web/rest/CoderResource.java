package com.juez.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.juez.domain.Coder;

import com.juez.repository.CoderRepository;
import com.juez.web.rest.util.HeaderUtil;
import com.juez.service.dto.CoderDTO;
import com.juez.service.mapper.CoderMapper;
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

    private final CoderRepository coderRepository;

    private final CoderMapper coderMapper;

    public CoderResource(CoderRepository coderRepository, CoderMapper coderMapper) {
        this.coderRepository = coderRepository;
        this.coderMapper = coderMapper;
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
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new coder cannot already have an ID")).body(null);
        }
        Coder coder = coderMapper.toEntity(coderDTO);
        coder = coderRepository.save(coder);
        CoderDTO result = coderMapper.toDto(coder);
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
        Coder coder = coderMapper.toEntity(coderDTO);
        coder = coderRepository.save(coder);
        CoderDTO result = coderMapper.toDto(coder);
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
        List<Coder> coders = coderRepository.findAllWithEagerRelationships();
        return coderMapper.toDto(coders);
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
        Coder coder = coderRepository.findOneWithEagerRelationships(id);
        CoderDTO coderDTO = coderMapper.toDto(coder);
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
        coderRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
