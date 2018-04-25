package com.juez.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.juez.domain.TestCase;

import com.juez.repository.TestCaseRepository;
import com.juez.web.rest.util.HeaderUtil;
import com.juez.service.dto.TestCaseDTO;
import com.juez.service.mapper.TestCaseMapper;
import com.juez.service.dto.ProblemDTO;
import io.github.jhipster.web.util.ResponseUtil;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.http.HttpHeaders;
import org.springframework.http.*;
import com.juez.web.rest.util.*;
import java.util.List;
import java.util.Optional;
import com.juez.service.FileService;

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
    private final FileService fileService;

    public TestCaseController(TestCaseRepository testCaseRepository, TestCaseMapper testCaseMapper, FileService fileService) {
        this.testCaseRepository = testCaseRepository;
        this.testCaseMapper = testCaseMapper;
        this.fileService = fileService;
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
     * Input is the problem id
     * @return the inputs and outputs that are meant to be shown 
     */
    @GetMapping("/test-cases/show-problem/{id}")
    @Timed
    public ResponseEntity<List<TestCaseDTO>> getTestCaseShowByProblem(@PathVariable Long id,@RequestParam String show) {
        log.debug("REST request to get TestCase : {}", id);
        Boolean shown = show.equals("1")?true:false;
        List<TestCase> page = testCaseRepository.findAllByProblemIdAndShow(id,shown);
        return new ResponseEntity<>(testCaseMapper.toDto(page), HttpStatus.OK);
    }

    @GetMapping("/test-cases/problem-show/{id}")
    public ResponseEntity<String> getTestCaseFile(@PathVariable Long id, @RequestParam String show) throws Exception {
        log.debug("REST request to get TestCase : {}", id);
        Boolean shown = show.equals("1")?true:false;
        List<TestCase> page = testCaseRepository.findAllByProblemIdAndShow(id,shown);
        
        JSONArray json = new JSONArray();
        for(TestCase test: page){
            String content;
            JSONObject value = new JSONObject();
            content = readFile(getPath(id, test.getInputfl()),StandardCharsets.UTF_8);
            value.put("input",content);
            content = readFile(getPath(id, test.getOutputfl()),StandardCharsets.UTF_8);
            value.put("output",content);
            json.put(value);
        }

        System.out.println("-----CONTENT-----------");
        System.out.println(json);
        return ResponseEntity.ok().body(json.toString());
    }

    static String readFile(String path, Charset encoding) throws IOException {
    byte[] encoded = Files.readAllBytes(Paths.get(path));
    return new String(encoded, encoding);
    }
    public String getPath(Long problemId, String fileName){
        return getCurrentDir()+"/test_cases/"+problemId+"/"+fileName;
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
        
        //Start save files to disk
        String destinationInput = createDir(problem_id)+inputFile.getOriginalFilename();
        String destinationOutput = createDir(problem_id)+outputFile.getOriginalFilename();
        File fileInput = new File(destinationInput);
        System.out.println("------------------------------------");
        System.out.println(destinationInput);
        System.out.println(destinationOutput);
        System.out.println("------------------------------------");
        try {
			inputFile.transferTo(fileInput);
		} catch (IllegalStateException e) {
            log.debug("ERROR INPUT FILE");
			e.printStackTrace();
		} catch (IOException e) {
            log.debug("ERROR INPUT FILE 2");
			e.printStackTrace();
		}
        File fileOutput = new File(destinationOutput);
        try {
			outputFile.transferTo(fileOutput);
		} catch (IllegalStateException e) {
            log.debug("ERROR OUTPUT FILE");
			e.printStackTrace();
		} catch (IOException e) {
            log.debug("ERROR OUTPUT FILE 2");
			e.printStackTrace();
		}

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

    public String getCurrentDir() {
        String currentDir = System.getProperty("user.dir");
        return currentDir;
    }
    public String createDir(String problemId) {
        String currentDir = getCurrentDir();
        String dirToCreate = currentDir +"/test_cases/"+problemId;
        fileService.runScript("mkdir -p "+dirToCreate, "");
        return dirToCreate+"/";
    }
}
