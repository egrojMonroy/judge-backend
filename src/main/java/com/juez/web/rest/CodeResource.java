package com.juez.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.juez.domain.Code;

import com.juez.repository.CodeRepository;
import com.juez.web.rest.errors.BadRequestAlertException;
import com.juez.web.rest.util.HeaderUtil;
import com.juez.service.dto.CodeDTO;
import com.juez.service.mapper.CodeMapper;
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
 * REST controller for managing Code.
 */
@RestController
@RequestMapping("/api")
public class CodeResource {

    private final Logger log = LoggerFactory.getLogger(CodeResource.class);

    private static final String ENTITY_NAME = "code";

    private final CodeRepository codeRepository;

    private final CodeMapper codeMapper;

    public CodeResource(CodeRepository codeRepository, CodeMapper codeMapper) {
        this.codeRepository = codeRepository;
        this.codeMapper = codeMapper;
    }

    /**
     * POST  /codes : Create a new code.
     *
     * @param codeDTO the codeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new codeDTO, or with status 400 (Bad Request) if the code has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/codes")
    @Timed
    public ResponseEntity<CodeDTO> createCode(@RequestBody CodeDTO codeDTO) throws URISyntaxException {
        log.debug("REST request to save Code : {}", codeDTO);
        if (codeDTO.getId() != null) {
            throw new BadRequestAlertException("A new code cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Code code = codeMapper.toEntity(codeDTO);
        code = codeRepository.save(code);
        CodeDTO result = codeMapper.toDto(code);
        return ResponseEntity.created(new URI("/api/codes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /codes : Updates an existing code.
     *
     * @param codeDTO the codeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated codeDTO,
     * or with status 400 (Bad Request) if the codeDTO is not valid,
     * or with status 500 (Internal Server Error) if the codeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/codes")
    @Timed
    public ResponseEntity<CodeDTO> updateCode(@RequestBody CodeDTO codeDTO) throws URISyntaxException {
        log.debug("REST request to update Code : {}", codeDTO);
        if (codeDTO.getId() == null) {
            return createCode(codeDTO);
        }
        Code code = codeMapper.toEntity(codeDTO);
        code = codeRepository.save(code);
        CodeDTO result = codeMapper.toDto(code);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, codeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /codes : get all the codes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of codes in body
     */
    @GetMapping("/codes")
    @Timed
    public List<CodeDTO> getAllCodes() {
        log.debug("REST request to get all Codes");
        List<Code> codes = codeRepository.findAll();
        return codeMapper.toDto(codes);
        }

    /**
     * GET  /codes/:id : get the "id" code.
     *
     * @param id the id of the codeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the codeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/codes/{id}")
    @Timed
    public ResponseEntity<CodeDTO> getCode(@PathVariable Long id) {
        log.debug("REST request to get Code : {}", id);
        Code code = codeRepository.findOne(id);
        CodeDTO codeDTO = codeMapper.toDto(code);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(codeDTO));
    }

    /**
     * DELETE  /codes/:id : delete the "id" code.
     *
     * @param id the id of the codeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/codes/{id}")
    @Timed
    public ResponseEntity<Void> deleteCode(@PathVariable Long id) {
        log.debug("REST request to delete Code : {}", id);
        codeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
