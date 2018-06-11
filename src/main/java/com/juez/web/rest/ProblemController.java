package com.juez.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.juez.domain.Problem;

import com.juez.repository.ProblemRepository;
import com.juez.web.rest.errors.BadRequestAlertException;
import com.juez.web.rest.util.HeaderUtil;
import com.juez.web.rest.util.PaginationUtil;
import com.juez.service.ProblemService;
import com.juez.service.dto.ProblemDTO;
import com.juez.service.mapper.ProblemMapper;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Problem.
 */
@RestController
@RequestMapping("/api")
public class ProblemController {

    private final Logger log = LoggerFactory.getLogger(ProblemController.class);

    private static final String ENTITY_NAME = "problem";

    private final ProblemRepository problemRepository;

    private final ProblemMapper problemMapper;

    private final ProblemService problemService;
    public ProblemController(
        ProblemRepository problemRepository, 
        ProblemMapper problemMapper,
        ProblemService problemService
        ) {
        this.problemRepository = problemRepository;
        this.problemMapper = problemMapper;
        this.problemService = problemService;
    }

    /**
     * GET  /problems : get all the problems.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of problems in body
     */
    @GetMapping("/problems/active")
    @Timed
    public ResponseEntity<List<ProblemDTO>> getAllProblems(Pageable pageable) {
        log.debug("REST request to get a page of Problems");
        Page<Problem> page = problemRepository.findByActive(true,pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/problems");
        return new ResponseEntity<>(problemMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }


    /**
     * GET  /asset-types/:name and description
     *
     * @param name the name of the assetTypeDTO to retrieve
     * @param description the description of the assetTypeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the assetTypeDTO, or with status 404 (Not Found)
     */
   
    @GetMapping("/problems/search")
    @Timed 
    public ResponseEntity<List<ProblemDTO>> getProblemByName(@RequestParam String name , Pageable pageable) {
        log.debug("REST request to get problems : {}", name);
        Page<Problem> problems = problemRepository.findByNameContainingIgnoreCaseAndActive(name, true, pageable);
        Page<ProblemDTO> page = problems.map(problemMapper::toDto);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/asset-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    /**
     * GET  /asset-types/:name and description
     *
     * @param name the name of the assetTypeDTO to retrieve
     * @param description the description of the assetTypeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the assetTypeDTO, or with status 404 (Not Found)
     */
   
    @GetMapping("/problems/download")
    @Timed 
    public ResponseEntity<Resource> getProblemPDF(@RequestParam String fileName) {
        log.debug("REST request to get problems : {}", fileName);
        Resource resource = problemService.loadFileAsResource(fileName);
        String contentType = "application/pdf";
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .body(resource);
    }
    /**
     * GET  /asset-types/:name and description
     *
     * @param name the name of the assetTypeDTO to retrieve
     * @param description the description of the assetTypeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the assetTypeDTO, or with status 404 (Not Found)
     */
   
    @GetMapping("/problems/download/sample")
    @Timed 
    public ResponseEntity<Resource> getSamplePDF() {
        log.debug("REST request to get problems : {}");
        Resource resource = problemService.loadSampleAsResource();
        String contentType = "application/msword";
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .body(resource);
    }

     /**
     * GET  /problems : get all the problems.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of problems in body
     */
    @GetMapping("/problems/creator")
    @Timed
    public ResponseEntity<List<ProblemDTO>> getAllProblemsByCreator(Pageable pageable) {
        log.debug("REST request to get a page of Problems");
        Page<ProblemDTO> page = problemService.findAllByCreator(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/problems");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * POST  /codes : Create a new code.
     *
     * @param codeDTO the codeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new codeDTO, or with status 400 (Bad Request) if the code has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @throws IOException
     */
    @PostMapping("/problems/upload")
    @ResponseBody
    public ResponseEntity<String> createCode(@RequestPart MultipartFile reportFile, @RequestParam String problemId) throws URISyntaxException, IOException {
       String fileName = problemService.storeFile(reportFile, problemId);
        return ResponseEntity.created(new URI("/api/problems/"+reportFile.getOriginalFilename()))
            .body(fileName);
    }
    
    public String getCurrentDir() {
        String currentDir = System.getProperty("user.dir");
        return currentDir;
    }

}
