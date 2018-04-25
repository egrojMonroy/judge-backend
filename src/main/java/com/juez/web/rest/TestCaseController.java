package com.juez.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.juez.domain.TestCase;

import com.juez.repository.TestCaseRepository;
import com.juez.web.rest.util.HeaderUtil;
import com.juez.service.dto.TestCaseDTO;
import com.juez.service.mapper.TestCaseMapper;
import com.juez.service.dto.ProblemDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import java.net.URI;
import java.net.URISyntaxException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.*;
import com.juez.web.rest.util.*;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing TestCase.
 */
@RestController
@RequestMapping("/api")
public class TestCaseController {

    private final Logger log = LoggerFactory.getLogger(TestCaseController.class);

    private static final String ENTITY_NAME = "testCase";

    private final TestCaseRepository testCaseRepository;

    private final TestCaseMapper testCaseMapper;

    public TestCaseController(TestCaseRepository testCaseRepository, TestCaseMapper testCaseMapper) {
        this.testCaseRepository = testCaseRepository;
        this.testCaseMapper = testCaseMapper;
    }
    /**
     *  
     * @return the ResponseEntity with status 200 (OK) and with body the testCaseDTO, or with status 404 (Not Found)
     */
    @GetMapping("/test-cases/problem/{id}")
    @Timed
    public ResponseEntity<List<TestCaseDTO>> getTestCaseByProblem(@PathVariable Long id, Pageable pageable) {
        log.debug("REST request to get TestCase : {}", id);
        Page<TestCaseDTO> page = testCaseRepository.findAllByProblemId(id, pageable).map(testCaseMapper::toDto);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/test-cases");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * POST  /test-cases : Create a new testCase.
     *
     * @param testCaseDTO the testCaseDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new testCaseDTO, or with status 400 (Bad Request) if the testCase has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/test-cases/create")
    @Timed
    public ResponseEntity<TestCaseDTO> createTestCase(@RequestPart MultipartFile inputFile,@RequestPart MultipartFile outputFile, @RequestPart String show, @RequestPart String problem_id ) throws URISyntaxException {
        TestCaseDTO testCaseDTO = new TestCaseDTO();
        testCaseDTO.setInputfl(inputFile.getOriginalFilename());
        testCaseDTO.setOutputfl(outputFile.getOriginalFilename());
        testCaseDTO.setShow(show.equals("1")?true:false);
        testCaseDTO.setProblemId(Long.parseLong(problem_id));
        log.debug("REST request to save TestCase : {}", testCaseDTO);
        
        System.out.println(show);
        if (testCaseDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new testCase cannot already have an ID")).body(null);
        }
        TestCase testCase = testCaseMapper.toEntity(testCaseDTO);
        testCase = testCaseRepository.save(testCase);
        TestCaseDTO result = testCaseMapper.toDto(testCase);
        return ResponseEntity.created(new URI("/api/test-cases/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }


}
