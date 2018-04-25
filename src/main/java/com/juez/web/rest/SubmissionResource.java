package com.juez.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.juez.domain.Submission;

import com.juez.repository.SubmissionRepository;
import com.juez.web.rest.errors.BadRequestAlertException;
import com.juez.web.rest.util.HeaderUtil;
import com.juez.service.dto.SubmissionDTO;
import com.juez.service.mapper.SubmissionMapper;
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
 * REST controller for managing Submission.
 */
@RestController
@RequestMapping("/api")
public class SubmissionResource {

    private final Logger log = LoggerFactory.getLogger(SubmissionResource.class);

    private static final String ENTITY_NAME = "submission";

    private final SubmissionRepository submissionRepository;

    private final SubmissionMapper submissionMapper;

    public SubmissionResource(SubmissionRepository submissionRepository, SubmissionMapper submissionMapper) {
        this.submissionRepository = submissionRepository;
        this.submissionMapper = submissionMapper;
    }

    /**
     * POST  /submissions : Create a new submission.
     *
     * @param submissionDTO the submissionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new submissionDTO, or with status 400 (Bad Request) if the submission has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/submissions")
    @Timed
    public ResponseEntity<SubmissionDTO> createSubmission(@RequestBody SubmissionDTO submissionDTO) throws URISyntaxException {
        log.debug("REST request to save Submission : {}", submissionDTO);
        if (submissionDTO.getId() != null) {
            throw new BadRequestAlertException("A new submission cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Submission submission = submissionMapper.toEntity(submissionDTO);
        submission = submissionRepository.save(submission);
        SubmissionDTO result = submissionMapper.toDto(submission);
        return ResponseEntity.created(new URI("/api/submissions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /submissions : Updates an existing submission.
     *
     * @param submissionDTO the submissionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated submissionDTO,
     * or with status 400 (Bad Request) if the submissionDTO is not valid,
     * or with status 500 (Internal Server Error) if the submissionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/submissions")
    @Timed
    public ResponseEntity<SubmissionDTO> updateSubmission(@RequestBody SubmissionDTO submissionDTO) throws URISyntaxException {
        log.debug("REST request to update Submission : {}", submissionDTO);
        if (submissionDTO.getId() == null) {
            return createSubmission(submissionDTO);
        }
        Submission submission = submissionMapper.toEntity(submissionDTO);
        submission = submissionRepository.save(submission);
        SubmissionDTO result = submissionMapper.toDto(submission);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, submissionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /submissions : get all the submissions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of submissions in body
     */
    @GetMapping("/submissions")
    @Timed
    public List<SubmissionDTO> getAllSubmissions() {
        log.debug("REST request to get all Submissions");
        List<Submission> submissions = submissionRepository.findAll();
        return submissionMapper.toDto(submissions);
        }

    /**
     * GET  /submissions/:id : get the "id" submission.
     *
     * @param id the id of the submissionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the submissionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/submissions/{id}")
    @Timed
    public ResponseEntity<SubmissionDTO> getSubmission(@PathVariable Long id) {
        log.debug("REST request to get Submission : {}", id);
        Submission submission = submissionRepository.findOne(id);
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(submissionDTO));
    }

    /**
     * DELETE  /submissions/:id : delete the "id" submission.
     *
     * @param id the id of the submissionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/submissions/{id}")
    @Timed
    public ResponseEntity<Void> deleteSubmission(@PathVariable Long id) {
        log.debug("REST request to delete Submission : {}", id);
        submissionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
